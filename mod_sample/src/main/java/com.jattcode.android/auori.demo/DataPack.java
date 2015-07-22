/*
 *
 *  * Copyright (c) 2015. JattCode.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 *
 *
 */

package com.jattcode.android.auori.demo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DataPack {

    public static final int SHAPE_SQUARE = 0;
    public static final int SHAPE_LONG = 1;
    public static final int SHAPE_WIDE = 2;
    public static final int SHAPE_THIN = 3;
    public static final int SHAPE_FLAT = 4;

    public static String getShapeName(int shape) {
        switch (shape) {
            case SHAPE_LONG: return "Long";
            case SHAPE_WIDE: return "Wide";
            case SHAPE_THIN: return "Thin";
            case SHAPE_FLAT: return "Flat";
            case SHAPE_SQUARE: return "Square";
        }
        return "BAD";
    }

    private static class ImageItem {
        final int shape;
        final int resId;
        private ImageItem(int shape, int resId) {
            this.shape = shape;
            this.resId = resId;
        }

        public String getShape() {
            return getShapeName(shape) + " " + resId;
        }
    }

    static {
        List<ImageItem> items = new ArrayList<>();

        items.add(new ImageItem(SHAPE_LONG, R.mipmap.long_1));
        items.add(new ImageItem(SHAPE_LONG, R.mipmap.long_2));
        items.add(new ImageItem(SHAPE_LONG, R.mipmap.long_3));
        items.add(new ImageItem(SHAPE_LONG, R.mipmap.long_4));

        items.add(new ImageItem(SHAPE_WIDE, R.mipmap.wide_1));
        items.add(new ImageItem(SHAPE_WIDE, R.mipmap.wide_2));
        items.add(new ImageItem(SHAPE_WIDE, R.mipmap.wide_3));
        items.add(new ImageItem(SHAPE_WIDE, R.mipmap.wide_4));

        images = items.toArray(new ImageItem[items.size()]);
    }

    private final Context context;

    private DataPack(Context context) {
        this.context = context;
    }

    private static volatile DataPack singleton;

    public static synchronized DataPack with(Context context) {
        if  (singleton == null) {
            singleton = new DataPack(context);
        }
        return singleton;
    }

    private static ImageItem[] images;

    private int cursor = -1;

    private ImageItem next(int shape) {
        if (++cursor >= images.length) cursor = 0;
        if (shape > -1) {
            while(images[cursor].shape != shape) {
                cursor++;
                if (cursor >= images.length) cursor = 0;
            }
        }

        Log.d("AA", "found shape");
        ImageItem result = images[cursor];

        return result;
    }

    public int currentId() {
        return currentId(-1);
    }

    public int nextId() {
        return nextId(-1);
    }

    public Drawable nextDrawable() {
        return nextDrawable(-1);
    }

    public Drawable currentDrawable() {
        return currentDrawable(-1);
    }

    public int currentId(int shape) {
        int cur = cursor < 0 ? 0 : cursor;
        if (shape < 0 || images[cur].shape == shape) {
            return images[cur].resId;
        } else {
            return next(shape).resId;
        }
    }

    // returns a nextid of a specific shape
    public int nextId(int shape) {
        return next(shape).resId;
    }

    public Drawable currentDrawable(int shape) {
        return context.getResources().getDrawable(currentId(shape));
    }

    public Drawable nextDrawable(int shape) {
        return context.getResources().getDrawable(nextId(shape));
    }

}
