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

    private FadedTextView mShow;

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
                } else {
                    right = false;
                    newPosition = position;
                }
                float showAlpha = right ? 1.0f - positionOffset : positionOffset;
                float hideAlpha = right ? positionOffset : 1.0f - positionOffset;
                mShow.setInAlpha(showAlpha);
                mShow.setOutAlpha(hideAlpha);
                if (newPosition < texts.size()) {
                    mShow.setOutText(texts.get(newPosition));
                }
            }

            @Override
            public void onPageSelected(int position) {
                mShow.setInText(texts.get(position));
                mShow.setInAlpha(1);
                mShow.setOutAlpha(0);
                mShow.setOutText("");

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mPager.setAdapter(adapter);
        mShow.setInText(texts.get(mPager.getCurrentItem()));
        int position = mPager.getCurrentItem() == texts.size() - 1 ? texts.size() : mPager.getCurrentItem() + 1;
        mShow.setOutText(texts.get(position));
        //        mShow.set(texts.get(position));
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
