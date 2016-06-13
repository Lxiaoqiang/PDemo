package rxjava.xq.com.pdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rxjava.xq.com.pdemo.R;
import rxjava.xq.com.pdemo.utils.L;

/**
 * Created by lhq on 2016/6/13.
 */
public class SecondFragment extends Fragment{

    private final String TAG = SecondFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_layout,null);

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
}
