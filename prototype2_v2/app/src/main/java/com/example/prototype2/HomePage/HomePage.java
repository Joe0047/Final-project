package com.example.prototype2.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prototype2.Marker.MovableFloatingActionButton;
import com.example.prototype2.R;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends Fragment {
    private View aView;
private RecyclerView carRecyclerView;
private GridLayoutManager gridLayoutManager;
private HomeRecyclerViewDataAdapter carDataAdapter;
private List<HomeRecyclerViewItem> carItemList = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        aView=inflater.inflate(R.layout.activity_card_view, container, false);
            // Create the recyclerview.
        initializeCarItemList();
        carRecyclerView = (RecyclerView)aView.findViewById(R.id.card_view_recycler_list);
            // Create the grid layout manager with 2 columns.
        gridLayoutManager = new GridLayoutManager(getActivity(),1);
            // Set layout manager.
            carRecyclerView.setLayoutManager(gridLayoutManager);
            // Create car recycler view data adapter with car item list.
            carDataAdapter = new HomeRecyclerViewDataAdapter(carItemList);
            // Set data adapter.
            carRecyclerView.setAdapter(carDataAdapter);
        return aView;
        }
    private void initializeCarItemList()
    {
        if(carItemList == null)
        {
            carItemList = new ArrayList<HomeRecyclerViewItem>();
            carItemList.add(new HomeRecyclerViewItem("地圖圍欄", R.drawable.brochure6,"此功能在出發活動前，設定好所要到達的目的地，預計活動時間，當有異常行為系統會自動發報","前往地圖圍欄"));
//            carItemList.add(new HomeRecyclerViewItem("危險程度分析", R.drawable.dangers,"(此功能未上線)蒐集校園內所發生過的意外事件，列出危險熱區警示","前往危險程度分析"));
            carItemList.add(new HomeRecyclerViewItem("救援地圖標記", R.drawable.marker,"標記學校附近的求救點，也可在此功能內直接撥打求救電話","前往救援地圖標記"));
            carItemList.add(new HomeRecyclerViewItem("信賴聯絡人", R.drawable.truster,"分享您的位置訊息給許可的聯絡人，只有在偵測到異常時才會向您的聯絡人公開您的所在位置，並持續更新","前往信賴聯絡人"));
//            carItemList.add(new HomeRecyclerViewItem("語音偵測", R.drawable.voice,"(此功能未上線)利用您的麥克風在背景偵測異常","前往語音偵測"));
        }
    }
//    public void switchContent(int id, Fragment fragment) {
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        ft.replace(id, fragment, fragment.toString());
//        ft.addToBackStack(null);
//        ft.commit();
//    }

}