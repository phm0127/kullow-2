package com.yakgwa.kullow.map.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yakgwa.kullow.map.Path.Path;
import com.yakgwa.kullow.map.Path.PathRepository;
import com.yakgwa.kullow.map.Point.Point;
import com.yakgwa.kullow.map.Point.PointRepository;

import java.util.List;

public class MapViewModel extends AndroidViewModel {
    private PointRepository mPointRepository;
    private LiveData<List<Point>> mAllPoints;
    private LiveData<List<Point>> mAllNodes;

    private PathRepository mPathRepository;
    private LiveData<List<Path>> mAllPaths;

    public MapViewModel(@NonNull Application application) {
        super(application);

        mPointRepository = new PointRepository(application);
        mAllPoints = mPointRepository.getmAllPoints();
        mAllNodes = mPointRepository.getAllNodes();

        mPathRepository = new PathRepository(application);
        mAllPaths = mPathRepository.getmAllPaths();

    }

    public LiveData<List<Point>> getAllPoinst(){
        return mAllPoints;
    }
    public LiveData<List<Path>> getAllPaths(){
        return mAllPaths;
    }
    public LiveData<List<Point>> getAllNode() { return mAllNodes; }

    public void insertPoint(Point point) { mPointRepository.insert(point); }
    public void insertPath(Path path) { mPathRepository.insert(path); }

    public double getLatitudeByPointId(long pid){
        return mPointRepository.getLatitudeByPointId(pid);
    }
    public double getLongitudeByPointId(long pid){
        return mPointRepository.getLongitudeByPointId(pid);
    }

    public Point getPointById(long pid){
        return mPointRepository.getPointById(pid);
    }
}
