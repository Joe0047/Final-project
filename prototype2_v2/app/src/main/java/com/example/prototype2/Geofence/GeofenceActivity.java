package com.example.prototype2.Geofence;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.prototype2.R;
import com.example.prototype2.Trust.alarmReceiver;
import com.example.prototype2.Trust.alarmReceiver2;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.app.Activity.RESULT_OK;

public class GeofenceActivity extends Fragment {

    private Button doSetAlarm;
    private Button doSetAlarm2;
    private Button setPlace;
    private Button setArea;
    private Button doCheck;
    private TextView textPlace;
    private TextView textArea;
    private TextView mTextView;
    private TextView mTextView2;
    private String sArea = "";
    private int Area = 0;
    public LatLng Center;
    final GregorianCalendar calendar = new GregorianCalendar();
    final GregorianCalendar calendar2 = new GregorianCalendar();
    private View aView;
    private ImageButton setWhere1;
    private ImageButton setRange1;
    private ImageButton setArrive1;
    private ImageButton setLeave1;
    private ImageButton submitCheck;
//    private Button angryButton ;
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        aView = inflater.inflate(R.layout.activity_geofence, container, false);
        doFindView();

setWhere1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        setPlace();
    }
});
setRange1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(textArea.length()==0){
            Toast.makeText(getActivity(), "範圍尚未設定", Toast.LENGTH_LONG).show();
        }
        else{
            sArea = textArea.getText().toString();
            Area = Integer.parseInt(sArea);
            Toast.makeText(getActivity(), "範圍設定完成", Toast.LENGTH_LONG).show();
    }
    }
});
setArrive1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(getActivity(), new OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                //sendBroadcast(intent);
                String tmps = "設定到達時間為" + format(hourOfDay) + ":" + format(minute);
                mTextView.setText(tmps);
            }
        }, hour, minute, true).show();
    }
});
setLeave1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        calendar2.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar2.get(Calendar.HOUR_OF_DAY);
        int minute = calendar2.get(Calendar.MINUTE);
        new TimePickerDialog(getActivity(), new OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar2.setTimeInMillis(System.currentTimeMillis());
                calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar2.set(Calendar.MINUTE, minute);
                calendar2.set(Calendar.SECOND, 0);
                calendar2.set(Calendar.MILLISECOND, 0);

                //sendBroadcast(intent);
                String tmps = "設定離開時間為" + format(hourOfDay) + ":" + format(minute);
                mTextView2.setText(tmps);
            }
        }, hour, minute, true).show();
    }
});
//        setPlace.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                setPlace();
//            }
//        });
//
//        setArea.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(textArea.length()==0){
//                    Toast.makeText(getActivity(), "範圍尚未設定", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    sArea = textArea.getText().toString();
//                    Area = Integer.parseInt(sArea);
//                    Toast.makeText(getActivity(), "範圍設定完成", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        angryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Area != 0 && Center != null){
//                    Intent intent = new Intent(getActivity(), alarmReceiver.class);
//                    sArea = textArea.getText().toString();
//                    Area = Integer.parseInt(sArea);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("Center",Center);
//                    bundle.putInt("Area",Area);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(),alarmReceiver.class).putExtra("bundle",bundle), 0);
//                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(getContext().ALARM_SERVICE);
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(),alarmReceiver2.class), 0);
//                    AlarmManager alarmManager2 = (AlarmManager)getActivity().getSystemService(getContext().ALARM_SERVICE);
//                    alarmManager2.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
//                    Toast.makeText(getActivity(), "設定完成", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Toast.makeText(getActivity(), "尚未設定完成", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        submitCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Area != 0 && Center != null){
                    Intent intent = new Intent(getActivity(), alarmReceiver.class);
                    sArea = textArea.getText().toString();
                    Area = Integer.parseInt(sArea);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Center",Center);
                    bundle.putInt("Area",Area);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(),alarmReceiver.class).putExtra("bundle",bundle), 0);
                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(getContext().ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(),alarmReceiver2.class), 0);
                    AlarmManager alarmManager2 = (AlarmManager)getActivity().getSystemService(getContext().ALARM_SERVICE);
                    alarmManager2.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
                    Toast.makeText(getActivity(), "設定完成", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(), "尚未設定完成", Toast.LENGTH_LONG).show();
                }
            }
        });
//        doCheck.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(Area != 0 && Center != null){
//                    Intent intent = new Intent(getActivity(), alarmReceiver.class);
//                    sArea = textArea.getText().toString();
//                    Area = Integer.parseInt(sArea);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("Center",Center);
//                    bundle.putInt("Area",Area);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(),alarmReceiver.class).putExtra("bundle",bundle), 0);
//                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(getContext().ALARM_SERVICE);
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(),alarmReceiver2.class), 0);
//                    AlarmManager alarmManager2 = (AlarmManager)getActivity().getSystemService(getContext().ALARM_SERVICE);
//                    alarmManager2.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
//                    Toast.makeText(getActivity(), "設定完成", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Toast.makeText(getActivity(), "尚未設定完成", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        // 實作DatePickerDialog的onDateSet方法，設定日期後將所設定的日期show在textDate上
//        doSetAlarm.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar.get(Calendar.MINUTE);
//                new TimePickerDialog(getActivity(), new OnTimeSetListener() {
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        calendar.setTimeInMillis(System.currentTimeMillis());
//                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                        calendar.set(Calendar.MINUTE, minute);
//                        calendar.set(Calendar.SECOND, 0);
//                        calendar.set(Calendar.MILLISECOND, 0);
//
//                        //sendBroadcast(intent);
//                        String tmps = "設定到達時間為" + format(hourOfDay) + ":" + format(minute);
//                        mTextView.setText(tmps);
//                    }
//                }, hour, minute, true).show();
//            }
//        });
//
//        doSetAlarm2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                calendar2.setTimeInMillis(System.currentTimeMillis());
//                int hour = calendar2.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar2.get(Calendar.MINUTE);
//                new TimePickerDialog(getActivity(), new OnTimeSetListener() {
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        calendar2.setTimeInMillis(System.currentTimeMillis());
//                        calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                        calendar2.set(Calendar.MINUTE, minute);
//                        calendar2.set(Calendar.SECOND, 0);
//                        calendar2.set(Calendar.MILLISECOND, 0);
//
//                        //sendBroadcast(intent);
//                        String tmps = "設定離開時間為" + format(hourOfDay) + ":" + format(minute);
//                        mTextView2.setText(tmps);
//                    }
//                }, hour, minute, true).show();
//            }
//        });
        //setDoCheck();
        return aView;
    }
    public void doFindView() {
//        doSetAlarm = (Button) aView.findViewById(R.id.button);
//        doSetAlarm2 = (Button) aView.findViewById(R.id.button2);
        textPlace = (TextView) aView.findViewById(R.id.placetext);
        textPlace.bringToFront();
//         angryButton = (Button) aView.findViewById(R.id.angry_btn);
//         angryButton.bringToFront();
        textArea =(TextView) aView.findViewById(R.id.editArea);
        textArea.bringToFront();
        mTextView=(TextView)aView.findViewById(R.id.textView);
        mTextView.bringToFront();
        mTextView2 = (TextView)aView.findViewById(R.id.textView2);
        mTextView2.bringToFront();
//        setPlace = (Button)aView.findViewById(R.id.buttonPlace);
//        setArea = (Button)aView.findViewById(R.id.setArea);
//        doCheck = (Button) aView.findViewById(R.id.check);
        setWhere1=(ImageButton)aView.findViewById(R.id.imageButton);
        setRange1=(ImageButton)aView.findViewById(R.id.imageButton2);
        setRange1.bringToFront();
setArrive1 =(ImageButton)aView.findViewById(R.id.imageButton3);
setLeave1=(ImageButton)aView.findViewById(R.id.imageButton4);
setLeave1.bringToFront();
submitCheck=(ImageButton)aView.findViewById(R.id.imageButton6);
submitCheck.bringToFront();
    }


    // setPlace Button on Click 顯示地點設定視窗
    private void setPlace(){
        int REQUEST_PLACE_PICKER = 1;

        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {
            intent = intentBuilder.build(getActivity());
            startActivityForResult(intent, REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    //placePicker 結果輸出
    @Override
    //動過不知道對不對
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data,getContext() );
//錯了再改activity
                Log.e("place", place.toString());
                textPlace.setText(place.getName().toString());
                Center = place.getLatLng();
            }
        }
    }

    public boolean onCreateOptionsMenu() {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
    public String format(int time){
        String str = ""+time;
        if(str.length() == 1){
            str = "0"+str;
        }
        return str;
    }
}
