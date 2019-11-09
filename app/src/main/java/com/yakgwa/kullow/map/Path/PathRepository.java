package com.yakgwa.kullow.map.Path;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.yakgwa.kullow.map.MapDatabase;

import java.util.List;

public class PathRepository {
    private PathDao pathDao;

    private LiveData<List<Path>> mAllPaths;

    public PathRepository(Application application){
        MapDatabase db = MapDatabase.getINSTANCE(application);

        pathDao = db.pathDao();

        mAllPaths = pathDao.getAllPath();
    }

    public LiveData<List<Path>> getmAllPaths(){
        return mAllPaths;
    }

    public void insert (Path path) {
        new insertAsyncTask(pathDao).execute(path);
    }
    /**
     * https://codelabs.developers.google.com/codelabs/android-room-with-a-view/index.html?index=..%2F..index#7
     * AsyncTask
     */
    private static class insertAsyncTask extends AsyncTask<Path, Void, Void> {

        private PathDao mAsyncTaskDao;

        insertAsyncTask(PathDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Path... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public LiveData<List<Long>> getPointIdBySrcPointId(long pid){
        return pathDao.getPointIdBySrcPointId(pid);
    }
    public LiveData<List<Long>> getPointIdByDestPointId(long pid){
        return pathDao.getPointIdByDestPointId(pid);
    }
}
