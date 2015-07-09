package com.jattcode.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jattcode.android.R;

/**
 * http://stackoverflow.com/questions/2991110/android-how-to-stretch-an-image-to-the-screen-width-while-maintaining-aspect-ra
 * http://www.ryadel.com/2015/02/21/android-proportionally-stretch-imageview-fit-whole-screen-width-maintaining-aspect-ratio/
 */
public class XCropImageView extends ImageView {

    // Why not enums
    // http://stackoverflow.com/questions/24491160/when-to-use-enum-int-constants
    public static final class ScaleCropType {
        public static final int FITWITDH_STARTCROP = 0;
        public static final int FITWIDTH_CENTERCROP = 1;
        public static final int FITWIDTH_ENDCROP = 2;
        public static final int FITHEIGHT_STARTCROP = 3;
        public static final int FITHEIGHT_CENTERCROP = 4;
        public static final int FITHEIGHT_ENDCROP = 5;
    }

    private int mScaleCropType = -1;

    public XCropImageView(Context context) {
        super(context);
        initFromAttributes(context, null, 0, 0);
    }

    public XCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs, 0, 0);
    }

    public XCropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFromAttributes(context, attrs, defStyle, 0);
    }

    /**
     * INitialize the attributes for the XCropImageView
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     * @param defStyleRes A resource identifier of a style resource that
     *        supplies default values for the view, used only if
     *        defStyleAttr is 0 or can not be found in the theme. Can be 0
     *        to not look for defaults.
     * @see #View(Context, AttributeSet, int)
     */
    private void initFromAttributes(Context context, AttributeSet attrs,
                               int defStyleAttr, int defStyleRes) {
        // Read and apply provided attributes
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.XCropImageView, defStyleAttr, defStyleRes);
        mScaleCropType = a.getInt(R.styleable.XCropImageView_scaleCropType, mScaleCropType);
        a.recycle();

        if (mScaleCropType > -1) setScaleType(ScaleType.MATRIX);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null && mScaleCropType > -1) {

            final int dw = d.getIntrinsicWidth();
            final int dh = d.getIntrinsicHeight();
            int dx = 0, dy = 0;
            int msWidth = 0, msHeight = 0;
            final float scale;

            if (mScaleCropType < 3) { // fix width
                // 1. get anchor by width. constrain to drawablewidth if wrap-content
                msWidth = MeasureSpec.getSize(widthMeasureSpec); // the view width
                if (getLayoutParams().width == -2) { // wrap
                    msWidth = dw < msWidth ? dw : msWidth;
                }

                // 2. compute scale and theoretical height
                scale = (float) msWidth / dw; // scale_via_width
                final int h = (int) (dh * scale); // theoretical = height x scale_via_width

                // 3. constrain by maxheight
                // if match_parent then additional constraint if viewport < maxheight
                // if wrap_content then anything works even if viewport < or >  maxheight
                int maxHeight = getMaxHeight();
                msHeight = getLayoutParams().height;
                if (msHeight > -2) { // match parent
                    if (msHeight == -1) msHeight = MeasureSpec.getSize(heightMeasureSpec);
                    maxHeight = msHeight < maxHeight ? msHeight : maxHeight;
                }
                msHeight = h > maxHeight ? maxHeight : h; // limited height

                // 4. translate
                if (mScaleCropType == ScaleCropType.FITWIDTH_CENTERCROP) {
                    // if you want center crop shift it up by 50% aka 0.5f
                    dy = (int)( (msHeight - h) * 0.5f + 0.5f ); // + 0.5f for rounding
                } else if (mScaleCropType == ScaleCropType.FITWIDTH_ENDCROP) {
                    // if you want bottom crop shift it up by 100% aka 1.0f
                    dy = (int)( (msHeight - h) * 1.0f + 0.5f ); // + 0.5f for rounding
                }

            } else {
                // 1. get anchor by height. constrain to drawableheight if wrap-content
                msHeight = MeasureSpec.getSize(heightMeasureSpec); // the view height
                if (getLayoutParams().height == -2) { // wrap
                    msHeight = dh < msHeight ? dh : msHeight;
                }

                // 2. compute scale and theoretical width
                scale = (float) msHeight / dh; // scale_via_height
                final int w = (int) (dw * scale); // theoretical = width x scale_via_height

                // 3. constrain by maxwidth
                // if match_parent then additional constraint if viewport < maxwidth
                // if wrap_content then anything works even if viewport < or >  maxwidth
                int maxWidth = getMaxWidth();
                msWidth = getLayoutParams().width;
                if (msWidth > -2) { // match parent or is set
                    if (msWidth == -1) msWidth = MeasureSpec.getSize(widthMeasureSpec);
                    maxWidth = msWidth < maxWidth ? msWidth : maxWidth;
                }
                msWidth = w > maxWidth ? maxWidth : w; // limited width

                if (mScaleCropType == ScaleCropType.FITHEIGHT_CENTERCROP) {
                    // if you want center crop shift it left by 50% aka 0.5f
                    dx = (int)( (msWidth - w) * 0.5f + 0.5f ); // + 0.5f for rounding
                } else if (mScaleCropType == ScaleCropType.FITHEIGHT_ENDCROP) {
                    // if you want bottom crop shift it up by 100% aka 1.0f
                    dx = (int)( (msWidth - w) * 1.0f + 0.5f ); // + 0.5f for rounding
                }
            }

            // this is to scale it only by width
            // - the pivot point is at (0,0)
            // for top crop we dont need to translate it
            Matrix matrix = getImageMatrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);
            setImageMatrix(matrix);
            setMeasuredDimension(msWidth, msHeight);
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}