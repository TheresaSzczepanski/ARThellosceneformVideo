package com.google.ar.sceneform.samples.hellosceneform;


    /*
     * Copyright 2017 Google Inc. All Rights Reserved.
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *   http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/** Helper to ask camera permission. */
    public class CameraPermissionHelper extends AppCompatActivity {
        private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
        private static final int CAMERA_PERMISSION_CODE = 0;

        /** Check to see we have the necessary permissions for this app. */
        /** Launch Application Setting to grant permission. */
        public static void launchPermissionSettings(Activity activity) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            activity.startActivity(intent);
        }

    private static final String REQUIRED_PERMISSIONS[] = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS,
                CAMERA_PERMISSION_CODE);
    }

    public static boolean hasCameraPermission(Activity activity) {
        for(String p : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, p) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity) {
        for(String p : REQUIRED_PERMISSIONS) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, p)) {
                return true;
            }
        }
        return false;
    }

}
