package net.brisan.opengl_test;

import android.opengl.GLES20;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static net.brisan.opengl_test.MyRenderer.loadShader;

public class Cube {

    static final int COORDS_PER_VERTEX = 3;
    static float cubeCoords[] = {   // in counterclockwise order:
            -0.5f, -0.5f,  0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f
    };
    static float cubeColors[] = {   // in counterclockwise order:
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 1.0f
    };
    static short cubeElements[] = {
            // front
            0, 1, 2,
            2, 3, 0,
            // top
            3, 2, 6,
            6, 7, 3,
            // back
            7, 6, 5,
            5, 4, 7,
            // bottom
            4, 5, 1,
            1, 0, 4,
            // left
            4, 0, 3,
            3, 7, 4,
            // right
            1, 5, 6,
            6, 2, 1
    };
    private final int mProgram;
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 vMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec3 vColor;" +
                    "varying vec3 fColor;" +
                    "void main() {" +
                    "   gl_Position = vMVPMatrix * vPosition;" +
                    "   fColor = vColor;" +
                    "}";
    int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec2 fResolution;" +
                    "uniform float fTime;" +
                    "varying vec3 fColor;" +
                    "void main()" +
                    "{" +
                    "   gl_FragColor = vec4(fColor,1.0);" +
                    "}";
    int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
    int [] buffers = new int[3];
    private int vPositionHandle;
    private int vColorHandle;
    private int vMVPMatrixHandle;
    private int fColorHandle;
    private int fResolutionHandle;
    private int fTimeHandle;


    public Cube() {
        GLES20.glGenBuffers(3,buffers,0);

        // initialize vertex byte buffer for shape coordinates
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        ByteBuffer bb = ByteBuffer.allocateDirect(cubeCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(cubeCoords);
        vertexBuffer.position(0);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
        ByteBuffer bbcolor = ByteBuffer.allocateDirect(cubeColors.length * 4);
        bbcolor.order(ByteOrder.nativeOrder());
        FloatBuffer colorBuffer = bbcolor.asFloatBuffer();
        colorBuffer.put(cubeColors);
        colorBuffer.position(0);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colorBuffer.capacity() * 4, colorBuffer, GLES20.GL_STATIC_DRAW);


        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers[2]);
        ByteBuffer bbelem = ByteBuffer.allocateDirect(cubeElements.length * 2);
        bbelem.order(ByteOrder.nativeOrder());
        ShortBuffer elemBuffer = bbelem.asShortBuffer();
        elemBuffer.put(cubeElements);
        elemBuffer.position(0);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, elemBuffer.capacity() * 2, elemBuffer, GLES20.GL_STATIC_DRAW);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables

        vPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        vColorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
        fColorHandle = GLES20.glGetUniformLocation(mProgram, "fColor");
        fResolutionHandle = GLES20.glGetUniformLocation(mProgram, "fResolution");
        fTimeHandle = GLES20.glGetUniformLocation(mProgram, "fTime");
        vMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "vMVPMatrix");
        MyRenderer.checkGlError("glGetUniformLocation");

    }

    public void draw(float[] mvpMatrix, float[] resolution) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        //  Attribute Vertex Position
        GLES20.glEnableVertexAttribArray(vPositionHandle);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,buffers[0]);
        GLES20.glVertexAttribPointer(
                vPositionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                0,
                0
        );

        //  Attribute Vertex Color
        GLES20.glEnableVertexAttribArray(vColorHandle);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,buffers[1]);
        GLES20.glVertexAttribPointer(
                vColorHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                0,
                0
        );

        //  Uniform Resolution
        GLES20.glUniform2fv(fResolutionHandle, 1, resolution, 0);

        //  Uniform Time
        float time = (float)(SystemClock.uptimeMillis());
        GLES20.glUniform1f(fTimeHandle, time);

        //  Uniform MVPMatrix
        GLES20.glUniformMatrix4fv(vMVPMatrixHandle, 1, false, mvpMatrix, 0); //  Set MVPMatrix as mvpMatrix
        MyRenderer.checkGlError("glUniformMatrix4fv");

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers[2]);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, cubeElements.length, GLES20.GL_UNSIGNED_SHORT, 0);
        //  Draw the cube
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vPositionHandle);
        GLES20.glDisableVertexAttribArray(vColorHandle);
    }
}