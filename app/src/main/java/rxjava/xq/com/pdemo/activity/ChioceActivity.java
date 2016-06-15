package rxjava.xq.com.pdemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rxjava.xq.com.pdemo.R;
import rxjava.xq.com.pdemo.entity.User;

public class ChioceActivity extends AppCompatActivity {

    @BindView(R.id.lv_choice)
    ListView lvv;
    @BindView(R.id.tv_show)
    TextView show;

    List<User> list = new ArrayList<>();

    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chioce);
        ButterKnife.bind(this);
        lvv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        initData();
    }


    private void initData() {
        for (int i = 0; i < 20; i++) {
            list.add(new User(i, "大毅" + i, i % 2 == 0 ? 1 : 2));
        }
        lvv.setAdapter(new ChoiceAdapter(list, this));
    }

    @OnClick(R.id.btn_choice)
    void showCheckItem() {
        if (lvv.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {
            int positon = lvv.getCheckedItemPosition();
            str += positon;
            show.setText(str);
        } else if (lvv.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
            //对应adapter需要重写hasStableIds并返回true才能拿到值
            long[] positons = lvv.getCheckedItemIds();
            for (int i = 0; i < positons.length; i++) {
                str += positons[i];
            }
            show.setText(str);
        }
    }

    class ChoiceAdapter extends BaseAdapter {
        private Context mContext;
        private List<User> list;

        public ChoiceAdapter(List<User> list, Context context) {
            this.list = list;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 返回true listview.getCheckedItemIds()才能拿到值
         *
         * @return
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_choice_layout, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            User u = list.get(position);
            holder.tv.setText(u.getName());

            return convertView;
        }

        class ViewHolder {
            TextView tv;
            CheckBox cb;

            public ViewHolder(View view) {
                tv = (TextView) view.findViewById(R.id.tv_choice);
                cb = (CheckBox) view.findViewById(R.id.cb_choice);
            }
        }
    }
}
