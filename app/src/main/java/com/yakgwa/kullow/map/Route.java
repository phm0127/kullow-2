package com.yakgwa.kullow.map;

import android.location.Location;

import com.yakgwa.kullow.map.Point.Point;

import java.util.ArrayList;

public class Route {
    private ArrayList<Point> pointList;
    private double distanceSrcToDest;

    public Route(Point startPoint){
        this.pointList = new ArrayList<>();
        this.pointList.add(startPoint);
        this.distanceSrcToDest = 0;
    }

    public void addPoint(Point point){
        Point oldPoint = this.pointList.get(this.pointList.size()-1);
        pointList.add(point);

        Location newLocation = new Location("dummeyProvider");
        newLocation.setLatitude(point.getLatitude());
        newLocation.setLongitude(point.getLongitude());

        Location oldLocation = new Location("dummyProvider");
        oldLocation.setLatitude(oldPoint.getLatitude());
        oldLocation.setLongitude(oldPoint.getLongitude());

        this.distanceSrcToDest += oldLocation.distanceTo(newLocation);
//        Log.e("ROUTING", oldPoint.getPointText()+"-"+point.getPointText()+" = "+this.distanceSrcToDest);
    }

    public ArrayList<Point> getPointList(){
        return this.pointList;
    }

    public double getDistanceSrcToDest(){
        return this.distanceSrcToDest;
    }
}
