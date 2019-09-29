package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {

    String cityName="";
    TextView txtCityName;
    ImageView imgBack;
    ListView lv;
    CustomAdapter customAdapter;
    ArrayList<Weather> arrayListWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Init();
        Intent intent = getIntent();
        String city= intent.getStringExtra("city");
        Log.d("ketqua",city);
        if(cityName.equals(""))
        {
            cityName="Thanh pho Ho Chi Minh";
            Get7Day(cityName);
        }
        else
        {
            cityName=city;
            Get7Day(cityName);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void Init()
    {
        imgBack=(ImageView)findViewById(R.id.imageViewBack);
        txtCityName=(TextView)findViewById(R.id.textViewCityName);
        lv=(ListView)findViewById(R.id.listView);
        arrayListWeather=new ArrayList<Weather>();
        customAdapter=new CustomAdapter(Main2Activity.this,arrayListWeather);
        lv.setAdapter(customAdapter);
    }
    private void Get7Day(String data)
    {
        RequestQueue requestQueue= Volley.newRequestQueue(Main2Activity.this);
        String url="https://api.openweathermap.org/data/2.5/forecast?q="+data+"&units=metric&cnt=7&appid=5d3d41a8449d1b864c40602f3920a905";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjectCity=jsonObject.getJSONObject("city");
                    String cityName=jsonObjectCity.getString("name");
                    txtCityName.setText(cityName);
                    JSONArray jsonArrayList=jsonObject.getJSONArray("list");
                    for(int i=0;i<jsonArrayList.length();i++)
                    {
                        JSONObject jsonObjectOneDay =jsonArrayList.getJSONObject(i);
                        String day=jsonObjectOneDay.getString("dt");
                        long l=Long.valueOf(day);
                        Date date=new Date(l*1000L);
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE yyyy-MM-dd");
                        String Day=simpleDateFormat.format(date);

                        JSONObject jsonObjectMain=jsonObjectOneDay.getJSONObject("main");
                        String temp_min=jsonObjectMain.getString("temp_min");
                        String temp_max=jsonObjectMain.getString("temp_max");
                        Double a = Double.valueOf(temp_max);
                        String temperatureMax=String.valueOf(a.intValue());
                        Double b = Double.valueOf(temp_min);
                        String temperatureMin=String.valueOf(b.intValue());

                        JSONArray jsonArrayWeather=jsonObjectOneDay.getJSONArray("weather");
                        JSONObject jsonObjectWeather=jsonArrayWeather.getJSONObject(0);
                        String state=jsonObjectWeather.getString("description");
                        String icon=jsonObjectWeather.getString("icon");
                        arrayListWeather.add(new Weather(Day,state,icon,temperatureMax,temperatureMin));

                    }
                    customAdapter.notifyDataSetChanged();

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
}
