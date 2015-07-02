package com.dynasofts.products.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class AddProduct extends AppCompatActivity implements View.OnClickListener {

    private EditText mProductId, mProductType;
    private TextInputLayout mProductIdContainer, mProductTypeContainer;
    private Button mSave;
    private String productId, productType;
    private Session mSession;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mSession = new Session(this);

        mProductId = (EditText) findViewById(R.id.product_id);
        mProductType = (EditText) findViewById(R.id.product_type);

        mProductIdContainer = (TextInputLayout) findViewById(R.id.product_id_container);
        mProductTypeContainer = (TextInputLayout) findViewById(R.id.product_type_container);

        mSave = (Button) findViewById(R.id.save_btn);

        mSave.setOnClickListener(this);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Saving");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
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
        boolean isValidated = true;
        productId = String.valueOf(mProductId.getText());
        productType = String.valueOf(mProductType.getText());


        if (productId.isEmpty()) {
            mProductIdContainer.setError("Product id is required");
            isValidated = false;
        } else {
            mProductIdContainer.setError("");
        }


        if (isValidated) {

            mProgressDialog.show();

            JSONObject mJsonObject = new JSONObject();
            try {
                mJsonObject.put("userid", mSession.getUserId());
                mJsonObject.put("productid", productId);
                mJsonObject.put("producttype", productType);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            String url = Config.URL + "products.php?type=add";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, url, mJsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            mProgressDialog.dismiss();

                            try {
                                if (response.getBoolean("success"))
                                {
                                    finish();
                                }
                                L.m(response.toString());
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
}
