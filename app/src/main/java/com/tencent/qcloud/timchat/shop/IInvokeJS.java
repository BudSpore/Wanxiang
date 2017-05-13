package com.tencent.qcloud.timchat.shop;


import com.tencent.qcloud.timchat.annotation.InvokeJSInterface;
import com.tencent.qcloud.timchat.annotation.Param;
import com.tencent.qcloud.timchat.annotation.ParamCallback;

/**
 * Created by niuxiaowei on 16/8/28.
 */
public interface IInvokeJS {


    public static class City{
        @Param("cityName")
        public String cityName;

        @Param("cityProvince")
        public String cityProvince;

        public int cityId;


    }

    @InvokeJSInterface("exam")
    void exam(@Param("username") String testContent, @Param("id") String goodsId,@ParamCallback IJavaCallback2JS iJavaCallback2JS);
    @InvokeJSInterface("getPhoto")
    void getPhoto(@Param("photoName") String testContent, @ParamCallback IJavaCallback2JS iJavaCallback2JS);

    @InvokeJSInterface("setCookie")
    void setCookie(@Param("username") String testContent, @ParamCallback IJavaCallback2JS iJavaCallback2JS);

    @InvokeJSInterface("exam1")
    void exam1(@Param City city, @ParamCallback IJavaCallback2JS iJavaCallback2JS);

    @InvokeJSInterface("exam2")
    void exam2(@Param City city, @Param("contry") String contry, @ParamCallback IJavaCallback2JS iJavaCallback2JS);

    @InvokeJSInterface("exam3")
    void exam3(@Param(value = "city") City city, @Param("contry") String contry, @ParamCallback IJavaCallback2JS iJavaCallback2JS);

    @InvokeJSInterface("exam4")
    void exam4(@ParamCallback IJavaCallback2JS iJavaCallback2JS);
}
