package com.dynasofts.products.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dynasofts.products.Core.Config;
import com.dynasofts.products.Core.Session;
import com.dynasofts.products.Logging.L;
import com.dynasofts.products.Network.VolleySingleton;
import com.dynasofts.products.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEmail, mPassword;
    Button mLogin;
    Session mSession;
    AlertDialog.Builder mAlertDialog;
    ProgressDialog mProgressDialog;
    private String email;
    private String password;

    TextInputLayout mEmailContainer, mPasswordContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mSession = new Session(this);

        mEmail = (EditText) findViewById(R.id.email_address);
        mPassword = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.button_login);

        mEmailContainer = (TextInputLayout) findViewById(R.id.email_address_container);
        mPasswordContainer = (TextInputLayout) findViewById(R.id.password_container);

        mLogin.setOnClickListener(this);
        mAlertDialog = new AlertDialog.Builder(this).setMessage("Email/Password is Invalid").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        Boolean is_validated = true;

        email = String.valueOf(mEmail.getText());
        password = String.valueOf(mPassword.getText());


        if (email.isEmpty()) {
            mEmailContainer.setError("Email is required");
            is_validated = false;
        } else if (!isValidEmail(email)) {
            mEmailContainer.setError("Invalid email address");
            is_validated = false;
        } else {
            mEmailContainer.setError("");
        }

        if (password.isEmpty()) {
            mPasswordContainer.setError("Password is required");
            is_validated = false;
        } else {
            mPasswordContainer.setError("");
        }


        if (is_validated == true) {


            mProgressDialog.show();

            JSONObject mJsonObject = new JSONObject();
            try {
                mJsonObject.put("email", email);
                mJsonObject.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String url = Config.URL + "login.php";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url, mJsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            mProgressDialog.dismiss();
//                        L.m(response.toString());
                            try {
                                Boolean user_exist = response.getBoolean("user_exist");
                                if (user_exist == true) {
                                    mSession.setSession(response.getString("id"), response.getString("name"), email);
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                } else {
                                    mAlertDialog.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            L.m(error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    return params;
                }
            };


            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance().addToRequestQueue(jsObjRequest);
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
