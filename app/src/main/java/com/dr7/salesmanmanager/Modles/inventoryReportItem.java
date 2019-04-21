package com.dr7.salesmanmanager.Modles;

public class inventoryReportItem {

    private String itemNo;
    private String name;
    private double qty;
    private String categoryId;


    public inventoryReportItem(String itemNo, String name, double qty, String categoryId) {
        this.itemNo = itemNo;
        this.name = name;
        this.qty = qty;
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public inventoryReportItem() {
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}
