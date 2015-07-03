package com.dynasofts.products.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dynasofts.products.Pojo.Product_set;
import com.dynasofts.products.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keshav K on 7/1/2015.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private List<Product_set> products = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ClickListisner clickListisner;

    public ProductsAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setProducts(List<Product_set> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        products.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.product_item_subview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Product_set cProduct_set = products.get(i);
        myViewHolder.mProductId.setText(cProduct_set.getProduct_id());
        myViewHolder.mProductType.setText(cProduct_set.getProduct_type());

        if (cProduct_set.getUdate().equals("null") || cProduct_set.getUdate().equals("")) {
            myViewHolder.mDate.setText("NA");
        } else {
            myViewHolder.mDate.setText(cProduct_set.getUdate());
        }

        if (cProduct_set.getIs_available()) {
            myViewHolder.mStatusIcon.setImageResource(R.drawable.ic_checkbox_marked_circle_outline_grey600_36dp);
            myViewHolder.mStatusIcon.setColorFilter(mContext.getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
        } else {
            myViewHolder.mStatusIcon.setImageResource(R.drawable.ic_close_circle_outline_grey600_36dp);
            myViewHolder.mStatusIcon.setColorFilter(mContext.getResources().getColor(R.color.error_color), PorterDuff.Mode.SRC_ATOP);
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setClickListisner(ClickListisner clickListisner) {
        this.clickListisner = clickListisner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mProductId, mProductType, mDate;
        ImageView mStatusIcon;
        Button mEdit, mDelete;


        public MyViewHolder(View itemView) {
            super(itemView);

            mProductId = (TextView) itemView.findViewById(R.id.product_id);
            mProductType = (TextView) itemView.findViewById(R.id.product_type);
            mStatusIcon = (ImageView) itemView.findViewById(R.id.status_icon);
            mDate = (TextView) itemView.findViewById(R.id.date);

            mEdit = (Button) itemView.findViewById(R.id.button_edit);
            mDelete = (Button) itemView.findViewById(R.id.button_delete);

            mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListisner.onEdit(products.get(getAdapterPosition()));
                }
            });

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListisner.onDelete(products.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }

    public interface ClickListisner {
        void onEdit(Product_set product_set);

        void onDelete(Product_set product_set, int adapterPosition);
    }
}
