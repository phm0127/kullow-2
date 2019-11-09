package com.yakgwa.kullow.map.Point;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.yakgwa.kullow.map.MapDatabase;

import java.util.List;

public class PointRepository {
    private PointDao pointDao;
    private LiveData<List<Point>> mAllPoints;
    private LiveData<List<Point>> mAllNodes;

    public PointRepository(Application application){
        MapDatabase db = MapDatabase.getINSTANCE(application);

        pointDao = db.pointDao();

        mAllPoints = pointDao.getAllPoint();
        mAllNodes = pointDao.getAllNode();
    }

    public LiveData<List<Point>> getmAllPoints(){
        return mAllPoints;
    }
    public LiveData<List<Point>> getAllNodes() {return mAllNodes; }

    public void insert(Point point){
        new insertAsyncTask(pointDao).execute(point);
    }
    /**
     * https://codelabs.developers.google.com/codelabs/android-room-with-a-view/index.html?index=..%2F..index#7
     * AsyncTask
     */
    private static class insertAsyncTask extends AsyncTask<Point, Void, Void> {

        private PointDao mAsyncTaskDao;

        insertAsyncTask(PointDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Point... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public double getLatitudeByPointId(long pid){
        return pointDao.getLatitudeByPointId(pid);
    }
    public double getLongitudeByPointId(long pid){
        return pointDao.getLongitudeByPointId(pid);
    }
    public Point getPointById(long pid){
        return pointDao.getPointById(pid);
    }
}
