package com.emergency;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

//import android.Context;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        //Typeface typeface=Typeface.createFromAsset(getAssets(),"airliner.png");
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            //Place place
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            String number=googlePlace.get("Phone Number");
           // String number=googlePlace.get()
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.snippet(vicinity+number);
            String info=markerOptions.getSnippet();
            Log.d(placeName,vicinity);
            if(placeName.toLowerCase().contains("airport") || vicinity.toLowerCase().contains("airport"))
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.airliner));
            else if(placeName.toLowerCase().contains("hospital") || placeName.toLowerCase().contains("medic") || placeName.toLowerCase().contains("clinic")
                    || vicinity.toLowerCase().contains("hospital")|| vicinity.toLowerCase().contains("medic")|| vicinity.toLowerCase().contains("clinic"))
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.medical));
            else if(placeName.toLowerCase().contains("police") || vicinity.toLowerCase().contains("police"))
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.policeman));
          //  else
            //    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));


            mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }


}


