package com.yakgwa.kullow.ar;

import com.yakgwa.kullow.ar.SampleApplication.utils.MeshObject;

import java.nio.Buffer;

public class Arrow extends MeshObject {
    private static float a = 40.0f;
    private static float b = 40.0f;
    private static float x = 20.0f;
    private static float y = 20.0f;
    private static float z1 = 10.0f;
    private static float z2 = -1.0f;

    private final static float[] planeVertices =
            {
                    0.0f, 50.5f, 50.0f, //vertex 1  위 중앙
                    50.5f, -50.5f, 50.0f, //vertex 2  오른쪽 아래
                    -50.5f, -50.5f, 50.0f //vertex 3  왼쪽 아래

            };
    private final static float[] planeTexcoords =
            {
                    0.5f, 1.0f,  //vertex 1
                    1.0f, 0.0f,  //vertex 2
                    0.0f, 0.0f   //vertex 3
            };
    private final static float[] planeNormals =
            {
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f
            };
    private final static short[] planeIndices =
            {
                    0, 1, 2
            };

    int vertices_length = planeVertices.length;
    int indices_length = planeIndices.length;



    private Buffer mVertBuff;
    private Buffer mTexCoordBuff;
    private Buffer mNormBuff;
    private Buffer mIndBuff;

    public Arrow(){
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


/*
private final static double planeVertices[] =
{
2.1E-5f, 0.0f, -0.023521f, 2.1E-5f, 0.005f, 0.0f, -0.014679f, 0.0f, 0.010679f, 2.1E-5f, 0.0f, 0.0f, 2.1E-5f, -0.005f, 0.0f, 0.014721f, 0.0f, 0.010679f,
};
private final static double planeTexcoords[] =
{
85.106796, -32.132702, 0.031997, 9.39638, -61.449799, -32.132702, -0.065554, 0.0, 71.468903, 0.0, -0.065554, 19.684999, -0.065554, -19.684999, -71.599998, 0.0, -61.513802, 32.263802, -0.031997, -9.26528, 85.042801, 32.263802, 85.106796, 32.132702, -61.449799, 32.132702, 0.031997, -9.39638, -92.6007, 0.0, 0.0, -19.684999, 0.0, 0.0, 0.0, 19.684999, 61.513802, -32.263802, 0.031997, 9.26528, -85.042801, -32.263802,
};
private final static double planeNormals[] =
{
0.4355, -0.8805, 0.1872, -0.5878, 0.0, -0.809, 0.5878, 0.0, -0.809, 0.4355, -0.8805, -0.1872, 0.4355, 0.8805, 0.1872, -1.0, 0.0, 0.0, -0.4355, -0.8805, 0.1872,
};
private final static short planeIndices[] =
{
1, 1, 1, 2, 2, 1, 3, 3, 1, 4, 4, 2, 3, 5, 2, 2, 6, 2, 3, 5, 2, 4, 4, 2, 5, 7, 2, 4, 4, 3, 6, 8, 3, 5, 7, 3, 6, 8, 3, 4, 4, 3, 2, 6, 3, 6, 9, 4, 5, 10, 4, 1, 11, 4, 1, 12, 5, 3, 13, 5, 5, 14, 5, 1, 15, 6, 5, 16, 6, 4, 17, 6, 1, 15, 6, 4, 17, 6, 2, 18, 6, 6, 19, 7, 2, 20, 7, 1, 21, 7,
};

int vertices_length = 6;
int indices_length = 30;
 */