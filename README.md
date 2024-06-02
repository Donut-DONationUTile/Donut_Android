# [2024 GDSC Solution Challenge Top 10] DONUT_Android [![APK file](https://img.shields.io/badge/-APK%20file-blue)](https://drive.google.com/file/d/1g9B9qp6Sc10ojrjxJZ483lGM3o34yWJt/view?usp=sharing) [![Demo Video](https://img.shields.io/badge/-Demo%20Video-red)](https://youtu.be/qjlmdKrCPaI)

>_“A shilling is the measure of less pleasure, or satisfaction of any kind, to a rich man than to a poor one. The happiness which an additional shilling brings to a poor man is much greater than that which it brings to a rich one.”_
</br></br>_- Principles of Economics (8th ed.) PLL v6.0 (generated September, 2011) 16 -_

</br>

# 🍩 Overview
[![Demo Video](https://github.com/akimcse/akimcse/assets/63237214/3aac2345-cb7c-4037-8ed8-a5fa99ef7fc3)](https://youtu.be/qjlmdKrCPaI)

  `DONUT` is a sustainable donation platform tailored to the developmental characteristics of adolescents, providing a stigma-free process for beneficiaries. </br></br>
  By utilizing unused resources such as gift vouchers, amounting to 900 million KRW annually in South Korea, it facilitates low-income youth to purchase groceries and essential items.

</br></br>

# 📂 Table of Contents
  - [Project Architecture](#Project-Architecture)
  - [Android Tech Stacks](#Android-Tech-stacks)
  - [How to Run](#How-to-Run)
  - [How to Use DONUT](#How-to-Use-DONUT)
  - [TEAM ZPE (0.8)](#TEAM-ZPE-(0.8))

</br></br>

# 🛠️ Project Architecture

<image src='https://github.com/akimcse/akimcse/assets/63237214/49c749af-5579-42fe-b45c-d85a9c258dd8'/>
</br></br>

#### DONUT is comprised of a Client Application, Web Server, Database, Storage, and AI Server.
  The Client Application is implemented on `Android` using `Kotlin`, with the MVVM architecture to separate UI logic from business logic and increase code reusability and scalability. Through various interactions with users, the Client Application forwards requests to the Web Server.
  </br></br>
  The Web Server processes client requests and can communicate with the AI Server in need. It is implemented based on `Spring Boot` and utilizes `Redis` for JWT processing. Both Spring Boot and Redis are deployed in `Docker` containers via `GCP` Compute Engine. CI/CD is established using GCP Code Build and Artifact Registry for agile development.
  </br></br>
  A `MySQL` 8.0-based GCP Cloud SQL instance serves as the main Database. GCP Cloud Storage is used for the Image bucket, where URLs of inserted objects are stored in the Database.
  </br></br>
  The AI Model, served by `FastAPI`, is hosted on a separate VM from the Web Server. It is also deployed using Docker in preparation for utilizing `Kubernetes` for resource management caused by an increase in the number of users. Low-resolution gift card images forwarded from the Web Server to the AI Server are enhanced to high resolution using `TensorFlow`’s ESRGAN model and then stored in the GCP Storage bucket. The URLs of the stored objects are also updated in the Database.

</br></br>

# ⚙️ Android Tech Stacks
<table class="tg">
<tbody>
  <tr>
    <td><b>Architecture</b></td>
    <td>MVVM</td>
  </tr>
  <tr>
    <td><b>Jetpack Components</b></td>
    <td>AppCompat, LifeCycles, ViewModel, LiveData, viewPager2, CameraX</td>
  </tr>
  <tr>
    <td><b>library</b></td>
    <td>Standard Library, Material Design, Glide, MPAndroidChart</td>
  </tr>
  <tr>
    <td><b>Network</b></td>
    <td>OkHttp, Retrofit2, GSON, Coroutine</td>
  </tr>
  <tr>
    <td><b>SDK</b></td>
    <td>ML Kit(Text Recogniztion v2), Firebase(Authentication, Cloud Messaging)</td>
  </tr>
</tbody>
</table>

</br></br>

# 🚀 How to Run

#### Software requirement

- Android studio Flamingo 2022.2.1
- compileSdk 34 or higher
- minSdk 33 or higher

#### Hardware requirement

- CameraX supported Android Device within [this link](https://developer.android.com/media/camera/camerax/devices)
(CameraX is supported on most Android devices running Android 5.0 (API level 21) and higher.)

#### How to Run the Application
1. Check the requirement above.
2. Download the [APK file](https://drive.google.com/file/d/1g9B9qp6Sc10ojrjxJZ483lGM3o34yWJt/view?usp=sharing) on your Android device.
3. Run the apllication from APK file

</br></br>

# ✨ How to Use DONUT
<image src='https://github.com/akimcse/akimcse/assets/63237214/845d8d38-2b73-4897-ba63-c45ba32e28d0'/>
