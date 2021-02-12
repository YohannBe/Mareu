package com.example.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.mareu.view.ListMeetingPageAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListMeetingApiService service;
    private List<Meeting> mListMeeting;
    RecyclerView recyclerView;
    FloatingActionButton addMeetingButton;
    private Dialog dateDialog;
    private boolean rangeCollapsed, withoutRangeCollapsed;
    private TextView withRange, withoutRange;
    private Button filterButtonDate, cancelButtonDate;
    private LinearLayout rangeLayout;
    private RadioGroup withoutRangeLayout;
    private RadioButton radioButtonSpecific, radioButtonBefore, radioButtonAfter;
    private DatePicker datePicker, datePickerRange;
    private Date dateService;
    private ImageView closeDialog;

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
        dateService = new Date();
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
                showDialogLocation();
                return true;
            case R.id.menu_filter_date:
                showDialogDate();
                return true;
            case R.id.menu_filter_clear:
                mListMeeting = service.getMeetingList();
                initList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogDate() {
        dateDialog.setContentView(R.layout.custom_dialog_date);
        initWidgetDialog(dateDialog);
        initClickListenerDateDialog();

        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    private void initClickListenerDateDialog() {
        withRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rangeCollapsed) {
                    rangeLayout.setVisibility(View.VISIBLE);
                    rangeCollapsed = false;
                } else {

                    rangeLayout.setVisibility(View.GONE);
                    rangeCollapsed = true;
                }
            }
        });

        withoutRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (withoutRangeCollapsed) {
                    withoutRangeLayout.setVisibility(View.VISIBLE);
                    withoutRangeCollapsed = false;
                } else {
                    withoutRangeLayout.setVisibility(View.GONE);
                    withoutRangeCollapsed = true;

                }
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
                if (checkDialogInputRange()) {
                    mListMeeting = service.getFilteredMeetingListRange(new Date(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear()),
                            new Date(datePickerRange.getDayOfMonth(), datePickerRange.getMonth(), datePickerRange.getYear()));
                    initList();
                }
                dateDialog.dismiss();
            }
        });

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.dismiss();
            }
        });

    }

    private boolean checkDialogInputRange() {
        if (withoutRangeLayout.getVisibility() == rangeLayout.getVisibility()) {
            Toast.makeText(MainActivity.this, "You can choose only one filter method, please expend only the spinner or the radio button",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        } else if (withoutRangeLayout.getVisibility() == View.VISIBLE && rangeLayout.getVisibility() != View.VISIBLE) {
            String choice = null;
            if (radioButtonSpecific.isChecked()) {
                choice = "specific";
            } else if (radioButtonBefore.isChecked()) {
                choice = "before";
            } else if (radioButtonAfter.isChecked()) {
                choice = "after";
            }
            if (choice != null) {
                mListMeeting = service.getFilteredMeetingList(choice, new Date(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear()));
                initList();
                return false;
            } else
                Toast.makeText(MainActivity.this, "You have to choose at least on radio button",
                        Toast.LENGTH_LONG)
                        .show();
            return false;
        } else {
            boolean range = dateService.compareDate(new Date(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear()),
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
        rangeCollapsed = true;
        withoutRangeCollapsed = true;
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
        closeDialog = dateDialog.findViewById(R.id.close_cross_imageview);
    }

    private void showDialogLocation() {
        Toast.makeText(MainActivity.this, "zoubi2", Toast.LENGTH_SHORT).show();
    }

    /**
     * Fired if the user clicks on a delete button
     *
     * @param event
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        mListMeeting.remove(event.meeting);
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
