### Overview of XCropImageView

XCropImageView provides 11 configurations
 
* FitWidth  - 3 configurations - TOP/BOTTOM/CENTER
* FitHeight - 3 configurations - CENTER/LEFT/RIGHT
* FitBest   - 5 configurations - TOP/BOTTOM/CENTER/LEFT/RIGHT depending on which scale is used.
 
The configurations for FitWidth + LEFT/RIGHT are correct, but they doesn't show any visual difference. This is because when an image is fit to width, the full width is already within the visible left and right bounds. They are effectively ALWAYS aligned LEFT and RIGHT.
 
Similarly for FitHeight, it is effectively ALWAYS aligned TOP and BOTTOM

THe configurations are more visible with FitBest, which will decide which bound to fit to. The default android scaleType=centerCrop is effectively 1 of the above configurations FitBest + CENTER

### Include in build

```
repositories {
    maven { url 'https://dl.bintray.com/jimcoven/public/' }
}

dependencies {
    compile 'com.jattcode:android-xcropimageview:0.1'
}
```

### How to use XCropImageView

Just create an imageview as usual in xml. Then specify the configurations "app:scaleCropType" and "app:alignTo".
If "app:scaleCropType" is not specified, this reverts back to the default behaviour an Android ImageView.

```
<com.jattcode.android.view.XCropImageView
    android:id="@+id/image_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:scaleCropType="fit_width"
    app:alignTo="top"
/>
```

Supported configurations for "app:scaleCropType" are:
* fit_width : Stretch to fit width
* fit_fill  : Stretch to fit width or height to fill
* fit_height: Stretch to fit height

Supported configurations for "app:alignTo" are:
* top    : align top edge
* bottom : align bottom edge
* center : align center
* left   : align left edge
* right  : align right edge


