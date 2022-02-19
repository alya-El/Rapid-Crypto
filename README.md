# RAPID CRYPTO
An Android application that offers a user-friendly trading simulator for cryptocurrencies. This app primarily targets users who want to learn how to utilize cryptocurrency. It provides real-time market data for different cryptocurrencies. This enables these users to experiment with any cryptocurrency without risking their real money or assets.

## Getting Started
After discussing and brainstorming ideas, we began making our user stores. Below shows some of the main user stories:
<p align="center">
    <img src="https://user-images.githubusercontent.com/94018581/147533495-f0990e10-2c7e-43e7-8043-1cb12a32054d.png" width="420" height="220">
  <img src="https://user-images.githubusercontent.com/94018581/147533587-d9ad02bb-96e5-4117-b10e-a73e416caf2e.png" width="420" height="220">
  <img src="https://user-images.githubusercontent.com/94018581/147533749-b0bdf5f7-4877-429e-a90a-f5b7238a0205.png" width="420" height="220">
  <img src="https://user-images.githubusercontent.com/94018581/147534559-c744bc47-8422-462b-8fba-239491fadaf8.png" width="420" height="220">

## Architecture (MVVM)
For our project, we follwed the MVVM architecture, which is recommended by Google:
<p align="center"> 
  <img src="https://user-images.githubusercontent.com/94018581/147536230-27219fda-bc2d-48e5-9cba-b467c5ec99fb.png" width="500" height="320">


Through agile methodology, we transformed the following user stories into these layouts:
  <p align="center">
    <img src="https://user-images.githubusercontent.com/94018581/147534234-9e0651f0-81c9-4fd4-bc64-25bd9b6edc55.png" width="500" height="320">
  <img src="https://user-images.githubusercontent.com/94018581/147534602-9f5326cb-3389-4927-b624-24b7fed89685.png" width="500" height="320">


## Issues
The main issues we faced throughout the implementation process were:
   
### 1. Unfamiliarity with Kotlin:
All our group members were unfimiliar with Kotlin. This forced our developers to learn as the course progressed as well as the project development phase. To help with this issue, we ensured our strongest and most experienced mobile developer be our main code reviewer. This helped with our code quality and assured us we were following best practices to our best ability.
    
### 2. Implementing coroutines and using mobile features:
Implementation of coroutines was hard because of its asynchronous behaviour but we were able to pull it through by exploring Kotlin official documents. Moreover, we had faced issues initially in using mobile features such as camera and gallery programmatically. It requires user permission, and it varies from user to user. We resolve this issue by adding a check on user permissions and asking the user in case permission is not provided.

## Authors

* Alya El-Serafi 
* Kartik Gevariya 
* Kevin Shanks
* Liam Brown
* Rotesh Chhabra

## Prerequisites

To have a local copy of this project up and running on your local machine, you will first need to install the following software / libraries / plug-ins

- Android Studio

## References

- https://firebase.google.com/
- https://github.com/PhilJay/MPAndroidChart#examples
- https://www.geeksforgeeks.org/implementing-edit-profile-data-functionality-in-social-media-android-app/
- https://www.tutorialspoint.com/how-to-download-image-from-url-in-android


