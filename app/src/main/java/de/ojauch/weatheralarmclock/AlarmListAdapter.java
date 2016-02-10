package de.ojauch.weatheralarmclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * ArrayAdapter to display the alarm information in a list view
 *
 * @author Oskar Jauch
 */
public class AlarmListAdapter extends ArrayAdapter<Alarm> {
    public AlarmListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AlarmListAdapter(Context context, int resource, List<Alarm> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.alarm_row, null);
        }

        Alarm alarm = getItem(position);

        if (alarm != null) {
            TextView textTime = (TextView) v.findViewById(R.id.alarm_row_time);
            Switch switchEnabled = (Switch) v.findViewById(R.id.alarm_row_enabled);
            CheckBox checkRain = (CheckBox) v.findViewById(R.id.alarm_row_rain);
            CheckBox checkFrost = (CheckBox) v.findViewById(R.id.alarm_row_frost);

            if (textTime != null) {
                textTime.setText(alarm.getTime());
            }

            if (switchEnabled != null) {
                switchEnabled.setChecked(alarm.isEnabled());
            }

            if (checkRain != null) {
                checkRain.setChecked(alarm.getRain());
            }

            if (checkFrost != null) {
                checkFrost.setChecked(alarm.getFrost());
            }
        }

        return v;
    }
}
