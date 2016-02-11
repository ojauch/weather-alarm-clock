package de.ojauch.weatheralarmclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import de.ojauch.weatheralarmclock.sql.AlarmDbHelper;

/**
 * ArrayAdapter to display the alarm information in a list view
 *
 * @author Oskar Jauch
 */
public class AlarmListAdapter extends ArrayAdapter<Alarm> {
    AlarmDbHelper mDbHelper;

    public AlarmListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mDbHelper = new AlarmDbHelper(context);
    }

    public AlarmListAdapter(Context context, int resource, List<Alarm> items) {
        super(context, resource, items);
        mDbHelper = new AlarmDbHelper(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.alarm_row, null);
        }

        final Alarm alarm = getItem(position);

        if (alarm != null) {
            TextView textTime = (TextView) v.findViewById(R.id.alarm_row_time);
            Switch switchEnabled = (Switch) v.findViewById(R.id.alarm_row_enabled);
            CheckBox checkRain = (CheckBox) v.findViewById(R.id.alarm_row_rain);
            CheckBox checkFrost = (CheckBox) v.findViewById(R.id.alarm_row_frost);
            Button buttonDelete = (Button) v.findViewById(R.id.alarm_row_delete);

            if (textTime != null) {
                textTime.setText(alarm.getTime());
            }

            if (switchEnabled != null) {
                switchEnabled.setChecked(alarm.isEnabled());
                switchEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Alarm alarmChanged = getItem(position);
                        if (isChecked) {
                            alarmChanged.enable();
                        } else {
                            alarmChanged.disable();
                        }
                        mDbHelper.updateAlarmEntry(alarmChanged);
                    }
                });
            }

            if (checkRain != null) {
                checkRain.setChecked(alarm.getRain());
                checkRain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Alarm alarmChanged = getItem(position);
                        alarmChanged.setRain(isChecked);
                        mDbHelper.updateAlarmEntry(alarmChanged);
                    }
                });
            }

            if (checkFrost != null) {
                checkFrost.setChecked(alarm.getFrost());
                checkFrost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Alarm alarmChanged = getItem(position);
                        alarmChanged.setFrost(isChecked);
                        mDbHelper.updateAlarmEntry(alarmChanged);
                    }
                });
            }

            if (buttonDelete != null) {
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Alarm currentAlarm = getItem(position);
                        mDbHelper.remove(currentAlarm);
                        remove(currentAlarm);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        return v;
    }
}
