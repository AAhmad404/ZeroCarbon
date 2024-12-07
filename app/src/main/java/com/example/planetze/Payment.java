package com.example.planetze;

import android.os.AsyncTask;
import android.os.Bundle;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.media3.common.util.Log;
import com.example.planetze.databinding.ActivityEcoBalanceDestinationBinding;
import com.example.planetze.databinding.ActivityEcoBalanceDestinationBinding;




import androidx.media3.common.util.UnstableApi;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.planetze.databinding.ActivityPaymentBinding;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetConfigurationKtxKt;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.paymentsheet.PaymentSheetResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    Button btn;
    String PUSHABLE_KEY = "pk_test_51QNjq9F4HnTs1FIdZQt1c9LRqmQTguGHdET56UYsLniR3h2QIrrY1gdZa80lF8tf8mge5idM5ghTdFOYLSTf420U00fJdiiGaD";
    String SECRET_KEY;
    String clientid;

    String Ephermalkey;
    String ClientSecret;
    PaymentSheet paymentSheet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @NonNull ActivityPaymentBinding binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btn =  findViewById(R.id.button2);

        // allows ows us to speak from our app to stripe
        PaymentConfiguration.init(this, PUSHABLE_KEY);
        // sets up paymentsheet
        paymentSheet =  new PaymentSheet(this,paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentflow();
            }
        });



        String url = "https://api.stripe.com/v1/customers";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    clientid = jsonObject.getString("id");
                    Toast.makeText(Payment.this,clientid, Toast.LENGTH_SHORT).show();
                    getEphermalKey();

                } catch (JSONException e) {
                    //throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  map = new HashMap<>();
                map.put("Authorization","Bearer "+SECRET_KEY);
                return map;
            }
        };

        // sets up a list for possible network requests
        RequestQueue requestQueue =  Volley.newRequestQueue(Payment.this);
        requestQueue.add(stringRequest);




    }

    private void getEphermalKey() {
        String url = "https://api.stripe.com/v1/ephemeral_keys";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Ephermalkey = jsonObject.getString("id");
                    Toast.makeText(Payment.this,Ephermalkey, Toast.LENGTH_SHORT).show();
                    getClientSecret(clientid, Ephermalkey);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  map = new HashMap<>();
                map.put("Authorization","Bearer "+SECRET_KEY);
                map.put("Stripe-Version","2024-11-20.acacia");

                return map;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  map = new HashMap<>();
                map.put("customer", clientid);
                return map;
            }
        };

        RequestQueue requestQueue =  Volley.newRequestQueue(Payment.this);
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String clientid, String ephermalkey) {
        String url = "https://api.stripe.com/v1/payment_intents";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ClientSecret = jsonObject.getString("client_secret");
                    Toast.makeText(Payment.this,ClientSecret, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  map = new HashMap<>();
                map.put("Authorization","Bearer "+SECRET_KEY);

                return map;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  map = new HashMap<>();
                map.put("customer", clientid);
                map.put("amount", "1000" + "00");
                map.put("currency", "cad");
                map.put("automatic_payment_methods[enabled]", "true");

                return map;
            }
        };

        RequestQueue requestQueue =  Volley.newRequestQueue(Payment.this);
        requestQueue.add(stringRequest);

    }

    private void paymentflow() {
        // if (Ephermalkey != null)
        paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration("Plantze",
                new PaymentSheet.CustomerConfiguration(clientid, Ephermalkey)));
    }
    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this,"DONE", Toast.LENGTH_SHORT).show();
    }
        else
            Toast.makeText(this,"LOOK not working", Toast.LENGTH_SHORT).show();


    }
}

