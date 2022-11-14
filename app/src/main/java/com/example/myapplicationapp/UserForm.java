package com.example.myapplicationapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserForm extends AppCompatActivity {
    Spinner STA_NAME, district;
    EditText route_name,geom;
    TextView latlong,type_of_route,type_of_road;
    Button nationalize, non_nationalize, other, NH, SH, MDR, ODR, express_way, Save, mark_route;
    LinearLayout road_type;
    private static final String TAG = "UserForm";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        getSupportActionBar().setTitle("                          Transport App");
        STA_NAME = findViewById(R.id.STA_NAME);
        district = findViewById(R.id.district);
        route_name = findViewById(R.id.route_name);
        nationalize = findViewById(R.id.nationalize);
        non_nationalize = findViewById(R.id.non_nationalize);
        other = findViewById(R.id.other);
        NH = findViewById(R.id.NH);
        SH = findViewById(R.id.SH);
        MDR = findViewById(R.id.MDR);
        ODR = findViewById(R.id.ODR);
        express_way = findViewById(R.id.express_way);
        Save = findViewById(R.id.Save);
        mark_route = findViewById(R.id.mark_route);
        road_type = findViewById(R.id.road_type);
        latlong = findViewById(R.id.latlong1);
        type_of_route = findViewById(R.id.type_of_route);
        type_of_road = findViewById(R.id.type_of_road);
       // geom= findViewById(R.id.geom);

        Intent i = getIntent();
        String value = i.getStringExtra("lat_value");
       // String value1 = i.getStringExtra("geom_value");
        latlong.setText(value);
        //geom.setText(value1);


        nationalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k=nationalize.getText().toString();
                type_of_route.setText(k);

            }
        });
        non_nationalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k=non_nationalize.getText().toString();
                type_of_route.setText(k);

            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k=other.getText().toString();
                type_of_route.setText(k);

            }
        });

        NH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k=NH.getText().toString();
                type_of_road.setText(k);

            }
        });
        SH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k=SH.getText().toString();
                type_of_road.setText(k);

            }
        });
        MDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k=MDR.getText().toString();
                type_of_road.setText(k);

            }
        });
        ODR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k=ODR.getText().toString();
                type_of_road.setText(k);

            }
        });
        express_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k=express_way.getText().toString();
                type_of_road.setText(k);

            }
        });

        getState();

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });


        mark_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void saveInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.42.171:8081/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Model model = new Model(
                STA_NAME.getSelectedItem().toString(),
                district.getSelectedItem().toString(),
                route_name.getText().toString(),
                type_of_route.getText().toString(),
                type_of_road.getText().toString(),
                latlong.getText().toString()

                );

        Call<Model> postCall = apiInterface.addData(model);
        postCall.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(UserForm.this, "successful" + Log.e(TAG, "response body:" + response.body()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Toast.makeText(UserForm.this, "failed" + Log.e(TAG,t.getLocalizedMessage()), Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void getState() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.42.171:8081/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<State>> call = apiInterface.getState();
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(UserForm.this, "message:"+ response.code(), Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG,"message:"+response.body());
                List<State> states = response.body();
                states.add(new State(-1,"------SELECT-------"));
                String[] items = new String[states.size()];
                for(int i=0;i< states.size();i++){
                    items[i]= states.get(i).getSname();
                }
                ArrayAdapter<String> adapter;
                adapter= new ArrayAdapter<String>(UserForm.this, android.R.layout.simple_spinner_item,items);
                STA_NAME.setAdapter(adapter);
                STA_NAME.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        int getStateId= (int) states.get(i).getSid();
                        get_District(getStateId);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                Toast.makeText(UserForm.this,
                        "SOMETHING WENT WRONG " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });

}

    private void get_District(int getStateId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.42.171:8081/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<District>> call1 = apiInterface.getDistrict(getStateId);
        call1.enqueue(new Callback<List<District>>() {
            @Override
            public void onResponse(Call<List<District>> call, Response<List<District>> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(UserForm.this, "District value fetching!!" , Toast.LENGTH_SHORT).show();
                }
                List<District> districts = response.body();
                districts.add(new District(-1,"------SELECT-------"));
                String[] items1 = new String[districts.size()];
                for(int i=0;i< districts.size();i++){
                    items1[i]= districts.get(i).getDname();
                }
                ArrayAdapter<String> adapter1;
                adapter1= new ArrayAdapter<String>(UserForm.this, android.R.layout.simple_spinner_item,items1);
                district.setAdapter(adapter1);
                district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onFailure(Call<List<    District>> call, Throwable t) {

            }
        });

    }
}








