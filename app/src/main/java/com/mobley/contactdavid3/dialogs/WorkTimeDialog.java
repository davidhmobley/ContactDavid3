package com.mobley.contactdavid3.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobley.contactdavid3.ContactDavid3App;
import com.mobley.contactdavid3.R;

public class WorkTimeDialog extends DialogFragment implements View.OnClickListener {
    protected static final String TAG = WorkTimeDialog.class.getSimpleName();

    private ContactDavid3App mApp;
    private TextView mStartTV, mEndTV;
    private EditText mStartET, mEndET;
    private Button mGoButton;
    private String mStartTime;
    private String mEndTime;

    public static DialogFragment newInstance() {
        DialogFragment dialogFragment = new WorkTimeDialog();
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setStyle(DialogFragment.STYLE_NORMAL, R.style.My_DialogStyle);

        mApp = ((ContactDavid3App) getActivity().getApplication());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(getString(R.string.work_time_title));

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.work_time_dialog, null);

        mStartTV = view.findViewById(R.id.startTimeTV);
        mStartET = (EditText) view.findViewById(R.id.startTime);
        mStartET.setText(mStartTime);

        mEndTV = view.findViewById(R.id.endTimeTV);
        mEndET = (EditText) view.findViewById(R.id.endTime);
        mEndET.setText(mEndTime);

        mGoButton = (Button) view.findViewById(R.id.goButton);
        mGoButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        boolean bGood = true;

        if (view == mGoButton) {
            //hideKeyboard(this);

            if (TextUtils.isEmpty(mStartET.getText())) {
                bGood = false;
                mStartTV.setTextColor(Color.RED);
                mStartET.requestFocus();
            } else {
                mStartTV.setTextColor(Color.BLACK);
            }

            if (bGood && TextUtils.isEmpty(mEndET.getText())) {
                bGood = false;
                mEndTV.setTextColor(Color.RED);
                mEndET.requestFocus();
            } else {
                mEndTV.setTextColor(Color.BLACK);
            }

            if (bGood) {
                //save values
                SharedPreferences.Editor editor = mApp.getAppPrefs().edit();
                editor.putString(ContactDavid3App.PREF_TIME_START_KEY, mStartET.getText().toString());
                editor.commit();

                editor = mApp.getAppPrefs().edit();
                editor.putString(ContactDavid3App.PREF_TIME_END_KEY, mEndET.getText().toString());
                editor.commit();

                dismiss();
            }
        }
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }
}
