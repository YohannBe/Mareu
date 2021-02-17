package com.example.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mareu.event.DeleteMeetingEvent;
import com.example.mareu.injection.Injection;
import com.example.mareu.model.Date;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.ListMeetingApiService;
import com.example.mareu.utils.Utils;
import com.example.mareu.view.ListMeetingPageAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.example.mareu.utils.Utils.chosenRooms;

public class MainActivity extends AppCompatActivity {

    private ListMeetingApiService service;
    private List<Meeting> mListMeeting, filteredList1, filteredList2;
    RecyclerView recyclerView;
    FloatingActionButton addMeetingButton;
    private Dialog dateDialog, locationDialog;
    private TextView withRange, withoutRange;
    private Button filterButtonDate, cancelButtonDate, filterButtonLocation, cancelButtonLocation;
    private LinearLayout rangeLayout, meetingRoom2, meetingRoom3;
    private RadioGroup withoutRangeLayout;
    private RadioButton radioButtonSpecific, radioButtonBefore, radioButtonAfter;
    private DatePicker datePicker, datePickerRange;
    private Date chosenDate;
    private CheckBox a, b, c, d, e, f, g, h, i, j;
    private ImageView closeDialogDate, closeDialogLocation;
    private ImageButton addCheckBoxGroup1, addCheckBoxGroup2, minimRadioGroup1, minimRadioGroup2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
    }

    private void initWidget() {
        service = Injection.getListMeetingService();
        mListMeeting = service.getMeetingList();
        addMeetingButton = findViewById(R.id.add_meeting);
        recyclerView = findViewById(R.id.recycleView_list_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        ListMeetingPageAdapter customAdapter = new ListMeetingPageAdapter(mListMeeting, MainActivity.this);
        recyclerView.setAdapter(customAdapter);
        sendUserToCreateMeeting(addMeetingButton);
        dateDialog = new Dialog(this);
        locationDialog = new Dialog(this);
    }

    private void sendUserToCreateMeeting(FloatingActionButton addMeetingButton) {
        addMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddMeetingActivity = new Intent(MainActivity.this, AddMeeting.class);
                startActivity(toAddMeetingActivity);
            }
        });

    }

    private void initList() {
        recyclerView.setAdapter(new ListMeetingPageAdapter(mListMeeting, MainActivity.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_filter_location:
                showDialogLocation(false);
                return true;
            case R.id.menu_filter_date:
                showDialogDate(false);
                return true;
            case R.id.menu_filter_clear:
                mListMeeting = service.getMeetingList();
                initList();
                return true;
            case R.id.menu_filter_both:
                showDialogDate(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogDate(boolean filteredBoth) {
        dateDialog.setContentView(R.layout.custom_dialog_date);
        initWidgetDialog(dateDialog);
        initClickListenerDateDialog(filteredBoth);

        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    private void initClickListenerDateDialog(final boolean filteredBoth) {
        withRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibilityView(rangeLayout);
            }
        });

        withoutRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibilityView(withoutRangeLayout);
            }
        });

        cancelButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.dismiss();
            }
        });

        filterButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenDate = new Date(datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());
                if (checkDialogInputRange(filteredBoth)) {
                    Toast.makeText(MainActivity.this, "range",
                            Toast.LENGTH_LONG)
                            .show();

                    if (filteredBoth) {
                        filteredList1 = service.getFilteredMeetingListRange(chosenDate,
                                new Date(datePickerRange.getDayOfMonth(), datePickerRange.getMonth(), datePickerRange.getYear()));
                        showDialogLocation(filteredBoth);
                    } else {
                        mListMeeting = service.getFilteredMeetingListRange(chosenDate,
                                new Date(datePickerRange.getDayOfMonth(), datePickerRange.getMonth(), datePickerRange.getYear()));
                        initList();
                    }
                }
                dateDialog.dismiss();
            }
        });

        closeDialogDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.dismiss();
            }
        });

    }

    private boolean checkDialogInputRange(boolean filteredBoth) {
        if (withoutRangeLayout.getVisibility() == rangeLayout.getVisibility()) {
            Toast.makeText(MainActivity.this, "You can choose only one filter method, please expend only the spinner or the radio button",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (withoutRangeLayout.getVisibility() == View.VISIBLE && rangeLayout.getVisibility() != View.VISIBLE) {
            Utils.EnumDate choice = null;
            if (radioButtonSpecific.isChecked()) {
                choice = Utils.EnumDate.specific;
            } else if (radioButtonBefore.isChecked()) {
                choice = Utils.EnumDate.before;
            } else if (radioButtonAfter.isChecked()) {
                choice = Utils.EnumDate.after;
            }
            if (choice != null) {

                if (filteredBoth) {
                    filteredList1 = service.getFilteredMeetingList(choice, chosenDate);
                    showDialogLocation(filteredBoth);
                    return false;
                } else mListMeeting = service.getFilteredMeetingList(choice, chosenDate);
                initList();
                return false;
            } else
                Toast.makeText(MainActivity.this, "You have to choose at least on radio button",
                        Toast.LENGTH_LONG)
                        .show();
            return false;
        } else {
            boolean range = Utils.compareDate(chosenDate,
                    new Date(datePickerRange.getDayOfMonth(), datePickerRange.getMonth(), datePickerRange.getYear()));
            if (!range) {
                Toast.makeText(MainActivity.this, "Please choose a second date bigger than the first date",
                        Toast.LENGTH_LONG)
                        .show();
            }
            return range;
        }
    }

    private void initWidgetDialog(Dialog dateDialog) {
        datePicker = dateDialog.findViewById(R.id.dialog_date_datepicker);
        datePickerRange = dateDialog.findViewById(R.id.dialog_date_datepicker_range);
        withRange = dateDialog.findViewById(R.id.dialog_date_range);
        withoutRange = dateDialog.findViewById(R.id.dialog_date_without_range);
        rangeLayout = dateDialog.findViewById(R.id.dialog_filter_range_date);
        withoutRangeLayout = dateDialog.findViewById(R.id.dialog_date_radiogroup);
        cancelButtonDate = dateDialog.findViewById(R.id.dialog_date_button_cancel);
        filterButtonDate = dateDialog.findViewById(R.id.dialog_date_button_filter);
        radioButtonSpecific = dateDialog.findViewById(R.id.date_dialog_radio_specific_day);
        radioButtonBefore = dateDialog.findViewById(R.id.date_dialog_radio_before);
        radioButtonAfter = dateDialog.findViewById(R.id.date_dialog_radio_after);
        closeDialogDate = dateDialog.findViewById(R.id.close_cross_imageview);
    }

    private void showDialogLocation(final boolean filteredBoth) {
        locationDialog.setContentView(R.layout.custom_dialog_location);
        initWidgetLocationDialog(locationDialog);
        initOnclickDialogLocation(locationDialog, filteredBoth);

        locationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        locationDialog.show();
    }

    private void initOnclickDialogLocation(Dialog locationDialog, final boolean filteredBoth) {
        closeDialogLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationDialog.dismiss();
            }
        });
        cancelButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationDialog.dismiss();
            }
        });

        filterButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenRooms = Utils.checkDialogBoxes(a, b, c, d, e, f, g, h, i, j);
                if (chosenRooms.size() > 0) {
                    Log.i("jevaistrouver", "laaa");
                    if (filteredBoth) {
                        Log.i("jevaistrouver", "laaa222");
                        filteredList2 = service.getFilteredMeetingListLocation(chosenRooms);
                        mListMeeting = service.getFilteredBothMeetingList(filteredList1, filteredList2);
                    } else mListMeeting = service.getFilteredMeetingListLocation(chosenRooms);
                    initList();
                    locationDialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Nothing was chosen", Toast.LENGTH_SHORT).show();
            }
        });

        addCheckBoxGroup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGroupVisibility(v,minimRadioGroup1,meetingRoom2);
            }
        });

        addCheckBoxGroup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGroupVisibility(v,minimRadioGroup2,meetingRoom3);
            }
        });

        minimRadioGroup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGroupVisibility(v,addCheckBoxGroup1,meetingRoom2);
            }
        });

        minimRadioGroup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGroupVisibility(v,addCheckBoxGroup2,meetingRoom3);
            }
        });
    }

    private void setGroupVisibility(View v, ImageButton add, LinearLayout group){
        setVisibilityView(v);
        setVisibilityView(add);
        setVisibilityView(group);
    }

    private void setVisibilityView(View v) {
        if (v.getVisibility() == View.VISIBLE)
            v.setVisibility(View.GONE);
        else v.setVisibility(View.VISIBLE);
    }

    private void initWidgetLocationDialog(Dialog locationDialog) {
        addCheckBoxGroup1 = locationDialog.findViewById(R.id.dialog_location_button_addone);
        addCheckBoxGroup2 = locationDialog.findViewById(R.id.dialog_location_button_addtwo);
        minimRadioGroup1 = locationDialog.findViewById(R.id.dialog_location_button_minimalizeone);
        minimRadioGroup2 = locationDialog.findViewById(R.id.dialog_location_button_minimalizetwo);
        meetingRoom2 = locationDialog.findViewById(R.id.dialog_location_groupcheckboxtwo);
        meetingRoom3 = locationDialog.findViewById(R.id.dialog_location_groupcheckboxthree);
        closeDialogLocation = locationDialog.findViewById(R.id.close_cross_imageview_location);
        filterButtonLocation = locationDialog.findViewById(R.id.dialog_location_button_filter);
        cancelButtonLocation = locationDialog.findViewById(R.id.dialog_location_button_cancel);
        a = locationDialog.findViewById(R.id.dialog_location_radio_button_a);
        b = locationDialog.findViewById(R.id.dialog_location_radio_button_b);
        c = locationDialog.findViewById(R.id.dialog_location_radio_button_c);
        d = locationDialog.findViewById(R.id.dialog_location_radio_button_d);
        e = locationDialog.findViewById(R.id.dialog_location_radio_button_e);
        f = locationDialog.findViewById(R.id.dialog_location_radio_button_f);
        g = locationDialog.findViewById(R.id.dialog_location_radio_button_g);
        h = locationDialog.findViewById(R.id.dialog_location_radio_button_h);
        i = locationDialog.findViewById(R.id.dialog_location_radio_button_i);
        j = locationDialog.findViewById(R.id.dialog_location_radio_button_j);

    }

    /**
     * Fired if the user clicks on a delete button
     *
     * @param event
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        service.deleteMeeting(event.meeting);
        mListMeeting = service.getMeetingList();
        initList();
        Toast.makeText(this, "Meeting is deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
