package com.dynasofts.products.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dynasofts.products.Adapters.ProductsAdapter;
import com.dynasofts.products.Core.Config;
import com.dynasofts.products.Core.Session;
import com.dynasofts.products.Logging.L;
import com.dynasofts.products.Network.VolleySingleton;
import com.dynasofts.products.Pojo.Product_set;
import com.dynasofts.products.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements ProductsAdapter.ClickListisner {

    private static final int REQUEST_CODE = 1;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private ProductsAdapter mProductsAdapter;
    private List<Product_set> Products = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Session mSession;
    private ProgressDialog mProgressDialog;

    private CoordinatorLayout mMainLayout;
    private LinearLayout mNoConnectionLayout;

    private Button mRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mSession = new Session(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        mMainLayout = (CoordinatorLayout) findViewById(R.id.main_layout);
        mNoConnectionLayout = (LinearLayout) findViewById(R.id.no_connection);
        mRetry = (Button) findViewById(R.id.button_retry);

        if (!mSession.isNetworkAvailable())
        {
            mMainLayout.setVisibility(View.GONE);
            mNoConnectionLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            mMainLayout.setVisibility(View.VISIBLE);
            mNoConnectionLayout.setVisibility(View.GONE);
            setRecyclerView();
        }


        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSession.isNetworkAvailable())
                {
                    mMainLayout.setVisibility(View.GONE);
                    mNoConnectionLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    mMainLayout.setVisibility(View.VISIBLE);
                    mNoConnectionLayout.setVisibility(View.GONE);
                    setRecyclerView();
                }
            }
        });

    }


    public void setRecyclerView() {


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);




        if (!mSession.is_logged()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }



        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent mIntent = new Intent(getApplicationContext(), AddProduct.class);
                mIntent.putExtra("is_add", true);
                startActivityForResult(mIntent, REQUEST_CODE);
            }
        });


        mSwipeRefreshLayout.setRefreshing(true);
        getProducts();
        mProductsAdapter = new ProductsAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mProductsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mProductsAdapter.setProducts(Products);
        mProductsAdapter.setClickListisner(this);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProducts();
            }
        });


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Deleting");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
    }


    public void getProducts() {

        Products.clear();

        String url = Config.URL + "products.php?type=get&user_id=" + mSession.getUserId();

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.POST, url, (String) null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject product = (JSONObject) response.get(i);
                                Products.add(new Product_set(product.getString("uproduct_id"), product.getString("product_id"), product.getString("product_type"), product.getBoolean("is_available"), product.getString("udate")));
                            }
                            mProductsAdapter.setProducts(Products);
                            mSwipeRefreshLayout.setRefreshing(false);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mSwipeRefreshLayout.setRefreshing(true);
        getProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mSession.logout();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEdit(Product_set product_set) {
        Intent mIntent = new Intent(this, AddProduct.class);

        mIntent.putExtra("is_add", false);
        mIntent.putExtra("uproduct_id", product_set.getUproduct_id());
        mIntent.putExtra("product_id", product_set.getProduct_id());
        mIntent.putExtra("product_type", product_set.getProduct_type());
        mIntent.putExtra("udate", product_set.getUdate());

        startActivityForResult(mIntent, REQUEST_CODE);
    }

    @Override
    public void onDelete(final Product_set product_set, final int adapterPosition) {

        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you really want to delete?")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        mProgressDialog.show();


                        String url = Config.URL + "products.php?type=delete&uproduct_id=" + product_set.getUproduct_id();

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.POST, url, (String) null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        mProgressDialog.dismiss();

                                        try {
                                            if (response.getBoolean("success")) {
                                                mProductsAdapter.deleteItem(adapterPosition);
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
                })
                .setNegativeButton(android.R.string.no, null).show();


    }
}
