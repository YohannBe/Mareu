package com.example.mareu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mareu.injection.Injection;
import com.example.mareu.model.Date;
import com.example.mareu.model.Hour;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.User;
import com.example.mareu.service.ListMeetingApiService;
import com.example.mareu.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddMeeting extends AppCompatActivity {
    private EditText descriptionEdittext, userNameEdittext, userMailEdittext, durationHour, durationMinute;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private LinearLayout parentLinearLayout;
    private final List<View> rowViewList = new ArrayList<>();
    private Dialog resumeDialog;
    private ImageView close, mCircle;
    private TextView userText, locationText, dateText, hourText, descriptionText, userDateAskedText, durationTextView;
    private Button addNewMeetingButton, cancelDialog;
    private Meeting meeting;
    private ListMeetingApiService service;
    private RadioButton a, b, c, d, e, f, g, h, i, j, blue, red, purple, orange;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        initAddMeetingActivity();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void initAddMeetingActivity() {
        service = Injection.getListMeetingService();
        context = getApplicationContext();
        a = findViewById(R.id.add_meeting_radio_button_a);
        b = findViewById(R.id.add_meeting_radio_button_b);
        c = findViewById(R.id.add_meeting_radio_button_c);
        d = findViewById(R.id.add_meeting_radio_button_d);
        e = findViewById(R.id.add_meeting_radio_button_e);
        f = findViewById(R.id.add_meeting_radio_button_f);
        g = findViewById(R.id.add_meeting_radio_button_g);
        h = findViewById(R.id.add_meeting_radio_button_h);
        i = findViewById(R.id.add_meeting_radio_button_i);
        j = findViewById(R.id.add_meeting_radio_button_j);
        blue = findViewById(R.id.add_meeting_radio_blue);
        red = findViewById(R.id.add_meeting_radio_red);
        purple = findViewById(R.id.add_meeting_radio_purple);
        orange = findViewById(R.id.add_meeting_radio_orange);
        timePicker = findViewById(R.id.hour_timepicker);
        datePicker = findViewById(R.id.date_calendar);
        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        descriptionEdittext = findViewById(R.id.description_edittext);
        userNameEdittext = findViewById(R.id.userName_edittext);
        userMailEdittext = findViewById(R.id.userMail_edittext);
        resumeDialog = new Dialog(this);
        durationHour = findViewById(R.id.add_meeting_duration_hour);
        durationMinute = findViewById(R.id.add_meeting_duration_minute);
    }


    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.participant_item, null);
        rowViewList.add(rowView);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
        rowViewList.remove(v.getParent());
        parentLinearLayout.removeView((View) v.getParent());
    }

    public void showCalendar(View view) {
        if (datePicker.getVisibility() == View.GONE) {
            startTransformation(view);
            datePicker.setVisibility(View.VISIBLE);
        } else {
            startTransformation(view);
            datePicker.setVisibility(View.GONE);
        }
    }

    public void showTimePiker(View view) {
        if (timePicker.getVisibility() == View.GONE) {
            startTransformation(view);
            timePicker.setVisibility(View.VISIBLE);
        } else {
            startTransformation(view);
            timePicker.setVisibility(View.GONE);
        }
    }

    public void startTransformation(View view) {
        TransitionManager.beginDelayedTransition((ViewGroup) view.getRootView(), new AutoTransition());
    }

    public void checkDialog(View view) {

        String location, date, hour, description, userName, userMail, duration;

        location = Utils.checkAndAddLocation(context, a, b, c, d, e, f, g, h, i, j);

        Date dateObject = new Date(datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());
        date = Utils.dateToString(dateObject);

        Hour hourObject = new Hour(timePicker.getHour(), timePicker.getMinute());
        hour = Utils.hourToString(hourObject);

        userName = Utils.checkAndAddString(context, userNameEdittext);
        userMail = Utils.checkAndAddString(context, userMailEdittext);
        User user = new User(userName, userMail);

        String hourDuration = durationHour.getText().toString();
        String minuteDuration = durationMinute.getText().toString();


        description = Utils.checkAndAddString(context, descriptionEdittext);

        List<String> getText = getTextFromEditTextParticipant();

        if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(hour) && getText != null && location != null && Utils.checkDuration(hourDuration, minuteDuration)
                && Utils.checkMeetingDisponibility(service, location, dateObject, hourObject, hourDuration, minuteDuration, getApplicationContext())) {
            Hour durationObject = new Hour(Integer.parseInt(hourDuration), Integer.parseInt(minuteDuration));
            meeting = new Meeting(hourObject, dateObject, dateObject, location, getText, user, description, getCircle(), durationObject);
            openDialog(location, date, hour, description, userName, userMail, durationObject);
        } else
            Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
    }

    private void openDialog(String location, String date, String hour, String description, String userName, String userMail, Hour durationObject) {

        resumeDialog.setContentView(R.layout.custom_dialogtwo);
        initDialog(resumeDialog);
        initText(location, date, hour, description, userName, durationObject);
        initCircle();
        resumeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close.setOnClickListener(v -> resumeDialog.dismiss());
        cancelDialog.setOnClickListener(v -> resumeDialog.dismiss());
        addNewMeetingButton.setOnClickListener(v -> {
            service.addMeeting(meeting);
            Intent toMainActivity = new Intent(AddMeeting.this, MainActivity.class);
            startActivity(toMainActivity);
        });
        resumeDialog.show();
    }

    private void initCircle() {
        if (blue.isChecked())
            mCircle.setImageResource(R.drawable.circle_view_blue);
        else if (red.isChecked())
            mCircle.setImageResource(R.drawable.circle_view_red);
        else if (purple.isChecked())
            mCircle.setImageResource(R.drawable.circle_view_purple);
        else if (orange.isChecked())
            mCircle.setImageResource(R.drawable.circle_view_orange);
    }

    private int getCircle() {
        if (blue.isChecked())
            return R.drawable.circle_view_blue;
        else if (red.isChecked())
            return R.drawable.circle_view_red;
        else if (purple.isChecked())
            return R.drawable.circle_view_purple;
        else if (orange.isChecked())
            return R.drawable.circle_view_orange;
        return R.drawable.circle_view;
    }

    private void initText(String location, String date, String hour, String description, String userName, Hour durationObject) {

        String userNameS = getString(R.string.admin_field_dialog_resum) +" "+ userName;
        String roomS = getString(R.string.room_field_dialog_resum)+" " + location;
        String dateS = getString(R.string.date_field_dialog_resum)+" " + date;
        String hourS = getString(R.string.hour_field_dialog_resum)+" " + hour;
        String topicS = getString(R.string.topic_field_dialog_resum) +" "+ description;
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.pattern_date));
        String currentDate = getString(R.string.made_the_field_dialog_resum) +" " + sdf.format(new java.util.Date());
        String durationS = getString(R.string.duration_add_meeting)+" " + Utils.hourToString(durationObject);

        userText.setText(userNameS);
        locationText.setText(roomS);
        dateText.setText(dateS);
        hourText.setText(hourS);
        descriptionText.setText(topicS);
        userDateAskedText.setText(currentDate);
        durationTextView.setText(durationS);
    }

    private void initDialog(Dialog resumeDialog) {
        userText = resumeDialog.findViewById(R.id.dialog_user_name);
        locationText = resumeDialog.findViewById(R.id.dialog_location);
        dateText = resumeDialog.findViewById(R.id.dialog_date);
        hourText = resumeDialog.findViewById(R.id.dialog_hour);
        descriptionText = resumeDialog.findViewById(R.id.dialog_description);
        userDateAskedText = resumeDialog.findViewById(R.id.dialog_user_asked_date);
        close = resumeDialog.findViewById(R.id.close_cross_imageview);
        addNewMeetingButton = resumeDialog.findViewById(R.id.dialog_button_add);
        cancelDialog = resumeDialog.findViewById(R.id.dialog_button_cancel);
        mCircle = resumeDialog.findViewById(R.id.add_meeting_dialog_circle);
        durationTextView = resumeDialog.findViewById(R.id.dialog_duration);
    }

    private List<String> getTextFromEditTextParticipant() {
        View viewRow;
        EditText editText;
        List<String> allParticipant = new ArrayList<>();
        for (int i = 0; i < rowViewList.size(); i++) {
            viewRow = rowViewList.get(i);
            editText = viewRow.findViewById(R.id.mail_edit_text);
            allParticipant.add(editText.getText().toString());
        }
        if (allParticipant.size() != 0)
            return allParticipant;
        else return null;
    }
}