package com.yakgwa.kullow.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.util.FusedLocationSource;

public class MapData implements Parcelable {

    NaverMap mNaverMap;
    private FusedLocationSource locationSource;

    public MapData(){
    }
    public MapData(Parcel in){
        readFromPacel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(mNaverMap);
        parcel.writeValue(locationSource);
    }

    private void readFromPacel(Parcel in){
        mNaverMap = (NaverMap) in.readValue(NaverMap.class.getClassLoader());
        locationSource = (FusedLocationSource) in.readValue(FusedLocationSource.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MapData createFromParcel(Parcel in) {
            return new MapData(in);
        }

        public MapData[] newArray(int size) {
            return new MapData[size];
        }
    };

    public NaverMap getmNaverMap() {
        return mNaverMap;
    }

    public FusedLocationSource getLocationSource() {
        return locationSource;
    }

    public void setmNaverMap(NaverMap mNaverMap) {
        this.mNaverMap = mNaverMap;
    }

    public void setLocationSource(FusedLocationSource locationSource) {
        this.locationSource = locationSource;
    }
}
