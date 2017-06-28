package com.samples.location.reversegeocodingmap;

import android.app.Activity;
import android.content.Intent;
import android.location.*;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class GeocoderActivity extends Activity {

    private TextView text;
    private Button bMap;
    private LocationManager manager;
    private Geocoder geocoder;
    private Location currentLocation;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            printLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            printLocation(null);
        }
    };

    public View.OnClickListener bMap_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (currentLocation != null){
                Uri uri = Uri.parse(String.format("geo:%f,%f",
                        currentLocation.getAltitude(),
                        currentLocation.getLongitude()));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoder);

        text = (TextView)findViewById(R.id.text);

        bMap = (Button)findViewById(R.id.bMap);
        bMap.setOnClickListener(bMap_onClick);

        geocoder = new Geocoder(getApplicationContext());

        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        printLocation(currentLocation);

    }

    public void printLocation(Location location){
        if (location != null){
            text.append("Longitude:\t" + location.getLongitude() +
                    "\nLatitude:\t" + location.getLatitude());
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 20);
                for (int i = 0; i < addresses.size(); i++){
                    Address address = addresses.get(i);
                    text.append("\nAddress# " + i +
                            "\n\tLocality: " + address.getLocality() +
                            "\n\tCountryName: " + address.getCountryName() +
                            "-" + address.getCountryCode() +
                            "\n\tFeatureName: " + address.getFeatureName() +
                            "\n\tPostalCode: " + address.getPostalCode()
                    );
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }else {
            text.setText("Location unavailable");
        }
    }
}
