package pt.ulisboa.tecnico.cmov.conversationalist;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pt.ulisboa.tecnico.cmov.conversationalist.data.Data;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.Chatroom;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.GeoChatroom;

public class JoinChatroomActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    private static List<? extends Chatroom> filterAlreadyJoined(List<Chatroom> joinedChatrooms, List<? extends Chatroom> allChatrooms) {
        var ret = new ArrayList<>(allChatrooms);
        for (var chat : allChatrooms) {
            if (joinedChatrooms.contains(chat)) {
                ret.remove(chat);
            }
        }
        return ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_chatroom);
        var joinedChatrooms = Data.getJoinedChatrooms().getChatrooms();
        var publicChatrooms = filterAlreadyJoined(joinedChatrooms, BackendManager.getPublicChatrooms().getList());
        var geoChatrooms = filterAlreadyJoined(joinedChatrooms, BackendManager.getGeoChatrooms().getList());
        var chatrooms = new ArrayList<Chatroom>();
        chatrooms.addAll(publicChatrooms);
        chatrooms.addAll(geoChatrooms);
        var availableChatroomIds = new ArrayList<String>();
        for(var chat : chatrooms)
            availableChatroomIds.add(chat.getChatId());
        var adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, availableChatroomIds);
        ListView chatroomsListView = findViewById(R.id.availableChatroomsListView);
        chatroomsListView.setOnItemClickListener((parent, view, position, id) -> {
            var chat = chatrooms.get(position);
            Log.i("mytag", "joining " + chat.getChatId());
            if(Objects.equals(chat.getType(), "public")) {
                BackendManager.joinRoom(Data.getUsername(), chat.getChatId());
                Data.updateJoinedChatrooms();
                var intent = new Intent(this, ChatroomActivity.class);
                intent.putExtra("chatId", chat.getChatId());
                finish();
                startActivity(intent);
            }
            if(Objects.equals(chat.getType(), "geo")) {
                Log.i("mytag", "joining geo");
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                getLocationPermission();
                getDeviceLocation();
                if(mLastKnownLocation != null) {
                    Log.i("mytag", "location isn't null yay");
                    Location chatLocation = new Location(mLastKnownLocation);
                    Log.i("mytag", "goodchatlat" + ((GeoChatroom) chat).getLatitude());
                    Log.i("mytag", "goodchatlng" + ((GeoChatroom) chat).getLongitude());
                    chatLocation.setLatitude(((GeoChatroom) chat).getLatitude());
                    chatLocation.setLongitude(((GeoChatroom) chat).getLongitude());
                    Log.i("mytag", "lat" + mLastKnownLocation.getLatitude() + "long" + mLastKnownLocation.getLongitude());
                    Log.i("mytag", "chatlat" + chatLocation.getLatitude() + "chatlong" + chatLocation.getLongitude());

                    if(chatLocation.distanceTo(mLastKnownLocation) < ((GeoChatroom) chat).getRadius()) {
                        BackendManager.joinRoom(Data.getUsername(), chat.getChatId());
                        Data.updateJoinedChatrooms();
                        var intent = new Intent(this, ChatroomActivity.class);
                        intent.putExtra("chatId", chat.getChatId());
                        finish();
                        startActivity(intent);
                    }
                }
                else {
                    Log.i("mytag", "mlastknownlocation is null");
                }
            }
        });
        chatroomsListView.setAdapter(adapter);
    }

    public static double distance_Between_LatLong(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371.01; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2));
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Log.i("mytag", "is this task really successful though? wtf man");
                            mLastKnownLocation = task.getResult();
                            if(mLastKnownLocation == null)
                                Log.i("mytag", "told ya man, shit's null");
                            else {
                                Log.d("mytag", "Latitude: " + mLastKnownLocation.getLatitude());
                                Log.d("mytag", "Longitude: " + mLastKnownLocation.getLongitude());
                                LatLng lastKnown = new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude());
                            }
                        } else {
                            Log.d("mytag", "Current location is null. Using defaults.");
                            Log.e("mytag", "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}