package com.example.prototype2.HomePage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototype2.R;


/**
 * Created by Jerry on 3/17/2018.
 */

public class HomeRecyclerViewItemHolder extends RecyclerView.ViewHolder {

    private TextView carTitleText = null;

    private ImageView carImageView = null;
     private TextView carDescribeView=null;
    private Button fragmentNameTra=null;

    public HomeRecyclerViewItemHolder(View itemView) {
        super(itemView);

        if(itemView != null)
        {
            carTitleText = (TextView)itemView.findViewById(R.id.card_view_image_title);

            carImageView = (ImageView)itemView.findViewById(R.id.card_view_image);

            carDescribeView=(TextView)itemView.findViewById(R.id.card_view_image_describe);

            fragmentNameTra=(Button) itemView.findViewById(R.id.button5);
        }
    }

    public TextView getCarTitleText() {
        return carTitleText;
    }

    public ImageView getCarImageView() {
        return carImageView;
    }
    public TextView getCarDescribeView() {
        return carDescribeView;
    }
    public Button getFragmentNameTra(){return fragmentNameTra;}
}