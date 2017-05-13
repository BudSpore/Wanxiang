package com.tencent.qcloud.timchat.shop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tencent.qcloud.timchat.R;

/**
 * Created by 2-1Ping on 2017/5/7.
 */

public class UserDialog extends Dialog implements View.OnClickListener{

    int layoutRes;//布局文件
    Context context;
    Button usecamera;
    Button usephoto;
    Button back;
     LeaveMyDialogListener listener;

    public interface LeaveMyDialogListener{
         void onClick(View view);
    }

    public UserDialog(Context context) {
        super(context);
        this.context = context;
    }
    /**
     * 自定义布局的构造方法
     * @param context
     * @param resLayout
     */
    public UserDialog(Context context,int resLayout){
        super(context);
        this.context = context;
        this.layoutRes=resLayout;
    }
    /**
     * 自定义主题及布局的构造方法
     * @param context
     * @param theme
     * @param resLayout
     */
    public UserDialog(Context context, int theme,int resLayout,LeaveMyDialogListener leaveMyDialogListener){
        super(context, theme);
        this.context = context;
        this.layoutRes=resLayout;
        this.listener=leaveMyDialogListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutRes);
        usecamera = (Button)findViewById(R.id.usecamera);
        usephoto   = (Button)findViewById(R.id.usephoto);
        back = (Button)findViewById(R.id.back);
        usecamera.setOnClickListener(this);
        usephoto.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        listener.onClick(v);
    }


}
