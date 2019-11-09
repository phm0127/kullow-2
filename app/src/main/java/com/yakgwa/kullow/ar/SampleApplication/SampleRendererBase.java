/*===============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2015 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/

package com.yakgwa.kullow.ar.SampleApplication;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.vuforia.Vuforia;
import com.yakgwa.kullow.ar.SampleApplication.utils.Texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SampleRendererBase implements GLSurfaceView.Renderer
{
    //Using the Accelometer & Gyroscoper
    private SensorManager mSensorManager = null;

    //Using the Gyroscope
    private SensorEventListener mGyroLis;
    private Sensor mGgyroSensor = null;

    //Roll and Pitch
    private double pitch;
    private double roll;
    private double yaw;

    //timestamp and dt
    private double timestamp;
    private double dt;

    // for radian -> dgree
    private double RAD2DGR = 180 / Math.PI;
    private static final float NS2S = 1.0f/1000000000.0f;
    /////////////////////////////////////////////////////////////
    float gpsX = 34.541564f;
    float gpsY = 27.078726f;
    float calX = 0.0f;
    float calY= 0.0f;
    float angle = 0.0f;
    ////////////////////////////////////////////////////////////


    private static final String LOGTAG = "SampleRendererBase";

    protected SampleAppRenderer mSampleAppRenderer;
    protected SampleApplicationSession vuforiaAppSession;
    protected Vector<Texture> mTextures;

    //////////////////////////////////////////////////////////////////
    private static final String TAG = "MyGLRenderer";
    private Tetrahedron mTet;
    private float height, width;
    public float xTouch, yTouch;
    Random rand = new Random();

    private final float[] mMVPMatrix = new float[16]; //model view and projection matrix
    private final float[] mProjMatrix = new float[16]; //projection matrix
    private final float[] mVMatrix = new float[16]; //view matrix
    private final float[] mRotationMatrix = new float[16]; //rotation matrix
    private float[] drawColor = { rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1f };
    private float[] mModelMatrix = new float[16];

    @SuppressLint("NewApi")
//    SampleRendererBase(Context context)
//    {
//
//        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//
//        //Using the Accelometer
//        mGgyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        mGyroLis = new GyroscopeListener();
//        mSensorManager.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_UI);
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//
//        //used for correct drawing and touch
//        this.height = size.y;
//        this.width = size.x;
//        this.xTouch = this.yTouch = 0;
//    }
//    public SampleRendererBase()
//    {
//
//    }








    ///////////////////////////////////////////////////////////////////
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        Log.d(LOGTAG, "GLRenderer.onSurfaceCreated");

        // Call Vuforia function to (re)initialize rendering after first use
        // or after OpenGL ES context was lost (e.g. after onPause/onResume):
        vuforiaAppSession.onSurfaceCreated();

        mSampleAppRenderer.onSurfaceCreated();


        /////////////////////////////////////////////////////////////////////
        //GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, Vuforia.requiresAlpha() ? 0.0f
                : 1.0f);

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        //eye positions
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -3f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -1.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        Matrix.setLookAtM(mVMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);


    }

    
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        Log.d(LOGTAG, "GLRenderer.onSurfaceChanged");

        // Call Vuforia function to handle render surface size changes:
        vuforiaAppSession.onSurfaceChanged(width, height);

        // RenderingPrimitives to be updated when some rendering change is done
        onConfigurationChanged();



        ///////////////////////////////////////////////////////////////////////
        height=10;
        width=10;
        GLES20.glViewport(0, 0, width, height);

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }


    @Override
    public void onDrawFrame(GL10 gl)
    {
        ////////

        ////////
        ////////

        // Call our function to render content from SampleAppRenderer class
        mSampleAppRenderer.render();

        //////////////////////////////////////////////////////////////////
        // Draw background color
        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, Vuforia.requiresAlpha() ? 0.0f
                : 1.0f);

        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0.0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        long time = SystemClock.uptimeMillis() % 10000L;
        //float angleValue = angle/(360.0f / 10000.0f);
        //float angleInDegrees = (360.0f / 10000.0f) * (angleValue);
        float angleInDegrees = (360.0f / 10000.0f) * ((int)time);

        mTet = new Tetrahedron();

        // Draw the triangle facing straight on.
        Matrix.setIdentityM(mRotationMatrix, 0);
        //Matrix.translateM(mRotationMatrix, 0, angleInDegrees, 0.0f, 0);
        Matrix.rotateM(mRotationMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);


        mTet.draw(mMVPMatrix);
    }

    public static int loadShader(int type, String shaderCode)
    {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static void checkGlError(String glOperation)
    {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }



    public void onConfigurationChanged()
    {
        mSampleAppRenderer.onConfigurationChanged();
    }

/*
       자이로 센서
 */
//    private class GyroscopeListener implements SensorEventListener {
//
//        @Override
//        public void onSensorChanged(SensorEvent event) {
//
//            /* 각 축의 각속도 성분을 받는다. */
//            double gyroX = event.values[0];
//            double gyroY = event.values[1];
//            double gyroZ = event.values[2];
//
//            /* 각속도를 적분하여 회전각을 추출하기 위해 적분 간격(dt)을 구한다.
//             * dt : 센서가 현재 상태를 감지하는 시간 간격
//             * NS2S : nano second -> second */
//            dt = (event.timestamp - timestamp) * NS2S;
//            timestamp = event.timestamp;
//
//            /* 맨 센서 인식을 활성화 하여 처음 timestamp가 0일때는 dt값이 올바르지 않으므로 넘어간다. */
//            if (dt - timestamp*NS2S != 0) {
//
//                /* 각속도 성분을 적분 -> 회전각(pitch, roll)으로 변환.
//                 * 여기까지의 pitch, roll의 단위는 '라디안'이다.
//                 * SO 아래 로그 출력부분에서 멤버변수 'RAD2DGR'를 곱해주어 degree로 변환해줌.  */
//                pitch = pitch + gyroY*dt;
//                roll = roll + gyroX*dt;
//                yaw = yaw + gyroZ*dt;
//                calY=gpsY - event.values[1];
//                calX=gpsX - event.values[0];
//                angle = (calX+calY)*(float)RAD2DGR;
//                Log.e("LOG", "GYROSCOPE           [X]:" + String.format("%.4f", event.values[0])
//                        + "           [Y]:" + String.format("%.4f", event.values[1])
//                        + "           [Z]:" + String.format("%.4f", event.values[2])
//                        + "           [Pitch]: " + String.format("%.1f", pitch*RAD2DGR)
//                        + "           [Roll]: " + String.format("%.1f", roll*RAD2DGR)
//                        + "           [Yaw]: " + String.format("%.1f", yaw*RAD2DGR)
//                        + "           [dt]: " + String.format("%.4f", dt));
//                System.out.println("GYROSCOPE           [X]:" + String.format("%.4f", event.values[0])
//                        + "           [Y]:" + String.format("%.4f", event.values[1])
//                        + "           [Z]:" + String.format("%.4f", event.values[2])
//                        + "           [Pitch]: " + String.format("%.1f", pitch*RAD2DGR)
//                        + "           [Roll]: " + String.format("%.1f", roll*RAD2DGR)
//                        + "           [Yaw]: " + String.format("%.1f", yaw*RAD2DGR)
//                        + "           [dt]: " + String.format("%.4f", dt));
//
//
//            }
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//        }
//    }
}


//////////////////////////////////////////////////////////////////////////////////////

class Tetrahedron
{

    enum STYLE
    {
        OLD, NEW
    }

    private com.yakgwa.kullow.ar.SampleApplication.Tetrahedron.STYLE codeType = com.yakgwa.kullow.ar.SampleApplication.Tetrahedron.STYLE.NEW;

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer mColors;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;

    private final String vertexShaderCode = "uniform mat4 uMVPMatrix;" + " attribute vec4 vPosition;" + "void main() {" + "  gl_Position = vPosition * uMVPMatrix;" + "}";

    private final String fragmentShaderCode = "precision mediump float;" + "uniform vec4 vColor;" + "void main() {" + "  gl_FragColor = vColor;" + "}";

    // number of coordinates per vertex in this array
// 72d angles at center, 108 angle at vertex
    static final int COORDS_PER_VERTEX = 3;
    static final int COLOR_DATA_SIZE = 4;

    static float[] tetCoords = {0.0f, 0.622008459f, 0.0f,//
            -0.5f, -0.311004243f, 0.0f,//
            0.5f, -0.311004243f, 0.0f,//
            0.0f, 0.0f, .622008459f};

    static float[] colors = {
            //face one
            0.2f, 0.0f, 0.0f, 1.0f,//
            1.0f, 0.5f, 0.5f, 1.0f,//
            1.0f, 0.5f, 0.5f, 1.0f,//
            //face two
            0.5f, 0.0f, 0.0f, 1.0f,//
            1.0f, 0.5f, 0.5f, 1.0f,//
            1.0f, 0.5f, 0.5f, 1.0f,//
            //face three
            1.0f, 0.0f, 0.0f, 1.0f,//
            1.0f, 0.0f, 0.0f, 1.0f,//
            1.0f, 0.0f, 0.0f, 1.0f,//
            //face four
            0.1f, 0.0f, 0.0f, 1.0f,//
            1.0f, 0.5f, 0.5f, 1.0f,//
            1.0f, 0.5f, 0.5f, 1.0f,//
    };

    String[] attributes = { "a_Position", "a_Color" };

    private short[] drawOrder = {0, 1, 2, 3, 0, 1};
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final int colorStride = COLOR_DATA_SIZE * 4;

    float[] color = {.5f, .5f, .5f, 1f};

    public Tetrahedron()
    {
        // initialize vertex byte buffer for shape coordinates

        //this.color = color;
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                tetCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(tetCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        // (# of coordinate values * 2 bytes per short)
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        mColors = ByteBuffer.allocateDirect(colors.length * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mColors.put(colors);
        mColors.position(0);

        if (codeType == com.yakgwa.kullow.ar.SampleApplication.Tetrahedron.STYLE.NEW)
        {

            final String vertexShader = getVertexShader();
            final String fragmentShader = getFragmentShader();

            int vertexShaderHandle = SampleRendererBase.loadShader(GLES20.GL_VERTEX_SHADER, vertexShader);
            int fragmentShaderHandle = SampleRendererBase.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShader);
            mProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(mProgram, vertexShaderHandle);
            GLES20.glAttachShader(mProgram, fragmentShaderHandle);
            for (int i = 0; i < attributes.length; i++)
            {
                GLES20.glBindAttribLocation(mProgram, i, attributes[i]);
            }

            GLES20.glLinkProgram(mProgram);

        }
        else
        {
            int vertexShaderHandle = SampleRendererBase.loadShader(GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode);
            int fragmentShaderHandle = SampleRendererBase.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode);
            mProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(mProgram, vertexShaderHandle);
            GLES20.glAttachShader(mProgram, fragmentShaderHandle);
            for (int i = 0; i < attributes.length; i++)
            {
                GLES20.glBindAttribLocation(mProgram, i, attributes[i]);
            }

            GLES20.glLinkProgram(mProgram);

        }
    }

    protected String getVertexShader()
    {

        // TODO: Explain why we normalize the vectors, explain some of the vector math behind it all. Explain what is eye space.
        final String vertexShader = "uniform mat4 u_MVPMatrix;      \n" // A constant representing the combined model/view/projection matrix.
                + "uniform mat4 u_MVMatrix;       \n" // A constant representing the combined model/view matrix.
                + "attribute vec4 a_Position;     \n" // Per-vertex position information we will pass in.
                + "attribute vec4 a_Color;        \n" // Per-vertex color information we will pass in.
                + "varying vec4 v_Color;          \n" // This will be passed into the fragment shader.

                + "void main()                    \n" // The entry point for our vertex shader.
                + "{                              \n"
                // Transform the vertex into eye space.
                + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
                // Multiply the color by the illumination level. It will be interpolated across the triangle.
                + "   v_Color = a_Color;                                       \n"
                // gl_Position is a special variable used to store the final position.
                // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                + "   gl_Position = u_MVPMatrix * a_Position;                            \n" + "}                                                                     \n";

        return vertexShader;
    }

    protected String getFragmentShader()
    {
        final String fragmentShader = "precision mediump float;       \n" // Set the default precision to medium. We don't need as high of a
                // precision in the fragment shader.
                + "varying vec4 v_Color;          \n" // This is the color from the vertex shader interpolated across the
                // triangle per fragment.
                + "void main()                    \n" // The entry point for our fragment shader.
                + "{                              \n" + "   gl_FragColor = v_Color;     \n" // Pass the color directly through the pipeline.
                + "}                              \n";

        return fragmentShader;
    }

    public void draw(float[] mvpMatrix)
    {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        if (codeType == com.yakgwa.kullow.ar.SampleApplication.Tetrahedron.STYLE.NEW)
        {
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
            mMVMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix");
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
            mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                    false, vertexStride, vertexBuffer);

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(mPositionHandle);

            // Pass in the color information
            GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE, GLES20.GL_FLOAT, false,
                    colorStride, mColors);
            GLES20.glEnableVertexAttribArray(mColorHandle);

            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
            SampleRendererBase.checkGlError("glUniformMatrix4fv");

        }
        else
        {

            // get handle to vertex shader's vPosition member
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(mPositionHandle);

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                    false, vertexStride, vertexBuffer);

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

            // Set color for drawing the triangle
            GLES20.glUniform4fv(mColorHandle, 1, color, 0);

            // get handle to shape's transformation matrix
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
            SampleRendererBase.checkGlError("glGetUniformLocation");

            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
            SampleRendererBase.checkGlError("glUniformMatrix4fv");

        }

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT,
                drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}



