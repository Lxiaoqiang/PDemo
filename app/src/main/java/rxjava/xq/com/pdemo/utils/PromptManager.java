package rxjava.xq.com.pdemo.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import xdemo.xq.com.xdemo.R;


/**
 * 提示信息的管理
 */

public class PromptManager {
    static AlertDialog alertDialog;
    private static Dialog loadingDialog;

    public static void showProgressDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        loadingDialog = new Dialog(context, R.style.LoadingDialogStyle);
        loadingDialog.setContentView(view);
        //Animation anim = AnimationUtils.loadAnimation(context, R.anim.anim_loading);
        //view.findViewById(R.id.iv).startAnimation(anim);
        //loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
    }

    public static void closeProgressDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 当判断当前手机没有网络时使用
     *
     * @param context
     */
    public static void showNoNetWork(final Context context) {
        if (alertDialog == null) {
            Builder builder = new Builder(context);
            builder.setTitle("大集客")
                    .setMessage("当前无网络")
                    .setPositiveButton("设置", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到系统的网络设置界面
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            context.startActivity(intent);
                        }
                    }).setNegativeButton("知道了", null);
            alertDialog = builder.create();
        }
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    /**
     * 退出系统
     *
     * @param context
     */
    public static void showExitSystem(Context context) {
        Builder builder = new Builder(context);
        builder
                //
                .setTitle(R.string.app_name)
                        //
                .setMessage("是否退出应用")
                .setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process
                                .myPid());
                        // 多个Activity——懒人听书：没有彻底退出应用
                        // 将所有用到的Activity都存起来，获取全部，干掉
                        // BaseActivity——onCreated——放到容器中
                    }
                })//
                .setNegativeButton("取消", null)//
                .show();

    }


}
