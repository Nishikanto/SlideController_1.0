package com.example.root.slidecontroller;

/**
 * Created by root on 10/2/16.
 */

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<IpClass> {
    private final Context context;
    private final ArrayList<IpClass> ips;

    public CustomAdapter(Context context, ArrayList<IpClass> ips) {
        super(context, -1, ips);
        this.context = context;
        this.ips = ips;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.ip_listview_row, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.nameView);
        TextView ip = (TextView) rowView.findViewById(R.id.ipView);
        ImageView activeBtn = (ImageView) rowView.findViewById(R.id.activeButton);

        activeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBhelper dBhelper = new DBhelper(getContext());
                dBhelper.deactivateAllIp();
                dBhelper.activateIp(ips.get(position).getId());
                updateIpsList(dBhelper.getAllIps());
            }
        });

        name.setText(ips.get(position).getName());
        ip.setText(ips.get(position).getIp());
        if(ips.get(position).getActive() == 1){
            activeBtn.setImageResource(R.drawable.ic_blue);
        }else
            activeBtn.setImageResource(R.drawable.ic_white);


        /*TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);
        // change the icon for Windows and iPhone
        String s = values[position];
        if (s.startsWith("iPhone")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }*/

        return rowView;
    }

    public void updateIpsList(ArrayList<IpClass> newlist) {
        ips.clear();
        ips.addAll(newlist);
        this.notifyDataSetChanged();
    }
}