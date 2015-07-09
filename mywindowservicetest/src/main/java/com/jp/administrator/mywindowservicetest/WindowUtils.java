package com.jp.administrator.mywindowservicetest;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/7/7.
 */
public class WindowUtils {
    private static View mView = null;
    private static WindowManager mWindowManager = null;
    private static Boolean isShow = false;
    private static Button btnClear, btnSetting;
    private static Context mcontext;
    static Handler handler=null;
    private static Boolean issettingShow = false;

    public static void showPopupWindow(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        mcontext = context;
        if (isShow) {
            return;
        }
        isShow = true;
        mView = setUpView(context);
        final WindowManager.LayoutParams layoutParams = getLayoutParams();
        mWindowManager.addView(mView, layoutParams);

//        handler=new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (msg.what==1)
//                {
//                    System.out.println("==================>msg get");
//                    View v= addSettingLayout();
//                    WindowManager.LayoutParams params=new WindowManager.LayoutParams();
//                    params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//                    int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
//                    params.type= PixelFormat.TRANSLUCENT;
//                    params.flags = flags;
//                    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                    params.gravity = Gravity.CENTER;
//                    mWindowManager.addView(v,params);
//                    System.out.println("addSettingLayout()==================>msg get");
//
//                }
//
//            }
//        };
    }

    private static WindowManager.LayoutParams getLayoutParams() {
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        layoutParams.flags = flags;

        // 不设置这个弹出框的透明遮罩显示为黑
        layoutParams.format = PixelFormat.RGBA_8888;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }

    public static View setUpView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.mywindow, null);
        btnClear = (Button) view.findViewById(R.id.clearBtn);
        btnSetting = (Button) view.findViewById(R.id.settingBtn);
        final View popupWindowView = view.findViewById(R.id.popupWindow);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = new Rect();
                popupWindowView.getGlobalVisibleRect(rect);
                if (!rect.contains(x, y)) {
                    WindowUtils.hidePopupWindow();
                }
                return false;
            }
        });
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        WindowUtils.hidePopupWindow();
                        return true;
                    default:
                        return false;
                }

            }
        });
        buttonListener();
        return view;
    }

    private static void buttonListener() {

//清理内存的按钮
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("------------->");
                System.out.println( "==============可获得的内存："+MemoryClear.getAvailMemory(mcontext)+";总内存："+MemoryClear.getTotalMemory(mcontext));
                Animation animation= AnimationUtils.loadAnimation(mcontext,R.anim.tip);
                AccelerateInterpolator lin=new AccelerateInterpolator();
                animation.setInterpolator(lin);
                btnClear.setAnimation(animation);
                System.out.println("================清理内存的数量："+MemoryClear.clearCount(mcontext));
                Toast.makeText(mcontext,"清理内存的数量:"+MemoryClear.clearCount(mcontext),Toast.LENGTH_SHORT).show();
                System.out.println("=================清理完毕后：" + "可获得的内存：" + MemoryClear.getAvailMemory(mcontext) + ";总内存：" + MemoryClear.getTotalMemory(mcontext));
            }
        });
//设置按钮
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopService();
                System.out.println("setting------------->");
//                 new Thread(new Runnable() {
//                     @Override
//                     public void run() {
//                         Message msg=new Message();
//                         msg.what=1;
//                         handler.sendMessage(msg);
//                     }
//                 }).start();
                View mView=addSettingLayout();
                mWindowManager.addView(mView,getLayoutParams());
            }

        });

    }

    private static View addSettingLayout()
    {

        issettingShow = true;
        LayoutInflater inflater=LayoutInflater.from(mcontext);
        final View layout=inflater.inflate(R.layout.settinglayout,null);
        Switch BtnSwitch= (Switch) layout.findViewById(R.id.settingSwitch);
        final View settingWindowView = layout.findViewById(R.id.settingLayout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = new Rect();
                settingWindowView.getGlobalVisibleRect(rect);
                if (!rect.contains(x, y)) {
                    mWindowManager.removeView(layout);
                }
                return false;
            }
        });
        layout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        mWindowManager.removeView(layout);
                        return true;
                    default:
                        return false;
                }

            }
        });
        BtnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Toast.makeText(mcontext,"qqqqqq",Toast.LENGTH_SHORT).show();
                    System.out.println("_______>open");
                    stopService();
                    hidePopupWindow();
                    mWindowManager.removeView(layout);

                }else
                {
                    System.out.println("_______>close");
                }
            }
        });
        return layout;
    }

    private static void stopService() {
        Intent intent = new Intent(mcontext.getApplicationContext(), SuspensionService.class);
        mcontext.stopService(intent);

    }
    private static void startService() {
        Intent intent = new Intent(mcontext.getApplicationContext(), SuspensionService.class);
        mcontext.startService(intent);

    }
    public static void hidePopupWindow() {
        if (isShow && null != mView) {
            mWindowManager.removeView(mView);
            isShow = false;
        }
    }

}
