package com.example.mareu.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.event.DeleteMeetingEvent;
import com.example.mareu.model.Meeting;
import com.example.mareu.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static android.content.ContentValues.TAG;


public class ListMeetingPageAdapter extends RecyclerView.Adapter<ListMeetingPageAdapter.ViewHolder> {
    private final List<Meeting> mDataSet;
    Context context;


    public ListMeetingPageAdapter(List<Meeting> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title_one;
        private final TextView description;
        private final ImageButton deleteButton;
        private final View avatar;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(v1 -> Log.d(TAG, "Element " + getAdapterPosition() + " clicked."));
            title_one = v.findViewById(R.id.item_list_name);
            description = v.findViewById(R.id.item_list_description);
            deleteButton = v.findViewById(R.id.item_list_delete_button);
            avatar = v.findViewById(R.id.item_list_avatar);
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public ListMeetingPageAdapter(Context context, List<Meeting> dataSet) {
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reunion_item, viewGroup, false);
        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        Meeting meeting = mDataSet.get(position);
        String sentenceTitle = "Réunion " + meeting.getLocation() + " - " + Utils.hourToString(meeting.getHour()) +
                " - " + meeting.getUser().getUserName();
        viewHolder.title_one.setText(sentenceTitle);
        Drawable drawable = ContextCompat.getDrawable(context, meeting.getDrawable());
        viewHolder.avatar.setBackground(drawable);

        String listMail = "";

        if (meeting.getListMail().size() == 1)
            listMail = meeting.getListMail().get(0);
        else {
            for (int i = 0; i < meeting.getListMail().size(); i++) {
                listMail = listMail + meeting.getListMail().get(i) + ", ";
            }
        }
        viewHolder.description.setText(listMail);

        viewHolder.itemView.setOnClickListener(
                v -> Toast.makeText(context, Utils.dateToString(meeting.getMeetingDate()), Toast.LENGTH_SHORT).show());

        viewHolder.deleteButton.setOnClickListener(
                v -> EventBus.getDefault().post(new DeleteMeetingEvent(meeting)));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
