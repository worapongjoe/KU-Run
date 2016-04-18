package kusrc.worapong.preyapron.sriwan.kurun;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private boolean gpsABoolean, networkABoolean;
    private double myLatADouble, myLngADouble;
    private String[] resultStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Setup
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        resultStrings = getIntent().getStringArrayExtra("Result");


        //My Loop
        //myLoop();

    }   // Main Method

    private void myLoop() {

        Log.d("5April", "Location = " + myLatADouble + " , " + myLngADouble);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myLoop();
            }
        }, 3000);

    } // myLoop

    @Override
    protected void onResume() {
        super.onResume();

        locationManager.removeUpdates(locationListener);

        //นี่คือค่าเริ่มต้นของ Map ถ้าไม่ได้ต่อ GPS หรือ Net
        myLatADouble = 13.668066;
        myLngADouble = 100.622454;

        Location networkLocation = findLocation(LocationManager.NETWORK_PROVIDER, "Cannot Connected Internet");
        if (networkLocation != null) {
            myLatADouble = networkLocation.getLatitude();
            myLngADouble = networkLocation.getLongitude();
        }
        Location gpsLocation = findLocation(LocationManager.GPS_PROVIDER, "No Card GPS");
        if (gpsLocation != null) {
            myLatADouble = gpsLocation.getLatitude();
            myLngADouble = gpsLocation.getLongitude();
        }

    }   // onResume

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public Location findLocation(String strProvider,
                                 String strError) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {

            locationManager.requestLocationUpdates(strProvider,
                    1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        } else {
            Log.d("5April", strError);
        }

        return location;
    }


    //Class
    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            myLatADouble = location.getLatitude();
            myLngADouble = location.getLongitude();

        }   // onLocationChange

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        goToCenterMap();

        createAllMarker();

    }   // onMapReady

    private void createAllMarker() {

        mMap.clear();   // Delete All Marker

        //for user
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(myLatADouble, myLngADouble))
                .icon(BitmapDescriptorFactory.fromResource(findIconMarker(resultStrings[7]))));

        //Update Lat, Lng to mySQL
        updateLatLngToMySQL();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createAllMarker();
            }
        }, 3000);

    }   // createAllMarker

    private void updateLatLngToMySQL() {

        String strID = resultStrings[0];
        Log.d("18April", "id ==> " + strID);

        String strLat = Double.toString(myLatADouble);
        String strLng = Double.toString(myLngADouble);

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("isAdd", "true")
                .add("id", strID)
                .add("Lat", strLat)
                .add("Lng", strLng)
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://swiftcodingthai.com/keng/php_edit_location.php")
                .post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("18April", "error ==> " + e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {

                try {

                } catch (Exception e) {
                    Log.d("18April", "error ==> " + e.toString());
                }

            }
        });

    }   // update

    private int findIconMarker(String resultString) {

        int intIcon = R.drawable.kon48;
        int intkey = Integer.parseInt(resultString);

        switch (intkey) {

            case 0:
                intIcon = R.drawable.kon48;
                break;
            case 1:
                intIcon = R.drawable.rat48;
                break;
            case 2:
                intIcon = R.drawable.bird48;
                break;
            case 3:
                intIcon = R.drawable.doremon48;
                break;
            case 4:
                intIcon = R.drawable.nobita48;
                break;

        } //switch




        return intIcon;
    }

    private void goToCenterMap() {

        try {

            LatLng centerLatLng = new LatLng(myLatADouble, myLngADouble);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 16));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }   // toToCenterMap

}   // Main Class