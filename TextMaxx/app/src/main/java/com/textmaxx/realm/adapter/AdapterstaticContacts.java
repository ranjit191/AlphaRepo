//package com.textmaxx.realm.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.textmaxx.R;
//import com.textmaxx.realm.models.StaticModelContacts;
//
//import java.util.List;
//
//public class AdapterstaticContacts extends BaseAdapter {
//    Context context;
//    List<StaticModelContacts> rowItems;
//
//    public AdapterstaticContacts(Context context, List<StaticModelContacts> items) {
//        this.context = context;
//        this.rowItems = items;
//    }
//
//    public static boolean isNumericArray(String str) {
//        if (str == null)
//            return false;
//        for (char c : str.toCharArray())
//            if (c < '0' || c > '9')
//                return false;
//        return true;
//    }
//
//    @Override
//    public int getCount() {
//        return rowItems.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return rowItems.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return rowItems.size();
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//
//        LayoutInflater mInflater = (LayoutInflater)
//                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.custlist_contacts, null);
//            holder = new ViewHolder();
//            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt1);
//            holder.txtDesc = (ImageView) convertView.findViewById(R.id.txt2);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//
//        StaticModelContacts rowItem = (StaticModelContacts) getItem(position);
//
//
////        holder.txtTitle.setText(rowItem.getTitle());
//
//
//        if (isNumericArray(rowItem.getTitle())) {
//
//            String cellNumber = rowItem.getTitle();
////
//            String asubstring = cellNumber.substring(0, 1);
//
//            if (asubstring.equals("1")) {
//
//                String cell = cellNumber.substring(1);
//
//                String st = new String(cell);
//                st = new StringBuffer(st).insert(3, "-").toString();
//                st = new StringBuffer(st).insert(7, "-").toString();
//                holder.txtTitle.setText(st);
//            } else {
//                String st = new String(cellNumber);
//                st = new StringBuffer(st).insert(3, "-").toString();
//                st = new StringBuffer(st).insert(7, "-").toString();
//                holder.txtTitle.setText(st);
//            }
//
//
//        } else {
//            holder.txtTitle.setText(rowItem.getTitle());
//        }
//
//
//        if (position % 2 == 0) {
//            convertView.setBackgroundColor(Color.parseColor("#EAF4FC"));
//        } else {
//            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
//        }
//        return convertView;
//    }
//
//    /*private view holder class*/
//    private class ViewHolder {
//        ImageView imageView;
//        TextView txtTitle;
//        ImageView txtDesc;
//    }
//
//}