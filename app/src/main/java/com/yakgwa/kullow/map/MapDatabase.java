package com.yakgwa.kullow.map;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.yakgwa.kullow.map.Path.Path;
import com.yakgwa.kullow.map.Path.PathDao;
import com.yakgwa.kullow.map.Point.Point;
import com.yakgwa.kullow.map.Point.PointDao;


@Database(entities = {Path.class, Point.class}, version = 1)
public abstract class MapDatabase extends RoomDatabase {
    private static volatile MapDatabase INSTANCE;

    public abstract PointDao pointDao();
    public abstract PathDao pathDao();

    public static MapDatabase getINSTANCE(Context context) {
        if(INSTANCE == null){
            synchronized (MapDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MapDatabase.class, "map.db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static MapDatabase getINSTANCE(){
        if(INSTANCE == null){
            return null;
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PointDao mPointDao;
        private final PathDao mPathDao;

        PopulateDbAsync(MapDatabase db) {
            mPointDao = db.pointDao();
            mPathDao = db.pathDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            mPointDao.deleteAll();
            mPathDao.deleteAll();


            Point point = new Point(1, 37.545062, 127.076660, "branch");mPointDao.insert(point);
            point = new Point(2, 37.544294, 127.076734, "branch");mPointDao.insert(point);
            point = new Point(3, 37.543777, 127.076754, "branch");mPointDao.insert(point);
            point = new Point(4, 37.543524, 127.076759, "branch");mPointDao.insert(point);
            point = new Point(5, 37.542812, 127.076786, "branch");mPointDao.insert(point);
            point = new Point(6, 37.542508, 127.076500, "branch");mPointDao.insert(point);
            point = new Point(7, 37.542318, 127.076110, "branch");mPointDao.insert(point);
            point = new Point(8, 37.542588, 127.076013, "branch");mPointDao.insert(point);
            point = new Point(9, 37.542775, 127.075351, "branch");mPointDao.insert(point);
            point = new Point(10, 37.543688, 127.075807, "branch");mPointDao.insert(point);
            point = new Point(11, 37.543716, 127.077451, "branch");mPointDao.insert(point);
            point = new Point(12, 37.543380, 127.077363, "branch");mPointDao.insert(point);
            point = new Point(13, 37.543674, 127.078016, "branch");mPointDao.insert(point);
            point = new Point(14, 37.543336, 127.077969, "branch");mPointDao.insert(point);
            point = new Point(15, 37.542379, 127.077860, "branch");mPointDao.insert(point);
            point = new Point(16, 37.542423, 127.077442, "branch");mPointDao.insert(point);
            point = new Point(17, 37.542461, 127.077109, "branch");mPointDao.insert(point);
            point = new Point(18, 37.542284, 127.078667, "branch");mPointDao.insert(point);
            point = new Point(19, 37.541578, 127.078544, "branch");mPointDao.insert(point);
            point = new Point(20, 37.541593, 127.075771, "branch");mPointDao.insert(point);
            point = new Point(21, 37.543562, 127.077416, "새천년관");mPointDao.insert(point);
            point = new Point(22, 37.541564, 127.078726, "공학관A동");mPointDao.insert(point);
            point = new Point(23, 37.541962, 127.077929, "학생회관");mPointDao.insert(point);
            point = new Point(24, 37.542432, 127.078693, "문과대학");mPointDao.insert(point);
            point = new Point(25, 37.543161, 127.075344, "행정관");mPointDao.insert(point);
            point = new Point(26, 37.542343, 127.075603, "박물관");mPointDao.insert(point);
            point = new Point(27, 37.544277, 127.076266, "경영관");mPointDao.insert(point);
            point = new Point(28, 37.543667, 127.078305, "건축대학");mPointDao.insert(point);
            point = new Point(29, 37.541518, 127.075329, "종합강의동");mPointDao.insert(point);
            point = new Point(30, 37.544081, 127.075408, "상허연구관");mPointDao.insert(point);
            point = new Point(31, 37.543924, 127.074673, "사범대학");mPointDao.insert(point);
            point = new Point(32, 37.543915, 127.074807, "branch");mPointDao.insert(point);
            point = new Point(33, 37.543753, 127.074775, "branch");mPointDao.insert(point);
            point = new Point(34, 37.543668, 127.075366, "branch");mPointDao.insert(point);
            point = new Point(35, 37.543789, 127.076411, "branch");mPointDao.insert(point);
            point = new Point(36, 37.543583, 127.076377, "branch");mPointDao.insert(point);
            point = new Point(37, 37.543081, 127.076152, "황소상");mPointDao.insert(point);
            point = new Point(38, 37.542987, 127.076774, "branch");mPointDao.insert(point);
            point = new Point(39, 37.542825, 127.077353, "새천년공연장");mPointDao.insert(point);
            point = new Point(40, 37.542825, 127.077900, "branch");mPointDao.insert(point);
            point = new Point(41, 37.542837, 127.073247, "예술디자인대학");mPointDao.insert(point);
            point = new Point(42, 37.542697, 127.073535, "branch");mPointDao.insert(point);
            point = new Point(43, 37.542648, 127.073707, "branch");mPointDao.insert(point);
            point = new Point(44, 37.542803, 127.073777, "branch");mPointDao.insert(point);
            point = new Point(45, 37.542869, 127.074190, "branch");mPointDao.insert(point);
            point = new Point(46, 37.542407, 127.074313, "branch");mPointDao.insert(point);
            point = new Point(47, 37.542407, 127.074680, "언어교육원");mPointDao.insert(point);
            point = new Point(48, 37.542227, 127.074644, "branch");mPointDao.insert(point);
            point = new Point(49, 37.542038, 127.075470, "branch");mPointDao.insert(point);
            point = new Point(50, 37.541819, 127.077357, "branch");mPointDao.insert(point);
            point = new Point(51, 37.541519, 127.077304, "branch");mPointDao.insert(point);
            point = new Point(52, 37.541442, 127.078523, "branch");mPointDao.insert(point);
            point = new Point(53, 37.541100, 127.078454, "branch");mPointDao.insert(point);
            point = new Point(54, 37.541040, 127.079677, "공학관C동");mPointDao.insert(point);
            point = new Point(55, 37.540538, 127.079602, "신공학관");mPointDao.insert(point);
            point = new Point(56, 37.542188, 127.079720, "branch");mPointDao.insert(point);
            point = new Point(57, 37.542018, 127.079709, "공학관B동");mPointDao.insert(point);
            point = new Point(58, 37.542675, 127.073005, "branch");mPointDao.insert(point);
            point = new Point(59, 37.542470, 127.073446, "branch");mPointDao.insert(point);
            point = new Point(60, 37.541500, 127.072995, "branch");mPointDao.insert(point);
            point = new Point(61, 37.541751, 127.073663, "상허기념도서관1층");mPointDao.insert(point);
            point = new Point(62, 37.541588, 127.073564, "branch");mPointDao.insert(point);
            point = new Point(63, 37.541473, 127.073972, "branch");mPointDao.insert(point);
            point = new Point(64, 37.541915, 127.074165, "상허기념도서관3층");mPointDao.insert(point);
            point = new Point(65, 37.541326, 127.075392, "branch");mPointDao.insert(point);
            point = new Point(66, 37.540964, 127.075466, "branch");mPointDao.insert(point);

            Path path = new Path(1, 1, 2);mPathDao.insert(path);
            path = new Path(2, 2, 27);mPathDao.insert(path);
            path = new Path(3, 2, 3);mPathDao.insert(path);
            path = new Path(4, 3, 4);mPathDao.insert(path);
            path = new Path(5, 3, 11);mPathDao.insert(path);
            path = new Path(6, 4, 5);mPathDao.insert(path);
            path = new Path(7, 4, 10);mPathDao.insert(path);
            path = new Path(8, 4, 12);mPathDao.insert(path);
            path = new Path(9, 5, 6);mPathDao.insert(path);
            path = new Path(10, 5, 17);mPathDao.insert(path);
            path = new Path(11, 6, 17);mPathDao.insert(path);
            path = new Path(12, 6, 7);mPathDao.insert(path);
            path = new Path(13, 7, 26);mPathDao.insert(path);
            path = new Path(14, 7,  20);mPathDao.insert(path);
            path = new Path(15, 20, 29);mPathDao.insert(path);
            path = new Path(16, 6,  8);mPathDao.insert(path);
            path = new Path(17, 7,  8);mPathDao.insert(path);
            path = new Path(18, 8,  9);mPathDao.insert(path);
            path = new Path(19, 9,  25);mPathDao.insert(path);
            path = new Path(20, 10, 25);mPathDao.insert(path);
            path = new Path(21, 11, 13);mPathDao.insert(path);
            path = new Path(22, 11, 21);mPathDao.insert(path);
            path = new Path(23, 12, 14);mPathDao.insert(path);
            path = new Path(24, 12, 21);mPathDao.insert(path);
            path = new Path(25, 13, 14);mPathDao.insert(path);
            path = new Path(26, 13, 28);mPathDao.insert(path);
            path = new Path(27, 14, 15);mPathDao.insert(path);
            path = new Path(28, 15, 16);mPathDao.insert(path);
            path = new Path(29, 16, 17);mPathDao.insert(path);
            path = new Path(30, 15, 18);mPathDao.insert(path);
            path = new Path(31, 16, 23);mPathDao.insert(path);
            path = new Path(32, 18, 24);mPathDao.insert(path);
            path = new Path(33, 18, 19);mPathDao.insert(path);
            path = new Path(34, 19, 22);mPathDao.insert(path);
            path = new Path(35, 27, 35);mPathDao.insert(path);
            path = new Path(36, 35, 36);mPathDao.insert(path);
            path = new Path(37, 35, 3);mPathDao.insert(path);
            path = new Path(38, 30, 35);mPathDao.insert(path);
            path = new Path(39, 30, 34);mPathDao.insert(path);
            path = new Path(40, 34, 10);mPathDao.insert(path);
            path = new Path(41, 36, 37);mPathDao.insert(path);
            path = new Path(42, 37, 38);mPathDao.insert(path);
            path = new Path(43, 37, 8);mPathDao.insert(path);
            path = new Path(44, 12, 38);mPathDao.insert(path);
            path = new Path(45, 38, 39);mPathDao.insert(path);
            path = new Path(46, 39, 40);mPathDao.insert(path);
            path = new Path(47, 12, 40);mPathDao.insert(path);
            path = new Path(48, 39, 15);mPathDao.insert(path);
            path = new Path(49, 34, 33);mPathDao.insert(path);
            path = new Path(50, 33, 32);mPathDao.insert(path);
            path = new Path(51, 32, 31);mPathDao.insert(path);
            path = new Path(52, 9, 45);mPathDao.insert(path);
            path = new Path(53, 44, 45);mPathDao.insert(path);
            path = new Path(54, 45, 46);mPathDao.insert(path);
            path = new Path(55, 46, 48);mPathDao.insert(path);
            path = new Path(56, 47, 48);mPathDao.insert(path);
            path = new Path(57, 48, 49);mPathDao.insert(path);
            path = new Path(58, 49, 7);mPathDao.insert(path);
            path = new Path(59, 43, 44);mPathDao.insert(path);
            path = new Path(60, 43, 42);mPathDao.insert(path);
            path = new Path(61, 41, 42);mPathDao.insert(path);
            path = new Path(62, 42, 59);mPathDao.insert(path);
            path = new Path(63, 58, 59);mPathDao.insert(path);
            path = new Path(64, 59, 60);mPathDao.insert(path);
            path = new Path(65, 59, 48);mPathDao.insert(path);
            path = new Path(66, 48, 64);mPathDao.insert(path);
            path = new Path(67, 63, 64);mPathDao.insert(path);
            path = new Path(68, 61, 62);mPathDao.insert(path);
            path = new Path(69, 62, 63);mPathDao.insert(path);
            path = new Path(70, 29, 20);mPathDao.insert(path);
            path = new Path(71, 29, 65);mPathDao.insert(path);
            path = new Path(72, 65, 66);mPathDao.insert(path);
            path = new Path(73, 20, 66);mPathDao.insert(path);
            path = new Path(74, 16, 50);mPathDao.insert(path);
            path = new Path(75, 50, 51);mPathDao.insert(path);
            path = new Path(76, 51, 52);mPathDao.insert(path);
            path = new Path(77, 19, 52);mPathDao.insert(path);
            path = new Path(78, 52, 53);mPathDao.insert(path);
            path = new Path(79, 53, 54);mPathDao.insert(path);
            path = new Path(80, 18, 56);mPathDao.insert(path);
            path = new Path(81, 56, 57);mPathDao.insert(path);


            return null;
        }
    }
}
