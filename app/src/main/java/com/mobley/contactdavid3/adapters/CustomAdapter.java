package com.mobley.contactdavid3.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobley.contactdavid3.ContactDavid3App;
import com.mobley.contactdavid3.R;
import com.mobley.contactdavid3.sql.tables.Actions;

import java.util.Calendar;
import java.util.List;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    protected static final String TAG = CustomAdapter.class.getSimpleName();

    private ContactDavid3App mApp;
    private List<Actions> mActions;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView actionsRowType, actionsRowTimestamp, actionsRowSendTo;

        public ViewHolder(View v) {
            super(v);
            actionsRowType = v.findViewById(R.id.actionsRowType);
            actionsRowTimestamp = v.findViewById(R.id.actionsRowTimestamp);
            actionsRowSendTo = v.findViewById(R.id.actionsRowSendTo);
        }

        public TextView getActionsRowType() {
            return actionsRowType;
        }

        public TextView getActionsRowTimestamp() {
            return actionsRowTimestamp;
        }

        public TextView getActionsRowSendTo() {
            return actionsRowSendTo;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param actions List<Actions> containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter(List<Actions> actions, ContactDavid3App app) {
        mActions = actions;
        mApp = app;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.actions_row, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getActionsRowType().setText(mActions.get(position).getType());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mActions.get(position).getTimestamp());
        viewHolder.getActionsRowTimestamp().setText(
                String.format(mApp.getString(R.string.timestamp_str),
                                cal.get(Calendar.MONTH) + 1,
                                cal.get(Calendar.DAY_OF_MONTH),
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                cal.get(Calendar.SECOND)));

        //viewHolder.getActionsRowSendTo().setText(mActions.get(position).getSendTo());
        String sendTo = mActions.get(position).getSendTo();

        if ((mActions.get(position).getType().equals("Text")) ||
           (mActions.get(position).getType().equals("Phone")))
        {
            int nFound = sendTo.indexOf("tel:");
            if (nFound == -1) {
                viewHolder.getActionsRowSendTo().setText(formatPhoneNumber(sendTo));
            } else {
                // remove "tel:"
                viewHolder.getActionsRowSendTo().setText(formatPhoneNumber(sendTo.substring(nFound+4)));
            }
        } else {
            // Email
            viewHolder.getActionsRowSendTo().setText(sendTo);
        }
    }

    private String formatPhoneNumber(String phoneNumber) {
        StringBuffer sb = new StringBuffer();

        sb.append('(');
        sb.append(phoneNumber.substring(0, 3));
        sb.append(") ");
        sb.append(phoneNumber.substring(3, 6));
        sb.append('-');
        sb.append(phoneNumber.substring(6));

        return sb.toString();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mActions.size();
    }
}
