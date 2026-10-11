# X-Anime Search Android App

A minimal Android app with:
- X-ANIME branding
- Telegram join button: https://t.me/+HCtqGjybzAwzZGU1
- Search box that opens Google Search results
- No app database, account, hosting, or app-managed search-history feature
- Returns to the home screen when reopened

## Build it
1. Install Android Studio on a computer.
2. Extract this ZIP.
3. In Android Studio choose **Open** and select the `XAnimeSearch` folder.
4. Allow Gradle to sync and install any requested Android SDK components.
5. Test with **Run** on an Android device or emulator.
6. For Google Play, use **Build > Generate Signed Bundle / APK > Android App Bundle** and create a signing key.

## Important notes
- This project is source code, not a ready-made APK/AAB.
- Publishing requires a Google Play developer account and compliance with current Play policies.
- Google Play may reject a very simple website wrapper. Consider adding original functionality and a clear privacy policy before submission.
- The app clears local WebView cookies/cache when starting and closing, but Google may still associate searches with your Google account, IP address, or other Google services. This app cannot erase Google's server-side activity.
- Before publishing, review the app's privacy disclosures and test on your target Android versions.
- The app ID is `com.xanime.search`. Change it before publishing if you want a different unique package name.
