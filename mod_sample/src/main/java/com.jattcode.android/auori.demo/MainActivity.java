package com.jattcode.android.auori.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jattcode.android.auori.view.AuoriCropImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

    private static final Configuration[] configs;

    static {
        List<Configuration> items = new ArrayList<>();

        items.add(new Configuration(0, 1)); // FIT_WIDTH + ALIGN_TOP
        items.add(new Configuration(0, 2)); // FIT_WIDTH + ALIGN_BOTTOM
        items.add(new Configuration(0, 3)); // FIT_WIDTH + ALIGN_CENTER
        //items.add(new Configuration(0, 4)); // FIT_WIDTH + ALIGN_LEFT
        //items.add(new Configuration(0, 5)); // FIT_WIDTH + ALIGN_RIGHT

        items.add(new Configuration(1, 1)); // FIT_FILL + ALIGN_TOP
        items.add(new Configuration(1, 2)); // FIT_FILL + ALIGN_BOTTOM
        items.add(new Configuration(1, 3)); // FIT_FILL + ALIGN_CENTER
        items.add(new Configuration(1, 4)); // FIT_FILL + ALIGN_LEFT
        items.add(new Configuration(1, 5)); // FIT_FILL + ALIGN_RIGHT

        //items.add(new Configuration(2, 1)); // FIT_HEIGHT + ALIGN_TOP
        //items.add(new Configuration(2, 2)); // FIT_HEIGHT + ALIGN_BOTTOM
        items.add(new Configuration(2, 3)); // FIT_HEIGHT + ALIGN_CENTER
        items.add(new Configuration(2, 4)); // FIT_HEIGHT + ALIGN_LEFT
        items.add(new Configuration(2, 5)); // FIT_HEIGHT + ALIGN_RIGHT

        configs = items.toArray(new Configuration[items.size()]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView title = (TextView)findViewById(R.id.title_view);

        final ImagePageAdapter adapter = new ImagePageAdapter(this, configs);
        final ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(adapter);
        title.setText(pager.getAdapter().getPageTitle(0));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(adapter.getPageTitle(position));
                View view = adapter.getItem(position);
                Log.d("AA", "view = " + view);
                if (view != null) {
                    ((ImagePageAdapter.VHItem)view.getTag()).sync();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private static class ImagePageAdapter extends ObjectPageAdaper {

        private class VHItem {
            AuoriCropImageView image;
            int resId;

            VHItem(View itemView) {
                this.image = (AuoriCropImageView) itemView.findViewById(R.id.image_view);
                itemView.setOnClickListener(clicker);
                itemView.setTag(this);
            }

            private void sync() {
                resId = DataPack.with(context).currentId();
                image.setImageResource(resId);
            }

            private void next() {
                resId = DataPack.with(context).nextId();
                image.setImageResource(resId);
            }
        }

        private final View.OnClickListener clicker = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((VHItem) view.getTag()).next();
            }
        };

        private final LayoutInflater inflater;

        private final Context context;

        private final Configuration[] configs;

        ImagePageAdapter(Context context, Configuration[] configs) {
            this.context = context;
            this.configs = configs;
            this.inflater = LayoutInflater.from(context);
        }

        public String getPageTitle(int position) {
            return configs[position].getConfiguration();
        }

        public View getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getCount() {
            return configs.length;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            VHItem holder;
            if (view == null) {
                view = inflater.inflate(R.layout.auori_cropimageview, parent, false);
                holder = new VHItem(view);
            } else {
                holder = (VHItem) view.getTag();
            }

            bindView(holder, position);
            return view;
        }

        private void bindView(final VHItem holder, int position) {
            Configuration config = configs[position];
            holder.resId = DataPack.with(context).currentId();
            holder.image.setImageResource(holder.resId);
            holder.image.setCropType(config.scaleCropType);
            holder.image.setCropAlignment(config.alignment);
        }

    }

    /**
     * A page adapter which works with a large data set by reusing views.
     */
    private abstract static class ObjectPageAdaper extends PagerAdapter {

        private static class RecycleItem {
            int position = -1;
            final View view;
            RecycleItem(View view) {
                this.view = view;
            }
        }

        // Views that can be reused.
        private final List<RecycleItem> recycler = new ArrayList<>();

        @Override
        public abstract int getCount();

        public abstract View getView(int position, View view, ViewGroup parent);

        @Override
        public Object instantiateItem(ViewGroup parent, int position) {
            RecycleItem item = recycler.isEmpty() ? null : recycler.remove(0);

            View view = (item != null) ? item.view : null;
            view = getView(position, view, parent);

            if (item == null) {
                item = new RecycleItem(view);
            }
            item.position = position;

            parent.addView(item.view);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            RecycleItem item = (RecycleItem) object;
            if (item != null) {
                item.position = -1;
                recycler.add(item);
                container.removeView(item.view);
            }
        }

        @Override
        public boolean isViewFromObject(View v, Object obj) {
            return v == ((RecycleItem) obj).view;
        }

        /**
         * Attempts to return the view based on position.
         * It can only return -x/+x of current position of the view pager.
         * @param position
         * @return view or null. If it returns null, it means its being recycled at the moment
         */
        public View getItem(int position) {
            for (RecycleItem item: recycler) {
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