package net.brisan.opengl_test;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//TODO Colocar los metodos loadShader y checkGLerror en una clase Utils
//TODO Fusionar la parte de escena de la clase Cube con la de la clase MyRenderer

public class MyRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Cube mCube;
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private float[] resolution = new float[2];

    public static float mAngle;
    public static double vel = 0.0;

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static void checkGlError(String glOperation) {
        int error;
        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            String errorStr;
            switch (error)
            {
                case GLES20.GL_INVALID_ENUM:
                    errorStr = "Invalid enum";
                    break;
                case GLES20.GL_INVALID_FRAMEBUFFER_OPERATION:
                    errorStr = "Invalid framebuffer operation";
                    break;
                case GLES20.GL_INVALID_OPERATION:
                    errorStr = "Invalid operation";
                    break;
                case GLES20.GL_INVALID_VALUE:
                    errorStr = "Invalid value";
                    break;
                default:
                    errorStr = "Unknown";
            }
            Log.e(TAG, glOperation + ": glError - " + errorStr);
            throw new RuntimeException(glOperation + ": glError - " + errorStr);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearColor(1.0f, 0.4f, 0.4f, 1.0f);
        mCube = new Cube();

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        resolution[1] = (float)height;
        resolution[0] = (float)width;
        GLES20.glViewport(0, 0, width, height);


        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 7);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        float[] scratch = new float[16];
        //float angle = 0.090f * ((int) time);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the rotation matrix
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 1.0f, 1.0f, 1.0f);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //-----------------------------Pos. Camara--P. de mira--Dir. de pie-----

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //mCube.draw(mMVPMatrix,color);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);


        mCube.setMVPMatrix(scratch);
        mCube.setResolution(resolution);
        // Draw Cube
        mCube.draw();
        vel *= 0.99;
        setAngle((float) (getAngle() + (vel * 180.0 /320)));

    }

    public static void setAngle(float angle)
    {
        mAngle = angle;
    }

    public static float getAngle()
    {
        return mAngle;
    }
}