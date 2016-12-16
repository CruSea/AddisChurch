package com.gcme.addischurch.addischurch.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcme.addischurch.addischurch.Model.Schedules;
import com.gcme.addischurch.addischurch.R;

import java.util.List;

/**
 * Created by kzone on 12/10/2016.
 */

public class ScheduleAdapter extends BaseAdapter {
    private List<Schedules> churchSchedule;
    private Context myContext;

    public ScheduleAdapter(List<Schedules> churchSchedule, Context myContext) {
        this.churchSchedule = churchSchedule;
        this.myContext = myContext;
    }

    @Override
    public int getCount() {
        return churchSchedule.size();
    }

    @Override
    public Object getItem(int i) {
        return churchSchedule.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.schedule_list_item, null, false);
        TextView tvcategory= (TextView) view.findViewById(R.id.schedulecategory);
        TextView tvDate= (TextView) view.findViewById(R.id.scheduledate);
        TextView tvTime= (TextView) view.findViewById(R.id.scheduletime);
        tvcategory.setText(churchSchedule.get(i).getCatagory());
        tvDate.setText(churchSchedule.get(i).getDate());
        tvTime.setText(churchSchedule.get(i).getTime());
        return view;
    }
}
