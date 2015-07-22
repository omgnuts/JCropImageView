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
import com.jattcode.android.auori.demo.DataEngine.ImageItem;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static class Configuration {
        private final int scaleCropType;
        private final int alignment;
        private Configuration(int type, int align) {
            scaleCropType = type;
            alignment = align;
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

        final ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new ImagePageAdapter(this, new int[] { 1, 2 }));
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

        private class VHItem {
            ImageView image;
            int shape;
            int resId;

            VHItem(View itemView) {
                this.image = (ImageView) itemView.findViewById(R.id.image_view);
                itemView.setOnClickListener(clicker);
                itemView.setTag(this);
            }
        }

        private final View.OnClickListener clicker = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AA", "clicked");
                VHItem holder = (VHItem) view.getTag();
                holder.resId = DataPack.with(context).nextId(holder.shape);
                holder.image.setImageResource(holder.resId);
            }
        };

        private final LayoutInflater inflater;

        private final Context context;

        private final int[] shapes;

        ImagePageAdapter(Context context, int[] shapes) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.shapes = shapes;
        }

        public String getPageTitle(int position) {
            return "Some title";
        }

        @Override
        public int getCount() {
            return shapes.length;
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
            holder.shape = shapes[position];
            holder.resId = DataPack.with(context).currentId(holder.shape);
            holder.image.setImageResource(holder.resId);
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
