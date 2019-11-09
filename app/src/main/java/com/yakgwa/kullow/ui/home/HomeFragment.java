/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yakgwa.kullow.ui.home;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.ar.core.Anchor;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.PointCloud;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;
import com.yakgwa.kullow.R;
import com.yakgwa.kullow.ar.DisplayRotationHelper;
import com.yakgwa.kullow.ar.rendering.BackgroundRenderer;
import com.yakgwa.kullow.ar.rendering.ObjectRenderer;
import com.yakgwa.kullow.ar.rendering.PlaneRenderer;
import com.yakgwa.kullow.ar.rendering.PointCloudRenderer;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import uk.co.appoly.arcorelocation.LocationMarker;
import uk.co.appoly.arcorelocation.LocationScene;
import uk.co.appoly.arcorelocation.rendering.AnnotationRenderer;
import uk.co.appoly.arcorelocation.rendering.ImageRenderer;
import uk.co.appoly.arcorelocation.utils.ARLocationPermissionHelper;

/**
 * AR Location Sample + Naver Mini Map
 */
public class HomeFragment extends Fragment implements GLSurfaceView.Renderer, OnMapReadyCallback {
    private static final String TAG = HomeFragment.class.getSimpleName();

    // Rendering. The Renderers are created here, and initialized when the GL surface is created.
    private GLSurfaceView mSurfaceView;

    private Session mSession;
    private DisplayRotationHelper mDisplayRotationHelper;

    private final BackgroundRenderer mBackgroundRenderer = new BackgroundRenderer();
    private final PlaneRenderer mPlaneRenderer = new PlaneRenderer();
    private final PointCloudRenderer mPointCloud = new PointCloudRenderer();

    // Temporary matrix allocated here to reduce number of allocations for each frame.
    private final float[] mAnchorMatrix = new float[16];

    private final ArrayList<Anchor> mAnchors = new ArrayList<>();

    private LocationScene locationScene;

    // Naver Maps
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_func, container, false);
        FragmentManager fm = getParentFragmentManager();

        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.navermaps);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.navermaps, mapFragment).commit();
        }

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(Bundle mState){
        super.onActivityCreated(mState);

        mSurfaceView = getView().findViewById(R.id.surfaceview);
        mDisplayRotationHelper = new DisplayRotationHelper(/*context=*/ getContext());

        // Set up renderer.
        mSurfaceView.setPreserveEGLContextOnPause(true);
        mSurfaceView.setEGLContextClientVersion(2);
        mSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        mSurfaceView.setRenderer(this);
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        Exception exception = null;
        String message = null;
        try {
            mSession = new Session(/* context= */ getContext());
        } catch (UnavailableArcoreNotInstalledException e) {
            message = "Please install ARCore";
            exception = e;
        } catch (UnavailableApkTooOldException e) {
            message = "Please update ARCore";
            exception = e;
        } catch (UnavailableSdkTooOldException e) {
            message = "Please update this app";
            exception = e;
        } catch (Exception e) {
            message = "This device does not support AR";
            exception = e;
        }


        if (message != null) {
//            showSnackbarMessage(message, true);
            Log.e(TAG, "Exception creating session", exception);
            return ;
        }

        // Create default config and check if supported.
        Config config = new Config(mSession);
        if (!mSession.isSupported(config)) {
//            showSnackbarMessage("This device does not support AR", true);
        }
        mSession.configure(config);


        // Set up our location scene
        locationScene = new LocationScene(getContext(), getActivity(), mSession);

////내가 입력한 값
        // Image marker at 출발지방향(지나온 방향)
        LocationMarker eiffelTower =  new LocationMarker(
                127.077676, 37.535743,
                new ImageRenderer("eiffel.png")
        );
        eiffelTower.setOnTouchListener(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),
                        "Touched Eiffel Tower", Toast.LENGTH_SHORT).show();
            }
        });
        eiffelTower.setTouchableSize(1000);
        locationScene.mLocationMarkers.add(
                eiffelTower
        );

        // Annotation at 목적지방향(가야 할 방향)
        locationScene.mLocationMarkers.add(
                new LocationMarker(
                        127.077445,37.543548,
                        new AnnotationRenderer("새천년")));

        //특정 위치에 object 띄워놓는 용도
        // Example of using your own renderer.
        // Uses a slightly modified version of hello_ar_java's ObjectRenderer
        locationScene.mLocationMarkers.add(
                new LocationMarker(
                        127.077445,37.543548,
                        new ObjectRenderer("andy.obj", "andy.png")));
    }

    @Override
    public void onResume() {
        super.onResume();

        // ARCore requires camera permissions to operate. If we did not yet obtain runtime
        // permission on Android M and above, now is a good time to ask the user for it.
        if (ARLocationPermissionHelper.hasPermission(getActivity())) {
            if(locationScene != null)
                locationScene.resume();
            if (mSession != null) {
//                showLoadingMessage();
                // Note that order matters - see the note in onPause(), the reverse applies here.
                try {
                    mSession.resume();
                } catch (CameraNotAvailableException e) {
                    e.printStackTrace();
                }
            }
            mSurfaceView.onResume();
            mDisplayRotationHelper.onResume();
        } else {
            ARLocationPermissionHelper.requestPermission(getActivity());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(locationScene != null)
            locationScene.pause();
        // Note that the order matters - GLSurfaceView is paused first so that it does not try
        // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
        // still call mSession.update() and get a SessionPausedException.
        mDisplayRotationHelper.onPause();
        mSurfaceView.onPause();
        if (mSession != null) {
            mSession.pause();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (!ARLocationPermissionHelper.hasPermission(getActivity())) {
            Toast.makeText(getContext(),
                "Camera permission is needed to run this application", Toast.LENGTH_LONG).show();
            if (!ARLocationPermissionHelper.shouldShowRequestPermissionRationale(getActivity())) {
                // Permission denied with checking "Do not ask again".
                ARLocationPermissionHelper.launchPermissionSettings(getActivity());
            }
            getActivity().finish();
        }

        // Naver Map, Location Permission
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, results)) {
            return;
        }

        super.onRequestPermissionsResult(
                requestCode, permissions, results);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        // Create the texture and pass it to ARCore session to be filled during update().
        mBackgroundRenderer.createOnGlThread(/*context=*/ getContext());
        if (mSession != null) {
            mSession.setCameraTextureName(mBackgroundRenderer.getTextureId());
        }

        try {
            mPlaneRenderer.createOnGlThread(/*context=*/getContext(), "trigrid.png");
        } catch (IOException e) {
            Log.e(TAG, "Failed to read plane texture");
        }
        mPointCloud.createOnGlThread(/*context=*/getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mDisplayRotationHelper.onSurfaceChanged(width, height);
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear screen to notify driver it should not load any pixels from previous frame.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (mSession == null) {
            return;
        }
        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.
        mDisplayRotationHelper.updateSessionIfNeeded(mSession);

        try {
            // Obtain the current frame from ARSession. When the configuration is set to
            // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
            // camera framerate.
            Frame frame = mSession.update();
            Camera camera = frame.getCamera();

            // Draw background.
            mBackgroundRenderer.draw(frame);

            // Draw location markers
            locationScene.draw(frame);

            // If not tracking, don't draw 3d objects.
            if (camera.getTrackingState() == TrackingState.PAUSED) {
                return;
            }

            // Get projection matrix.
            float[] projmtx = new float[16];
            camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f);

            // Get camera matrix and draw.
            float[] viewmtx = new float[16];
            camera.getViewMatrix(viewmtx, 0);

            // Compute lighting from average intensity of the image.
            final float lightIntensity = frame.getLightEstimate().getPixelIntensity();

            // Visualize tracked points.
            PointCloud pointCloud = frame.acquirePointCloud();
            mPointCloud.update(pointCloud);
            mPointCloud.draw(viewmtx, projmtx);

            // Application is responsible for releasing the point cloud resources after
            // using it.
            pointCloud.release();

            // Visualize planes.
            mPlaneRenderer.drawPlanes(
                mSession.getAllTrackables(Plane.class), camera.getDisplayOrientedPose(), projmtx);

            // Visualize anchors created by touch.
            float scaleFactor = 1.0f;
            for (Anchor anchor : mAnchors) {
                if (anchor.getTrackingState() != TrackingState.TRACKING) {
                    continue;
                }
                // Get the current pose of an Anchor in world space. The Anchor pose is updated
                // during calls to session.update() as ARCore refines its estimate of the world.
                anchor.getPose().toMatrix(mAnchorMatrix, 0);

            }



        } catch (Throwable t) {
            // Avoid crashing the application due to unhandled exceptions.
            Log.e(TAG, "Exception on the OpenGL thread", t);
        }
    }
    //for navermaponcamera
    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        naverMap.setLocationSource(locationSource);
        naverMap.getUiSettings().setLocationButtonEnabled(false);
        naverMap.getUiSettings().setZoomControlEnabled(false);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Face);
    }
}
