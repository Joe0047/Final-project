package com.example.prototype2.HomePage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototype2.R;

import java.util.List;


public class HomeRecyclerViewDataAdapter extends RecyclerView.Adapter<HomeRecyclerViewItemHolder> {
    private List<HomeRecyclerViewItem> carItemList;
    private Context context;
    public HomeRecyclerViewDataAdapter(List<HomeRecyclerViewItem> carItemList) {
        this.carItemList = carItemList;
    }

    @Override
    public HomeRecyclerViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        final View carItemView = layoutInflater.inflate(R.layout.activity_card_view_item, parent, false);

        // Get car title text view object.
        final TextView carTitleView = (TextView) carItemView.findViewById(R.id.card_view_image_title);
        // Get car image view object.
        final ImageView carImageView = (ImageView) carItemView.findViewById(R.id.card_view_image);
        // When click the image.
        final TextView carDescribeView = (TextView) carItemView.findViewById(R.id.card_view_image_describe);

        final Button linkButton = (Button) carItemView.findViewById(R.id.button5);

        carImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get car title text.
                String carTitle = carTitleView.getText().toString();
                // Create a snackbar and show it.
//                Snackbar snackbar = Snackbar.make(carImageView, "You click " + carTitle +" image", Snackbar.LENGTH_LONG);
//                carDescribeView.setVisibility(v.GONE);
                carDescribeView.setVisibility(carDescribeView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                linkButton.setVisibility(linkButton.getVisibility() == Button.VISIBLE ? Button.GONE : Button.VISIBLE);
//                snackbar.show();
            }
        });

//        linkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String link = linkButton.getText().toString();
//                switch (link) {
//                    case "前往地圖圍欄":
////                        Fragment mFragment = new GeofenceActivity();
////                        Bundle mBundle = new Bundle();
////                        mFragment.setArguments(mBundle);
////                        switchContent(R.id.content_frame, mFragment);
////                        Snackbar snackbar = Snackbar.make(linkButton, "You click " + link + " image", Snackbar.LENGTH_LONG);
////                        snackbar.show();
////
//                        Intent intent=new Intent(context,GeofenceActivity.class);
//                        context.startActivity(intent);
////                        FragmentManager fragment =((AppCompatActivity)context).getSupportFragmentManager();
////                        FragmentTransaction fragmentTrans = fragment .beginTransaction();
////                        fragmentTrans.add(R.id.content_frame,new GeofenceActivity());
////                        fragmentTrans.remove(new HomePage());
////                        fragmentTrans.commit();
//                }
//
//            }
//        });

        // Create and return our custom Car Recycler View Item Holder object.
        HomeRecyclerViewItemHolder ret = new HomeRecyclerViewItemHolder(carItemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(HomeRecyclerViewItemHolder holder, int position) {
        if (carItemList != null) {
            // Get car item dto in list.
            HomeRecyclerViewItem carItem = carItemList.get(position);

            if (carItem != null) {
                // Set car item title.
                holder.getCarTitleText().setText(carItem.getCarName());
                // Set car image resource id.
                holder.getCarImageView().setImageResource(carItem.getCarImageId());
                holder.getCarDescribeView().setText(carItem.getCarDescribe());
                holder.getFragmentNameTra().setText(carItem.getFragmentName());
//                holder.getFragmentNameTra().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        FragmentManager fragment = (Fragment)carItemList.getSupportFragmentManager();
//                        FragmentTransaction fragmentTrans = fragment .beginTransaction();
//                        fragmentTrans.add(R.id.content_frame,new GeofenceActivity());
//                        fragmentTrans.remove(new HomePage());
//                        fragmentTrans.commit();
//                }
//            });
            }
        }
    }
//    public void switchContent(int id, Fragment fragment) {
//        if (fragment == null)
//            return;
//        if (fragment instanceof HomePage) {
//            HomePage mainActivity = (HomePage) fragment;
//            Fragment frag = new GeofenceActivity();
//            mainActivity.switchContent(id, frag);
//        }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (carItemList != null) {
            ret = carItemList.size();
        }
        return ret;
    }

}