package com.textmaxx.listview_adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.InterfaceListener;
import com.textmaxx.models.ModelFindContacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdapterFindContacts extends android.widget.BaseAdapter {
    Context context;
    List<ModelFindContacts> rowItems;
    List<String> array_cellno_finduser = new ArrayList<String>();
    SharedPreferences prefs;

    public AdapterFindContacts(Context context, List<ModelFindContacts> items) {
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cust_find_contacts, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt1);
            holder.txtDesc = (ImageView) convertView.findViewById(R.id.txt2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ModelFindContacts rowItem = (ModelFindContacts) getItem(position);
//set name
//        if (!(rowItem.getVarified().equals("null")) || (rowItem.getVarified() != null) || (!(rowItem.getVarified().equals("")))) {
//            if (rowItem.getVarified().equals("true")) {
//                Log.e("tag", "true true");
//                convertView.setClickable(false);
//                convertView.setEnabled(false);
//                holder.txtDesc.setBackgroundResource(R.drawable.info_con);
//
//            } else {
//                Log.e("tag", "false false");
//                holder.txtDesc.setBackgroundResource(R.drawable.plus2);
//            }
//        }


        holder.txtTitle.setText(rowItem.getTitle());
        //set image
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#EAF4FC"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        final ViewHolder finalHolder = holder;
        holder.txtDesc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                prefs = context.getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);

                saveCellArray();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SharedPreferenceConstants.infoPage, "info_find_user");
//                editor.putString("chat_cellno", array_cellno.get(position));
                editor.putString(SharedPreferenceConstants.CHAT_SCREEN_CELL, array_cellno_finduser.get(position).trim());
                editor.apply();
                InterfaceListener.getImageClickListenerInterface(finalHolder.txtDesc);
            }
        });
        return convertView;
    }

    public void saveCellArray() {
        String aa = prefs.getString("array_cellno_finduser", "");
        String replace = aa.replace("[", "");
        String replace1 = replace.replace("]", "");
        Log.e("tag", "array" + aa);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        for (int i = 0; i < myList.size(); i++) {
            array_cellno_finduser.add(myList.get(i));
        }
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        ImageView txtDesc;
    }

}