package com.jattcode.android.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * http://stackoverflow.com/questions/2991110/android-how-to-stretch-an-image-to-the-screen-width-while-maintaining-aspect-ra
 * http://www.ryadel.com/2015/02/21/android-proportionally-stretch-imageview-fit-whole-screen-width-maintaining-aspect-ratio/
 */
public class XCropImageView extends ImageView {

    enum ScaleCropType {
        FIXWIDTH_TOPCROP,       // Width != WRAP_CONTENT
        FIXWIDTH_CENTERCROP,    // Width != WRAP_CONTENT
        FIXWIDTH_BOTTOMCROP,    // Width != WRAP_CONTENT
        FIXHEIGHT_TOPCROP,      // Height != WRAP_CONTENT
        FIXHEIGHT_CENTERCROP,   // Height != WRAP_CONTENT
        FIXHEIGHT_BOTTOMCROP    // Height != WRAP_CONTENT
    }

    private final int MATRIX_TOP_CROP = 0;
    private final int MATRIX_CENTER_CROP = 1;
    private final int MATRIX_BOTTOM_CROP = 2;

    private static final int MAX_HEIGHT = 360;
    private int maxHeight;
    private final int cropType = MATRIX_CENTER_CROP;

    public XCropImageView(Context context) {
        super(context);
        initComponent(context, null, 0, 0);
    }

    public XCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context, attrs, 0, 0);
    }

    public XCropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initComponent(context, attrs, defStyle, 0);
    }

	/* Initializers */

    /**
     * Initialize this view, by inflating it, finding its UI element references,
     * and applying the custom attributes provided by the programmer.
     *
     * @param context
     *            The Activity Context.
     * @param attrs
     *            The view's attributes.
     * @param defStyle
     *            The default style to apply, if no one was provided.
     */
    private void initComponent(Context context, AttributeSet attrs,
                               int defStyle, int defStyleRes) {
        maxHeight = (int) (MAX_HEIGHT * getContext().getResources().getDisplayMetrics().density);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            final int vwidth = MeasureSpec.getSize(widthMeasureSpec); // the view width
            final float scale = (float) vwidth / d.getIntrinsicWidth(); // scale_via_width
            final int h = (int) (d.getIntrinsicHeight() * scale); // theoretical height x scale_via_width
            final int vheight = h > maxHeight ? maxHeight : h; // limited height

            // this is to scale it only by width
            // - the pivot point is at (0,0)
            // for top crop we dont need to translate it
            Matrix matrix = getImageMatrix();
            matrix.setScale(scale, scale);

            if (cropType == MATRIX_CENTER_CROP) {
                // if you want center crop shift it up by 50% aka 0.5f
                float dy = (vheight - h) * 0.5f;
                matrix.postTranslate(0, (int) (dy + 0.5f)); // + 0.5f for rounding
            }

            else if (cropType == MATRIX_BOTTOM_CROP) {
                // if you want bottom crop shift it up by 100% aka 1.0f
                float dy = (vheight - h) * 1.0f;
                matrix.postTranslate(0, (int) (dy + 0.5f)); // + 0.5f for rounding
            }

            setImageMatrix(matrix);
            setMeasuredDimension(vwidth, vheight);

        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}