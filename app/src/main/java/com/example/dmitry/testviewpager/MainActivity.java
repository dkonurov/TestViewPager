package com.example.dmitry.testviewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static ArrayMap<Integer, String> sMap = new ArrayMap<>();

    static {
        sMap.put(R.drawable.first, "Первый текст");
        sMap.put(R.drawable.second, "Второй тескт уже меняем ненмого текст");
        sMap.put(R.drawable.third, "Третий текст тоже попробуем");
    }

    private ViewPager mPager;

    private TextView mShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Set<Map.Entry<Integer, String>> set = sMap.entrySet();
        List<Integer> ids = new ArrayList<>();
        final List<String> texts = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : set) {
            ids.add(entry.getKey());
            texts.add(entry.getValue());
        }
        mShow = findViewById(R.id.show);
        mShow.setTextSize(24);
        mPager = findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(this, ids);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int newPosition;
                boolean right = true;
                if (position == mPager.getCurrentItem()) {
                    newPosition = position + 1;
                    if (newPosition > texts.size() - 1) {
                        newPosition = texts.size() - 1;
                    }
                } else {
                    right = false;
                    newPosition = position;
                }

                float alpha = 0;
                if (positionOffset >= 0.5f) {
                    mShow.setText(right ? texts.get(newPosition) : texts.get(mPager.getCurrentItem()));
                    alpha = (positionOffset - 0.5f);
                } else {
                    mShow.setText(right ? texts.get(mPager.getCurrentItem()) : texts.get(newPosition));
                    alpha = 0.5f - positionOffset;
                }
                alpha *= 2;
                mShow.setAlpha(alpha);
            }

            @Override
            public void onPageSelected(int position) {
                mShow.setText(texts.get(position));
                mShow.setAlpha(1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mPager.setAdapter(adapter);
        mShow.setText(texts.get(mPager.getCurrentItem()));
        mShow.setAlpha(1);
    }

    public static class PagerAdapter extends android.support.v4.view.PagerAdapter {

        private final LayoutInflater mInflater;

        @NonNull
        private List<Integer> mIds = new ArrayList<>();

        public PagerAdapter(@NonNull Context context, @Nullable List<Integer> integers) {
            mInflater = LayoutInflater.from(context);
            if (integers != null) {
                mIds.addAll(integers);
            }
        }

        @Override
        public int getCount() {
            return mIds.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mInflater.inflate(R.layout.item_pager, container, false);
            ImageView imageView = view.findViewById(R.id.image);
            imageView.setImageResource(mIds.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
