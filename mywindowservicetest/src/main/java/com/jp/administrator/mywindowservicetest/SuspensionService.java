package com.jp.administrator.mywindowservicetest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * Created by Administrator on 2015/7/7.
 */
public class SuspensionService extends Service {
   Context context=null;
    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;
   public static Button mFloatView;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        System.out.println("---->start first successful!");
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
       // 设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
//        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
          wmParams.gravity=Gravity.CENTER|Gravity.LEFT;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
//        wmParams.x = 0;
//        wmParams.y =0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        mFloatLayout= (LinearLayout) inflater.inflate(R.layout.myfloatwindow,null);
        mWindowManager.addView(mFloatLayout, wmParams);

        mFloatView = (Button)mFloatLayout.findViewById(R.id.btn);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        //设置监听浮动窗口的触摸移动
//        mFloatView.setOnTouchListener(new View.OnTouchListener()
//        {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                // TODO Auto-generated method stub
//                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
////                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth()/2;
//                wmParams.x = (int) event.getRawX()-mFloatView.getWidth()/2+50;
//                //减25为状态栏的高度
////                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight()/2-25;
//                  wmParams.y = (int) event.getRawY()- mFloatView.getHeight()/2+50;
//
//                //刷新
//                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
//                return false;  //此处必须返回false，否则OnClickListener获取不到监听
//            }
//        });

        mFloatView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                WindowUtils.showPopupWindow(context);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), " start successful!", Toast.LENGTH_SHORT).show();
        System.out.println("----------->onStartCommand successful!");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        System.out.println("----------->service Destroy");
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }
}
