package com.google.myemployee;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.myemployee.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import static com.android.volley.VolleyLog.TAG;

public class ProfilActivity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    EditText txtNama, txtNik, txtHp, txtTLahir, txtTglLahir, txtAlamat, txtNegara, txtJekel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        txtTglLahir = findViewById(R.id.txtTanggalChange);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        txtTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ProfilActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtNama = findViewById(R.id.txtNamaChange);
        txtNik = findViewById(R.id.txtNikChange);
        txtJekel = findViewById(R.id.txtJekelChange);
        txtHp = findViewById(R.id.txtHpChange);
        txtTLahir =  findViewById(R.id.txtTempatChange);
        txtTglLahir = findViewById(R.id.txtTanggalChange);
        txtAlamat = findViewById(R.id.txtAlamatChange);
        txtNegara = findViewById(R.id.txtNegaraChange);


        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_ME + user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {

                                JSONObject userJson = obj.getJSONObject("user");

                                txtNama.setText(userJson.getString("nama"));
                                txtNik.setText(userJson.getString("nik"));
                                txtHp.setText(userJson.getString("nohp"));
                                txtTLahir.setText(userJson.getString("tlahir"));

                                String date = userJson.getString("tgllahir");
                                SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
                                Date newDate=spf.parse(date);
                                spf= new SimpleDateFormat("dd MMMM yyyy");
                                date = spf.format(newDate);

                                txtTglLahir.setText(date);
                                txtAlamat.setText(userJson.getString("alamat"));
                                txtNegara.setText(userJson.getString("negara"));
                                txtJekel.setText(userJson.getString("jekel"));

                                Log.e("Successfully Get Data!", obj.toString());
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Log.e("Successfully Get Data!", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = txtNama.getText().toString();
                String nik = txtNik.getText().toString();
                String jekel = txtJekel.getText().toString();
                String noHP = txtHp.getText().toString();
                String tlahir = txtTLahir.getText().toString();
                String alamat = txtAlamat.getText().toString();
                String negara = txtNegara.getText().toString();

                String date_parse = txtTglLahir.getText().toString();
                Date date = null;
                DateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy");
                try {
                    date = inputFormat.parse(date_parse);
                    System.out.println(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputText = outputFormat.format(date);
                String tglLahir = outputText;

                if (nama.trim().length() > 0) {
                    StringRequest strReq = new StringRequest(Request.Method.PUT, URLs.URL_UPDATE + user.getId(), new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "Update Response: " + response.toString());
                            try {
                                JSONObject jObj = new JSONObject(response);
                                Log.e("Successfully Update!", jObj.toString());

                                Toast.makeText(getApplicationContext(),
                                        jObj.getString("message"), Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Error: " + error.getMessage());
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){

                        @Override
                        protected Map<String, String> getParams() {
                            // Posting parameters to login url
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("nama", nama);
                            params.put("nohp", noHP);
                            params.put("nik", nik);
                            params.put("tlahir", tlahir);
                            params.put("alamat", alamat);
                            params.put("negara", negara);
                            params.put("jekel", jekel);
                            params.put("tgllahir", tglLahir);

                            return params;
                        }

                    };

                    VolleySingleton.getInstance(ProfilActivity.this).addToRequestQueue(strReq);
                }else{
                    Toast.makeText(getApplicationContext() ,"Pastikan Nama Anda Terisi", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.btnchgPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), PassActivity.class));
            }
        });
    }

    private void updateLabel(){
        String myFormat="dd MMMM yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat);
        txtTglLahir.setText(dateFormat.format(myCalendar.getTime()));
    }
}
