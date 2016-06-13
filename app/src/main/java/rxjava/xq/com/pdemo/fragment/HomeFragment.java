package rxjava.xq.com.pdemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rxjava.xq.com.pdemo.R;
import rxjava.xq.com.pdemo.activity.RecordActivity;
import rxjava.xq.com.pdemo.activity.RxJavaTestActivity;
import rxjava.xq.com.pdemo.utils.L;

/**
 * Created by lhq on 2016/6/13.
 */
public class HomeFragment extends Fragment{

    private final String TAG = HomeFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout,null);
        ButterKnife.bind(this,view);
        L.i(TAG,"oncreateView");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        L.i(TAG,"onAttach---context");
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        L.i(TAG,"onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        L.i(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        L.i(TAG,"onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        L.i(TAG,"onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        L.i(TAG,"onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        L.i(TAG,"onResume");
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        L.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        L.i(TAG,"onHiddenChanged");
        super.onHiddenChanged(hidden);
    }

    @OnClick(R.id.btn_record_time)
    void goRecordActivity(){
        startActivity(new Intent(getActivity(),RecordActivity.class));
    }
    @OnClick(R.id.btn_test_rxjava)
    void goTestRxJavaActivity(){
        startActivity(new Intent(getActivity(),RxJavaTestActivity.class));
    }
}
