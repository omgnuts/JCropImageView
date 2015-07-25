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

package com.github.jimcoven.demo.jcropimageview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final Configuration[] configs;

    static {
        List<Configuration> items = new ArrayList<>();

        items.add(new Configuration(1, 0)); // FIT_FILL + ALIGN_TOP
        items.add(new Configuration(1, 1)); // FIT_FILL + ALIGN_BOTTOM
        items.add(new Configuration(1, 2)); // FIT_FILL + ALIGN_CENTER
        items.add(new Configuration(1, 3)); // FIT_FILL + ALIGN_LEFT
        items.add(new Configuration(1, 4)); // FIT_FILL + ALIGN_RIGHT

        items.add(new Configuration(0, 0)); // FIT_WIDTH + ALIGN_TOP
        items.add(new Configuration(0, 1)); // FIT_WIDTH + ALIGN_BOTTOM
        items.add(new Configuration(0, 2)); // FIT_WIDTH + ALIGN_CENTER
        //items.add(new Configuration(0, 3)); // FIT_WIDTH + ALIGN_LEFT
        //items.add(new Configuration(0, 4)); // FIT_WIDTH + ALIGN_RIGHT

        //items.add(new Configuration(2, 0)); // FIT_HEIGHT + ALIGN_TOP
        //items.add(new Configuration(2, 1)); // FIT_HEIGHT + ALIGN_BOTTOM
        items.add(new Configuration(2, 2)); // FIT_HEIGHT + ALIGN_CENTER
        items.add(new Configuration(2, 3)); // FIT_HEIGHT + ALIGN_LEFT
        items.add(new Configuration(2, 4)); // FIT_HEIGHT + ALIGN_RIGHT

        configs = items.toArray(new Configuration[items.size()]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        LayoutParams llparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        for (int c = 0; c < configs.length; c++) {
            Button button = new Button(this);
            button.setLayoutParams(llparams);
            button.setText(configs[c].getConfigurationName());
            button.setId(c);
            button.setOnClickListener(clicker);
            container.addView(button);
        }

    }

    final View.OnClickListener clicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Configuration config = configs[view.getId()];

            Intent intent = new Intent(MainActivity.this, JCropImageViewActivity.class);
            intent.putExtra("config", config);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    };

}
