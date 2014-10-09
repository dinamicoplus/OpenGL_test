package net.brisan.opengl_test;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class OpenGLActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        GLSurfaceView mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
        Cube.c = this.getApplicationContext();
    }
}
