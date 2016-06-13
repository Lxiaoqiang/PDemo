package rxjava.xq.com.pdemo.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxjava.xq.com.pdemo.R;
import rxjava.xq.com.pdemo.fragment.HomeFragment;
import rxjava.xq.com.pdemo.fragment.SecondFragment;
import rxjava.xq.com.pdemo.fragment.ThirdFragment;
import rxjava.xq.com.pdemo.view.ImageViewWithTextView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.viewpager)
    ViewPager pager;
    @BindView(R.id.indicator_one)
    ImageViewWithTextView one;
    @BindView(R.id.indicator_two)
    ImageViewWithTextView two;
    @BindView(R.id.indicator_three)
    ImageViewWithTextView three;



    private List<Fragment> fragmentList = new ArrayList<>();
    private List<ImageViewWithTextView> tabList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tabList.add(one);
        tabList.add(two);
        tabList.add(three);
        fragmentList.add(new HomeFragment());
        fragmentList.add(new SecondFragment());
        fragmentList.add(new ThirdFragment());
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    tabList.get(position).setIconAlpha(1 - positionOffset);
                    tabList.get(position + 1).setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        resetTab();
        one.setIconAlpha(1.0f);
    }
    @OnClick(R.id.indicator_one)
    void clickOne(){
        tabList.get(0).setIconAlpha(1.0f);
        pager.setCurrentItem(0,false);
    }
    @OnClick(R.id.indicator_two)
    void clickTwo(){
        tabList.get(1).setIconAlpha(1.0f);
        pager.setCurrentItem(1,false);
    }
    @OnClick(R.id.indicator_three)
    void clickThree(){
        tabList.get(2).setIconAlpha(1.0f);
        pager.setCurrentItem(2,false);//第二个参数false，取消viewpager的滑动动画
    }
    private void resetTab() {
        for (ImageViewWithTextView im : tabList) {
            im.setIconAlpha(0);
        }
    }
    class TabPagerAdapter extends FragmentPagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

}
