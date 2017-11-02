package com.textmaxx.realm.adapter;

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
import java.util.TimeZone;

public class AdapterChatStatic extends BaseAdapter {
    Context context;
    List<ModelChat> rowItems;
    SharedPreferences prefs;

    public AdapterChatStatic(Context context, List<ModelChat> items) {
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
//            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.tv_mesg_right = (TextView) convertView.findViewById(R.id.tv_mesg_right);
            holder.tv_mesg_left = (TextView) convertView.findViewById(R.id.tv_mesg_left);
            holder.iv_image_receive = (ImageView) convertView.findViewById(R.id.iv_image_receive);

            holder.rel_rec = (RelativeLayout) convertView.findViewById(R.id.rel_rec);
            holder.rel_send = (RelativeLayout) convertView.findViewById(R.id.rel_send);


            holder.iv_image_sent = (ImageView) convertView.findViewById(R.id.iv_image_sent);
            holder.tv_dateleft = (TextView) convertView.findViewById(R.id.tv_dateleft);
            holder.tv_dateright = (TextView) convertView.findViewById(R.id.tv_dateright);
            holder.mlin_send = (LinearLayout) convertView.findViewById(R.id.lin_send);
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

                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(time1);
                    String timein12Format = new SimpleDateFormat("hh:mm a").format(dateObj);
                    holder.tv_dateleft.setText(date + ", " + timein12Format);
                } catch (final ParseException e) {
                    e.printStackTrace();
                }


//                String convertedTime = UtcTimeToLocalTime(datetime);
//                holder.tv_dateleft.setText(convertedTime);
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
            Log.e("tag", "time sent sent " + rowItem.getMsgSentTIme());
//            holder.tv_dateleft.setText(UtcTimeToLocalTime((rowItem.getMsgSentTIme()).split("T")[1]));

//            holder.tv_dateright.setText(UtcTimeToLocalTime(("2016-12-29T09:08:34").split("T")[1]));


            if (rowItem.getMsgSentTIme() != null && rowItem.getMsgSentTIme().trim().length() > 0) {
                String datetime = rowItem.getMsgSentTIme();

                String date = datetime.split("T")[0];
                String time1 = datetime.split("T")[1];

                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(time1);
                    String timein12Format = new SimpleDateFormat("hh:mm a").format(dateObj);
                    holder.tv_dateright.setText(date + ", " + timein12Format);
                } catch (final ParseException e) {
                    e.printStackTrace();
                }

//                String convertedTime = UtcTimeToLocalTime(datetime);

            }


            holder.iv_image_sent.setImageResource(R.drawable.chat_user_image);
            holder.rel_rec.setVisibility(View.GONE);
            holder.rel_send.setVisibility(View.VISIBLE);
            if (rowItem.getComment().equals("true")) {
                holder.mlin_send.setBackgroundResource(R.drawable.right_chat);

                holder.tv_mesg_right.setText(rowItem.getMessage());
                holder.tv_mesg_right.setTextColor(Color.parseColor("#000000"));

            } else if (rowItem.getComment().equals("false")) {
                holder.mlin_send.setBackgroundResource(R.drawable.user_2_chatbox);


                    holder.tv_mesg_right.setTextColor(Color.parseColor("#ffffff"));

                holder.tv_mesg_right.setText(rowItem.getMessage());
//                holder.tv_mesg_right.setTextColor(Color.parseColor("#ffffff"));
//                holder.tv_mesg_right.setAlpha((float) .8);


            } else {
                holder.mlin_send.setBackgroundResource(R.drawable.user_2_chatbox);
                holder.tv_mesg_right.setText(rowItem.getMessage());
                holder.tv_mesg_right.setTextColor(Color.parseColor("#ffffff"));
//                holder.tv_mesg_right.setAlpha((float) .8);
            }

//            holder.tv_dateright.setText(UtcTimeToLocalTime(("2016-12-29T09:08:34").split("T")[1]));
        }


//        if (position % 2 == 0) {
//            convertView.setBackgroundColor(Color.parseColor("#EAF4FC"));
//        } else {
//            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
//        }


        final ViewHolder finalHolder = holder;
//        if (position == 0) {
//            Toast.makeText(context, "position is zero", Toast.LENGTH_SHORT).show();
//        }
        return convertView;
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
    private String UtcTimeToLocalTime(String utcTime) {
        SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        readFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;

        try {
            date = readFormat.parse(utcTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        readFormat.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd, hh:mm cust_live_update");
        return writeFormat.format(date);
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView name, tv_mesg_right, tv_dateleft, tv_dateright, tv_mesg_left;
        ImageView iv_image_sent, iv_image_receive;
        RelativeLayout rel_send, rel_rec;
        LinearLayout mlin_send;
    }

}