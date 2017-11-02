package com.textmaxx.adapter_recycler;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
import com.textmaxx.Utils.RecyclerItemClickListener;
import com.textmaxx.models.ModelTabContacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xamarin on 04/08/17.
 */

public class RecycleAdapterContacts extends RecyclerView.Adapter<RecycleAdapterContacts.MyViewHolder> implements RecyclerItemClickListener.OnItemClickListener {
    List<ModelTabContacts> rowItems;
    SharedPreferences prefs;
    List<String> array_cellno = new ArrayList<String>();
    Context context;
//	List<Match> rowItems;

    public RecycleAdapterContacts(Context context, List<ModelTabContacts> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

//	public RecycleAdapterContacts(FragmentActivity activity, RealmResults<StaticModelContacts> staticModelContactses) {
//
//	}


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.custlist_contacts, parent, false));


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//		ModelTabContacts rowItem = (ModelTabContacts) getItem(position);


        if (isNumericArray(rowItems.get(position).getTitle())) {

            String cellNumber = rowItems.get(position).getTitle();
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
            holder.txtTitle.setText(rowItems.get(position).getTitle());
        }
        if (rowItems.get(position).getVarified().equals(false) || rowItems.get(position).getVarified().equals("false")) {
            holder.imgInfo.setImageResource(R.drawable.plus2);

        } else {
            holder.imgInfo.setImageResource(R.drawable.info_con);
        }
        holder.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs = context.getSharedPreferences(GlobalConstants.PREF_NAME, Context.MODE_PRIVATE);
                saveCellArray();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SharedPreferenceConstants.infoPage, "info_contacts");
//                editor.putString("chat_cellno", array_cellno.get(position));
                editor.putString("chat_cellno", array_cellno.get(position).trim());
                editor.apply();
                InterfaceListener.getImageClickListenerInterface(holder.imgInfo);
            }
        });

//        holder.txtTitle.setText(rowItems.get(position).getTitle());

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#EAF4FC"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    @Override
    public void onItemClick(View view, int position) {


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        ImageView imgInfo;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt1);
            imgInfo = (ImageView) itemView.findViewById(R.id.imgInfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InterfaceListener.getMonOnRecyclerClick(getPosition());
                }
            });
        }


    }

    public static boolean isNumericArray(String str) {
        if (str == null)
            return false;
        for (char c : str.toCharArray())
            if (c < '0' || c > '9')
                return false;
        return true;
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
}
