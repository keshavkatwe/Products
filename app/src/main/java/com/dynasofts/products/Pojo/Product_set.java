package com.dynasofts.products.Pojo;

/**
 * Created by Keshav K on 7/1/2015.
 */
public class Product_set {
    private String udate;
    private String uproduct_id;
    private String product_id;
    private String product_type;
    private Boolean is_available;

    public Product_set(String uproduct_id, String product_id, String product_type, Boolean is_available, String udate) {
        this.uproduct_id = uproduct_id;
        this.product_id = product_id;
        this.product_type = product_type;
        this.is_available = is_available;
        this.udate = udate;
    }

    public String getUproduct_id() {
        return uproduct_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_type() {
        return product_type;
    }

    public Boolean getIs_available() {
        return is_available;
    }

    public String getUdate() {
        return udate;
    }
}
