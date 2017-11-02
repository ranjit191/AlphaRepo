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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.textmaxx.R;
import com.textmaxx.models.ModelChat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class

AdapterChat extends BaseAdapter {
    Context context;
    List<ModelChat> rowItems;
    SharedPreferences prefs;

    public AdapterChat(Context context, List<ModelChat> items) {
        this.context = context;
        this.rowItems = items;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ModelChat rowItem = rowItems.get(position);
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custlist_chat, null);
            holder = new ViewHolder();
            holder.lin_send = (LinearLayout) convertView.findViewById(R.id.lin_send);
            holder.tv_mesg_right = (TextView) convertView.findViewById(R.id.tv_mesg_right);
            holder.tv_mesg_left = (TextView) convertView.findViewById(R.id.tv_mesg_left);
            holder.iv_image_receive = (ImageView) convertView.findViewById(R.id.iv_image_receive);

            holder.rel_rec = (RelativeLayout) convertView.findViewById(R.id.rel_rec);
            holder.rel_send = (RelativeLayout) convertView.findViewById(R.id.rel_send);


            holder.iv_image_sent = (ImageView) convertView.findViewById(R.id.iv_image_sent);
            holder.tv_dateleft = (TextView) convertView.findViewById(R.id.tv_dateleft);
            holder.tv_dateright = (TextView) convertView.findViewById(R.id.tv_dateright);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


//        holder.name.setText(rowItem.getName());

        if (rowItem.getDirection().equals("in")) {
            holder.iv_image_sent.setVisibility(View.GONE);
            holder.tv_dateright.setVisibility(View.GONE);
            holder.iv_image_receive.setVisibility(View.VISIBLE);
            holder.tv_dateleft.setVisibility(View.VISIBLE);


            Log.e("tag", "time received received " + rowItem.getMsgSentTIme());
            if (rowItem.getMsgSentTIme() != null && rowItem.getMsgSentTIme().trim().length() > 0) {
                String datetime = rowItem.getMsgSentTIme();
                String date = datetime.split("T")[0];
                String time1 = datetime.split("T")[1];
                Log.e("tag", "time time1 time1 " + time1);
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(time1);
                    String timein12Format = new SimpleDateFormat("hh:mm a").format(dateObj);
                    holder.tv_dateleft.setText(date + ", " + timein12Format);
                } catch (final ParseException e) {
                    e.printStackTrace();
                }

//                try {
//                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
//                    final Date dateObj = sdf.parse(datetime);
//                    String timein12Format = new SimpleDateFormat("K:mm a").format(dateObj);
//                    holder.tv_dateleft.setText(timein12Format);
//                } catch (final ParseException e) {
//                    e.printStackTrace();
//                }
//                String convertedTime = UtcTimeToLocalTimesimple(datetime);


//                String convertedTime = UtcTimeToLocalTime("time to be converted");

            }

//            holder.tv_dateleft.setText(UtcTimeToLocalTime((rowItem.getMsgSentTIme()).split("T")[1]));
//            Log.e("tag", "time time" + rowItem.getMsgSentTIme());

            holder.iv_image_receive.setImageResource(R.drawable.chat_user_image);
            holder.rel_send.setVisibility(View.GONE);
            holder.rel_rec.setVisibility(View.VISIBLE);

            if (rowItem.getComment().equals("true")) {
                holder.tv_mesg_left.setText(rowItem.getMessage());
                holder.tv_mesg_left.setTextColor(Color.parseColor("#ffffff"));

            } else if (rowItem.getComment().equals("false")) {
                holder.tv_mesg_left.setText(rowItem.getMessage());
                holder.tv_mesg_left.setTextColor(Color.parseColor("#ffffff"));
//                holder.tv_mesg_left.setAlpha((float) .8);


            } else {
                holder.tv_mesg_left.setText(rowItem.getMessage());
                holder.tv_mesg_left.setTextColor(Color.parseColor("#ffffff"));
//                holder.tv_mesg_left.setAlpha((float) .8);
            }

            //

//            holder.tv_dateleft.setText(UtcTimeToLocalTime(rowItem.getMsgSentTIme().split("T")[1]));


        } else {

            holder.iv_image_receive.setVisibility(View.GONE);
            holder.tv_dateleft.setVisibility(View.GONE);
            holder.iv_image_sent.setVisibility(View.VISIBLE);
            holder.tv_dateright.setVisibility(View.VISIBLE);

            if (rowItem.getMsgSentTIme() != null && rowItem.getMsgSentTIme().trim().length() > 0) {
                String datetime = rowItem.getMsgSentTIme();

//                String convertedTime = UtcTimeToLocalTime(datetime);

                String date = datetime.split("T")[0];
                String time = datetime.split("T")[1];
                Log.e("tag", "time" + time + "time");
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(time);
                    String timein12Format = new SimpleDateFormat("hh:mm a").format(dateObj);
                    holder.tv_dateright.setText(date + ", " + timein12Format);
                } catch (final ParseException e) {
                    e.printStackTrace();
                }


            }


            holder.iv_image_sent.setImageResource(R.drawable.chat_user_image);
            holder.rel_rec.setVisibility(View.GONE);
            holder.rel_send.setVisibility(View.VISIBLE);
            if (rowItem.getComment().equals("true")) {
                holder.lin_send.setBackgroundResource(R.drawable.right_chat);

                holder.tv_mesg_right.setText(rowItem.getMessage());
                holder.tv_mesg_right.setTextColor(Color.parseColor("#000000"));

            } else if (rowItem.getComment().equals("false")) {
                holder.lin_send.setBackgroundResource(R.drawable.user_2_chatbox);


//                holder.tv_mesg_right.setAlpha((float) .8);
//                Log.e("tag", "righttext  : " + rowItem.getMessage());

                    holder.tv_mesg_right.setTextColor(Color.parseColor("#ffffff"));

                holder.tv_mesg_right.setText(rowItem.getMessage());
            } else {
                holder.lin_send.setBackgroundResource(R.drawable.user_2_chatbox);
                holder.tv_mesg_right.setText(rowItem.getMessage());
                holder.tv_mesg_right.setTextColor(Color.parseColor("#ffffff"));
//                holder.tv_mesg_right.setAlpha((float) .8);
            }

        }


        final ViewHolder finalHolder = holder;
        return convertView;
    }


//
//    private String UtcTimeToLocalTime(String utcTime) {
//        SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        readFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date date = null;
//
//        try {
//            date = readFormat.parse(utcTime);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        readFormat.setTimeZone(TimeZone.getDefault());
//        SimpleDateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd, hh:mm cust_live_update");
//        return writeFormat.format(date);
//    }


    /*private view holder class*/
    private class ViewHolder {
        TextView name, tv_mesg_right, tv_dateleft, tv_dateright, tv_mesg_left;
        ImageView iv_image_sent, iv_image_receive;
        RelativeLayout rel_send, rel_rec;
        LinearLayout lin_send;

    }


}