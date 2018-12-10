package com.google.ar.sceneform.samples.hellosceneform;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.display.VirtualDisplay;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import android.media.MediaRecorder;
import android.media.*;
import  android.media.projection.*;
import android.view.*;
import java.io.File;
import android.os.Handler;
import android.os.Looper;
import android.hardware.display.DisplayManager;
import java.io.IOException;
import android.util.DisplayMetrics;
import android.widget.ToggleButton;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;


public class RenderableList {

/*
    public static Renderable[] renderableList = new ModelRenderable[2];
    public static String[] renderableFilePathList = new String[2];
    public Renderable searchingRenderable;
    private ModelRenderable madnessRenderable;


    Need to make a list that has all of the renderables and their file path



    private void allRenderablesToList(){

        // Could have some issues with this updating.
        renderableList[0] = searchingRenderable;
        renderableList[1] = madnessRenderable;

        renderableFilePathList[0] = "Searching.sfb";
        renderableFilePathList[1] = "Madness.sfb";
    }

    public void renderAllRenderables(){

        CompletableFuture<Texture> futureTexture = Texture.builder()
                .setSource(this, Uri.parse("Madness.sfb"))
                .build();


    }

*/

}
