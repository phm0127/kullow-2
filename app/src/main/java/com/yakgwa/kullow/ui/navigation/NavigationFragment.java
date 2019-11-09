package com.yakgwa.kullow.ui.navigation;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;
import com.yakgwa.kullow.R;
import com.yakgwa.kullow.map.Point.DestPointSpinnerAdapter;
import com.yakgwa.kullow.map.Point.Point;
import com.yakgwa.kullow.map.Point.SrcPointSpinnerAdapter;
import com.yakgwa.kullow.map.Router;
import com.yakgwa.kullow.map.viewmodel.MapViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * kullow, Routing Fragment
 * Naver Map(Full style), Spinner(Src, Dest)
 */
public class NavigationFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    // Naver Map
    private FusedLocationSource locationSource;
    private NaverMap mNaverMap;

    // Map ViewModel
    private MapViewModel mMapViewModel;

    // Routing
    private Spinner srcSpinner;
    private Spinner destSpinner;
    private Marker srcMarker;
    private Marker destMarker;
    private long srcPointId;
    private long destPointId;

    // Spinner Initializer
    private boolean srcSpinnerInit;
    private boolean destSpinnerInit;

    // Routing path result
    private PathOverlay path;

    // Handler - for Threading
    private Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_navigation_func, container, false);

        // Main Thread Handler init
        handler = new Handler();

        // Naver Map Fragment init
        FragmentManager fm = getParentFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.navermaps);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.navermaps, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // Routing Marker
        srcMarker = new Marker();
        srcMarker.setIcon(MarkerIcons.BLUE);
        destMarker = new Marker();
        destMarker.setIcon(MarkerIcons.RED);

        // Route Path OverLay
        path = new PathOverlay();

        // Spinner Init
        srcPointId = 0;
        destPointId = 0;
        srcSpinnerInit = true;
        destSpinnerInit = true;


        /**
         * DataBase에서 데이터 가져오기!
         */
        mMapViewModel = new ViewModelProvider(this).get(MapViewModel.class);


        /**
         * Routing : Src Point Spinner & Dest Point Spinner
         */
        srcSpinner = root.findViewById(R.id.srcSpinner);
        destSpinner = root.findViewById(R.id.destSpinner);
        final SrcPointSpinnerAdapter srcPointSpinnerAdapter = new SrcPointSpinnerAdapter(getActivity());
        final DestPointSpinnerAdapter destPointSpinnerAdapter = new DestPointSpinnerAdapter(getActivity());
        srcSpinner.setAdapter(srcPointSpinnerAdapter);
        destSpinner.setAdapter(destPointSpinnerAdapter);
        srcSpinner.setSelected(false);
        destSpinner.setSelected(false);

        /**
         * Selected Listener
         */
        srcSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                srcSpinnerInit = false;
                return false;
            }
        });
        srcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                if(srcSpinnerInit){
                    return;
                }
                if(mNaverMap != null){
                    srcPointId = id;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            double latitude = mMapViewModel.getLatitudeByPointId(id);
                            double longitude = mMapViewModel.getLongitudeByPointId(id);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    LatLng latLng = new LatLng(latitude, longitude);
                                    srcMarker.setPosition(latLng);
                                    srcMarker.setMap(mNaverMap);
                                    Toast.makeText(getActivity().getApplicationContext(), (latLng.latitude + ", " + latLng.longitude), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).start();

                    // 선택된 2개의 건물, 위치가 다를 경우 길찾기 기능 수행
                    if((srcPointId != 0) && (destPointId != 0) &&(srcPointId != destPointId)){
                        /**
                         * Routing Thread Run
                         */
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("SLCT ITEM : ", (srcPointId)+"->"+(destPointId));
                                ArrayList<Point> pointListOnRoute;
                                Router router = new Router(mMapViewModel.getAllPoinst().getValue(), mMapViewModel.getAllPaths().getValue());
                                Point srcPoint = mMapViewModel.getPointById(srcPointId);
                                Point destPoint = mMapViewModel.getPointById(destPointId);
                                Log.e("RSRC POINT : ", srcPoint.getPointText());
                                Log.e("DEST POINT : ", destPoint.getPointText());

                                try{
                                    pointListOnRoute = router.getRoute(srcPoint, destPoint).getPointList();
                                    List<LatLng> drawPaths = new ArrayList<>();
                                    Log.e("@TEST@", String.valueOf(pointListOnRoute.size()));

                                    for(int i = 0; i < pointListOnRoute.size(); i++){
                                        drawPaths.add(new LatLng(pointListOnRoute.get(i).getLatitude(), pointListOnRoute.get(i).getLongitude()));
                                    }

                                    /**
                                     * Drawing Paths of Route
                                     */
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                path.setMap(null);
                                                path.setCoords(drawPaths);
                                                path.setMap(mNaverMap);
                                            } catch (IllegalArgumentException e){
                                                Log.e("FAIL_ROUTING", drawPaths.size()+"");
                                                e.printStackTrace();
                                            }


                                        }
                                    });
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        path.setMap(null);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        destSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                destSpinnerInit = false;
                return false;
            }
        });
        destSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                if(destSpinnerInit){
                    return;
                }
                if(mNaverMap != null){
                    destPointId = id;

                    // Marker UI Drawing
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            double latitude = mMapViewModel.getLatitudeByPointId(id);
                            double longitude = mMapViewModel.getLongitudeByPointId(id);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    LatLng latLng = new LatLng(latitude, longitude);
                                    destMarker.setPosition(latLng);
                                    destMarker.setMap(mNaverMap);
                                    Toast.makeText(getActivity().getApplicationContext(), (latLng.latitude + ", " + latLng.longitude), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).start();

                    // 선택된 2개의 건물, 위치가 다를 경우 길찾기 기능 수행
                    if((srcPointId != 0) && (destPointId != 0) &&(srcPointId != destPointId)){
                        /**
                         * Routing Thread Run
                         */
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("SLCT ITEM : ", (srcPointId)+"->"+(destPointId));
                                ArrayList<Point> pointListOnRoute;
                                Router router = new Router(mMapViewModel.getAllPoinst().getValue(), mMapViewModel.getAllPaths().getValue());
                                Point srcPoint = mMapViewModel.getPointById(srcPointId);
                                Point destPoint = mMapViewModel.getPointById(destPointId);
                                Log.e("RSRC POINT : ", srcPoint.getPointText());
                                Log.e("DEST POINT : ", destPoint.getPointText());

                                try{
                                    pointListOnRoute = router.getRoute(srcPoint, destPoint).getPointList();
                                    List<LatLng> drawPaths = new ArrayList<>();
                                    Log.e("@TEST@", String.valueOf(pointListOnRoute.size()));

                                    for(int i = 0; i < pointListOnRoute.size(); i++){
                                        drawPaths.add(new LatLng(pointListOnRoute.get(i).getLatitude(), pointListOnRoute.get(i).getLongitude()));
                                    }

                                    /**
                                     * Drawing Paths of Route
                                     */
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                path.setMap(null);
                                                path.setCoords(drawPaths);
                                                path.setMap(mNaverMap);
                                            } catch (IllegalArgumentException e){
                                                Log.e("FAIL_ROUTING", drawPaths.size()+"");
                                                e.printStackTrace();
                                            }


                                        }
                                    });
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        path.setMap(null);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mMapViewModel.getAllNode().observe(this, points -> {
            srcPointSpinnerAdapter.setPoints(points);
            destPointSpinnerAdapter.setPoints(points);
        });

        // Cannot Understand
        // if remove it, cause exception "Attempt to invoke interface method 'int java.util.List.size()' on a null object reference"
        mMapViewModel.getAllPoinst().observe(this, points -> {
            if(points == null || points.size() == 0){

            } else {

            }
        });
        mMapViewModel.getAllPaths().observe(this, paths -> {
            if(paths == null || paths.size() == 0){

            } else {

            }
        });


        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mNaverMap = naverMap;
        mNaverMap.setLocationSource(locationSource);
        mNaverMap.getUiSettings().setLocationButtonEnabled(true);
        mNaverMap.setLocationTrackingMode(LocationTrackingMode.Face);
    }
}
