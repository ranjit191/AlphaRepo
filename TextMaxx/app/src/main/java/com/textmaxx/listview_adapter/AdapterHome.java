package com.textmaxx.listview_adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.InterfaceListener;
import com.textmaxx.models.ModelTabHome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AdapterHome extends BaseAdapter {
    Context context;
    List<ModelTabHome> rowItems;
    SharedPreferences prefs;
    List<String> array_cellno = new ArrayList<String>();

    {

    }

//    public AdapterHome(FragmentActivity activity, RealmResults<ModelTabHome> modelTabHomes) {
//    }

    public AdapterHome(Context context, List<ModelTabHome> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
        this.array_cellno = array_cellno;
    }

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
            convertView = mInflater.inflate(R.layout.custlist_tab_home, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.img_info = (ImageView) convertView.findViewById(R.id.img_info);
            holder.rel_info = (RelativeLayout) convertView.findViewById(R.id.rel_info);
            holder.txt_msg = (TextView) convertView.findViewById(R.id.txt_msg);
            holder.txt_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.txt_no = (TextView) convertView.findViewById(R.id.txt_no);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ModelTabHome rowItem = (ModelTabHome) getItem(position);


//set name
        holder.txt_no.setText(rowItem.getCount());
        holder.txt_msg.setText(rowItem.getMessage());


////////setting  date
        if (rowItem.getSentTime() != null && rowItem.getSentTime().trim().length() > 0) {
            String datetime = rowItem.getSentTime();

            String date = datetime.split("T")[0];
            String time = datetime.split("T")[1];
            Log.e("tag", "time" + time + "time");
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(time);
                String timein12Format = new SimpleDateFormat("hh:mm a").format(dateObj);
                holder.txt_time.setText(date + ", " + timein12Format);
            } catch (final ParseException e) {
                e.printStackTrace();
            }


            Log.e("tag", "time isss" + time + "sssss");
//            String convertedTime = UtcTimeToLocalTime("time to be converted into utc");

        }

        //////////// setting name


//        holder.name.setText(rowItem.getName());
        if (isNumericArray(rowItem.getName())) {

            String cellNumber = rowItem.getName();
//
            String asubstring = cellNumber.substring(0, 1);

            if (asubstring.equals("1")) {

                String cell = cellNumber.substring(1);

                String st = new String(cell);
                st = new StringBuffer(st).insert(3, "-").toString();
                st = new StringBuffer(st).insert(7, "-").toString();
                holder.name.setText(st);
            } else {
//                Toast.makeText(getActivity(), "222222", Toast.LENGTH_SHORT).show();
                String st = new String(cellNumber);
                st = new StringBuffer(st).insert(3, "-").toString();
                st = new StringBuffer(st).insert(7, "-").toString();
                holder.name.setText(st);
            }


        } else {
            holder.name.setText(rowItem.getName());
        }

///////////


        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#EAF4FC"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        final ViewHolder finalHolder = holder;

        holder.rel_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                prefs = context.getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
                saveCellArray();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SharedPreferenceConstants.infoPage, "info_home2");
//                editor.putString("chat_cellno", array_cellno.get(position));
                editor.putString("chat_cellno", array_cellno.get(position).trim());
                editor.apply();
                InterfaceListener.getimageClickListenerInterfaceRel(finalHolder.rel_info);
//                AppController.application.modelTabHome = rowItems.get(position);
            }
        });

        return convertView;
    }

    public void saveCellArray() {
        String aa = prefs.getString("cell_array_home", "");
        String replace = aa.replace("[", "");
        String replace1 = replace.replace("]", "");
        Log.e("tag", "array" + aa);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        for (int i = 0; i < myList.size(); i++) {
            array_cellno.add(myList.get(i));
        }
    }

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

    public static boolean isNumericArray(String str) {
        if (str == null)
            return false;
        for (char c : str.toCharArray())
            if (c < '0' || c > '9')
                return false;
        return true;
    }

}