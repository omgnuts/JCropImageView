package com.jattcode.android.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jattcode.android.demo.DataEngine.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView title = (TextView)findViewById(R.id.title_view);

        final ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new ImagePageAdapter(this, DataEngine.getImages()));
        title.setText(pager.getAdapter().getPageTitle(0));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(pager.getAdapter().getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private static class ImagePageAdapter extends ObjectPageAdaper {

        private static class ViewHolder {
            ImageView image;

            ViewHolder(View itemView) {
                this.image = (ImageView) itemView.findViewById(R.id.image_view);
                itemView.setTag(this);
            }
        }

        final ImageItem[] images;

        final LayoutInflater inflater;

        ImagePageAdapter(Context context, ImageItem[] items) {
            this.inflater = LayoutInflater.from(context);
            this.images = items;
        }

        public String getPageTitle(int position) {
            return images[position].name;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.xcrop_imageview, null);
                holder = new ViewHolder(view);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            bindView(holder, position);
            return view;
        }

        private void bindView(final ViewHolder holder, int position) {
            holder.image.setImageResource(images[position].resId);
        }
    }

    /**
     * A page adapter which works with a large data set by reusing views.
     */
    private abstract static class ObjectPageAdaper extends PagerAdapter {

        // Views that can be reused.
        private final List<View> recycler = new ArrayList<View>();

        @Override
        public abstract int getCount();

        public abstract View getView(int position, View view, ViewGroup parent);

        @Override
        public Object instantiateItem(ViewGroup parent, int position) {
            View view = recycler.isEmpty() ? null : recycler.remove(0);
            view = getView(position, view, parent);
            parent.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            if (view != null) {
                recycler.add(view);
                container.removeView(view);
            }
        }

        @Override
        public boolean isViewFromObject(View v, Object obj) {
            return v == obj;
        }

        @Override
        public void notifyDataSetChanged() {
            recycler.clear();
            super.notifyDataSetChanged();
        }
    }

}
