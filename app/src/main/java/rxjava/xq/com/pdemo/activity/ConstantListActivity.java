package rxjava.xq.com.pdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rxjava.xq.com.pdemo.R;
import rxjava.xq.com.pdemo.constant.Constant;
import rxjava.xq.com.pdemo.utils.KeyBoardUtils;
import rxjava.xq.com.pdemo.view.sortlistview.CharacterParser;
import rxjava.xq.com.pdemo.view.sortlistview.PinyinComparator;
import rxjava.xq.com.pdemo.view.sortlistview.SideBar;
import rxjava.xq.com.pdemo.view.sortlistview.SortAdapter;
import rxjava.xq.com.pdemo.view.sortlistview.SortModel;

public class ConstantListActivity extends AppCompatActivity {

    @BindView(R.id.lv_constant)
    ListView listView;

    @BindView(R.id.et_clear)
    EditText input;

    @BindView(R.id.sidebar)
    SideBar sideBar;


    SortAdapter sortAdapter;
    List<SortModel> modelList;

    CharacterParser parser;
    PinyinComparator comparator;

    String[] constant = new String[]{"小明","阿旺","亮哥","小子",
            "陈敏","嘟嘟","·哈哈","aaa","json","c",
            "关羽","赵兴","刘邦","jake","oc","c#","java",
            "张飞","小傻逼","大傻逼","吕布","貂蝉","诸葛亮","刘伯温",};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constant_list);
        ButterKnife.bind(this);
        parser = CharacterParser.getInstance();
        comparator = new PinyinComparator();
        fillData();
        Collections.sort(modelList,comparator);
        sortAdapter = new SortAdapter(this,modelList);
        listView.setAdapter(sortAdapter);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                listView.setSelection(sortAdapter.getPositionForSection(s.charAt(0)));
            }
        });


    }

    private void fillData(){
        modelList = new ArrayList<>();
        String pinyin;
        String sortString;
        for (int i = 0; i < constant.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(constant[i]);
            // 汉字转换成拼音
            pinyin = parser.getSelling(constant[i]);
            sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            modelList.add(sortModel);
        }
    }


}
