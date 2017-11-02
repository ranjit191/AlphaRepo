package com.textmaxx.listview_adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.textmaxx.R;
import com.textmaxx.models.ModelTabGrps;
import com.textmaxx.models.ModelTabHome;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class AdapterGroup extends BaseAdapter {
    Context context;
    List<ModelTabGrps> rowItems;
    SharedPreferences prefs;
    List<String> array_cellno = new ArrayList<String>();

    {

    }

//    public AdapterHome(FragmentActivity activity, RealmResults<ModelTabHome> modelTabHomes) {
//    }

    public AdapterGroup(Context context, List<ModelTabGrps> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
        this.array_cellno = array_cellno;
    }

//    public AdapterGroup(Context context, List<ModelTabHome> rowItems) {
//    }
//
//    public AdapterGroup(Context context, List<ModelTabHome> rowItems) {
//    }

//    public AdapterGroup(FragmentActivity activity, RealmResults<ModelTabGrps> modelTabGrpses) {
//    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
//        return rowItems.indexOf(getItem(position));
        return rowItems.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custlist_tab_grps, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.img_info = (ImageView) convertView.findViewById(R.id.img_info);
//            holder.rel_info = (RelativeLayout) convertView.findViewById(R.id.rel_info);
//            holder.txt_msg = (TextView) convertView.findViewById(R.id.txt_msg);
//            holder.txt_time = (TextView) convertView.findViewById(R.id.tv_time);
//            holder.txt_no = (TextView) convertView.findViewById(R.id.txt_no);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ModelTabGrps rowItem = (ModelTabGrps) getItem(position);


//set name

        holder.name.setText(rowItem.getName());
//        holder.txt_no.setText(rowItem.getCount());
//        holder.txt_msg.setText(rowItem.getMessage());
//        if (rowItem.getSentTime() != null && rowItem.getSentTime().trim().length() > 0) {
//            String datetime = rowItem.getSentTime();
//            String time = datetime.split("T")[1];

//
//            try {
//                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
//                final Date dateObj = sdf.parse(time);
//                String timein12Format = new SimpleDateFormat("K:mm a").format(dateObj);
//                holder.txt_time.setText(timein12Format);
//            } catch (final ParseException e) {
//                e.printStackTrace();
//            }

//            String convertedTime = UtcTimeToLocalTime(time);
//        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#EAF4FC"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        final ViewHolder finalHolder = holder;

//        holder.rel_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                prefs = context.getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
//                saveCellArray();
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString(SharedPreferenceConstants.infoPage, "info_home");
////                editor.putString("chat_cellno", array_cellno.get(position));
//                editor.putString("chat_cellno", array_cellno.get(position).trim());
//                editor.apply();
//                InterfaceListener.getimageClickListenerInterfaceRel(finalHolder.rel_info);
////                AppController.application.modelTabHome = rowItems.get(position);
//            }
//        });

        return convertView;
    }

//    public void saveCellArray() {
//        String cust_live_update = prefs.getString("cell_array_home", "");
//        String replace = cust_live_update.replace("[", "");
//        String replace1 = replace.replace("]", "");
//        Log.e("tag", "array" + cust_live_update);
//        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
//        for (int i = 0; i < myList.size(); i++) {
//            array_cellno.add(myList.get(i));
//        }
//    }

//    private String UtcTimeToLocalTime(String utcTime) {
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date date = null;
//        try {
//            date = df.parse(utcTime);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        df.setTimeZone(TimeZone.getDefault());
//        SimpleDateFormat df2 = new SimpleDateFormat("hh:mm a");
//        return df2.format(date);
//    }

    /*private view holder class*/
    private class ViewHolder {
        TextView name, txt_msg, txt_time, txt_no;
        //        ImageView img_info;
        RelativeLayout rel_info;
    }

}