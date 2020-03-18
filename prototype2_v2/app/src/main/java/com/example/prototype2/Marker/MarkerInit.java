package com.example.prototype2.Marker;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.example.prototype2.Marker.MarkerFragment;
import com.example.prototype2.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerInit {

    public static LatLng chungcheng;
    public static LatLng seven11;
    public static LatLng family1, family2, family3, family4;
    public static LatLng AED1, AED2, AED3, AED4, AED5;
    public static LatLng phone1, phone2, phone3, phone4, phone5, phone6, phone7, phone8, phone9, phone10, phone11, phone12, phone13, phone14, phone15,
            phone16, phone17, phone18, phone19, phone20, phone21, phone22, phone23, phone24, phone25, phone26, phone27;

    public static Marker marker_chungcheng, marker_seven11, marker_family1, marker_family2, marker_family3, marker_family4;
    public static Marker marker_AED1, marker_AED2, marker_AED3, marker_AED4, marker_AED5;
    public static Marker marker_phone1, marker_phone2, marker_phone3, marker_phone4, marker_phone5, marker_phone6, marker_phone7, marker_phone8,
            marker_phone9, marker_phone10, marker_phone11, marker_phone12, marker_phone13, marker_phone14, marker_phone15, marker_phone16,
            marker_phone17, marker_phone18, marker_phone19, marker_phone20, marker_phone21, marker_phone22, marker_phone23, marker_phone24,
            marker_phone25, marker_phone26, marker_phone27;
    public static Marker[] markers_chungcheng = new Marker[1], markers_seven11 = new Marker[1], markers_family = new Marker[4], markers_AED = new Marker[5], markers_phone = new Marker[27];
    public static int chungcheng_length, seven11_length, family_length, AED_length, phone_length;
    public static int[] imgId = new int[MarkerFragment.N];


    public static void addChungchengMarker(Bitmap changedBmp, String title) {
        marker_chungcheng = MarkerFragment.mMap.addMarker(new MarkerOptions().position(chungcheng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        Marker[] m = {marker_chungcheng};
        chungcheng_length = m.length;
        int start = 0, end = chungcheng_length;
        for (int i = 0; i < chungcheng_length; i++) markers_chungcheng[i] = m[i];
        for (int i = start; i < end; i++)
            MarkerFragment.markers[i] = markers_chungcheng[i - start];
    }

    public static void addSeven11Marker(Bitmap changedBmp, String title) {
        marker_seven11 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(seven11)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        Marker[] m = {marker_seven11};
        seven11_length = m.length;
        int start = chungcheng_length, end = chungcheng_length + seven11_length;
        for (int i = 0; i < seven11_length; i++) markers_seven11[i] = m[i];
        for (int i = start; i < end; i++)
            MarkerFragment.markers[i] = markers_seven11[i - start];
    }

    public static void addFamilyMarker(Bitmap changedBmp, String title) {
        marker_family1 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(family1)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_family2 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(family2)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_family3 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(family3)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_family4 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(family4)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        Marker[] m = {marker_family1, marker_family2, marker_family3, marker_family4};
        family_length = m.length;
        int start = chungcheng_length + seven11_length, end = chungcheng_length + seven11_length + family_length;
        for (int i = 0; i < family_length; i++) markers_family[i] = m[i];
        for (int i = start; i < end; i++)
            MarkerFragment.markers[i] = markers_family[i - start];
    }

    public static void addAEDMarker(Bitmap changedBmp, String title) {
        marker_AED1 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(AED1)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_AED2 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(AED2)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_AED3 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(AED3)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_AED4 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(AED4)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_AED5 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(AED5)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        Marker[] m = {marker_AED1, marker_AED2, marker_AED3, marker_AED4, marker_AED5};
        AED_length = m.length;
        int start = chungcheng_length + seven11_length + family_length, end = chungcheng_length + seven11_length + family_length + AED_length;
        for (int i = 0; i < AED_length; i++) markers_AED[i] = m[i];
        for (int i = start; i < end; i++)
            MarkerFragment.markers[i] = markers_AED[i - start];
    }

    public static void addPhoneMarker(Bitmap changedBmp, String title) {
        marker_phone1 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone1)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone2 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone2)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone3 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone3)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone4 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone4)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone5 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone5)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone6 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone6)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone7 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone7)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone8 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone8)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone9 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone9)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone10 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone10)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone11 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone11)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone12 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone12)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone13 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone13)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone14 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone14)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone15 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone15)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone16 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone16)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone17 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone17)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone18 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone18)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone19 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone19)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone20 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone20)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone21 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone21)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone22 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone22)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone23 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone23)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone24 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone24)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone25 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone25)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone26 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone26)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        marker_phone27 = MarkerFragment.mMap.addMarker(new MarkerOptions().position(phone27)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(changedBmp)));
        Marker[] m = {marker_phone1, marker_phone2, marker_phone3, marker_phone4, marker_phone5, marker_phone6, marker_phone7, marker_phone8,
                marker_phone9, marker_phone10, marker_phone11, marker_phone12, marker_phone13, marker_phone14, marker_phone15, marker_phone16,
                marker_phone17, marker_phone18, marker_phone19, marker_phone20, marker_phone21, marker_phone22, marker_phone23, marker_phone24,
                marker_phone25, marker_phone26, marker_phone27};
        phone_length = m.length;
        int start = chungcheng_length + seven11_length + family_length + AED_length, end = chungcheng_length + seven11_length + family_length + AED_length + phone_length;
        for (int i = 0; i < phone_length; i++) markers_phone[i] = m[i];
        for (int i = start; i < end; i++)
            MarkerFragment.markers[i] = markers_phone[i - start];
    }

    public static void initPoints() {
        chungcheng = new LatLng(23.55849372417498, 120.47191462085345);
        seven11 = new LatLng(23.558740476218208, 120.46978712081909);
        family1 = new LatLng(23.55616378679361, 120.47182559967041);
        family2 = new LatLng(23.563082378630057, 120.47610640525818);
        family3 = new LatLng(23.560902773399874, 120.47235770718953);
        family4 = new LatLng(23.56132680679699, 120.48179349542261);
        AED1 = new LatLng(23.563065135346143, 120.47454060350378);
        AED2 = new LatLng(23.558224501327025, 120.47178051040271);
        AED3 = new LatLng(23.56568079664421, 120.47293217178117);
        AED4 = new LatLng(23.561854427189026, 120.47489802532277);
        AED5 = new LatLng(23.560123700282304, 120.4690045771506);
        phone1 = new LatLng(23.55996317249688, 120.4746264610767);
        phone2 = new LatLng(23.558711422632005, 120.4729970599899);
        phone3 = new LatLng(23.55931133175367, 120.47319017903897);
        phone4 = new LatLng(23.561924192844454, 120.4803026298348);
        phone5 = new LatLng(23.561765074209536, 120.47770049777228);
        phone6 = new LatLng(23.560683289093888, 120.47684219088751);
        phone7 = new LatLng(23.56172179947385, 120.4758922137571);
        phone8 = new LatLng(23.56215942833882, 120.47521093266732);
        phone9 = new LatLng(23.56360139256621, 120.47792380299165);
        phone10 = new LatLng(23.564515684837033, 120.47687237705782);
        phone11 = new LatLng(23.56310447475838, 120.47582095112398);
        phone12 = new LatLng(23.564854963048464, 120.47679191078737);
        phone13 = new LatLng(23.56247016285103, 120.47413652386263);
        phone14 = new LatLng(23.560177618686314, 120.47235858024794);
        phone15 = new LatLng(23.56053165979449, 120.47344755710799);
        phone16 = new LatLng(23.56472672940449, 120.47442608670633);
        phone17 = new LatLng(23.563954746521638, 120.4732137282316);
        phone18 = new LatLng(23.5611421335708, 120.47163658933084);
        phone19 = new LatLng(23.562100985659605, 120.4704939682905);
        phone20 = new LatLng(23.567039251411888, 120.47303845745614);
        phone21 = new LatLng(23.56505125588618, 120.47153466538828);
        phone22 = new LatLng(23.568394093028214, 120.4713729634883);
        phone23 = new LatLng(23.56767130134505, 120.47051465660354);
        phone24 = new LatLng(23.566088029473843, 120.46942299753448);
        phone25 = new LatLng(23.560994387099136, 120.46896033717042);
        phone26 = new LatLng(23.56374216987877, 120.4685915348523);
        phone27 = new LatLng(23.5640464685904, 120.47009791688606);
    }

    public static void initImg() {
        imgId[0] = R.drawable.img0;
        imgId[1] = R.drawable.img1;
        imgId[2] = R.drawable.img2;
        imgId[3] = R.drawable.img3;
        imgId[4] = R.drawable.img4;
        imgId[5] = R.drawable.img5;
        for (int i = 6; i < 11; i++) {
            imgId[i] = R.drawable.aed;
        }
        for (int i = 11; i < MarkerFragment.N; i++) {
            imgId[i] = R.drawable.phone;
        }
    }

    public static void initBounceMarker() {
        for (int i = 0; i < MarkerFragment.N; i++) {
            MarkerFragment.bounceMarker[i] = true;
        }
    }

    public static void setMarkerBounce(final Marker marker, final int bounceflag) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final long duration = 2000;
        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (MarkerFragment.bounceMarker[bounceflag]) {
                    marker.setAnchor(0.5f, 1);
                    return;
                }
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + t);

                if (t > 0.0) {
                    handler.postDelayed(this, 16);
                } else {
                    setMarkerBounce(marker, bounceflag);
                }
            }
        });
    }

    public static void animateCameraCurrentPosition(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        MarkerFragment.mMap.animateCamera(cameraUpdate);
    }

    public static void markerRemove() {
        chungchengMarkerRemove();
        seven11MarkerRemove();
        familyMarkerRemove();
        aedMarkerRemove();
        phoneMarkerRemove();
    }

    public static void chungchengMarkerRemove() {
        for (int i = 0; i < chungcheng_length; i++) {
            markers_chungcheng[i].remove();
        }
    }

    public static void seven11MarkerRemove() {
        for (int i = 0; i < seven11_length; i++) {
            markers_seven11[i].remove();
        }
    }

    public static void familyMarkerRemove() {
        for (int i = 0; i < family_length; i++) {
            markers_family[i].remove();
        }
    }

    public static void aedMarkerRemove() {
        for (int i = 0; i < AED_length; i++) {
            markers_AED[i].remove();
        }
    }

    public static void phoneMarkerRemove() {
        for (int i = 0; i < phone_length; i++) {
            markers_phone[i].remove();
        }
    }

    public static void animarFab(final FloatingActionButton ffab) {
        ffab.animate().scaleX(0.7f).scaleY(0.7f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                ffab.animate().scaleX(1f).scaleY(1f);
            }
        });
    }
}
