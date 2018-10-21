# ARThellosceneformVideo
Taking the Google AR Toolkit example hellosceneform as a start this project aims at adding the capability to record users playing with the app

**For build.gradle, it is important that you use minSdkVersion 25 target 28, and compile 28**
```
android {
    compileSdkVersion 28
    defaultConfig {
        applicationId "com.google.ar.sceneform.samples.hellosceneform"

        // 24 is the minimum since ARCore only works with 24 and higher.
        minSdkVersion 25
        targetSdkVersion 28
        versionCode 1
        versionName "1.0"
```
