# Visitor Manager App

[![Github contributors](https://img.shields.io/github/contributors/ashish7zeph/android-kotlin-Visitor-Manager-app?style=for-the-badge)](https://github.com/ashish7zeph/android-kotlin-Recipe-app/graphs/contributors)
[![Language max %](https://img.shields.io/github/languages/top/ashish7zeph/android-kotlin-Visitor-Manager-app?color=orange&style=for-the-badge)](https://kotlinlang.org/)

[![Build with android](https://forthebadge.com/images/badges/built-for-android.svg)](https://www.android.com/)

## Overview

Build a Visitor Manager app along with the integration of Firebase

It is a Visitor Manager app with Internet permissions enabled to access and store the user data on the online `Firebase Database` only after the user authentication with `Firebase Auth` and also made use of `Firebase Storage`, to store images

Firstly, user has to upload an Image either from camera (CameraX) or from gallery, that will be store online in `Firebase Storage`, then user has to Register by Phone number and OTP authentication, then his visit data is stored in `Firebase DB` as a New User
Secondly, if already registered user visits and registers again then his visit count will increase, thus, app is managing user visits like a `Digitalized Receptionist`

## Features

* Firebase API
* Firebase Auth
* Firebase Database
* Firebase Storage
* OTP Verification
* CameraX API
* Gallery Integration
* Splash screen
* Customised buttons
* Minimal Design
* Simplified Theme

## Platform
        -> Android Studio
        -> With Kotlin Support

## Instructions

1. Clone or download the repo: `https://github.com/ashish7zeph/android-kotlin-Visitor-Manager-app`
2. Navigate to the folder `android-kotlin-Visitor-Manager-app`
3. Navigate to the folder `android-kotlin-Visitor-Manager-app/app/src/` to access developers content
3. Navigate to the folder `apk` for users to access apk
4. Copy the apk from folder `apk` to an android phone
5. Install the apk

The app is finally installed on your Android mobile device !!

To directly download the apk visit the [link](https://github.com/ashish7zeph/android-kotlin-Visitor-Manager-app/tree/master/apk)

 # Screenshots:

<div style="display:flex;">
<img alt="App image" src="screenshots/img1 splash screen.jpg" width="30%" hspace="10">
<img alt="App image" src="screenshots/img2 main activity.jpg" width="30%" hspace="10">
<img alt="App image" src="screenshots/img3 CameraKit camera.jpg" width="30%" hspace="10">
</div>
<br/>
<br/>
<div style="display:flex;">
<img alt="App image" src="screenshots/img3 gallery.jpg" width="30%" hspace="10">
<img alt="App image" src="screenshots/img4 pic uploading.jpg" width="30%" hspace="10">
<img alt="App image" src="screenshots/img5 back to main.jpg" width="30%" hspace="10">
</div>
<br/>
<br/>
<div style="display:flex;">
<img alt="App image" src="screenshots/img6 enter phone num.jpg" width="30%" hspace="10">
<img alt="App image" src="screenshots/img7 signing in.jpg" width="30%" hspace="10">
<img alt="App image" src="screenshots/img8 OTP sent.jpg" width="30%" hspace="10">
</div>
<br/>
<br/>
<div style="display:flex;">
<img alt="App image" src="screenshots/img9 OTP written.jpg" width="30%" hspace="10">
<img alt="App image" src="screenshots/img10 visited.jpg" width="30%" hspace="10">
<img alt="App image" src="screenshots/img11 already visited user.jpg" width="30%" hspace="10">
</div>
<br/>
<br/>
<br/>

# Firebase Server:

![](https://github.com/ashish7zeph/testapp/blob/master/screenshots/firebase%20auth.png)
<br/>
<br/>
<br/>

![](https://github.com/ashish7zeph/testapp/blob/master/screenshots/firebase%20realtime%20database.png)
<br/>
<br/>
<br/>

![](https://github.com/ashish7zeph/testapp/blob/master/screenshots/firebase%20cloud%20storage.png)
<br/>
<br/>
<br/>

![](https://github.com/ashish7zeph/testapp/blob/master/screenshots/firebase%20images%20saved.png)
<br/>
<br/>
<br/>

## Kotlin Android Activity

For Kotlin code files visit the [link](https://github.com/ashish7zeph/android-kotlin-Visitor-Manager-app/tree/master/app/src/main/java/com/example/testapp/activities)

Backend files are stored in one package:

* [activity](https://github.com/ashish7zeph/android-kotlin-Visitor-Manager-app/tree/master/app/src/main/java/com/example/testapp/activities)

Frontend resource files are stored in `res` package

* [res](https://github.com/ashish7zeph/android-kotlin-Visitor-Manager-app/tree/master/app/src/main/res)

Android manifest file for the project:

* [AndroidManifest.xml](https://github.com/ashish7zeph/android-kotlin-Visitor-Manager-app/blob/master/app/src/main/AndroidManifest.xml)

