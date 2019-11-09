package com.yakgwa.kullow.ar;

import com.yakgwa.kullow.ar.SampleApplication.utils.MeshObject;

import java.nio.Buffer;

public class MyModel extends MeshObject {


    static float scaleNum=10;   // 크기 변경하려면 이 값 변경 * Default =1
    static float locationNum =140; // 위치 변경하려면 여기 값 변경 * Default 100


    private final static double[] planeVertices =
            {
                    -10.0f * scaleNum, locationNum, 50.0f, //bottom-left corner
                    10.0f * scaleNum, locationNum, 50.0f, //bottom-right corner
                    10.0f * scaleNum, locationNum + 20.0f * scaleNum, 50.0f, //top-right corner
                    -10.0f * scaleNum, locationNum + 20.0f * scaleNum, 50.0f //top-left corner
            };
    private final static double[] planeTexcoords =
            {
                    0.0f, 0.0f, //tex-coords at bottom-left corner
                    1.0f, 0.0f, //tex-coords at bottom-right corner
                    1.0f, 1.0f, //tex-coords at top-right corner
                    0.0f, 1.0f //tex-coords at top-left corner
            };
    private final static double[] planeNormals =
            {
                    0.0f, 0.0f, 1.0f, //normal at bottom-left corner
                    0.0f, 0.0f, 1.0f, //normal at bottom-right corner
                    0.0f, 0.0f, 1.0f, //normal at top-right corner
                    0.0f, 0.0f, 1.0f //normal at top-left corner

            };
    private final static short[] planeIndices =
            {
                    0, 1, 2, //triangle 1
                    2, 3, 0 // triangle 2
            };

    int vertices_length = 12;
    int indices_length = 6;



    private Buffer mVertBuff;
    private Buffer mTexCoordBuff;
    private Buffer mNormBuff;
    private Buffer mIndBuff;

    public MyModel(){
        mVertBuff = fillBuffer(planeVertices);
        mTexCoordBuff = fillBuffer(planeTexcoords);
        mNormBuff = fillBuffer(planeNormals);
        mIndBuff = fillBuffer(planeIndices);
    }


    @Override
    public Buffer getBuffer(BUFFER_TYPE bufferType) {
        Buffer result = null;
        switch (bufferType)
        {
            case BUFFER_TYPE_VERTEX:
                result = mVertBuff;
                break;
            case BUFFER_TYPE_TEXTURE_COORD:
                result = mTexCoordBuff;
                break;
            case BUFFER_TYPE_INDICES:
                result = mIndBuff;
                break;
            case BUFFER_TYPE_NORMALS:
                result = mNormBuff;
            default:
                break;
        }
        return result;
    }

    @Override
    public int getNumObjectVertex() {
        return vertices_length / 3;
    }

    @Override
    public int getNumObjectIndex() {
        return indices_length;
    }
}