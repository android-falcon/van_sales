package com.dr7.salesmanmanager;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.dr7.salesmanmanager.Modles.GMapV2Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class SalesManPlanLocations extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLngBounds.Builder builder;
    LatLngBounds bounds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man_plan_locations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
try {
    builder = new LatLngBounds.Builder();
    for(int i=0;i<ShowPlan.directionPoint.size();i++ )
        builder.include(ShowPlan.directionPoint.get(i));


}catch (Exception exception){

}


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 30));
            }
        });
        LatLng sydney;
        // Add a marker in Sydney and move the camera
        for(int i=0;i<ShowPlan.directionPoint.size();i++ ) {
            sydney = ShowPlan.directionPoint.get(i);
            mMap.addMarker(new MarkerOptions().position(sydney)
                    .icon(BitmapDescriptorFactory.fromBitmap(iconSize()))
                    .title(getSalesmanName(i)));


        }


        try {
            bounds = builder.build();
             CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
            mMap.animateCamera(cu);
        }
        catch (Exception exception){
            }


     //   mMap.moveCamera(CameraUpdateFactory.newLatLng(ShowPlan.directionPoint.get(0)));
        GMapV2Direction md = new GMapV2Direction();
        //   mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync();
     //   SupportMapFragment mapFragment = (SupportMapFragment)this.getSupportFragmentManager().findFragmentById(R.id.map);
       // mapFragment.getMapAsync(this);
     /*   Document doc = md.getDocument(sourcePosition, destPosition,
                GMapV2Direction.MODE_DRIVING);

        ArrayList<LatLng> directionPoint = md.getDirection(doc);*/
        Polyline polylin;
        if(ShowPlan.directionPoint.size()!=0)
polylin = mMap.addPolyline(ShowPlan.rectLine);








    }

    Bitmap iconSize(){
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.market);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        return smallMarker;
    }

String getSalesmanName(int i){
try {
    return MainActivity.DB_salesManPlanList.get(i).getCustName();
}
    catch (Exception exception){
        return " ";
    }

}
}