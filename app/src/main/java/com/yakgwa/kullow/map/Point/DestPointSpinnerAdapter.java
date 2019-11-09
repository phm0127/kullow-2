package com.yakgwa.kullow.map.Point;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.yakgwa.kullow.R;

import java.util.List;

public class DestPointSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private LayoutInflater mInflater;
    private List<Point> mItems;

    public DestPointSpinnerAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(mItems != null){
            return mItems.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(mItems != null){
            return mItems.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        if(mItems != null){
            return mItems.get(i).getPid();
        }
        return -1;
    }

    public void setPoints(List<Point> Points) {
        this.mItems = Points;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.spinner_dest_point, viewGroup, false);
        }

        Point item = (Point) getItem(i);
        TextView textView = view.findViewById(R.id.spinnerdestpointtext);
        textView.setText(item.getName());
        return view;
    }
    // Drop down item view as stated in the method name.
    @Override
    public View getDropDownView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.spinner_dest_dropdown_point, parent, false);
        }
        Point item = (Point) getItem(i);
        TextView textView = view.findViewById(R.id.spinnerdropdowndestpointtext);
        textView.setText(item.getName());
        return view;
    }
}
