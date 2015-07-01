package com.dynasofts.products.Activities;

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

import com.dynasofts.products.Core.Session;
import com.dynasofts.products.R;

public class AddProduct extends AppCompatActivity implements View.OnClickListener {

    private EditText mProductId, mProductType;
    private TextInputLayout mProductIdContainer, mProductTypeContainer;
    private Button mSave;
    private String productId, productType;
    private Session mSession;


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





        }
    }
}
