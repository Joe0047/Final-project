package com.example.prototype2.Trust;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;


public class alarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context,Intent intent) {
        int Area;
        LatLng center;
        Bundle bundle = intent.getParcelableExtra("bundle");
        Toast.makeText(context, "你設定的鬧鈴時間到了", Toast.LENGTH_LONG).show();
        center = bundle.getParcelable("Center");
        Area = bundle.getInt("Area",0);

        Intent alert = new Intent(context, alertBySMS.class);
        Intent map = new Intent(context, MapsActivity.class);
        map.putExtra("Center",center);
        map.putExtra("Area",Area);
        map.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(map);
        context.startActivity(alert);


    }
}
