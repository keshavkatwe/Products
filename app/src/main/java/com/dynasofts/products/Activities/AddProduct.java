package com.dynasofts.products.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

    private EditText mProductId, mDate;
    private TextInputLayout mProductIdContainer, mDatePickerContainer;
    private Button mSave;
    private String productId, productType, date;
    private Session mSession;
    private ProgressDialog mProgressDialog;
    private boolean is_add;
    private Spinner mSpinner;
    private CoordinatorLayout mMainLayout;

    private String uProduct_id;

    private DatePickerDialog mDatePickerDialog;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSession = new Session(this);

        mMainLayout = (CoordinatorLayout) findViewById(R.id.main_layout);
        mProductId = (EditText) findViewById(R.id.product_id);
        mDate = (EditText) findViewById(R.id.date);
        mSpinner = (Spinner) findViewById(R.id.spinner_types);

        mProductIdContainer = (TextInputLayout) findViewById(R.id.product_id_container);
        mDatePickerContainer = (TextInputLayout) findViewById(R.id.date_container);

        mSave = (Button) findViewById(R.id.save_btn);

        mSave.setOnClickListener(this);


        adapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Saving");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mDatePickerContainer.setOnClickListener(this);

        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mDatePickerDialog.show();
                }
            }
        });

        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                mDate.setText(date + "/" + (month + 1) + "/" + year);
            }
        }, 2000, 01, 01);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productType = (String) adapter.getItem(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent mIntent = getIntent();
        is_add = mIntent.getBooleanExtra("is_add", false);


        if (!is_add) {
            getSupportActionBar().setTitle("Edit Product");
            mProductId.setText(mIntent.getStringExtra("product_id"));
            mSpinner.setSelection(adapter.getPosition(mIntent.getStringExtra("product_type")));
            uProduct_id = mIntent.getStringExtra("uproduct_id");

            if (!mIntent.getStringExtra("udate").equals("null")) {
                mDate.setText(mIntent.getStringExtra("udate"));
            }

//            if (!mIntent.getStringExtra("udate").equals("")) {
//                String[] separated = mIntent.getStringExtra("udate").split("/");
//
//                int cDate = Integer.parseInt(separated[0]);
//                int cMonth = Integer.parseInt(separated[1]);
//                int cYear = Integer.parseInt(separated[2]);
//
////                mDatePickerDialog.updateDate(cDate,cMonth,cYear);
//            }
        }
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.date_container) {
//            mDatePickerDialog.show();
        } else {
            save();
        }
    }


    public void save() {
        boolean isValidated = true;
        productId = String.valueOf(mProductId.getText());
        date = String.valueOf(mDate.getText());

        if (productId.isEmpty()) {
            mProductIdContainer.setError("Product id is required");
            isValidated = false;
        } else {
            mProductIdContainer.setError("");
        }


        if (isValidated) {

            if (mSession.isNetworkAvailable()) {
                mProgressDialog.show();

                JSONObject mJsonObject = new JSONObject();
                try {
                    mJsonObject.put("userid", mSession.getUserId());
                    mJsonObject.put("productid", productId);
                    mJsonObject.put("producttype", productType);
                    mJsonObject.put("udate", date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url;
                if (is_add) {
                    url = Config.URL + "products.php?type=add";
                } else {
                    url = Config.URL + "products.php?type=edit&uproduct_id=" + uProduct_id;
                }
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, url, mJsonObject, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                mProgressDialog.dismiss();

                                try {
                                    if (response.getBoolean("success")) {
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
            else
            {
                Snackbar.make(mMainLayout, "No internet connection", Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        save();
                    }
                }).show();
            }
        }
    }

}
