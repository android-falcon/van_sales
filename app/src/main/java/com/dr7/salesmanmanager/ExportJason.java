package com.dr7.salesmanmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dr7.salesmanmanager.Modles.AddedCustomer;
import com.dr7.salesmanmanager.Modles.CustomerLocation;
import com.dr7.salesmanmanager.Modles.Item;
import com.dr7.salesmanmanager.Modles.Payment;
import com.dr7.salesmanmanager.Modles.SalesManItemsBalance;
import com.dr7.salesmanmanager.Modles.Settings;
import com.dr7.salesmanmanager.Modles.Transaction;
import com.dr7.salesmanmanager.Modles.Voucher;
import com.dr7.salesmanmanager.Modles.serialModel;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.dr7.salesmanmanager.Login.userNo;
import static com.dr7.salesmanmanager.MainActivity.password;
import static com.dr7.salesmanmanager.MainActivity.passwordFromAdmin;

public class ExportJason extends AppCompatActivity {

    public Context context;
    private ProgressDialog progressDialog;
    private JSONArray jsonArrayVouchers, jsonArrayItems, jsonArrayPayments , jsonArrayPaymentsPaper , jsonArrayAddedCustomer,
            jsonArrayTransactions, jsonArrayBalance ,jsonArrayStockRequest,jsonArrayLocation,jsonArraySerial,jsonArrayStockRequestMaster;
    DatabaseHandler mHandler;
    public static  SweetAlertDialog pd,pdValidation;

    public static List<Transaction> transactions = new ArrayList<>();
    public static List<Voucher> vouchers = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();
    public static List<Payment> payments = new ArrayList<>();
    public static List<Payment> paymentsPaper = new ArrayList<>();
    public static List<AddedCustomer> addedCustomer = new ArrayList<>();
    public static List<Voucher> requestVouchers = new ArrayList<>();
    public static List<Item> requestItems = new ArrayList<>();
     public  static  List<SalesManItemsBalance> salesManItemsBalanceList=new ArrayList<>();
    public  static  List<Item> stockRequestListList=new ArrayList<>();
    public  static  List<CustomerLocation> customerLocationList=new ArrayList<>();
    public  static  List<serialModel> serialModelList=new ArrayList<>();
    int  approveAdmin=-1,workOnLine=-1;
    boolean isPosted = true;
//    getCustomerLocation

    public ExportJason(Context context) throws JSONException {
        this.context = context;
        this.mHandler = new DatabaseHandler(context);
    }


    void startExportDatabase() throws JSONException {
        //
//

        try {
            serialModelList = mHandler.getAllSerialItems();
            Log.e("serialModelList",""+serialModelList);
            jsonArraySerial = new JSONArray();
            for (int i = 0; i < serialModelList.size(); i++)
            {
                if(serialModelList.get(i).getIsPosted().equals("0")){
                    jsonArraySerial.put(serialModelList.get(i).getJSONObject());
                }
            }
            Log.e("jsonArraySerial",""+jsonArraySerial.getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //*************************************************************
        customerLocationList = mHandler.getCustomerLocation();
        jsonArrayLocation = new JSONArray();
        for (int i = 0; i < customerLocationList.size(); i++)
        {
            customerLocationList.get(i).getCUS_NO();
            customerLocationList.get(i).getLATIT();

            customerLocationList.get(i).getLONG();

            if (customerLocationList.get(i).getIsPost() == 0) {
//                customerLocationList.get(i).setIsPost(1);
//

                jsonArrayLocation.put(customerLocationList.get(i).getJSONObject());
            }


        }

        //******************************************
        transactions = mHandler.getAlltransactions();

        jsonArrayTransactions = new JSONArray();
        for (int i = 0; i < transactions.size(); i++)
            if (transactions.get(i).getIsPosted() == 0) {
//                transactions.get(i).setIsPosted(1);
                jsonArrayTransactions.put(transactions.get(i).getJSONObject());
            }
        //******************************************
        try {

            jsonArrayBalance=new JSONArray();
            salesManItemsBalanceList=mHandler.getSalesManItemsBalance(Login.salesMan);
            for (int i = 0; i < salesManItemsBalanceList.size(); i++) {

                salesManItemsBalanceList.get(i).getSalesManNo();
                salesManItemsBalanceList.get(i).getItemNo();
                salesManItemsBalanceList.get(i).getQty();
                jsonArrayBalance.put(salesManItemsBalanceList.get(i).getJSONObject());
            }
        }
        catch ( Exception e)
        {

        }


        //******************************************
        stockRequestListList=mHandler.getAllStockRequestItems();
        jsonArrayStockRequest=new JSONArray();

        for (int i = 0; i < stockRequestListList.size(); i++)
        {
            if(stockRequestListList.get(i).getIsPosted()==0)
            {
                jsonArrayStockRequest.put(stockRequestListList.get(i).getJSONObject());
            }


        }

        //*************************************************
        requestVouchers=mHandler.getAllStockRequestVouchers();
        jsonArrayStockRequestMaster=new JSONArray();

        for (int i = 0; i < requestVouchers.size(); i++)
        {
            if(requestVouchers.get(i).getIsPosted()==0)
            {
                jsonArrayStockRequestMaster.put(requestVouchers.get(i).getJSONObject());
            }


        }

        //********************************************
        //********************************************

        vouchers = mHandler.getAllVouchers();// from voucher master
        jsonArrayVouchers = new JSONArray();
        for (int i = 0; i < vouchers.size(); i++)
            {
                if (vouchers.get(i).getIsPosted() == 0) {
                    jsonArrayVouchers.put(vouchers.get(i).getJSONObject());
                }
            }

        items = mHandler.getAllItems();
        jsonArrayItems = new JSONArray();
        for (int i = 0; i < items.size(); i++)
           {
               if (items.get(i).getIsPosted() == 0) {

                   jsonArrayItems.put(items.get(i).getJSONObject());

               }

            }

        payments = mHandler.getAllPayments();
        jsonArrayPayments = new JSONArray();
        for (int i = 0; i < payments.size(); i++)
     {
         if (payments.get(i).getIsPosted() == 0) {
//                payments.get(i).setIsPosted(1);
             jsonArrayPayments.put(payments.get(i).getJSONObject());
         }
            }

        paymentsPaper = mHandler.getAllPaymentsPaper();
        jsonArrayPaymentsPaper = new JSONArray();
        for (int i = 0; i < paymentsPaper.size(); i++)
        {
            if (paymentsPaper.get(i).getIsPosted() == 0) {
//                paymentsPaper.get(i).setIsPosted(1);
                jsonArrayPaymentsPaper.put(paymentsPaper.get(i).getJSONObject2());
            }
            }

        addedCustomer = mHandler.getAllAddedCustomer();
        jsonArrayAddedCustomer = new JSONArray();
        for (int i = 0; i < addedCustomer.size(); i++)
        {
//                addedCustomer.get(i).setIsPosted(1);
                jsonArrayAddedCustomer.put(addedCustomer.get(i).getJSONObject());
            }
       // getData();

       new ExportJason.JSONTask().execute();



    }


    private class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String do_="my";
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String ipAddress = "";
            Log.e("tagexPORT", "JsonResponse" );

            try {
                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();

            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ExportJason.this, R.string.fill_setting, Toast.LENGTH_SHORT).show();
            }

            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost  request = new HttpPost ();
                request.setURI(new URI("http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php"));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("_ID", "2"));
                nameValuePairs.add(new BasicNameValuePair("Sales_Voucher_M", jsonArrayVouchers.toString().trim()));

                nameValuePairs.add(new BasicNameValuePair("Sales_Voucher_D", jsonArrayItems.toString().trim()));

                nameValuePairs.add(new BasicNameValuePair("Payments", jsonArrayPayments.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("Payments_Checks", jsonArrayPaymentsPaper.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("TABLE_TRANSACTIONS", jsonArrayTransactions.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("Added_Customers", jsonArrayAddedCustomer.toString().trim()));

                nameValuePairs.add(new BasicNameValuePair("LOAD_VAN", jsonArrayBalance.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("CUSTOMER_LOCATION", jsonArrayLocation.toString().trim()));

                nameValuePairs.add(new BasicNameValuePair("ITEMSERIALS", jsonArraySerial.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("REQUEST_STOCK_M", jsonArrayStockRequestMaster.toString().trim()));
                nameValuePairs.add(new BasicNameValuePair("REQUEST_STOCK_D", jsonArrayStockRequest.toString().trim()));


                request.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));



                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("JsonResponse","Export"+JsonResponse);


                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex)
            {
                ex.printStackTrace();
                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                progressDialog.dismiss();
                return null;
            }


//            String JsonResponse = null;
////            String JsonDATA = params[0];
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//
//            try {
//                String ipAddress = mHandler.getAllSettings().get(0).getIpAddress(); // 10.0.0.115
//                String URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/index.php";
//
//                URL url = new URL(URL_TO_HIT);
//
//                String data = URLEncoder.encode("_ID", "UTF-8") + "=" +
//                        URLEncoder.encode(String.valueOf('2'), "UTF-8");
//
//               /* String table1 = data + "&" + "Sales_Voucher_M=" + jsonArrayVouchers.toString().trim();
//                String table2 = data + "&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim();
//                String table3 = data + "&" + "Payments="        + jsonArrayPayments.toString().trim();
//                String table4 = data + "&" + "Payments_Checks=" + jsonArrayPaymentsPaper.toString().trim();
//*/
//                String table1 = data + "&" + "Sales_Voucher_M=" + jsonArrayVouchers.toString().trim();
//                table1 +="&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim()
//                        // + "&" + "Sales_Voucher_D=" + jsonArrayItems.toString().trim()
//                        + "&" + "Payments="        + jsonArrayPayments.toString().trim()
//                        + "&" + "Payments_Checks=" + jsonArrayPaymentsPaper.toString().trim()
//                        + "&" + "Added_Customers=" + jsonArrayAddedCustomer.toString().trim()
//                        + "&" + "TABLE_TRANSACTIONS=" + jsonArrayTransactions.toString().trim()
//                        + "&" + "LOAD_VAN=" + jsonArrayBalance.toString().trim();//sales_man_item_balance
//
////                URLConnection conn = url.openConnection();
////
////                conn.setDoOutput(true);
////                conn.setDoInput(true);
//
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//                try {
////                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
////                    wr.write(table1);
////
////                    wr.flush();
//
//                    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                    wr.writeBytes(table1);
//                    wr.flush();
//                    wr.close();
//
//                } catch (Exception e){
//                    Log.e("here****" , e.getMessage());
//                }
//
//
//                // get response
//                reader = new BufferedReader(new
//                        InputStreamReader(httpURLConnection.getInputStream()));
//
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//
//                // Read Server Response
//                while((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                JsonResponse = sb.toString();
//                Log.e("tag", "" + JsonResponse);
//
//                return JsonResponse;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("tag", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s != null) {
                if (s.contains("SUCCESS")) {
                    mHandler.updateVoucher();
                    mHandler.updateVoucherDetails();
                    mHandler.updatePayment();
                    mHandler.updatePaymentPaper();
                    mHandler.updateAddedCustomers();
                    mHandler.updateTransactions();
                    mHandler.updateCustomersMaster();
                    mHandler.updateSerialTableIsposted();
                    mHandler.updateRequestStockMaster();
                    mHandler.updateRequestStockDetail();
                    getData();
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Success");
                } else {
                    if(s.contains("SAVING_ERRORDuplicate"))
                    {

                    progressDialog.dismiss();
                    try {
                        int indexError=s.indexOf("entry");

                        String errorMessage=s.substring(indexError,indexError+42);
                        showDialogError(errorMessage);
                    }
                    catch (Exception e)
                    {

                    }


                    }
//                    *********************************

                    Toast.makeText(context, "Failed to export data", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                try {

                        Log.e("onPostExecute","");
                        Toast.makeText(context, "Please check internet connection", Toast.LENGTH_SHORT).show();


                }
                catch (Exception e)
                {
                    progressDialog.dismiss();
                    Log.e("ExportException",""+e.getMessage().toString());

                }

            }
            progressDialog.dismiss();
        }
    }

    private void getData() {
        List<Settings> settings =mHandler.getAllSettings();
        if (settings.size() != 0) {
            workOnLine= settings.get(0).getWorkOnline();
            Log.e("workOnLine",""+workOnLine);
        }
        if(workOnLine==1) {
            isPosted=mHandler.isAllVoucher_posted();

                if(isPosted==true)
                {
                    ImportJason obj = new ImportJason(context);
                    obj.getItemBalance(userNo);
                }
                else{
                    Toast.makeText(context,R.string.failImpo_export_data , Toast.LENGTH_SHORT).show();
                }

        }

    }

    public void getPassowrdSetting() {
        new JSONTask_getPassword().execute();
    }
    private class JSONTask_getPassword extends AsyncTask<String, String, String> {

        public  String passwordValue="";
        String ipAddress="",URL_TO_HIT="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdValidation = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdValidation.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdValidation.setTitleText(context.getResources().getString(R.string.validation));
            pdValidation.setCancelable(false);
            pdValidation.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                ipAddress = mHandler.getAllSettings().get(0).getIpAddress();
                if(!ipAddress.equals(""))
                {
                    URL_TO_HIT = "http://" + ipAddress + "/VANSALES_WEB_SERVICE/admin.php";
                }
            }
            catch (Exception e)
            {
                passwordFromAdmin.setText("2021000");
            }
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI(URL_TO_HIT));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                Log.e("rowId","BasicNameValuePair"+passwordValue);
                nameValuePairs.add(new BasicNameValuePair("_ID", "25"));

                nameValuePairs.add(new BasicNameValuePair("PasswordType", "1"));


                request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));


                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("tagUpdate", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }
            //org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();



                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        password.setError(null);
                        passwordFromAdmin.setText("2021000");
                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
                password.setError(null);
                passwordFromAdmin.setText("2021000");
//                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String impo = "";
            JSONObject result=null;
            pdValidation.dismissWithAnimation();
            if (s != null) {
                if (s.contains("PasswordKeyValue")) {
                    try {
                        result = new JSONObject(s);
                        JSONArray notificationInfo = null;
                        notificationInfo = result.getJSONArray("PasswordKeyValue");
                        JSONObject infoDetail=null;
                        infoDetail = notificationInfo.getJSONObject(0);

                        Log.e("infoDetail","PasswordKeyValue"+infoDetail.get("passwordKey").toString());
                        passwordFromAdmin.setText(infoDetail.get("passwordKey").toString());







                    } catch (JSONException e) {
//                        progressDialog.dismiss();
                        e.printStackTrace();
                    }



                }

                else {
                    password.setError(null);
                    passwordFromAdmin.setText("2021000");
                    if (s.contains("Not definded id")) {
                        Toast.makeText(context, "Check WebServices Id 25", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            else {
                password.setError(null);
                passwordFromAdmin.setText("2021000");}
        }

    }
    private void showDialogError(String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getResources().getString(R.string.duplicateddata))
                .setContentText(message)

                .show();
    }
}
