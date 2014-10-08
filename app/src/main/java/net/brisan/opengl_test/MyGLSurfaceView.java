package net.brisan.opengl_test;

import android.content.Context;
import android.opengl.GLSurfaceView;

class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);

        setRenderer(new MyRenderer());

    }
}
