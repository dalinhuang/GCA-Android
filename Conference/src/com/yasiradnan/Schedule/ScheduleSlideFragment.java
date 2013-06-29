/*
 * Copyright (c) 2013, Ivan Mylyanyk
 * License: BSD-2 (see LICENSE)
 * 
 * This code was modified with Author permission.
 * Modifications:
 * Added 3 ArrayList
 * Defined Pages = 3
 * Added JSON parsing function
 * Implement switch statement for shwoing page specific list
 * 
 * */

package com.yasiradnan.Schedule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.yasiradnan.conference.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleSlideFragment extends Fragment {

    final static String ARG_PAGE = "page";

    private static ViewPager pager;

    private static int pageNumber;

    final static int totalPages = ScheduleMainActivity.totalPages;

    @SuppressWarnings("unchecked")
    public List<ScheduleItem>[] data = (ArrayList<ScheduleItem>[])new ArrayList[totalPages];

    public int getPageNumber;

    private void jsonParseData(int _getPageNumber) {
        try {
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this
                    .getResources().openRawResource(R.raw.program)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null;) {
                jsonBuilder.append(line).append("\n");
            }

            // Parse Json
            JSONTokener tokener = new JSONTokener(jsonBuilder.toString());
            JSONArray jsonArray = new JSONArray(tokener);
            _getPageNumber = getPageNumber;
            data[_getPageNumber] = new ArrayList<ScheduleItem>();
            JSONObject jsonObject = jsonArray.getJSONObject(_getPageNumber);
            String getDate = jsonObject.getString("date");
            JSONArray getFirstArray = new JSONArray(jsonObject.getString("events"));

            for (int i = 0; i < getFirstArray.length(); i++) {

                JSONObject getJSonObj = (JSONObject)getFirstArray.get(i);
                String time = getJSonObj.getString("time");
                //Log.e("Time Log",time);
                String type = getJSonObj.getString("type");
                String title = getJSonObj.getString("title");
                int typeId = getJSonObj.getInt("type_id");

                data[_getPageNumber].add(new ScheduleItem(time, title, typeId, getDate));

                /*
                 * Get Events
                 */
                if (typeId == 0) {

                    JSONArray getEventsArray = new JSONArray(getJSonObj.getString("events"));

                    for (int j = 0; j < getEventsArray.length(); j++) {

                        JSONObject getJSonEventobj = (JSONObject)getEventsArray.get(j);
                        int typeEventId = getJSonEventobj.getInt("type_id");

                        if (typeEventId == 1) {

                            String EventInfo = getJSonEventobj.getString("info");
                            String EventType = getJSonEventobj.getString("type");
                            String EventTitle = getJSonEventobj.getString("title");
                            String Eventtime = getJSonEventobj.getString("time");
                            data[_getPageNumber].add(new ScheduleItem(Eventtime, EventTitle, EventInfo,
                                    typeEventId, getDate));
                        } else {

                            String EventType = getJSonEventobj.getString("type");
                            String EventTitle = getJSonEventobj.getString("title");
                            String Eventtime = getJSonEventobj.getString("time");
                            data[_getPageNumber].add(new ScheduleItem(Eventtime, EventTitle, typeEventId,
                                    getDate));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.getStackTraceString(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.schedule, container, false);
        getPageNumber = pageNumber;
        /** 
         * JSON Parsing 
         */
        jsonParseData(getPageNumber);
        /**
         * Set header date 
         */
        ((TextView)rootView.findViewById(R.id.tvDay)).setText(data[pageNumber].get(pageNumber).getDate().toString());
        final ListView list = (ListView)rootView.findViewById(R.id.list);
        BinderData bindingData = new BinderData(this.getActivity(), data[pageNumber]);
        list.setAdapter(bindingData);

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                if (data[pager.getCurrentItem()].get(position).getItemType() == 0
                        || data[pager.getCurrentItem()].get(position).getItemType() == 3
                        || data[pager.getCurrentItem()].get(position).getItemType() == 2)
                    return;
                Intent intent = new Intent(ScheduleSlideFragment.this.getActivity(),
                        ContentExtended.class);
                intent.putExtra("title", data[pager.getCurrentItem()].get(position).getTitle());
                intent.putExtra("content", data[pager.getCurrentItem()].get(position).getContent());
                startActivity(intent);
            }
        });

        ImageButton ibLeft = (ImageButton)rootView.findViewById(R.id.ibLeft);
        if (pageNumber == 0)
            ibLeft.setVisibility(View.INVISIBLE);

        else
            ibLeft.setVisibility(View.VISIBLE);

        ibLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() > 0)
                    pager.setCurrentItem(pager.getCurrentItem() - 1, true);
            }
        });

        ImageButton ibRight = (ImageButton)rootView.findViewById(R.id.ibRight);
        if (pageNumber + 1 == totalPages)
            ibRight.setVisibility(View.INVISIBLE);
        else
            ibRight.setVisibility(View.VISIBLE);

        ibRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() < totalPages)
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        });

        return rootView;
    }

    public static Fragment create(int position) {
        Fragment fragment = new ScheduleSlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment create(int position, ViewPager _pager) {
        pageNumber = position;
        pager = _pager;
        Fragment fragment = new ScheduleSlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE);
    }

}
