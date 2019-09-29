package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Weather> arrayList;

    public CustomAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {

        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.listview,null);

        Weather weather=arrayList.get(position);

        TextView txtDay=(TextView) convertView.findViewById(R.id.textViewDay);
        TextView txtStatus=(TextView) convertView.findViewById(R.id.textViewState);
        TextView txtMaxTemp=(TextView) convertView.findViewById(R.id.textViewMaxTemp);
        TextView txtMinTemp=(TextView) convertView.findViewById(R.id.textViewMinTemp);
        ImageView imgStatus=(ImageView)convertView.findViewById(R.id.imageViewState);

        txtDay.setText(weather.Day);
        txtStatus.setText(weather.Status);
        txtMaxTemp.setText(weather.MaxTemp +"°C");
        txtMinTemp.setText(weather.MinTemp+"°C");
        Picasso.get().load("http://openweathermap.org/img/wn/"+weather.Image+".png").into(imgStatus);

        return convertView;
    }
}
