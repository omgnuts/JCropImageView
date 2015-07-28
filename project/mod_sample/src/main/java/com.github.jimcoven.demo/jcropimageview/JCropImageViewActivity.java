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
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jimcoven.demo.DataPack.ImageItem;
import com.github.jimcoven.view.JCropImageView;

import java.util.ArrayList;
import java.util.List;

public class JCropImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.jcrop_activity);

        Configuration config = getIntent().getParcelableExtra("config");

        ImagePageAdapter adapter = new ImagePageAdapter(this, config);
        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(adapter);

    }


    private static class ImagePageAdapter extends ObjectPageAdaper {

        private class VHItem {
            JCropImageView jcropImage;
            TextView jcropTitle;

            VHItem(View itemView) {
                this.jcropImage = (JCropImageView) itemView.findViewById(R.id.jcropimage_view);
                this.jcropTitle = (TextView) itemView.findViewById(R.id.jcrop_title);

                jcropImage.setCropType(config.cropType);
                jcropImage.setCropAlign(config.cropAlign);

                itemView.setTag(this);
            }
        }

        private final LayoutInflater inflater;

        private final Resources resources;

        private final ImageItem[] images;

        private final Configuration config;

        ImagePageAdapter(Context context, Configuration config) {
            this.resources = context.getResources();
            this.inflater = LayoutInflater.from(context);
            this.config = config;
            this.images = config.getImageItems();
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        protected View getView(int position, View view, ViewGroup parent) {
            VHItem holder;
            if (view == null) {
                view = inflater.inflate(R.layout.jcropitem_view, parent, false);
                holder = new VHItem(view);
            } else {
                holder = (VHItem) view.getTag();
            }

            bindView(holder, position);

            return view;
        }

        @SuppressWarnings("deprecation")
        private void bindView(final VHItem holder, int position) {
            Drawable image = resources.getDrawable(images[position].resId);

            holder.jcropImage.setImageDrawable(image);
            String jconfig = resources.getString(R.string.jcrop_image_view,
                    config.getConfigurationName());
            holder.jcropTitle.setText(jconfig);
        }

    }

    /**
     * A page adapter which works with a large data set by reusing views.
     */
    private abstract static class ObjectPageAdaper extends PagerAdapter {

        private static class RecycleItem {
            private int position = -1;
            private View view;
        }

        // Views that can be reused.
        private final List<RecycleItem> recycler = new ArrayList<>();
        private final List<RecycleItem> active = new ArrayList<>();

        @Override
        public abstract int getCount();

        protected abstract View getView(int position, View view, ViewGroup parent);

        @Override
        public Object instantiateItem(ViewGroup parent, int position) {
            RecycleItem item = recycler.isEmpty() ? new RecycleItem() : recycler.remove(0);
            item.view = getView(position, item.view, parent);
            item.position = position;

            parent.addView(item.view);
            active.add(item);

            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            RecycleItem item = (RecycleItem) object;
            if (item != null) {
                item.position = -1;
                recycler.add(item);
                container.removeView(item.view);
                active.remove(item);
            }
        }

        @Override
        public boolean isViewFromObject(View v, Object obj) {
            return v == ((RecycleItem) obj).view;
        }

        /**
         * Attempts to return the view based on position.
         * It can only return -x/+x of current position of the view pager.
         * @param position position of the adapter
         * @return view or null. If it returns null, it means its being recycled at the moment
         */
        protected View getItem(int position) {
            for (RecycleItem item : active) {
                if (item.position == position) {
                    return item.view;
                }
            }
            return null;
        }

        @Override
        public void notifyDataSetChanged() {
            recycler.clear();
            super.notifyDataSetChanged();
        }
    }

}