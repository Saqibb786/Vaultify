// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false



}
buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.3.10")
    }
}
// filepath: /C:/Users/Saqib/OneDrive - Punjab Group of Colleges/UCP/Semester 5/Mobile Application Development (E14) - Ihtisham Ul Haq/Assesments/Project/BankingProject/app/build.gradle
apply plugin: 'com.android.application'
apply plugin: 'com.google.gms.google-services' // Add this line

dependencies {
    // Add these lines
    implementation 'com.google.firebase:firebase-auth:21.0.1'
    implementation 'com.google.firebase:firebase-database:20.0.3'
}