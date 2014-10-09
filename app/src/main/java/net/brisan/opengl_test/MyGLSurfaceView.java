package net.brisan.opengl_test;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

class MyGLSurfaceView extends GLSurfaceView {

    private float mPreviousX;
    private float mPreviousY;

    public MyGLSurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);

        setRenderer(new MyRenderer());

        //Render solo cuando cambia algo en la pantalla
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;


                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }
                Log.i("x: ", ""+ x);
                Log.i("y: ", ""+ y);
                Log.i("dx: ", ""+ dx);
                Log.i("dy: ", ""+ dy);
                MyRenderer.vel = (dx + dy);
                 // = 180.0f / 320
                //requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

}
