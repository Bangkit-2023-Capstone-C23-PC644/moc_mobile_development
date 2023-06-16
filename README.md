# C23-PC644 - Medical Oversee Control - Mobile Development

## Author
- [Emirul Mukmin](https://github.com/emirulmukmin)

## App Description

Introducing MOC (Medical Oversee Control), a revolutionary Android app designed to enhance the hospital experience for patients. MOC's main feature is to provide users with real-time information on the current occupancy levels at hospitals, enabling them to make informed decisions about when to visit. Say goodbye to long queues and wasted time!

Hospitals can often be overwhelming and time-consuming, with patients enduring lengthy wait times and confusing queues. MOC aims to address these challenges by bridging the gap between hospitals and patients. By utilizing a remote connection through Retrofit API, MOC connects users with live updates on hospital quotas, categorized into three alert levels: crowded, very crowded, and vacant.

With MOC, patients can effortlessly check their position in the queue, ensuring they arrive at the hospital at the right time. No more guesswork or wasted hours sitting in crowded waiting rooms. This user-friendly app provides a seamless and efficient healthcare process, elevating the overall patient experience.

MOC leverages the power of Kotlin, a versatile mobile programming language, and incorporates the popular MVVM architecture pattern. The app's intuitive user interface is crafted using Android Studio native, resulting in a visually appealing and easily navigable platform.

Experience the future of hospital visits with MOC. Download the app now and take control of your healthcare journey. Say goodbye to long queues and hello to a hassle-free hospital experience!!

Download link: https://drive.google.com/drive/folders/1C04pKXoSlxlgijigwt9TVnKkWQIxwH7C?usp=sharing

### Screenshots
![AppScreenshots](https://raw.githubusercontent.com/emirulmukmin/moc_mobile_development/main/MOC%20App.png)

## Development

#### Requirements
* A Mac or Windows computer.
* Android Studio
* Android Virtual Device (AVD) > API 30

#### Dependencies
```Gradle
dependencies {
    implementation 'androidx.core:core-ktx:1.10.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.activity:activity-ktx:1.7.2'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.6.0'
    implementation 'androidx.navigation:navigation-ui-ktx:2.6.0'
    implementation 'de.hdodenhof:circleimageview:3.1.0'

    implementation "androidx.datastore:datastore-preferences:1.0.0"
    implementation "androidx.datastore:datastore-preferences-core:1.0.0"
    implementation "androidx.lifecycle:lifecycle-livedata-core-ktx:2.6.1"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.6.1"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
    implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"

    implementation 'com.github.bumptech.glide:glide:4.15.1'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
    implementation "com.squareup.okhttp3:logging-interceptor:4.9.3"
    implementation 'com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0'
    implementation 'com.google.android.gms:play-services-maps:18.1.0'
    implementation 'com.google.android.gms:play-services-location:21.0.1'
    
    implementation "androidx.room:room-runtime:2.5.1"
    implementation 'androidx.room:room-ktx:2.5.1'
    kapt "androidx.room:room-compiler:2.5.1"
    
 }
```
    
#### Plugins
```Gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
    id 'kotlin-parcelize'
    id 'com.google.android.libraries.mapsplatform.secrets-gradle-plugin'
    id 'com.google.gms.google-services'
}
```

### Clone this App

**Clone**
```bash
$ git clone https://github.com/robertheo15/moc_mobile_development.git
```

**Open in Android Studio**
* `File -> Open -> Navigate to folder that you clone this repo -> Open`

**Run this project on AVD**
* `Start AVD -> Run 'app'`

**Build this app**
* `Build -> Build Bundle(s)/APK(s) -> Build APK(s)`
