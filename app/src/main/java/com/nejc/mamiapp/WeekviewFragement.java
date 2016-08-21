package com.nejc.mamiapp;

// First fragment class representing a layout that is swipeable

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;


public class WeekviewFragement extends android.support.v4.app.Fragment {
    int month;
    int year;

    public WeekviewFragement() {
        super();
    }

    // Attach layout to the week overview Fragment instance
    public void attach_layout(int month, int year){
        this.month=month;
        this.year=year;
}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create the fragment layout out of default values
        View listviewLayout = inflater.inflate(R.layout.month_input_fragment, container, false);

        // GUI elements of the layout
        ListView listview_left= (ListView) listviewLayout.findViewById(R.id.listView);
        ListView listview_right = (ListView) listviewLayout.findViewById(R.id.listView_right);
        ImageView month_title = (ImageView) listviewLayout.findViewById(R.id.month_title);

        ArrayList<String> arrayList = new ArrayList<String>();
        // TODO: Use string.xml
        arrayList.add("P");
        arrayList.add("T");
        arrayList.add("S");
        arrayList.add("Č");
        arrayList.add("P");
        arrayList.add("S");
        arrayList.add("N");

        // Attach adapters to the listviews
        CustomAdapter adapter_listview_left = new CustomAdapter(getActivity().getApplicationContext(), arrayList, false, month, year);
        CustomAdapter adapter_listview_right = new CustomAdapter(getActivity().getApplicationContext(), arrayList, true, month, year);
        listview_left.setAdapter(adapter_listview_left);
        listview_right.setAdapter(adapter_listview_right);

        switch(month){
            case Calendar.JANUARY:
                month_title.setImageResource(R.drawable.januar);
                break;
            case Calendar.FEBRUARY:
                month_title.setImageResource(R.drawable.februar);
                break;
            case Calendar.MARCH:
                month_title.setImageResource(R.drawable.marec);
                break;
            case Calendar.APRIL:
                month_title.setImageResource(R.drawable.april);
                break;
            case Calendar.MAY:
                month_title.setImageResource(R.drawable.maj);
                break;
            case Calendar.JUNE:
                month_title.setImageResource(R.drawable.junij);
                break;
            case Calendar.JULY:
                month_title.setImageResource(R.drawable.julij);
                break;
            case Calendar.AUGUST:
                month_title.setImageResource(R.drawable.avgust);
                break;
            case Calendar.SEPTEMBER:
                month_title.setImageResource(R.drawable.september);
                break;
            case Calendar.OCTOBER:
                month_title.setImageResource(R.drawable.oktober);
                break;
            case Calendar.NOVEMBER:
                month_title.setImageResource(R.drawable.november);
                break;
            case Calendar.DECEMBER:
                month_title.setImageResource(R.drawable.december);
                break;
        }

        return listviewLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
