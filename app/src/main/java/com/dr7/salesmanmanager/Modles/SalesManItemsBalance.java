package com.dr7.salesmanmanager.Modles;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SalesManItemsBalance {

    private String ComapnyNo;
    private String SalesManNo;
    private String ItemNo;
    private double Qty;
    private double Act_Qty;
    public SalesManItemsBalance(){

    }
    List<SalesManItemsBalance>CASHREPORT;
    public List<SalesManItemsBalance> getSalesItemBalance() {
        return CASHREPORT;
    }

    public SalesManItemsBalance(String companyNo, String salesManNo, String itemNo, double qty) {
        this.ComapnyNo = companyNo;
        this.SalesManNo = salesManNo;
        this.ItemNo = itemNo;
        this.Qty = qty;
    }

    public double getAct_Qty() {
        return Act_Qty;
    }

    public void setAct_Qty(double act_Qty) {
        Act_Qty = act_Qty;
    }

    public String getCompanyNo() {
        return ComapnyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.ComapnyNo = companyNo;
    }

    public String getSalesManNo() {
        return SalesManNo;
    }

    public void setSalesManNo(String salesManNo) {
        this.SalesManNo = salesManNo;
    }

    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        this.ItemNo = itemNo;
    }

    public double getQty() {
        return Qty;
    }

    public void setQty(double qty) {
        this.Qty = qty;
    }
    Date currentTimeAndDate = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    String today = convertToEnglish( df.format(currentTimeAndDate));


//    public JSONObject getObj() {
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("date",today);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return obj;
//    }

    // VANCODE,LOADDATE,LOADQTY,ITEMCODE,NETQTY".
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("LOADTYPE", "1");
            obj.put("VANCODE", SalesManNo);
            obj.put("ITEMCODE", ItemNo);
            obj.put("LOADQTY", Qty);
            obj.put("NETQTY", Qty);
            obj.put("LOADDATE",today);
            obj.put("EXPORTED","0");
            obj.put("ADD_SALESMEN","0");
        } catch (JSONException e) {
            Log.e("TagSalesmanBalance" , "JSONException");
        }
        return obj;
    }
    public JSONObject getJSONObject_tempLoadVan() {
//        http://localhost:8085/SaveTempLoadVan?CONO=295&JSONSTR={JSN:[{LOADTYPE:2,VANCODE:0002,LOADQTY:50
        // ,ITEMCODE:123456,ADD_SALESMEN:0,NETQTY:50,EXPORTED:0,LOADDATE:22/05/2022}]}
        JSONObject obj = new JSONObject();
        try {


            obj.put("LOADTYPE", "2");
            obj.put("VANCODE", SalesManNo);
            obj.put("ITEMCODE", ItemNo);
            obj.put("LOADQTY", Qty);
            obj.put("NETQTY", Qty);
            obj.put("LOADDATE",today);
            obj.put("EXPORTED","0");
            obj.put("ADD_SALESMEN","0");
        } catch (JSONException e) {
            Log.e("TagSalesmanBalance" , "JSONException");
        }
        return obj;
    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0"));
        return newValue;
    }
}
