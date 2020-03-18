package com.example.prototype2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.prototype2.Fragments.ChatActivity;
import com.example.prototype2.Geofence.GeofenceActivity;
import com.example.prototype2.HomePage.HomePage;
import com.example.prototype2.HomePage.HomeRecyclerViewItem;
import com.example.prototype2.Marker.MarkerFragment;
import com.example.prototype2.Trust.alertBySMS;
import com.example.prototype2.Trust.alertMap;

import java.util.ArrayList;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RelativeLayout mFloatBtnWrapper;
    private AbsoluteLayout.LayoutParams mFloatBtnWindowParams;
    private AbsoluteLayout mFloatRootView;
    private DrawerLayout drawer;
    private Rect mFloatViewBoundsInScreens;
    private int mEdgePadding;
    private ImageView mImageView;
    private FloatingActionButton fab, btn_call, voice;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkVoiceCommandPermission();
        InitialSpeechRecognize();

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matchesFound = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matchesFound != null) {
                    keeper = matchesFound.get(0);
                    if (keeper.contains("help") || keeper.contains("救命")) {
                        Intent loc;
                        loc = new Intent(MainActivity.this, alertBySMS.class);
                        startActivity(loc);

                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        addFloatBtn();
//        setTouchListener();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        fab = (FloatingActionButton) findViewById(R.id.movableFloatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loc;
                loc = new Intent(MainActivity.this, alertBySMS.class);
//                loc.putExtra("data", "mainActivity");
                startActivity(loc);
            }
        });
        btn_call = findViewById(R.id.fab);
        btn_call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.askPermissWithPermissionCheck(MainActivity.this);
            }
        });
        voice = findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                speechRecognizer.startListening(speechRecognizerIntent);
                keeper = "";
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment fragment = new HomePage();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        switch (id) {
            case R.id.nav_before_danger_way1:
                fragment = new GeofenceActivity();
//                toolbar.setTitle("地圖圍欄");
                break;
//            case R.id.nav_before_danger_way2:
//                fragment = new MarkerFragment();
//                break;
            case R.id.nav_when_danger_way1:
                fragment = new MarkerFragment();
//                toolbar.setTitle("救援地圖標記");
                break;
            case R.id.nav_when_danger_way2:
                fragment = new alertMap();
//                toolbar.setTitle("信賴聯絡人狀態");
                break;
//            case R.id.nav_after_danger_way1:
//                fragment = new MarkerFragment();
//                break;
            case R.id.nav_help:
                intent = new Intent(this, ChatActivity.class);
//                toolbar.setTitle("聯絡人聊天室");
                break;
//            case R.id.nav_feedback:
//                intent = new Intent(this, FeedbackActivity.class);
//                break;
            default:
                fragment = new HomePage();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else {
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menuhome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_help:
                // change this code beacuse your app will crash
                startActivity(new Intent(this, ChatActivity.class));
                return true;
        }

        return false;
    }

    public boolean onHomeSelected(HomeRecyclerViewItem item) {
        String id = item.getFragmentName();
        Fragment fragment = null;
        Intent intent = null;

        switch (id) {
            case "前往地圖圍欄":
                fragment = new GeofenceActivity();
                break;
//            case R.id.nav_before_danger_way2:
//                fragment = new MarkerFragment();
//                break;
//            case R.id.nav_when_danger_way1:
//                fragment = new MarkerFragment();
//                break;
//            case R.id.nav_when_danger_way2:
//                fragment = new alertMap();
//                break;
//            case R.id.nav_after_danger_way1:
//                fragment = new MarkerFragment();
//                break;
//            case R.id.nav_help:
//                intent = new Intent(this, ChatActivity.class);
//                break;
//            case R.id.nav_feedback:
//                intent = new Intent(this, FeedbackActivity.class);
//                break;
//            default:
//                fragment = new HomePage();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else {
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        speechRecognizer.stopListening();
        speechRecognizer.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getCurrentFocus() != null && event.getAction() == KeyEvent.ACTION_DOWN) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new HomePage());
            ft.commit();
        } else {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("確定要退出嗎?");
            // 添加选择按钮并注册监听
            isExit.setButton("黑丟", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();
        }
        return false;

    }

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
        }
    };

    @NeedsPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void askPermiss() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:052721114"));
        startActivity(callIntent);
        Toasty.success(this, "撥號成功!", Toast.LENGTH_SHORT, true).show();
//        Toasty.success(this, "撥號成功!", Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void onPermiss(final PermissionRequest request) {
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})
    void refusePermiss() {
        Toasty.error(this, "尚未取得你的電話權限", Toast.LENGTH_SHORT, true).show();
    }

    private void checkVoiceCommandPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    private void InitialSpeechRecognize() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    }
}
