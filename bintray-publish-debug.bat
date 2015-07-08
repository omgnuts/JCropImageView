gradlew bintrayUpload -c bintray-aar-settings.gradle -Ppublish_mode=debug

@rem -c settings-library.gradle specifies a settings for publishing
@rem dont want to build the sample app if you are pushing the AAR file to bintray


