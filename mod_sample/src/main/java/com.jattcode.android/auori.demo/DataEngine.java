package com.jattcode.android.auori.demo;

import java.util.ArrayList;
import java.util.List;


public class DataEngine {

    private static ImageItem[] images;

    public static class ImageItem {
        final String name;
        final int resId;
        ImageItem(String name, int resId) {
            this.name = name;
            this.resId = resId;
        }
    }

    private static final int repeat = 10;

    private static ImageItem[] generateImageItems() {
        List<ImageItem> items = new ArrayList<ImageItem>();

        for (int c = 0; c < repeat; c++) {
            items.add(new ImageItem("small", R.mipmap.ic_launcher));
            items.add(new ImageItem("horizontal", R.mipmap.vertical_image));
            items.add(new ImageItem("vertical", R.mipmap.horizontal_image));
            items.add(new ImageItem("square", R.mipmap.square_image));
        }

        return items.toArray(new ImageItem[items.size()]);
    }

    public static ImageItem[] getImages() {
        if (images == null) {
            images = generateImageItems();
        }
        return images;
    }
}
