package com.example.prototype2.Marker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class MarkerFragment extends Fragment implements OnMapReadyCallback {

    public static final int N = 38;
    public static boolean[] bounceMarker = new boolean[N];
    public static GoogleMap mMap;
    public static Marker[] markers = new Marker[N];

    private static final int REQUEST_CODE_LOCATION_SETTINGS = 101;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    private int bounceFlag = -1, lastBounceFlag = -1, nowLength = 110;
    private MapView mMapView;
    private View mView;
    private FloatingActionButton btn_cross, btn_direct, btn_here;
    private CheckBox chungchengCheckbox, familyCheckbox, seven11Checkbox, aedCheckbox, phoneCheckbox;
    private boolean animateCameraFlag = true, getService = false, removeFlag = false;
    private boolean AEDFlag = true, chungchengFlag = true, familyFlag = true, phoneFlag = true, seven11Flag = true;
    private Location lastLocation;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    private double previousZoomLevel = 17;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int screenHeight;

    private Marker markerMe;
    private LatLng latLngMe, toLatLng;
    final MarkerFragment temp = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_marker, container, false);
        MarkerInit.initPoints();
        MarkerInit.initImg();
        MarkerInit.initBounceMarker();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;

        LinearLayout blank = mView.findViewById(R.id.blank);
        ViewGroup.LayoutParams params = blank.getLayoutParams();
        params.height = (int) (screenHeight * 0.13);
        blank.setLayoutParams(params);

        TextView notice = mView.findViewById(R.id.notice);
        ViewGroup.LayoutParams params1 = notice.getLayoutParams();
        params1.height = (int) (screenHeight * 0.1);
        notice.setLayoutParams(params1);

        NestedScrollView bottomSheet = mView.findViewById(R.id.bottom_sheet);
        ViewGroup.LayoutParams params2 = bottomSheet.getLayoutParams();
        params2.height = (int) (screenHeight * 0.4);
        bottomSheet.setLayoutParams(params2);

        LinearLayout infoup = mView.findViewById(R.id.infoup);
        ViewGroup.LayoutParams params3 = infoup.getLayoutParams();
        params3.height = (int) (screenHeight * 0.1);
        infoup.setLayoutParams(params3);

        TextView nowInfo = mView.findViewById(R.id.nowInfo);
        ViewGroup.LayoutParams params4 = nowInfo.getLayoutParams();
        params4.height = (int) (screenHeight * 0.22);
        nowInfo.setLayoutParams(params4);

        LinearLayout infodown = mView.findViewById(R.id.infodown);
        ViewGroup.LayoutParams params5 = infodown.getLayoutParams();
        params5.height = (int) (screenHeight * 0.27);
        infodown.setLayoutParams(params5);

        final ImageView arrow = mView.findViewById(R.id.arrow);
        ViewGroup.LayoutParams params6 = arrow.getLayoutParams();
        params6.height = (int) (screenHeight * 0.02);
        arrow.setLayoutParams(params6);

        LinearLayout markerCheckbox = mView.findViewById(R.id.markerCheckbox);
        ViewGroup.LayoutParams params7 = markerCheckbox.getLayoutParams();
        params7.height = (int) (screenHeight * 0.25);
        markerCheckbox.setLayoutParams(params5);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight((int) (screenHeight * 0.13));
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                TextView notice = mView.findViewById(R.id.notice);
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        notice.setText("請點選標記以確認資訊!");
                        arrow.setBackground(getResources().getDrawable(R.drawable.up));
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        notice.setText("請點選欲顯示的標記!");
                        arrow.setBackground(getResources().getDrawable(R.drawable.down));
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                LinearLayout blank = mView.findViewById(R.id.blank);
                ViewGroup.LayoutParams params = blank.getLayoutParams();
                params.height = (int) (screenHeight * 0.13 + screenHeight * 0.27 * slideOffset);
                blank.setLayoutParams(params);
            }
        });
        btn_cross = mView.findViewById(R.id.btn_cross);
        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkerInit.animarFab(btn_cross);
                LinearLayout info = mView.findViewById(R.id.info);
                LinearLayout markerCheckbox = (LinearLayout) mView.findViewById(R.id.markerCheckbox);
                TextView notice = mView.findViewById(R.id.notice);
                info.setVisibility(View.GONE);
                markerCheckbox.setVisibility(View.VISIBLE);
                notice.setVisibility(View.VISIBLE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                MarkerInit.initBounceMarker();
                bounceFlag = -1;
                lastBounceFlag = -1;
                TextView tvTitle = mView.findViewById(R.id.tvTitle);
                TextView tvSnippet = mView.findViewById(R.id.tvSnippet);
                tvTitle.setText("");
                tvSnippet.setText("");
            }
        });
        btn_direct = mView.findViewById(R.id.btn_direct);
        btn_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkerInit.animarFab(btn_direct);
                String uriStr = String.format(Locale.US, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                        latLngMe.latitude, latLngMe.longitude, toLatLng.latitude, toLatLng.longitude);
                Intent i = new Intent(getActivity().getApplicationContext(), Navigation.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",uriStr);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        btn_here = mView.findViewById(R.id.btn_here);
        btn_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkerInit.animarFab(btn_here);
                MarkerInit.animateCameraCurrentPosition(markers[bounceFlag].getPosition());
            }
        });

        chungchengCheckbox = mView.findViewById(R.id.chungcheng);
        chungchengCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chungchengCheckbox.isChecked()) {
                    chungchengFlag = true;
                    if (nowLength != 0) addChungchengToMap(nowLength, nowLength);
                } else {
                    chungchengFlag = false;
                    MarkerInit.chungchengMarkerRemove();
                }
            }
        });
        seven11Checkbox = mView.findViewById(R.id.seven11);
        seven11Checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seven11Checkbox.isChecked()) {
                    seven11Flag = true;
                    if (nowLength != 0) addSeven11ToMap(nowLength, nowLength);
                } else {
                    seven11Flag = false;
                    MarkerInit.seven11MarkerRemove();
                }
            }
        });
        familyCheckbox = mView.findViewById(R.id.family);
        familyCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familyCheckbox.isChecked()) {
                    familyFlag = true;
                    if (nowLength != 0) addFamilyToMap(nowLength, nowLength);
                } else {
                    familyFlag = false;
                    MarkerInit.familyMarkerRemove();
                }
            }
        });
        aedCheckbox = mView.findViewById(R.id.aed);
        aedCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aedCheckbox.isChecked()) {
                    AEDFlag = true;
                    if (nowLength != 0) addAEDToMap(nowLength, nowLength);
                } else {
                    AEDFlag = false;
                    MarkerInit.aedMarkerRemove();
                }
            }
        });
        phoneCheckbox = mView.findViewById(R.id.phone);
        phoneCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneCheckbox.isChecked()) {
                    phoneFlag = true;
                    if (nowLength != 0) addPhoneToMap(nowLength, nowLength);
                } else {
                    phoneFlag = false;
                    MarkerInit.phoneMarkerRemove();
                }
            }
        });
        return mView;
    }

    private void addChungchengToMap(int width, int height) {
        BitmapDrawable bitChungcheng = (BitmapDrawable) getResources().getDrawable(R.drawable.policeicon);
        Bitmap bChungcheng = bitChungcheng.getBitmap();
        Bitmap changedChungcheng = Bitmap.createScaledBitmap(bChungcheng, width, height, false);
//        MarkerInit.addChungchengMarker(changedChungcheng, getString(R.string.marker_title_chungcheng));
        MarkerInit.addChungchengMarker(changedChungcheng, "學校警衛室");
    }

    private void addFamilyToMap(int width, int height) {
        BitmapDrawable bitFamily = (BitmapDrawable) getResources().getDrawable(R.drawable.logo_family);
        Bitmap bFamily = bitFamily.getBitmap();
        Bitmap changedFamily = Bitmap.createScaledBitmap(bFamily, width, height, false);
        MarkerInit.addFamilyMarker(changedFamily, getString(R.string.marker_title_family));
    }

    private void addSeven11ToMap(int width, int height) {
        BitmapDrawable bitSeven11 = (BitmapDrawable) getResources().getDrawable(R.drawable.logo_seven11);
        Bitmap bSeven11 = bitSeven11.getBitmap();
        Bitmap changedSeven11 = Bitmap.createScaledBitmap(bSeven11, width, height, false);
        MarkerInit.addSeven11Marker(changedSeven11, getString(R.string.marker_title_seven11));
    }

    private void addAEDToMap(int width, int height) {
        BitmapDrawable bitAED = (BitmapDrawable) getResources().getDrawable(R.drawable.logo_aed);
        Bitmap bAED = bitAED.getBitmap();
        Bitmap changedAED = Bitmap.createScaledBitmap(bAED, width, height, false);
        MarkerInit.addAEDMarker(changedAED, getString(R.string.marker_title_AED));
    }

    private void addPhoneToMap(int width, int height) {
        BitmapDrawable bitPhone = (BitmapDrawable) getResources().getDrawable(R.drawable.logo_phone);
        Bitmap bPhone = bitPhone.getBitmap();
        Bitmap changedPhone = Bitmap.createScaledBitmap(bPhone, width, height, false);
        MarkerInit.addPhoneMarker(changedPhone, getString(R.string.marker_title_phone));
    }

    private void addMarkersToMap(int width, int height) {
        if (removeFlag) MarkerInit.markerRemove();
        removeFlag = true;
        if (chungchengFlag) addChungchengToMap(width, height);
        if (seven11Flag) addSeven11ToMap(width, height);
        if (familyFlag) addFamilyToMap(width, height);
        if (AEDFlag) addAEDToMap(width, height);
        if (phoneFlag) addPhoneToMap(width, height);
    }

    public GoogleMap.OnCameraChangeListener getCameraChangeListener() {
        return new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                if (position.zoom >= previousZoomLevel + 1 || position.zoom < previousZoomLevel) {
                    if (position.zoom == 20) {
                        addMarkersToMap(250, 250);
                        nowLength = 250;
                    } else if (position.zoom >= 19) {
                        addMarkersToMap(210, 210);
                        nowLength = 210;
                    } else if (position.zoom >= 18) {
                        addMarkersToMap(150, 150);
                        nowLength = 150;
                    } else if (position.zoom >= 17) {
                        addMarkersToMap(110, 110);
                        nowLength = 110;
                    } else if (position.zoom >= 16) {
                        addMarkersToMap(70, 70);
                        nowLength = 70;
                    } else {
                        MarkerInit.markerRemove();
                        nowLength = 0;
                    }
                    if (bounceFlag >= 0)
                        MarkerInit.setMarkerBounce(markers[bounceFlag], bounceFlag);
                }
                previousZoomLevel = Math.floor(position.zoom);
            }
        };
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnCameraChangeListener(getCameraChangeListener());
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        addMarkersToMap(100, 100);
        MyMarkerListener myMarkerListener = new MyMarkerListener();
        mMap.setOnMarkerClickListener(myMarkerListener);
        mMap.setOnInfoWindowClickListener(myMarkerListener);
    }

    private void requestLocationPermission() {
        // 如果裝置版本是6.0（包含）以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 取得授權狀態，參數是請求授權的名稱
            int hasPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            // 如果未授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                // 請求授權
                //     第一個參數是請求授權的名稱
                //     第二個參數是請求代碼
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION_PERMISSION);
            }
        }
    }

    private void createLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = location;
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                //get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String result = addresses.get(0).getLocality() + ":";
                    result += addresses.get(0).getCountryName();
                    latLngMe = new LatLng(latitude, longitude);
                    if (markerMe != null) markerMe.remove();
                    markerMe = mMap.addMarker(new MarkerOptions().position(latLngMe).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_now)).title(result));
                    mMap.setMaxZoomPreference(20);
                    if (animateCameraFlag) {
                        MarkerInit.animateCameraCurrentPosition(latLngMe);
                        animateCameraFlag = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    private boolean isOPen() {
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    private void currentLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Criteria criteria = new Criteria();    //資訊提供者選取標準
        bestProvider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(bestProvider, 5000, 0, locationListener);
    }

    private void openGPSDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("請打開GPS")
                .setMessage("為了定位您現在的位置，請打開GPS，謝謝!")
                .setPositiveButton("設定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        if (!isOPen()) {
            showToast("請開啟GPS");
            getService = false;
            openGPSDialog();
        } else {
            getService = true;
            createLocationListener();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getService) {
            currentLocationUpdates();  //更新位置
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getService) {
            locationManager.removeUpdates(locationListener);    //離開頁面時停止更新
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 如果是讀取位置授權請求
        if (requestCode == REQUEST_FINE_LOCATION_PERMISSION) {
            // 如果在授權請求選擇「允許」
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 啟動位置更新服務
                showToast("已讀取位置授權");
                if (!isOPen()) {
                    showToast("請開啟GPS");
                    getService = false;
                    openGPSDialog();
                } else {
                    getService = true;
                    createLocationListener();
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                }
            }
            // 如果在授權請求選擇「拒絕」
            else {
                // 顯示沒有授權的訊息
                showToast("未讀取位置授權");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private class MyMarkerListener implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
        @Override
        public boolean onMarkerClick(final Marker marker) {
            if (!isOPen()) {
                showToast("請開啟GPS");
                getService = false;
                openGPSDialog();
                return true;
            }
            if (lastLocation == null) {
                showToast("尚未收到目前位置");
                return true;
            }
            lastBounceFlag = bounceFlag;

            LinearLayout info = mView.findViewById(R.id.info);
            LinearLayout infodown = mView.findViewById(R.id.infodown);
            LinearLayout markerCheckbox = mView.findViewById(R.id.markerCheckbox);
            TextView nowInfo = mView.findViewById(R.id.nowInfo);
            TextView notice = mView.findViewById(R.id.notice);
            ImageView img_icon = mView.findViewById(R.id.img_icon);
            ImageView img = mView.findViewById(R.id.img);
            toLatLng = marker.getPosition();
            String title, snippet, text;
            TextView tvTitle, tvSnippet;
            float[] results = new float[1];

            title = marker.getTitle();
            tvTitle = mView.findViewById(R.id.tvTitle);
            tvTitle.setText(title);
            tvSnippet = mView.findViewById(R.id.tvSnippet);
            info.setVisibility(View.VISIBLE);
            markerCheckbox.setVisibility(View.GONE);
            notice.setVisibility(View.GONE);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            if (toLatLng.equals(latLngMe)) {
                snippet = "當前位置";

                infodown.setVisibility(View.GONE);
                nowInfo.setVisibility(View.VISIBLE);

                String message = "";
                message += "當前位置訊息:\n";
                Date date = new Date(lastLocation.getTime());
                DateFormat dateFormat = DateFormat.getDateTimeInstance();
                String time = dateFormat.format(date);
                message += "時間: " + time + "\n";
                message += "緯度: " + lastLocation.getLatitude() + "\n";
                message += "經度: " + lastLocation.getLongitude() + "\n";
                message += "精準度(公尺): " + lastLocation.getAccuracy() + "\n";
                message += "海拔高度(公尺): " + lastLocation.getAltitude() + "\n";
                message += "方向: " + lastLocation.getBearing() + "\n";
                message += "速度(公尺/秒): " + lastLocation.getSpeed() + "\n";
                nowInfo.setText(message);

                img_icon.setBackground(getResources().getDrawable(R.drawable.logo_now));
                tvSnippet.setText(snippet);
                MarkerInit.initBounceMarker();
                lastBounceFlag = -1;
                bounceFlag = -1;
                return true;
            }

            for (int i = 0; i < N; i++) {
                if (marker.equals(markers[i])) {
                    bounceFlag = i;
                    img.setBackground(getResources().getDrawable(MarkerInit.imgId[i]));
                    break;
                }
            }

            if (bounceFlag != lastBounceFlag) {
                MarkerInit.initBounceMarker();
                bounceMarker[bounceFlag] = false;
                MarkerInit.setMarkerBounce(marker, bounceFlag);
            }

            Location.distanceBetween(latLngMe.latitude, latLngMe.longitude, toLatLng.latitude, toLatLng.longitude, results);
            text = String.format(Locale.getDefault(), "距離%.2f公尺", results[0]);
            tvSnippet.setText(text);

            if (bounceFlag == 0)
                img_icon.setBackground(getResources().getDrawable(R.drawable.logo_chungcheng));
            if (bounceFlag == 1)
                img_icon.setBackground(getResources().getDrawable(R.drawable.logo_seven11));
            if (bounceFlag >= 2 && bounceFlag <= 5)
                img_icon.setBackground(getResources().getDrawable(R.drawable.logo_family));
            if (bounceFlag >= 6 && bounceFlag <= 10)
                img_icon.setBackground(getResources().getDrawable(R.drawable.logo_aed));
            if (bounceFlag >= 11 && bounceFlag <= 37)
                img_icon.setBackground(getResources().getDrawable(R.drawable.logo_phone));

            infodown.setVisibility(View.VISIBLE);
            nowInfo.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onInfoWindowClick(Marker marker) {
        }
    }
}
