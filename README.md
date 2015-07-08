- Overview (Purpose, AAR, Bintray|JCenter|MavenCentral)
- Requirements - Android Studio
- Explain how to use (SetupBintray, DownloadProject, ConfigureParams.)
- Link to website for detailed tutorial
- makes it easy to start projects or submit widgets for OSS. allow ppl to share ideas. Works off the shelf for scripts most of the time. easily setup to bintray. every open source project seems to specify differently- getting messy to integrate libraries. using a repo bintray is very efficient. 

TODO: Add mavencentral. a bit more on jcenter. setup repo to support snapshots.

A basic android template for building libraries using gradle and which is configured for publishing to jcenter.

### This template uploads in the new AAR format.

- /AndroidManifest.xml (mandatory)
- /classes.jar (mandatory)
- /res/ (mandatory)
- /R.txt (mandatory)
- /assets/ (optional)
- /libs/*.jar (optional)
- /jni/<abi>/*.so (optional)
- /proguard.txt (optional)
- /lint.jar (optional)


### Setup bintray 
Here's a good tutorial
... find those bookmarks

### Create "local.properties" file in the root project folder
* bintray.userid=[eg. user123] 
* bintray.apikey=[eg. 3ff1763398311f0d73ab7e42f3e46h3457132b42]
* bintray.gpgkey=[eg. password123]

### Bintray (either JCenter or MavenORG)
* The libraries will be pushed to bintray. You can specify this in the compile (see sample app).
* This repository does not support SNAPSHOT. To use that register for Sona in Bintray
 
```
 maven {
        url 'https://dl.bintray.com/jattcode/maven/'
    }
```

```
dependencies {
    compile 'com.jattcode.oss:android-library-template:0.1.0'
}
```

