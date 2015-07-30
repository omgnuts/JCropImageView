/*
 *  Copyright (c) 2015. Jim Coven
 *  http://www.github.com/jimcoven
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.jimcoven.demo;

import com.github.jimcoven.demo.jcropimageview.R;

import java.util.ArrayList;
import java.util.List;


public class DataPack {

    public static final int SHAPE_LONG = 0;
    public static final int SHAPE_WIDE = 1;
    public static final int SHAPE_ICON = 2;
    public static final int SHAPE_SQUARE = 3;
    public static final int SHAPE_THIN = 4;
    public static final int SHAPE_FLAT = 5;

    private static String getShapeName(int shape) {
        switch (shape) {
            case SHAPE_LONG:    return "Long";
            case SHAPE_WIDE:    return "Wide";
            case SHAPE_ICON:    return "Icon";
            case SHAPE_SQUARE:  return "Square";
            case SHAPE_THIN:    return "Thin";
            case SHAPE_FLAT:    return "Flat";
        }
        return "UNKNOWN";
    }

    public static class ImageItem {
        public final int shape;
        public final int resId;
        private ImageItem(int shape, int resId) {
            this.shape = shape;
            this.resId = resId;
        }

        public String getShapeName() {
            return DataPack.getShapeName(shape) + " " + resId;
        }
    }

    public static ImageItem[] getImages(int[] filters) {
        List<ImageItem> filtered = new ArrayList<>();
        for (int f : filters) {
            for (ImageItem ii : images) {
                if (ii.shape == f) {
                    filtered.add(ii);
                }
            }
        }
        return filtered.toArray(new ImageItem[filtered.size()]);
    }

    private static final ImageItem[] images;

    static {
        List<ImageItem> items = new ArrayList<>();

        items.add(new ImageItem(SHAPE_THIN, R.mipmap.thin_11));
        items.add(new ImageItem(SHAPE_THIN, R.mipmap.thin_12));
        items.add(new ImageItem(SHAPE_THIN, R.mipmap.thin_13));
        items.add(new ImageItem(SHAPE_THIN, R.mipmap.thin_14));
        items.add(new ImageItem(SHAPE_THIN, R.mipmap.thin_15));

        items.add(new ImageItem(SHAPE_FLAT, R.mipmap.flat_11));
        items.add(new ImageItem(SHAPE_FLAT, R.mipmap.flat_12));
        items.add(new ImageItem(SHAPE_FLAT, R.mipmap.flat_13));
        items.add(new ImageItem(SHAPE_FLAT, R.mipmap.flat_14));

        items.add(new ImageItem(SHAPE_LONG, R.mipmap.long_11));
        items.add(new ImageItem(SHAPE_LONG, R.mipmap.long_12));
        items.add(new ImageItem(SHAPE_LONG, R.mipmap.long_13));
        items.add(new ImageItem(SHAPE_LONG, R.mipmap.long_14));
        items.add(new ImageItem(SHAPE_LONG, R.mipmap.long_15));

        items.add(new ImageItem(SHAPE_WIDE, R.mipmap.wide_11));
        items.add(new ImageItem(SHAPE_WIDE, R.mipmap.wide_12));
        items.add(new ImageItem(SHAPE_WIDE, R.mipmap.wide_13));
        items.add(new ImageItem(SHAPE_WIDE, R.mipmap.wide_14));

        items.add(new ImageItem(SHAPE_ICON, R.mipmap.icon_11));
        items.add(new ImageItem(SHAPE_ICON, R.mipmap.icon_12));
        items.add(new ImageItem(SHAPE_ICON, R.mipmap.icon_13));
        items.add(new ImageItem(SHAPE_ICON, R.mipmap.icon_14));

        items.add(new ImageItem(SHAPE_SQUARE, R.mipmap.square_11));
        items.add(new ImageItem(SHAPE_SQUARE, R.mipmap.square_12));
        items.add(new ImageItem(SHAPE_SQUARE, R.mipmap.square_13));
        items.add(new ImageItem(SHAPE_SQUARE, R.mipmap.square_14));
        items.add(new ImageItem(SHAPE_SQUARE, R.mipmap.square_15));

        images = items.toArray(new ImageItem[items.size()]);
    }


}
