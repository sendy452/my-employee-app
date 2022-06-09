package com.google.myemployee;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.myemployee.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView txtNama, txtEmail, txtNik, txtNip, txtHp, txtTLahir, txtTglLahir, txtAlamat, txtNegara, txtJekel, txtDivisi, txtBidang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        txtNama = (TextView) findViewById(R.id.txtNamaShow);
        txtEmail = (TextView) findViewById(R.id.txtEmailShow);
        txtNik = (TextView) findViewById(R.id.txtNikShow);
        txtNip = (TextView) findViewById(R.id.txtNipShow);
        txtHp = (TextView) findViewById(R.id.txtHpShow);
        txtTLahir = (TextView) findViewById(R.id.txtTempatShow);
        txtTglLahir = (TextView) findViewById(R.id.txtTanggalShow);
        txtAlamat = (TextView) findViewById(R.id.txtAlamatShow);
        txtNegara = (TextView) findViewById(R.id.txtNegaraShow);
        txtJekel = (TextView) findViewById(R.id.txtJekelShow);
        txtDivisi = (TextView) findViewById(R.id.txtDivisiShow);
        txtBidang = (TextView) findViewById(R.id.txtBidangShow);


        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //txtEmail.setText(user.getEmail());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_ME + user.getId(),
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {

                                JSONObject userJson = obj.getJSONObject("user");

                                txtNama.setText(userJson.getString("nama"));
                                txtEmail.setText(userJson.getString("email"));
                                txtNik.setText(userJson.getString("nik"));
                                txtNip.setText(userJson.getString("nip"));
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
                                txtDivisi.setText(userJson.getString("nama_divisi"));
                                txtBidang.setText(userJson.getString("bidang"));

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

        findViewById(R.id.btnProfil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(getApplicationContext(), ProfilActivity.class));
            }
        });

        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }
}