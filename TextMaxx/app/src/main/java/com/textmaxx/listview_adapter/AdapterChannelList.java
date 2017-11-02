package com.textmaxx.listview_adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.textmaxx.R;
import com.textmaxx.models.ModelChannelList;

import java.util.List;

public class AdapterChannelList extends android.widget.BaseAdapter {
    Context context;
    List<ModelChannelList> rowItems;

    public AdapterChannelList(Context context, List<ModelChannelList> items) {
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
        return rowItems.indexOf(getItem(position));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custlist_channel_list, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt1);
            holder.txtDesc = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ModelChannelList rowItem = (ModelChannelList) getItem(position);
        holder.txtTitle.setText(rowItem.getTitle());


        if (rowItem.getImage().isEmpty() || rowItem.getImage().equals(null) || rowItem.getImage().equals(""))

        {

            holder.txtDesc.setImageResource(R.drawable.images);
        } else {


            Picasso.with(convertView.getContext()).load(rowItem.getImage()).into(holder.txtDesc);

        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#EAF4FC"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        ImageView txtDesc;
    }

}