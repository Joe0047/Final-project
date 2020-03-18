package com.example.prototype2.Trust;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype2.Fragments.APIService;
import com.example.prototype2.Model.User;
import com.example.prototype2.Notification.Client;
import com.example.prototype2.Notification.Data;
import com.example.prototype2.Notification.MyResponse;
import com.example.prototype2.Notification.Sender;
import com.example.prototype2.Notification.Token;
import com.example.prototype2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class alertBySMS extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    // 顯示經、緯度的畫面元件
    private TextView longitude_text, latitude_text,code_text;

    private EditText code;

    private Button set;

    // Google Play services API 用戶
    private GoogleApiClient googleApiClient;

    // 位置資訊更新物件
    private LocationRequest locationRequest;

    // 請求授權使用的請求代碼
    private static final int REQUEST_LOCATION_PERMISSION = 100;

    // 宣告控制讀取位置狀態圖示的 Handler 物件
    private Handler locationHandler = new LocationHandler(this);


    APIService apiService;

    private FirebaseUser fuser;

    DatabaseReference reference;
    DatabaseReference reference_help;
    DatabaseReference reference_cancel;

    MediaPlayer mediaplayer;

    String trust;
    String main;
    boolean send = true;

    boolean notify = false;

    // 宣告控制讀取位置狀態圖示的 Handler 類別
    private static class LocationHandler extends Handler {

        private final WeakReference<alertBySMS> hActivity;

        public LocationHandler(alertBySMS activity) {
            hActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            alertBySMS activity = hActivity.get();

            if (activity != null) {
                super.handleMessage(msg);
                // 隱藏讀取位置狀態圖示
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        processViews();

        // 建立 Google Play services API 用戶端物件
        configGoogleApiClient();

        // 建立位置資訊更新物件
        configLocationRequest();

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        startSiren();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
//        reference_help = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
//        reference_help.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                trust = user.getTrust();
//                main = user.getUsername();
//                String msg = user.getTrustName()+" help me!!!";
//                sendMessage(fuser.getUid(),trust,msg);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        set.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String done = code.getText().toString();
                String match = getSharedPreferences("pref", MODE_PRIVATE)
                        .getString("Pass", "");
                if(match == null){
                    Toast.makeText(alertBySMS.this, "尚未設定密碼！", Toast.LENGTH_SHORT).show();
                }
                else if(done.equals(match)){
                    reference_cancel = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                    reference_cancel.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("share", "false");
                            reference_cancel.updateChildren(map);
                            trust = user.getTrust();
                            main = user.getUsername();
                            String msg = "I'm safe now!!!";
                            sendMessage(fuser.getUid(),trust,msg);
                            sendNotifiaction(trust,main,msg);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    finish();
                }
                else {
                    Toast.makeText(alertBySMS.this, "密碼錯誤！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        // 連線到 Google Play services 用戶端服務
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        // 如果已經連線到 Google Play services 用戶端服務
        if (googleApiClient.isConnected()) {
            // 中斷 Google Play services 用戶端連線
            googleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        mediaplayer.stop();
        super.onDestroy();
    }

    private void processViews() {
        longitude_text = (TextView) findViewById(R.id.longitude_text);
        latitude_text = (TextView) findViewById(R.id.latitude_text);
        code_text = (TextView) findViewById(R.id.code_text);
        code = (EditText)findViewById(R.id.editText);
        set = (Button)findViewById(R.id.button3);

    }

    // 建立 Google Play services API 用戶端物件
    private synchronized void configGoogleApiClient() {
        if (googleApiClient == null) {
            // addConnectionCallbacks 設定連線狀態介面
            // addOnConnectionFailedListener 設定連線失敗介面
            // addApi 設定加入位置 API
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    // 建立與設定位置資訊更新物件
    private void configLocationRequest() {
        locationRequest = new LocationRequest();
        // 設定讀取位置資訊的間隔時間為一秒（1000ms）
        locationRequest.setInterval(1000);
        // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
        locationRequest.setFastestInterval(1000);
        // 設定優先讀取高精確度的位置資訊（GPS）
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // 實作 ConnectionCallbacks 介面的方法
    // 已經連線到 Google Play services
    @Override
    public void onConnected(Bundle bundle) {
        // 請求授權
        requestPermission();
    }

    // 實作 ConnectionCallbacks 介面的方法
    // Google Play services 連線中斷
    // int 參數是連線中斷的代號
    @Override
    public void onConnectionSuspended(int i) {
        Log.d("alertBySMS", "onConnectionSuspended: " + i);
    }

    // 實作 OnConnectionFailedListener 介面的方法
    // Google Play services 連線失敗
    // ConnectionResult 參數是連線失敗的資訊
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("alertBySMS",
                "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    // 實作 LocationListener 介面的方法
    // 位置資訊更新的時候，Android 會自動呼叫這個方法
    // Location 參數是目前的位置
    @Override
    public void onLocationChanged(Location location) {
        // 0.8 秒以後執行 Handler
        locationHandler.sendEmptyMessageDelayed(0, 100000);

        // 讀取目前位置的經、緯度
        final double longitude = location.getLongitude();
        final double latitude = location.getLatitude();

        // 設定經、緯度畫面元件
        longitude_text.setText(String.format("%.6f", longitude));
        latitude_text.setText(String.format("%.6f", latitude));

        if(send){
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            reference_help = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
            reference_help.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    trust = user.getTrust();
                    main = user.getUsername();
                    String msg = user.getTrustName()+" help me!!!";
                    sendMessage(fuser.getUid(),trust,msg);
                    sendNotifiaction(trust,main,msg);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            send = false;
        }

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put("latitude", ""+String.format("%.6f", latitude));
        map.put("longitude", ""+String.format("%.6f", longitude));
        map.put("share", "true");
        reference.updateChildren(map);

    }

    // 請求授權
    private void requestPermission() {
        // 如果裝置版本是6.0（包含）以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 取得讀取高精確度位置資訊授權狀態，參數是請求授權的名稱
            int hasPermission = checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION);

            // 如果未授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                // 請求讀取高精確度位置資訊授權
                //     第一個參數是請求授權的名稱
                //     第二個參數是請求代碼
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
                return;
            }
        }

        // 如果裝置版本是6.0以下，
        // 或是裝置版本是6.0（包含）以上，使用者已經授權
        // 啟動位置更新服務
        processLocation();
    }

    // 使用者完成授權的選擇以後，Android會呼叫這個方法
    //     第一個參數：請求授權代碼
    //     第二個參數：請求的授權名稱
    //     第三個參數：使用者選擇授權的結果
    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        // 如果是讀取位置授權請求
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // 如果在授權請求選擇「允許」
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 啟動位置更新服務
                processLocation();
            }
            // 如果在授權請求選擇「拒絕」
            else {
                // 顯示沒有授權的訊息
                Toast.makeText(this, "沒有讀取位置授權",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
        }
    }

    // 啟動位置更新服務
    private void processLocation() throws SecurityException {
        // 位置資訊更新的時候，應用程式會自動呼叫 LocationListener.onLocationChanged
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, alertBySMS.this);
    }


    public void startSiren(){
        mediaplayer = MediaPlayer.create(this,R.raw.police_siren);
        mediaplayer.setVolume(100,100);
        mediaplayer.start();
        mediaplayer.setLooping(true);
        mediaplayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
    }

    public void sendMessage(String sender, final String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);


        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(receiver);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(receiver)
                .child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid());
        

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    sendNotifiaction(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifiaction(final String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username+": "+message, "New Message",
                            receiver);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(alertBySMS.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
// 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
// 设置对话框标题
            isExit.setTitle("系统提示");
// 设置对话框消息
            isExit.setMessage("請輸入密碼才能退出！！！");
// 显示对话框
            isExit.show();
        }

        return false;

    }

}


