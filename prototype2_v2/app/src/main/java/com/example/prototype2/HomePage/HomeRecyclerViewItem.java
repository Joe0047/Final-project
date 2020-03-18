package com.example.prototype2.HomePage;

public class HomeRecyclerViewItem {

    // Save car name.
    private String carName;

    // Save car image resource id.
    private int carImageId;
    private String carDescribe;
    private String fragmentName;
    public HomeRecyclerViewItem(String carName, int carImageId,String carDescribe,String  fragmentName) {
        this.carName = carName;
        this.carImageId = carImageId;
        this.carDescribe=carDescribe;
        this.fragmentName= fragmentName;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public int getCarImageId() {
        return carImageId;
    }

    public void setCarImageId(int carImageId) {
        this.carImageId = carImageId;
    }
    public String getCarDescribe() {
        return carDescribe;
    }
    public void setCarDescribe(String carDescribe) {
        this.carDescribe = carDescribe;
    }
    public String getFragmentName() {
        return fragmentName;
    }
    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }
}