# 2048Rogue

This is a minimal Android implementation of 2048 as a touch-based grid game. The project uses the Android Gradle plugin and Kotlin.

## Building

1. Install the Android SDK and accept the required licenses.
2. Create a `local.properties` file pointing `sdk.dir` to your Android SDK location:
   
   ```
   sdk.dir=/path/to/android-sdk
   ```
3. Run `./gradlew assembleRelease` to build the APK.

The repository includes a GitHub Actions workflow that builds the release APK automatically.
