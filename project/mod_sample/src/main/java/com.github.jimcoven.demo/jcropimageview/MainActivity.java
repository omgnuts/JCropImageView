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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final Configuration[] configs;

    static {
        List<Configuration> items = new ArrayList<>();

        items.add(new Configuration(1, 0, R.drawable.fill_top)); // FIT_FILL + ALIGN_TOP
        items.add(new Configuration(1, 1, R.drawable.fill_bottom)); // FIT_FILL + ALIGN_BOTTOM
        items.add(new Configuration(1, 2, R.drawable.fill_center)); // FIT_FILL + ALIGN_CENTER
        items.add(new Configuration(1, 3, R.drawable.fill_left)); // FIT_FILL + ALIGN_LEFT
        items.add(new Configuration(1, 4, R.drawable.fill_right)); // FIT_FILL + ALIGN_RIGHT

        items.add(new Configuration(0, 0, R.drawable.width_top)); // FIT_WIDTH + ALIGN_TOP
        items.add(new Configuration(0, 1, R.drawable.width_bottom)); // FIT_WIDTH + ALIGN_BOTTOM
        items.add(new Configuration(0, 2, R.drawable.width_center)); // FIT_WIDTH + ALIGN_CENTER
        //items.add(new Configuration(0, 3)); // FIT_WIDTH + ALIGN_LEFT
        //items.add(new Configuration(0, 4)); // FIT_WIDTH + ALIGN_RIGHT

        //items.add(new Configuration(2, 0)); // FIT_HEIGHT + ALIGN_TOP
        //items.add(new Configuration(2, 1)); // FIT_HEIGHT + ALIGN_BOTTOM
        items.add(new Configuration(2, 2, R.drawable.height_center)); // FIT_HEIGHT + ALIGN_CENTER
        items.add(new Configuration(2, 3, R.drawable.height_left)); // FIT_HEIGHT + ALIGN_LEFT
        items.add(new Configuration(2, 4, R.drawable.height_right)); // FIT_HEIGHT + ALIGN_RIGHT

        configs = items.toArray(new Configuration[items.size()]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ConfigAdapter(this, configs));
    }


    public class ConfigAdapter extends Adapter<ConfigAdapter.ViewHolder> {

        // Provide a reference to the views for each data item
        // Provide access to all the views for a data item in a view holder
        public final class ViewHolder extends RecyclerView.ViewHolder
                implements RecyclerView.OnClickListener {
            private final ImageView image;
            private final TextView title;
            Configuration config;

            public ViewHolder(View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.image);
                title = (TextView)  view.findViewById(R.id.title_view);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JCropImageViewActivity.class);
                intent.putExtra("config", config);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        private final LayoutInflater inflater;

        private final Configuration[] configs;

        private ConfigAdapter(Context context, Configuration[] configs) {
            this.inflater = LayoutInflater.from(context);
            this.configs = configs;
        }

        // Create new items (invoked by the layout manager)
        // Usually involves inflating a layout from XML and returning the holder
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.mainitem_view, viewGroup, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Configuration config = configs[position];
            holder.image.setImageDrawable(getDrawable(config.display));
            holder.title.setText("(" + (position + 1) + ") " + config.getConfigurationName());
            holder.config = config;
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return this.configs.length;
        }

    }

    private void openGithub() {
        String url = "https://github.com/jimcoven/JCropImageView";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            // example 1:
            switch (item.getItemId()) {
                case R.id.github_link:
                    openGithub();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
