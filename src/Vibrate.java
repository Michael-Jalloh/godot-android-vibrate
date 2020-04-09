package org.godotengine.godot;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import com.godot.game.R;
import javax.microedition.khronos.opengles.GL10;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.os.Build;
import java.util.Arrays;

public class Vibrate extends Godot.SingletonBase {

    protected Activity appActivity;
    protected Context appContext;
    private Godot activity = null;
    private int instanceId = 0;

    Vibrator v;
    boolean can_vibrate = false;


    public void vibrate(int length) {
        if (this.can_vibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(length, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(length);
            }
	
        }
    }

    public void getInstanceId(int pInstanceId) {
        // You will need to call this method from Godot and pass in the get_instance_id().
        instanceId = pInstanceId;
    }

    static public Godot.SingletonBase initialize(Activity p_activity) {
        return new Vibrate(p_activity);
    }

    public Vibrate(Activity p_activity) {
        // Register class name and functions to bind.
        registerClass("Vibrate", new String[]
            {
                "vibrate",
                "getInstanceId"
            });
        this.appActivity = p_activity;
        this.appContext = appActivity.getApplicationContext();
        // You might want to try initializing your singleton here, but android
        // threads are weird and this runs in another thread, so to interact with Godot you usually have to do.
        this.activity = (Godot)p_activity;
        this.v = (Vibrator) this.appContext.getSystemService(Context.VIBRATOR_SERVICE);
        this.can_vibrate = v.hasVibrator();
        this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    // Useful way to get config info from "project.godot".
                    String key = GodotLib.getGlobal("plugin/api_key");
                    // SDK.initializeHere();
                }
        });

    }

    // Forwarded callbacks you can reimplement, as SDKs often need them.

    protected void onMainActivityResult(int requestCode, int resultCode, Intent data) {}
    protected void onMainRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {}

    protected void onMainPause() {}
    protected void onMainResume() {}
    protected void onMainDestroy() {}

    protected void onGLDrawFrame(GL10 gl) {}
    protected void onGLSurfaceChanged(GL10 gl, int width, int height) {} // Singletons will always miss first 'onGLSurfaceChanged' call.

}