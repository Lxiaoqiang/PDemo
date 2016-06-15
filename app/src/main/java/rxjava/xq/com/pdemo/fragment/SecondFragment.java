package rxjava.xq.com.pdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.waynell.videolist.visibility.calculator.SingleListViewItemActiveCalculator;
import com.waynell.videolist.visibility.items.ListItem;
import com.waynell.videolist.visibility.scroll.ItemsProvider;
import com.waynell.videolist.visibility.scroll.RecyclerViewItemPositionGetter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rxjava.xq.com.pdemo.R;
import rxjava.xq.com.pdemo.video.VideoViewHolder;
import rxjava.xq.com.pdemo.video.model.VideoItem;
import rxjava.xq.com.pdemo.view.MyRecycleView;

/**
 * Created by lhq on 2016/6/13.
 */
public class SecondFragment extends Fragment{

    private final String TAG = SecondFragment.class.getSimpleName();
    @BindView(R.id.recycler_view)
    MyRecycleView mRecyclerView;

    private int mScrollState = 0;

    private SingleListViewItemActiveCalculator mCalculator;

    private static final String url = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4";

    private static final String url2 = "http://techslides.com/demos/sample-videos/small.mp4";

    private static final String url3 = "http://download.wavetlan.com/SVV/Media/HTTP/H264/Other_Media/H264_test7_voiceclip_mp4_480x360.mp4";

    private static final String url4 = "http://download.wavetlan.com/SVV/Media/HTTP/MP4/ConvertedFiles/Media-Convert/Unsupported/test7.mp4";
    private static final String purl1 = "http://img10.3lian.com/sc6/show02/67/27/03.jpg";
    private static final String purl2 = "http://img10.3lian.com/sc6/show02/67/27/04.jpg";
    private static final String purl3 = "http://img10.3lian.com/sc6/show02/67/27/01.jpg";
    private static final String purl4 = "http://img10.3lian.com/sc6/show02/67/27/02.jpg";

    private String url10 = "http://video.uustudy.com.cn/11";
    private String url110 = "http://video.uustudy.com.cn/12";
    private String url1110 = "http://video.uustudy.com.cn/14";
    private String url11110 = "http://video.uustudy.com.cn/11";
    private String img = "http://video.uustudy.com.cn/11?vframe/jpg/offset/1/w/300/h/600";
    private String img1 = "http://video.uustudy.com.cn/12?vframe/jpg/offset/1/w/300/h/600";
    private String img2 = "http://video.uustudy.com.cn/14?vframe/jpg/offset/1/w/300/h/600";

    private List<VideoItem> mListItems = new ArrayList<>();
    VideoListAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_layout, null);
        ButterKnife.bind(this,view);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new VideoListAdapter();
        mCalculator = new SingleListViewItemActiveCalculator(adapter,new RecyclerViewItemPositionGetter(layoutManager, mRecyclerView));

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mScrollState = newState;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && adapter.getItemCount() > 0) {
                    mCalculator.onScrollStateIdle();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mCalculator.onScrolled(mScrollState);
            }
        });
        return view;
    }




    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            adapter.getHolder().videoView.stop();
        } else {
            adapter.getHolder().videoView.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mListItems.isEmpty()) {
            // need to call this method from list view handler in order to have filled list
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mCalculator.onScrollStateIdle();
                }
            });
        }
    }


    private class VideoListAdapter extends RecyclerView.Adapter<VideoViewHolder> implements ItemsProvider {


        public VideoListAdapter() {
            generateMockData();
        }

        private void generateMockData() {
            mListItems.add(new VideoItem(url10, img));
            mListItems.add(new VideoItem(url110, img1));
            mListItems.add(new VideoItem(url1110, img2));
            mListItems.add(new VideoItem(url11110, img1));
            mListItems.add(new VideoItem(url10, img));
            mListItems.add(new VideoItem(url1110, img2));
        }

        VideoViewHolder videoViewHolder;

        public VideoViewHolder getHolder() {
            return videoViewHolder;
        }

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            videoViewHolder = new VideoViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false));
            return videoViewHolder;
        }

        public VideoItem getItem(int position) {
            return mListItems.get(position);
        }

        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {
            holder.bind(position, getItem(position));
        }

        @Override
        public int getItemCount() {
            return mListItems.size();
        }

        @Override
        public ListItem getListItem(int position) {
            RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(position);
            if (holder instanceof ListItem) {
                return (ListItem) holder;
            }
            return null;
        }

        @Override
        public int listItemSize() {
            return getItemCount();
        }
    }
}
