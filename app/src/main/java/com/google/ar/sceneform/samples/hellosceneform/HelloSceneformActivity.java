/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
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
import java.util.concurrent.ExecutionException;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {
    private static final String TAG = HelloSceneformActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable searchingRenderable;
    private ModelRenderable madnessRenderable;
    private ModelRenderable sightRenderable;
    private ModelRenderable loseYourMindRenderable;
    private ModelRenderable sanityRenderable;
    private ModelRenderable findRenderable;
    private ModelRenderable shadowsRenderable;
    private ModelRenderable[] modelRenderableList = new ModelRenderable[6];


    private static final int PERMISSION_CODE = 1;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private static int DISPLAY_WIDTH = 480;
    private static int DISPLAY_HEIGHT = 640;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjection.Callback mMediaProjectionCallback;
    private ToggleButton mToggleButton;
    private MediaRecorder mMediaRecorder;
    private Surface getSurface;

    private int counter = 0;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }


        /*
        fileStringList[2] = "Sight.sfb";
        fileStringList[3] = "LoseYourMind.sfb";
        fileStringList[4] = "Shadows.sfb";
        fileStringList[5] = "Find.sfb";
        */

        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        DISPLAY_HEIGHT = metrics.heightPixels;
        DISPLAY_WIDTH = metrics.widthPixels;

        mToggleButton = (ToggleButton) findViewById(R.id.toggle);
        mToggleButton.setText("Click to record!");
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onToggleScreenShare(v);

            }
        });

        mMediaProjectionCallback = new MediaProjectionCallback();

        // Could just make a list of renderables



        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().



        CompletableFuture<ModelRenderable> searchingStage =
                ModelRenderable.builder().setSource(this, Uri.parse("ARTDanceInspiringSearching2.sfb")).build();
        CompletableFuture<ModelRenderable> sanityStage =
                ModelRenderable.builder().setSource(this, Uri.parse("ARTDanceInspiringSanity2.sfb")).build();
        CompletableFuture<ModelRenderable> madnessStage =
                ModelRenderable.builder().setSource(this, Uri.parse("ARTDanceInspiringMadness2.sfb")).build();
        CompletableFuture<ModelRenderable> findStage =
                ModelRenderable.builder().setSource(this, Uri.parse("ARTDanceInspiringFind2.sfb")).build();
        CompletableFuture<ModelRenderable> loseYourMindStage =
                ModelRenderable.builder().setSource(this, Uri.parse("ARTDanceInspiringLoseYourMind2.sfb")).build();
        CompletableFuture<ModelRenderable> sightStage =
                ModelRenderable.builder().setSource(this, Uri.parse("ARTDanceInspiringSight2.sfb")).build();
        CompletableFuture<ModelRenderable> shadowStage =
                ModelRenderable.builder().setSource(this, Uri.parse("ARTDanceInspiringShadows2.sfb")).build();
        //CompletableFuture<ModelRenderable> sightStage =
               // ModelRenderable.builder().setSource(this, Uri.parse("ARTDanceInspiringSight2.sfb")).build();

        CompletableFuture.allOf(
                searchingStage, sanityStage, madnessStage, findStage, loseYourMindStage, sightStage, shadowStage)
                .handle(
                        (notUsed, throwable) -> {
                            // When you build a Renderable, Sceneform loads its resources in the background while
                            // returning a CompletableFuture. Call handle(), thenAccept(), or check isDone()
                            // before calling get().

                            if (throwable != null) {
                                return null;
                            }

                            try {
                                searchingRenderable = searchingStage.get();
                                sanityRenderable = sanityStage.get();
                                madnessRenderable = madnessStage.get();
                                findRenderable = findStage.get();
                                loseYourMindRenderable = loseYourMindStage.get();
                                sightRenderable = sightStage.get();
                                shadowsRenderable = shadowStage.get();


                                //   sightRenderable = sightStage.get();
                                // Everything finished loading successfully.

                            } catch (InterruptedException | ExecutionException ex) {
                            }

                            return null;
                        });



        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    //if (searchingRenderable == null || madnessRenderable == null || sightRenderable == null || loseYourMindRenderable == null) {
                      //  return;
                    //}

                    // Create the Anchor.

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.

                    // Poor name for a variable, honestly.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);


                    modelRenderableList[0] = searchingRenderable;
                    modelRenderableList[1] = madnessRenderable;
                    modelRenderableList[2] = sightRenderable;
                    modelRenderableList[3] = loseYourMindRenderable;
                    modelRenderableList[4] = shadowsRenderable;
                    modelRenderableList[5] = sanityRenderable;


                    if(counter == 6){
                        counter = 0;
                    }

                    if(counter == 0){
                        andy.setRenderable(modelRenderableList[0]);
                    }
                    else if(counter == 1){
                        andy.setRenderable(modelRenderableList[1]);
                    }
                    else if(counter == 2){
                        andy.setRenderable(modelRenderableList[2]);

                    }
                    else if(counter == 3){
                        andy.setRenderable(modelRenderableList[3]);

                    }
                    else if(counter == 4){
                        andy.setRenderable(modelRenderableList[4]);
                    }
                    else if(counter == 5){
                        andy.setRenderable(modelRenderableList[5]);

                    }

                    andy.select();
                    counter = counter + 1;
                });
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaRecorder.reset();

            mMediaProjection = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PERMISSION_CODE) {
            Log.e(TAG, "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                    "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
            mToggleButton.setChecked(false);
            return;
        }
        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        mMediaProjection.registerCallback(mMediaProjectionCallback, null);
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    public void onToggleScreenShare(View view) {
        if (((ToggleButton) view).isChecked()) {
            initRecorder();
            prepareRecorder();
            mProjectionManager = (MediaProjectionManager) getSystemService
                    (Context.MEDIA_PROJECTION_SERVICE);


            mToggleButton.setBackgroundColor(Color.TRANSPARENT);
            mToggleButton.setText("   ");
            shareScreen();
        } else {
            //mMediaRecorder.stop();
            mMediaRecorder.reset();
            Log.v(TAG, "Recording Stopped");
            stopScreenSharing();
            mToggleButton.setText("Click to record!");
            mToggleButton.setVisibility(View.VISIBLE);
            mToggleButton.setBackgroundColor(getResources().getColor(R.color.lightBlueTheme));
        }
    }

    private void shareScreen() {
        if (mMediaProjection == null) {
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), PERMISSION_CODE);
            return;
        }
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();

    }

    private void stopScreenSharing() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mMediaRecorder.reset();
        //mMediaRecorder.release();
    }

    private VirtualDisplay createVirtualDisplay() {
        return mMediaProjection.createVirtualDisplay("MainActivity",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null /*Handler*/);
    }

    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            if (mToggleButton.isChecked()) {
                mToggleButton.setChecked(false);
                mMediaRecorder.stop();
                //mMediaRecorder.reset();
                Log.v(TAG, "Recording Stopped");
            }
            mMediaProjection = null;
            stopScreenSharing();
            Log.i(TAG, "MediaProjection Stopped");
        }
    }

    private void prepareRecorder() {
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    public String getFilePath() {
        // DCIM/Camera
        final String directory = Environment.getExternalStorageDirectory() + File.separator + "Recordings";
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "Failed to get External Storage", Toast.LENGTH_SHORT).show();
            return null;
        }
        final File folder = new File(directory);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        String filePath;
        if (success) {
            String videoName = ("capture_" + getCurSysDate() + ".mp4");
            filePath = directory + File.separator + videoName;
        } else {
            Toast.makeText(this, "Failed to create Recordings directory", Toast.LENGTH_SHORT).show();
            return null;
        }
        return filePath;
    }

    public String getCurSysDate() {
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
    }

    private void initRecorder() {
        int YOUR_REQUEST_CODE = 200; // could be something else..
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    YOUR_REQUEST_CODE);


            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
            }
            //CamcorderProfile cpHigh = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);

            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mMediaRecorder.setVideoFrameRate(30);
            mMediaRecorder.setOutputFile(getFilePath());
            mMediaRecorder.setVideoEncodingBitRate(10000000);

            //mMediaRecorder.setOutputFile(getFilePath());
        }
    }
}
