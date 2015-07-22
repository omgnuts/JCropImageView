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
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jattcode.android.auori.view.AuoriCropImageView;

import java.util.ArrayList;
import java.util.List;

public class MultiViewActivity extends AppCompatActivity {

    private static class Configuration {
        final int scaleCropType;
        final int alignment;

        private Configuration(int type, int align) {
            scaleCropType = type;
            alignment = align;
        }

        private String getConfiguration() {
            return getCropType(scaleCropType) + " : " + getAlignment(alignment);
        }

        private static String getCropType(int id) {
            switch(id) {
                case 0: return "FIT_WIDTH";
                case 1: return "FIT_FILL";
                case 2: return "FIT_HEIGHT";
            }
            return "NA";
        }

        private static String getAlignment(int id) {
            switch(id) {
                case 1: return "ALIGN_TOP_OF_IMAGEVIEW";
                case 2: return "ALIGN_BOTTOM_OF_IMAGEVIEW";
                case 3: return "ALIGN_CENTER_OF_IMAGEVIEW";
                case 4: return "ALIGN_LEFT_OF_IMAGEVIEW";
                case 5: return "ALIGN_RIGHT_OF_IMAGEVIEW";
            }
            return "NA";
        }
    }

    private static final List<Configuration[]> configs;

    static {
        configs = new ArrayList<>();

        for (int k = 0; k < 5; k++) {
            Configuration[] set = new Configuration[3];
            for (int type = 0; type < 3; type++) {
                set[type] = new Configuration(type, k+1);
            }
            configs.add(set);
        }

        configs.get(3)[0] = null; // FIT_WIDTH + ALIGN_LEFT
        configs.get(4)[0] = null; // FIT_WIDTH + ALIGN_RIGHT

        configs.get(0)[2] = null; // FIT_HEIGHT + ALIGN_TOP
        configs.get(1)[2] = null; // FIT_HEIGHT + ALIGN_BOTTOM
    }

    private static LinearLayout createLayout(Context context, LayoutParams lp, int orient) {
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(lp);
        layout.setOrientation(orient);
//        layout.setBackgroundColor(Color.CYAN);
        return layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multiview);

        final LinearLayout container = (LinearLayout) findViewById(R.id.container);
        container.setOrientation(LinearLayout.VERTICAL);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        final List<ImageView> views = new ArrayList<>();
        final View.OnClickListener clicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextId = DataPack.with(MultiViewActivity.this).nextId();
                for (ImageView view : views) {
                    view.setImageResource(nextId);
                }
                container.forceLayout();
            }
        };

        // Layout weights dont work properly when pushed against the sides.
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int height = (int) (0.75 * metrics.heightPixels / configs.size());
        int width = metrics.widthPixels / configs.get(0).length;
        int dimen = Math.min(height, width);
        Log.d("NN", "" + container.getMeasuredHeight());

        LayoutParams alp = new LayoutParams(dimen, dimen);
        alp.setMargins(1,1,1,1 );

        for (int c = 0; c < configs.size(); c++) {

            LinearLayout ll = createLayout(this, lp, LinearLayout.HORIZONTAL);
            Configuration[] set = configs.get(c);
//            ll.setWeightSum(set.length);

            for (int k = 0; k < set.length; k++) {
                AuoriCropImageView auori = new AuoriCropImageView(this);
                if (set[k] != null) {
                    auori.setCropType(set[k].scaleCropType);
                    auori.setCropAlignment(set[k].alignment);
                    views.add(auori);
                } else {
//                    auori.setBackgroundColor(Color.BLACK);
                }

                auori.setLayoutParams(alp);
                ll.addView(auori);
            }
            container.addView(ll);
        }

        findViewById(R.id.switch_image).setOnClickListener(clicker);
        findViewById(R.id.switch_image).performClick();
    }


}