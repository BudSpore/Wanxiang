package com.tencent.qcloud.timchat.shop;


import android.util.Log;

import com.tencent.qcloud.timchat.annotation.JavaInterface4JS;
import com.tencent.qcloud.timchat.annotation.Param;
import com.tencent.qcloud.timchat.annotation.ParamCallback;
import com.tencent.qcloud.timchat.annotation.ParamResponseStatus;

/**
 * java提供给js的接口
 * Created by niuxiaowei on 16/7/22.
 */
public class JavaInterfaces4JS {


    private GoodsMain mGoodsMain;

    public JavaInterfaces4JS(GoodsMain goodsMain) {
        mGoodsMain = goodsMain;
    }


    /**
     * 必须有无参构造函数
     */
    public static class Person {
        @Param("size")
        String size;
        @Param("goodsId")
        String goodsId;
        @Param("name")
        String name;
        @Param("friendIp")
        String friendIp;

        public Person() {
        }

        public Person(String size, String goodsId) {
            this.size = size;
            this.goodsId=goodsId;
        }
        public Person(String size) {
            this.size = size;
        }

    }

    /**
     * 发送响应状态的接口
     */
    public interface IResponseStatusCallback {
        void callbackResponse(@ParamResponseStatus ResponseStatus responseStatus);
    }

    public interface ITestJSCallback extends IResponseStatusCallback {
        void callback(@ParamResponseStatus ResponseStatus responseStatus, @Param("content") String content);
    }

    public interface ITest1JSCallback extends IResponseStatusCallback {
        void callback(@ParamResponseStatus ResponseStatus responseStatus, @Param Person person);
    }

//    @JavaInterface4JS("test")
//    public void test(@Param("msg") String msg, @ParamCallback ITestJSCallback jsCallback) {
//        mGoodsMain.setResult("js传递数据: " + msg);
//        jsCallback.callbackResponse(ResponseStatus.FAILED);
//    }

    @JavaInterface4JS("test")
    public void test(@Param Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {

        if (personInfo != null) {
            mGoodsMain.setResult("native的test接口被调用，js传递数据: " + "size=" + personInfo.size );

        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }
    @JavaInterface4JS("update")
    public void update(@Param Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {
        Log.d("接口","update");

        if (personInfo != null) {
            mGoodsMain.setResult("native的update接口被调用，js传递数据: " + "goodsId=" + personInfo.goodsId );
            mGoodsMain.getGoodsId(personInfo.goodsId);


        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }

    @JavaInterface4JS("upload")
    public void upload(@Param Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {
        Log.d("接口","update");

        if (personInfo != null) {
            mGoodsMain.setResult("native的update接口被调用，js传递数据: " + "goodsId=" + personInfo.goodsId );
            mGoodsMain.showdialog();


        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }

    @JavaInterface4JS("sendNameIp")
    public void sendNameIp(@Param Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {
        Log.d("接口","sendNameIp");

        if (personInfo != null) {
            mGoodsMain.setResult("native的sendNameIp接口被调用，js传递数据: " + "name=" + personInfo.name+"friendIp"+personInfo.friendIp );
            mGoodsMain.startchat(personInfo.name);

        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }

    @JavaInterface4JS("logout")
    public void logout(@Param Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {
        Log.d("接口","logout");

        if (personInfo != null) {
            mGoodsMain.logout();

        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }

    @JavaInterface4JS("get")
    public void get(@Param Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {
        Log.d("接口","get");

        if (personInfo != null) {
            mGoodsMain.setResult("native的update接口被调用，js传递数据: " + "goodsId=" + personInfo.goodsId );

        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }

    @JavaInterface4JS("skip")
    public void skip(@Param Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {
        Log.d("接口","skip");

        if (personInfo != null) {
            mGoodsMain.setResult("native的update接口被调用，js传递数据: " + "goodsId=" + personInfo.goodsId );
            Log.d("接口","update");

        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }

    @JavaInterface4JS("test1")
    public void test1(@Param Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {

        if (personInfo != null) {
            mGoodsMain.setResult("native的test1接口被调用，js传递数据: " + "name=" + personInfo.size);

        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }
    @JavaInterface4JS("AcSize")
    public void AcSize(@Param Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {

        if (personInfo != null) {
            mGoodsMain.setResult("native的AcSize接口被调用，js传递数据: " + "name=" + personInfo.size );

        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }


    @JavaInterface4JS("test2")
    public void test2(@Param(value = "person") Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {

        if (personInfo != null) {
            mGoodsMain.setResult("native的test2接口被调用，js传递数据: " + "name=" + personInfo.size);

        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }

    @JavaInterface4JS("test3")
    public void test3(@Param("jiguan") String jiguan, @Param(value = "person") Person personInfo, @ParamCallback ITest1JSCallback jsCallback) {

        if (personInfo != null) {
            mGoodsMain.setResult("native的test3接口被调用，js传递的数据: " + "jiguan=" + jiguan + " name=" + personInfo.size );

        }
        jsCallback.callback(ResponseStatus.OK, new Person("gzy"));
    }

    @JavaInterface4JS("test4")
    public void test3(@ParamCallback IResponseStatusCallback jsCallback) {

        mGoodsMain.setResult("native的test4无参接口被调用");

        jsCallback.callbackResponse(ResponseStatus.OK);
    }


}
