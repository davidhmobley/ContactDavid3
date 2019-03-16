package com.mobley.contactdavid3.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobley.contactdavid3.R;
import com.mobley.contactdavid3.sql.tables.Actions;

import java.util.List;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    protected static final String TAG = CustomAdapter.class.getSimpleName();

    private List<Actions> mActions;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView actionsRowType, actionsRowTimestamp;

        public ViewHolder(View v) {
            super(v);
            actionsRowType = (TextView) v.findViewById(R.id.actionsRowType);
            actionsRowTimestamp = (TextView) v.findViewById(R.id.actionsRowTimestamp);
        }

        public TextView getActionsRowType() {
            return actionsRowType;
        }

        public TextView getActionsRowTimestamp() {
            return actionsRowTimestamp;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param actions List<Actions> containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter(List<Actions> actions) {
        mActions = actions;
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
        viewHolder.getActionsRowTimestamp().setText(String.valueOf(mActions.get(position).getTimestamp()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mActions.size();
    }
}
