package com.mobley.contactdavid3.dialogs;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.mobley.contactdavid3.ContactDavid3App;
import com.mobley.contactdavid3.R;

public class WorkTimeDialog extends AppCompatDialogFragment implements View.OnClickListener {
    protected static final String TAG = WorkTimeDialog.class.getSimpleName();

    private ContactDavid3App mApp;
    private TimePicker mWorkTimeTP;
    private Button mGoButton;
    private String mTime;
    private boolean mProcessingStartTime = false;

    public static AppCompatDialogFragment newInstance() {
        AppCompatDialogFragment dialogFragment = new WorkTimeDialog();
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
        if (mProcessingStartTime) {
            dialog.setTitle(getString(R.string.work_time_start_title));
        } else {
            dialog.setTitle(getString(R.string.work_time_end_title));
        }

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.work_time_dialog, null);

        mWorkTimeTP = view.findViewById(R.id.workTimeTP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mWorkTimeTP.setHour(getHour(mTime));
            mWorkTimeTP.setMinute(getMinutes(mTime));
        }

        mGoButton = view.findViewById(R.id.goButton);
        mGoButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        int startHour=12, startMinutes=0, endHour=12, endMinutes=0;

        if (view == mGoButton) {
            //hideKeyboard(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (mProcessingStartTime) {
                    startHour = mWorkTimeTP.getHour();
                    startMinutes = mWorkTimeTP.getMinute();
                } else {
                    endHour = mWorkTimeTP.getHour();
                    endMinutes = mWorkTimeTP.getMinute();
                }
            }

            if (mProcessingStartTime) {
                String szStartTime = String.valueOf(startHour).concat(":").concat(String.valueOf(startMinutes));

                SharedPreferences.Editor editor = mApp.getAppPrefs().edit();
                editor.putString(ContactDavid3App.PREF_TIME_START_KEY, szStartTime);
                //editor.putString(ContactDavid3App.PREF_TIME_START_KEY, mStartET.getText().toString());
                editor.commit();
            } else {
                String szEndTime = String.valueOf(endHour).concat(":").concat(String.valueOf(endMinutes));

                SharedPreferences.Editor editor = mApp.getAppPrefs().edit();
                editor.putString(ContactDavid3App.PREF_TIME_END_KEY, szEndTime);
                //editor.putString(ContactDavid3App.PREF_TIME_END_KEY, mEndET.getText().toString());
                editor.commit();
            }

            dismiss();
        }
    }

    private int getHour(String theTime) {
        String hourStr, minuteStr;

        int colon = theTime.indexOf(':');
        if (colon == -1) {
            hourStr = theTime;
            minuteStr = "0";
        } else {
            hourStr = theTime.substring(0, colon);
            minuteStr = theTime.substring(colon+1);
        }
        return Integer.parseInt(hourStr);
    }

    private int getMinutes(String theTime) {
        String hourStr, minuteStr;

        int colon = theTime.indexOf(':');
        if (colon == -1) {
            hourStr = theTime;
            minuteStr = "0";
        } else {
            hourStr = theTime.substring(0, colon);
            minuteStr = theTime.substring(colon+1);
        }
        return Integer.parseInt(minuteStr);
    }

    public void setTime(String theTime) {
        mTime = theTime;
    }

    public void setProcessingStartTime(boolean mStartTime) {
        mProcessingStartTime = mStartTime;
    }
}
