package rxjava.xq.com.pdemo.view.deletelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rxjava.xq.com.pdemo.R;


/**
 * Created by lhq on 2016/3/31.
 */
public class SlideAdapter extends BaseAdapter {

    private SlideView mLastSlideViewWithStatusOn;
    private LayoutInflater mInflater;
    private List<MessageItem> mMessageItems = new ArrayList<>();
    private Context mContext;
    public SlideAdapter(Context context,List<MessageItem> list) {
        super();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mMessageItems = list;
    }

    @Override
    public int getCount() {
        return mMessageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SlideView slideView = (SlideView) convertView;
        if (slideView == null) {
            View itemView = mInflater.inflate(R.layout.item_listview_delete, null);

            slideView = new SlideView(mContext);
            slideView.setContentView(itemView);

            holder = new ViewHolder(slideView);
            slideView.setOnSlideListener(new SlideView.OnSlideListener() {
                @Override
                public void onSlide(View view, int status) {
                    if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
                        mLastSlideViewWithStatusOn.shrink();
                    }

                    if (status == SLIDE_STATUS_ON) {
                        mLastSlideViewWithStatusOn = (SlideView) view;
                    }
                }
            });
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        MessageItem item = mMessageItems.get(position);
        item.slideView = slideView;
        item.slideView.shrink();

        holder.icon.setImageResource(item.iconRes);
        holder.title.setText(item.title);
        holder.msg.setText(item.msg);
        holder.time.setText(item.time);
        holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageItems.remove(position);
                SlideAdapter.this.notifyDataSetChanged();
            }
        });

        return slideView;
    }
    class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView msg;
        public TextView time;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.icon);
            title = (TextView) view.findViewById(R.id.title);
            msg = (TextView) view.findViewById(R.id.msg);
            time = (TextView) view.findViewById(R.id.time);
            deleteHolder = (ViewGroup)view.findViewById(R.id.holder);
        }
    }
}

