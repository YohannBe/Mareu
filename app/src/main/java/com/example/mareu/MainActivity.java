package com.example.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import java.util.List;
import java.util.Objects;

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
        initMainActivity();
    }

    private void initMainActivity() {
        service = Injection.getListMeetingService();
        mListMeeting = service.getMeetingList();
        addMeetingButton = findViewById(R.id.add_meeting_floatting_button);
        recyclerView = findViewById(R.id.recycleView_list_item);

        dateDialog = new Dialog(this);
        locationDialog = new Dialog(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        ListMeetingPageAdapter customAdapter = new ListMeetingPageAdapter(mListMeeting, MainActivity.this);
        recyclerView.setAdapter(customAdapter);

        sendUserToCreateMeeting(addMeetingButton);
    }

    private void sendUserToCreateMeeting(FloatingActionButton addMeetingButton) {
        addMeetingButton.setOnClickListener(v -> {
            Intent toAddMeetingActivity = new Intent(MainActivity.this, AddMeeting.class);
            startActivity(toAddMeetingActivity);
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

    /**
     * ----- inflate the menu in the toolbar by taking te menu layout------
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * ----- switch will decide what to do new with the user input in the menu
     * 1 show the filter on location/room
     * 2 show filter on the date
     * 3 show both filters in order to filter the meeting with both of those constraint------
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    /**
     * ----- build and show the dialog filter on date ------
     */

    private void showDialogDate(boolean filteredBoth) {
        dateDialog.setContentView(R.layout.custom_dialog_date);
        initWidgetDialog(dateDialog);
        initClickListenerDateDialog(filteredBoth);

        Objects.requireNonNull(dateDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    /**
     * ----- init every views the user can click on
     * 1 textview "with range" which will collapse or expend itself in order to show another DatePicker
     * 2 texview withoutrange which will collapse or expend itself in order to show a radio button group with 3 choices
     * 3 cancel button in order to dismiss the dialog
     * 4 a cross on the upper right corner in order to dismiss the dialog------
     */

    private void initClickListenerDateDialog(final boolean filteredBoth) {
        withRange.setOnClickListener(view -> setVisibilityView(rangeLayout));

        withoutRange.setOnClickListener(v -> setVisibilityView(withoutRangeLayout));

        cancelButtonDate.setOnClickListener(v -> dateDialog.dismiss());

        filterButtonDate.setOnClickListener(v -> {
            chosenDate = new Date(datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());
            checkDialogInputRange(filteredBoth);
            dateDialog.dismiss();
        });
        closeDialogDate.setOnClickListener(v -> dateDialog.dismiss());
    }

    /**
     * ----- check if the date is right, and if so start filter
     * 1 if both views (withoutRangeLayout && rangeLayout) have the same visibility (both collapsed or both extended) a toast with an error will appear
     * 2 if the view withoutRangeLayout is visible, then the filter will take into account solely the radio button group
     * 2.1 if choice is not null then init the recyclerview, else toast with an error
     * 3 if range is expended first we check if the second date is greater than the first and then we return true in order to init the recyclerview with the meeting list in between the range
     * ------
     */

    private void checkDialogInputRange(boolean filteredBoth) {
        if (withoutRangeLayout.getVisibility() == rangeLayout.getVisibility()) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.error_method_date_dialog),
                    Toast.LENGTH_LONG)
                    .show();
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
                    showDialogLocation(true);
                } else mListMeeting = service.getFilteredMeetingList(choice, chosenDate);
                initList();
            } else
                Toast.makeText(MainActivity.this, getResources().getString(R.string.error_radiobutton_date),
                        Toast.LENGTH_LONG)
                        .show();
        } else {
            boolean range = Utils.compareDate(chosenDate,
                    new Date(datePickerRange.getDayOfMonth(), datePickerRange.getMonth(), datePickerRange.getYear()));

            if (range) {
                if (filteredBoth) {
                    filteredList1 = service.getFilteredMeetingListRange(chosenDate,
                            new Date(datePickerRange.getDayOfMonth(), datePickerRange.getMonth() + 1, datePickerRange.getYear()));
                    showDialogLocation(true);
                } else {
                    mListMeeting = service.getFilteredMeetingListRange(chosenDate,
                            new Date(datePickerRange.getDayOfMonth(), datePickerRange.getMonth() + 1, datePickerRange.getYear()));
                    initList();
                }
            } else {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.error_range_date),
                        Toast.LENGTH_LONG)
                        .show();
            }
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


    /**
     * ----- build and show the dialog filter on location ------
     */

    private void showDialogLocation(final boolean filteredBoth) {
        locationDialog.setContentView(R.layout.custom_dialog_location);
        initWidgetLocationDialog(locationDialog);
        initOnclickDialogLocation(locationDialog, filteredBoth);

        Objects.requireNonNull(locationDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        locationDialog.show();
    }

    private void initOnclickDialogLocation(Dialog locationDialog, final boolean filteredBoth) {
        closeDialogLocation.setOnClickListener(v -> locationDialog.dismiss());
        cancelButtonLocation.setOnClickListener(v -> locationDialog.dismiss());

        filterButtonLocation.setOnClickListener(v -> {
            chosenRooms = Utils.checkDialogBoxes(a, b, c, d, e, f, g, h, i, j);
            if (chosenRooms.size() > 0) {
                if (filteredBoth) {
                    filteredList2 = service.getFilteredMeetingListLocation(chosenRooms);
                    mListMeeting = service.getFilteredBothMeetingList(filteredList1, filteredList2);
                } else mListMeeting = service.getFilteredMeetingListLocation(chosenRooms);
                initList();
                locationDialog.dismiss();
            } else
                Toast.makeText(MainActivity.this, getResources().getString(R.string.nothing_chosen_dialog_filter_room), Toast.LENGTH_SHORT).show();
        });

        addCheckBoxGroup1.setOnClickListener(v -> setGroupVisibility(v, minimRadioGroup1, meetingRoom2));

        addCheckBoxGroup2.setOnClickListener(v -> setGroupVisibility(v, minimRadioGroup2, meetingRoom3));

        minimRadioGroup1.setOnClickListener(v -> setGroupVisibility(v, addCheckBoxGroup1, meetingRoom2));

        minimRadioGroup2.setOnClickListener(v -> setGroupVisibility(v, addCheckBoxGroup2, meetingRoom3));
    }

    private void setGroupVisibility(View v, ImageButton add, LinearLayout group) {
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
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        service.deleteMeeting(event.meeting);
        mListMeeting = service.getMeetingList();
        initList();
        Toast.makeText(this, getResources().getString(R.string.meeting_deleted_toast), Toast.LENGTH_SHORT).show();
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
