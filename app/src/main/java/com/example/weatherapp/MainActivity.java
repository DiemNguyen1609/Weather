package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    EditText editSearch;
    Button btnSearch,btnChangeActivity;
    TextView txtCity,txtCountry,txtStatus,txtHumidity,txtCloud,txtWind,txtDay,txtTemp;
    ImageView imgIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapping();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city =editSearch.getText().toString();
                if(city.equals(""))
                {
                    city="Thanh pho Ho Chi Minh";
                    GetCurrentWeatherData(city);
                }
                else
                {

                    GetCurrentWeatherData(city);
                }

            }
        });
        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city =editSearch.getText().toString();
                Intent intent =new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });
    }
    private void Mapping()
    {
        editSearch=(EditText)findViewById(R.id.editTextSearch);
        btnSearch=(Button)findViewById(R.id.buttonSearch);
        btnChangeActivity=(Button)findViewById(R.id.buttonChangeActivity);
        txtCity=(TextView)findViewById(R.id.textViewCity);
        txtCountry=(TextView)findViewById(R.id.textViewCountry);
        txtCloud=(TextView)findViewById(R.id.textViewCloud);
        txtWind=(TextView)findViewById(R.id.textViewWind);
        txtHumidity=(TextView)findViewById(R.id.textViewHumidity);
        txtTemp=(TextView)findViewById(R.id.textViewTemp);
        imgIcon=(ImageView)findViewById(R.id.imageIcon);
        txtStatus=(TextView) findViewById(R.id.textViewStatus);
        txtDay=(TextView)findViewById(R.id.textViewDay);
    }
    public void GetCurrentWeatherData(String data)
    {
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        String url="https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=5d3d41a8449d1b864c40602f3920a905";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day=jsonObject.getString("dt");
                            String city=jsonObject.getString("name");
                            txtCity.setText("City: "+city);

                            long l=Long.valueOf(day);
                            Date date=new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
                            String Day=simpleDateFormat.format(date);
                            txtDay.setText(Day);

                            JSONArray jsonArrayWeather=jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather= jsonArrayWeather.getJSONObject(0);
                            String status=jsonObjectWeather.getString("main");
                            String icon=jsonObjectWeather.getString("icon");

                            Picasso.get().load("http://openweathermap.org/img/wn/"+icon+".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain=jsonObject.getJSONObject("main");
                            String temp=jsonObjectMain.getString("temp");
                            String humidity=jsonObjectMain.getString("humidity");

                            Double a = Double.valueOf(temp);
                            String temperature=String.valueOf(a.intValue());
                            txtTemp.setText(temperature+"°C");
                            txtHumidity.setText(humidity+"%");

                            JSONObject jsonObjectWind= jsonObject.getJSONObject("wind");
                            String wind=jsonObjectWind.getString("speed");
                            txtWind.setText(wind);

                            JSONObject jsonObjectClouds= jsonObject.getJSONObject("clouds");
                            String clouds=jsonObjectClouds.getString("all");
                            txtCloud.setText(clouds+"%");

                            JSONObject jsonObjectSys= jsonObject.getJSONObject("sys");
                            String country=jsonObjectSys.getString("country");
                            txtCountry.setText("Country: "+country);







                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
}
