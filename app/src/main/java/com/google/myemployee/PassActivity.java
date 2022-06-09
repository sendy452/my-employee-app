package com.google.myemployee;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.myemployee.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import static com.android.volley.VolleyLog.TAG;

public class PassActivity extends AppCompatActivity {
    EditText txtPass, txtPassNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        txtPass = findViewById(R.id.txtPassChangeOld);
        txtPassNew = findViewById(R.id.txtPassChangeNew);


        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        findViewById(R.id.btnUpdatePass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = txtPass.getText().toString();
                String newpass = txtPassNew.getText().toString();

                StringRequest strReq = new StringRequest(Request.Method.PUT, URLs.URL_PASS + user.getId(), new Response.Listener<String>() {

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
                        params.put("password", pass);
                        params.put("newpassword", newpass);

                        return params;
                    }

                };

                VolleySingleton.getInstance(PassActivity.this).addToRequestQueue(strReq);
            }
        });

        findViewById(R.id.btnMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
