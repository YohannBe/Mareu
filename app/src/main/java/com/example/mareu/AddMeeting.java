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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mareu.injection.Injection;
import com.example.mareu.model.Date;
import com.example.mareu.model.Hour;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.User;
import com.example.mareu.service.ListMeetingApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class AddMeeting extends AppCompatActivity {
    private boolean hiddenCalendar, hiddenTimePicker;
    private EditText locationEditText, descriptionEdittext, userNameEdittext, userMailEdittext;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private LinearLayout parentLinearLayout;
    private List<View> rowViewList = new ArrayList<View>();
    private boolean empty = false;
    private Dialog resumeDialog;
    private ImageView close;
    private TextView userText, locationText, dateText, hourText, descriptionText, userDateAskedText;
    private Button addNewMeetingButton, cancelDialog;
    private Meeting meeting;
    private ListMeetingApiService service;
    private List<Meeting> mListMeeting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        initWidget();

    }


    private void initWidget() {
        service = Injection.getListMeetingService();
        mListMeeting = service.getMeetingList();
        hiddenCalendar = true;
        hiddenTimePicker = true;
        locationEditText = findViewById(R.id.location_edittext);
        timePicker = findViewById(R.id.hour_timepicker);
        datePicker = findViewById(R.id.date_calendar);
        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        descriptionEdittext = findViewById(R.id.description_edittext);
        userNameEdittext = findViewById(R.id.userName_edittext);
        userMailEdittext = findViewById(R.id.userMail_edittext);
        resumeDialog = new Dialog(this);
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
        if (hiddenCalendar) {
            startTransformation(view);
            datePicker.setVisibility(View.VISIBLE);
            hiddenCalendar = false;
        } else {
            startTransformation(view);
            datePicker.setVisibility(View.GONE);
            hiddenCalendar = true;
        }
    }

    public void showTimePiker(View view) {
        if (hiddenTimePicker) {
            startTransformation(view);
            timePicker.setVisibility(View.VISIBLE);
            hiddenTimePicker = false;
        } else {
            startTransformation(view);
            timePicker.setVisibility(View.GONE);
            hiddenTimePicker = true;
        }
    }

    public void startTransformation(View view) {
        TransitionManager.beginDelayedTransition((ViewGroup) view.getRootView(), new AutoTransition());
    }

    public void checkDialog(View view) {

        String location, date, hour, description, userName, userMail;

        location = checkAndAddString(locationEditText);

        Date dateObject = new Date(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
        date = dateObject.dateToString();

        Hour hourObject = new Hour(timePicker.getHour(), timePicker.getMinute());
        hour = hourObject.hourToString();

        userName = checkAndAddString(userNameEdittext);
        userMail = checkAndAddString(userMailEdittext);
        User user = new User(userName, userMail);

        description = checkAndAddString(descriptionEdittext);

        List<String> getText = getTextFromEditTextParticipant();

        if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(hour) && getText != null && !empty) {
            meeting = new Meeting(hourObject, dateObject, dateObject, location, getText, user, description);
            openDialog(location, date, hour, description, userName, userMail);
        } else
            Toast.makeText(this, "fuck you", Toast.LENGTH_SHORT).show();
    }

    private void openDialog(String location, String date, String hour, String description, String userName, String userMail) {

        resumeDialog.setContentView(R.layout.custom_dialogtwo);
        initDialog(resumeDialog);
        initText(location, date, hour, description, userName, userMail);
        resumeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeDialog.dismiss();
            }
        });
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeDialog.dismiss();
            }
        });
        addNewMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListMeeting.add(meeting);
                Intent toMainActivity = new Intent(AddMeeting.this, MainActivity.class);
                startActivity(toMainActivity);
            }
        });
        resumeDialog.show();
    }

    private void initText(String location, String date, String hour, String description, String userName, String userMail) {
        userText.setText(userName);
        locationText.setText(location);
        dateText.setText(date);
        hourText.setText(hour);
        descriptionText.setText(description);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = sdf.format(new java.util.Date());
        userDateAskedText.setText(currentDate);
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
    }

    private String checkAndAddString(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText().toString()))
            return editText.getText().toString();
        else {
            empty = true;
            Toast.makeText(AddMeeting.this, "it seems that some fields are missing", Toast.LENGTH_SHORT).show();
        }
        return null;
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

    public void close(View view) {
        finish();
    }

}