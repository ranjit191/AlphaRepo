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
import android.widget.Toast;

import com.textmaxx.R;
import com.textmaxx.SharedPref.SharedPreferenceConstants;
import com.textmaxx.Utils.GlobalConstants;
import com.textmaxx.Utils.InterfaceListener;
import com.textmaxx.models.ModelTabContacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdapterContacts extends android.widget.BaseAdapter {
    Context context;
    List<ModelTabContacts> rowItems;
    SharedPreferences prefs;
    List<String> array_cellno = new ArrayList<String>();

    public AdapterContacts(Context context, List<ModelTabContacts> items) {
        this.context = context;
        this.rowItems = items;
    }

    public static boolean isNumericArray(String str) {
        if (str == null)
            return false;
        for (char c : str.toCharArray())
            if (c < '0' || c > '9')
                return false;
        return true;
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
        return rowItems.indexOf(getItem(position));
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custlist_contacts, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt1);
            holder.imgInfo = (ImageView) convertView.findViewById(R.id.imgInfo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ModelTabContacts rowItem = (ModelTabContacts) getItem(position);

//        holder.txtTitle.setText(rowItem.getTitle());


        if (isNumericArray(rowItem.getTitle())) {

            String cellNumber = rowItem.getTitle();
//
            String asubstring = cellNumber.substring(0, 1);

            if (asubstring.equals("1")) {

                String cell = cellNumber.substring(1);

                String st = new String(cell);
                st = new StringBuffer(st).insert(3, "-").toString();
                st = new StringBuffer(st).insert(7, "-").toString();
                holder.txtTitle.setText(st);
            } else {
                String st = new String(cellNumber);
                st = new StringBuffer(st).insert(3, "-").toString();
                st = new StringBuffer(st).insert(7, "-").toString();
                holder.txtTitle.setText(st);
            }


        } else {
            holder.txtTitle.setText(rowItem.getTitle());
        }





        final ViewHolder finalHolder = holder;
        holder.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "hlo", Toast.LENGTH_SHORT).show();
                prefs = context.getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
                saveCellArray();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SharedPreferenceConstants.infoPage, "info_contacts");
//                editor.putString("chat_cellno", array_cellno.get(position));
                editor.putString("chat_cellno", array_cellno.get(position).trim());
                editor.apply();
                InterfaceListener.getImageClickListenerInterface(finalHolder.imgInfo);
            }
        });
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#EAF4FC"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
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

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        ImageView imgInfo;
    }

}