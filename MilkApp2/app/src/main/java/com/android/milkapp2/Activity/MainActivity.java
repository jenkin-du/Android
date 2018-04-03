package com.android.milkapp2.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.android.milkapp2.R;

import java.util.List;

public class MainActivity extends Activity {

    private TextView locationInfo;
    private Button btnShare;
    private Button btnGet;
    private LocationManager locationManager;
    private String provider;
    private MapView mapView;
    private AMap aMap;
    private Location location;
    private LatLng marker = new LatLng(32.115, 118.93);
    private double lati=32.115;
    private double longi=118.93;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationInfo = (TextView) findViewById(R.id.txt);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnGet = (Button) findViewById(R.id.btnGet);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();

        String position = "latitude is 32.115" + "\n" + "longitude is 118.93";
        locationInfo.setText(position);

        List<String> providerList = locationManager.getProviders(true);
              if (providerList.contains(LocationManager.NETWORK_PROVIDER))
            provider = LocationManager.NETWORK_PROVIDER;
        else if (providerList.contains(LocationManager.GPS_PROVIDER))
            provider = LocationManager.GPS_PROVIDER;
        else {
            Toast.makeText(MainActivity.this, "无法获得定位", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            showLocation();
        }

        locationManager.requestLocationUpdates(provider, 5000, 10, locationListener);

        btnGet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent it = new Intent(MainActivity.this, Get.class);
                startActivity(it);
            }
        });


        btnShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, GetPhoto.class);
                intent.putExtra("latitude", Double.toString(location.getLatitude()));
                intent.putExtra("longitude", Double.toString(location.getLongitude()));

                startActivityForResult(intent, 1);

            }
        });

        btnGet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MainActivity.this, Get.class);
                intent.putExtra("latitude", Double.toString(location.getLatitude()));
                intent.putExtra("longitude", Double.toString(location.getLongitude()));

                startActivity(intent);
            }
        });

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.location_marker);
        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);
        marker = new LatLng(32.1131, 118.9295);
        aMap.addMarker(new MarkerOptions().position(marker).icon(bd));

    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();

            aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker));
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            //setUpMap();
            aMap.getUiSettings().setZoomControlsEnabled(false);

        }
    }


    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();

        return Bitmap.createBitmap(cacheBitmap);
    }

    private void setUpMap() {

        MyLocationStyle myLocationStyle = new MyLocationStyle();

        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.mipig));

        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));


        aMap.setMyLocationStyle(myLocationStyle);

        //aMap.setLocationSource(this);


        aMap.getUiSettings().setMyLocationButtonEnabled(true);

        aMap.setMyLocationEnabled(true);
    }


    private void showLocation() {
        String position = "latitude is " + lati + "\n" + "longitude is " + longi;
        locationInfo.setText(position);
    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}


    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            lati=location.getLatitude()-0.0026;
            longi = location.getLongitude()+0.005;
            showLocation();
            marker = new LatLng(lati, longi);
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker));

            setUpMap();
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationManager != null)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1 && resultCode == RESULT_OK) {

            String message = data.getStringExtra("text");
            String imageFileName = data.getStringExtra("imageFileName");

            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View view = inflater.inflate(R.layout.mark, null);
            ImageView pic = (ImageView) view.findViewById(R.id.image);
            TextView textView = (TextView) view.findViewById(R.id.infotxt);

            if (!message.equals("")) {
                textView.setText(message);
            }

            if (!imageFileName.equals("")) {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFileName);
                pic.setImageBitmap(bitmap);
            }


            Bitmap MarkerView = getViewBitmap(view);
            BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(MarkerView);

            aMap.addMarker(new MarkerOptions().position(marker).icon(bd));
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


}

