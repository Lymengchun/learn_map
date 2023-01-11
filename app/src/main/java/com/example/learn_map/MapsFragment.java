package com.example.learn_map;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.learn_map.helper.Network;
import com.example.learn_map.helper.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.StrokeStyle;
import com.google.android.gms.maps.model.StyleSpan;
import com.google.maps.GeoApiContext;
import com.maps.route.extensions.MapExtensionKt;
import com.maps.route.model.TravelMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    LatLng latLng;
    String title;

    //constructor
    public MapsFragment(LatLng latLng, String title) {
        this.latLng = latLng;
        this.title = title;
    }

    //init variable
    MarkerOptions markerOptions;
    View view;
    SupportMapFragment supportMapFragment;
    Location myLocationChangeListener;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    /**
     * Flag indicating whether a requested permission has been denied after returning in {@link
     * #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;




    GeoApiContext geoApiContext;

    //route
    private GoogleMap mGoogleMap;
    ArrayList markerPoints = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        //assign id to variable
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.my_maps);
        supportMapFragment.getMapAsync(this);


        return view;
    }

    //implement google map
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {



        mGoogleMap = googleMap;
        mGoogleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
        geoApiContext = new GeoApiContext().setApiKey(getString(R.string.apikey));

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        mGoogleMap.setOnMyLocationButtonClickListener(this);
        mGoogleMap.setOnMyLocationClickListener(this);


        enableMyLocation();

//      mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                     @Override
                     public void onMapClick(@NonNull LatLng latLng) {
                         if (markerPoints.size() > 1) {
                             markerPoints.clear();
                             mGoogleMap.clear();
                         }

                         // Adding new item to the ArrayList
                         markerPoints.add(latLng);

                         // Creating MarkerOptions
                         MarkerOptions options = new MarkerOptions();

                         // Setting the position of the marker
                         options.position(latLng);


                         if (markerPoints.size() == 1) {
                             options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                         } else if (markerPoints.size() == 2) {
                             options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                         }

                         // Add new marker to the Google Map Android API V2
                         mGoogleMap.addMarker(options);

                         // Checks, whether start and end locations are captured
                         if (markerPoints.size() >= 2) {
                             LatLng origin = (LatLng) markerPoints.get(0);
                             LatLng dest = (LatLng) markerPoints.get(1);

                             Log.d("Demo","HI 2");
//                             mGoogleMap.clear();
//                             Uri mapIntentUri = Uri.parse("google.navigation:q="+origin.latitude+","+origin.longitude
//                                     +"&waypoints="+dest.latitude+","+dest.longitude
//                             );
//
//                             Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
//                             mapIntent.setPackage("com.google.android.apps.maps");
//                             if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
//                                 startActivity(mapIntent);
//                             }

//                             MapExtensionKt.drawRouteOnMap(mGoogleMap,
//                                     getString(R.string.apikey),
//                                     getContext(),
//                                     origin,
//                                     dest,
//                                     getResources().getColor(com.maps.route.R.color.pathColor),
//                                     true,true,13, TravelMode.DRIVING,null,
//                                     (estimates->{
////                                         Log.d("estimatedTimeOfArrival", "withUnit " + estimates.getDuration().getText());
////                                         Log.d("estimatedTimeOfArrival", "InMilliSec " + estimates.getDuration().getValue());
//
////                                         Google suggested path distance
////                                         Log.d("GoogleSuggestedDistance", "withUnit " + estimates.getDistance().getText());
////                                         Log.d("GoogleSuggestedDistance", "InMilliSec " + estimates.getDistance().getValue());
//
//                                         return null;
//                                     })
//                                     );

                             direction(origin,dest);




//                             String url = getDirectionsUrl( origin,  dest);
////
//                             Log.d("Demo",origin+","+dest);
//
//                             direction( origin,  dest);

                         }

                     }
                 });

                markerOptions = new MarkerOptions();
                //set mark position location
                markerOptions.position(latLng);
//                markerOptions.title(latLng.latitude + " KG " + latLng.longitude);
        //set map type
//                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                googleMap.clear();


//        set animation camera to mark position location and zoom 20%
                 googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));

                 Marker window = googleMap.addMarker(markerOptions
                         //Set description on marker pop up dialog
                                 .snippet("Innovation Through Technology")
                         //Set Title on marker pop up dialog
                                 .title(title)
                         //change icon marker
                         .icon(BitmapDescriptorFactory.fromResource(R.drawable.alien50))
                 );
        window.showInfoWindow();

        // Instantiates a new Polyline object and adds points to define a rectangle
        List<LatLng> hole = Arrays.asList(new LatLng(11.573438, 104.892923),
                new LatLng(11.573443, 104.892982),
                new LatLng(11.573363, 104.892993),
                new LatLng(11.573360, 104.892922),
                new LatLng(11.573438, 104.892923));
        Polygon hollowPolygon = mGoogleMap.addPolygon(new PolygonOptions().strokeColor(Color.RED)
                .add(new LatLng(11.573293, 104.893043),
                        new LatLng(11.573577, 104.892997),
                        new LatLng(11.573558, 104.892834),
                        new LatLng(11.573285, 104.892845),
                        new LatLng(11.573293, 104.893043))
                .addHole(hole)
                .fillColor(Color.DKGRAY)

        );

        Circle circle = mGoogleMap.addCircle(new CircleOptions().center(new LatLng(11.573203, 104.892949)).radius(10).strokeColor(Color.GREEN).fillColor(Color.RED));

        StyleSpan styleSpan = new StyleSpan(StrokeStyle.colorBuilder(Color.RED).build());
        Polyline polyline = mGoogleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(11.573168, 104.892804),new LatLng(11.573233, 104.893277),new LatLng(11.573431, 104.892990),new LatLng(11.573168, 104.892804))
                .color(Color.DKGRAY)

        );


// Get back the mutable Polyline
//        Polyline polyline = mGoogleMap.addPolyline(polylineOptions);
    }



    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private void direction(LatLng origin, LatLng dest){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String destStr = dest.latitude+", "+ dest.longitude;
        String originStr = origin.latitude+", "+origin.longitude;

        Log.d("Demo","Dest:"+destStr);
        Log.d("Demo","origin:"+originStr);

        String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon()
                .appendQueryParameter("destination",destStr)
                .appendQueryParameter("origin",originStr)
                .appendQueryParameter("mode","driving")
                .appendQueryParameter("key",getString(R.string.apikey))
                .toString();

        Network.getInstance().newCall(url, "", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    String jsonRes = response.body().string();
                    try{
                        JSONObject jsonObject = new JSONObject(jsonRes);
                        String encodedString = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline").getString("points");
                        Log.d("Demo","Data:"+ encodedString);
                        ArrayList<LatLng> puntos= decodePoly(encodedString);

                        Log.d("Demo","puntos:"+puntos);
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // Stuff that updates the UI
                                mGoogleMap.addPolyline(new PolylineOptions().addAll(puntos).color(Color.BLUE));
                            }
                        });

                    }catch (JSONException e){

                    }
                }

            }
        });
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.base_fragment,new MapsFragment(latLng,title)).commit();
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE, true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }



    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
        private final View myContentsView;

        MyInfoWindowAdapter() {
            this.myContentsView = getLayoutInflater().inflate(R.layout.custom_info_window,null);
        }

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {

            return null;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            return null;
        }
    }

    private ArrayList<LatLng> decodePoly(String encoded) {

        Log.i("Location", "String received: "+encoded);
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

//            LatLng p = new LatLng((int) (((double) lat /1E5)* 1E6), (int) (((double) lng/1E5   * 1E6)));
            LatLng p = new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));
            poly.add(p);
        }

        for(int i=0;i<poly.size();i++){
            Log.i("Location", "Point sent: Latitude: "+poly.get(i).latitude+" Longitude: "+poly.get(i).longitude);
        }
        return poly;
    }


}



