package com.example.mareu.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.event.DeleteMeetingEvent;
import com.example.mareu.model.Meeting;
import com.example.mareu.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static android.content.ContentValues.TAG;


public class ListMeetingPageAdapter extends RecyclerView.Adapter<ListMeetingPageAdapter.ViewHolder> {
    private List<Meeting> mDataSet;
    Context context;

    public ListMeetingPageAdapter(List<Meeting> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title_one, description;
        private ImageButton deleteButton;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            title_one = v.findViewById(R.id.item_list_name);
            description =  v.findViewById(R.id.item_list_description);
            deleteButton = v.findViewById(R.id.item_list_delete_button);
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

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
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
       String sentenceTitle = "Réunion " + meeting.getLocation() + " - " + meeting.getHour().hourToString() +
                " - " + meeting.getUser().getUserName();
        viewHolder.title_one.setText(sentenceTitle);

        String listMail = "";
        for (int i = 0; i<meeting.getListMail().size(); i++){
            listMail = listMail + meeting.getListMail().get(i) + ", ";
        }
        viewHolder.description.setText(listMail);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, Utils.dateToString(meeting.getMeetingDate()), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteMeetingEvent(meeting));            }
        });
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
