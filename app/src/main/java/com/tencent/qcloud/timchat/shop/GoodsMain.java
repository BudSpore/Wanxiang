package com.tencent.qcloud.timchat.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.timchat.Luban.Luban;
import com.tencent.qcloud.timchat.Luban.OnCompressListener;
import com.tencent.qcloud.timchat.MyApplication;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.annotation.JavaCallback4JS;
import com.tencent.qcloud.timchat.annotation.Param;
import com.tencent.qcloud.timchat.annotation.ParamResponseStatus;
import com.tencent.qcloud.timchat.model.FriendshipInfo;
import com.tencent.qcloud.timchat.model.GroupInfo;
import com.tencent.qcloud.timchat.model.UserInfo;
import com.tencent.qcloud.timchat.ui.ChatActivity;
import com.tencent.qcloud.timchat.ui.HomeActivity;
import com.tencent.qcloud.timchat.ui.SplashActivity;
import com.tencent.qcloud.timchat.utils.FileUtil;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by 2-1Ping on 2017/2/17.
 */

public class GoodsMain extends Activity {
    WebView mWebView;
    private SimpleJavaJsBridge mSimpleJavaJsBridge;
    String username;
    String ip;
    String goodsId;
    String postname="";
    boolean first=true;
    private static final int IMAGE_STORE = 200;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_TAKE_PHOTO = 4;
    private static final int REQUEST_CODE_CROUP_PHOTO = 3;
    private static final int  REQUEST_CODE_IMAGE=5;

    private static final int IMAGE_PREVIEW = 400;


    private Uri fileUri;
    private File file;
    UserDialog dialog;
    String imagename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsmain);

        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>(){
            @Override
            public void onError(int code, String desc){
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                Log.e("userName", "getSelfProfile failed: " + code + " desc");
            }

            @Override
            public void onSuccess(TIMUserProfile result){
                Log.e("我想要的用户名呢?", "getSelfProfile succ");
                Log.e("我想要的用户名呢", "identifier: " + result.getIdentifier() + " nickName: " + result.getNickName()
                        + " remark: " + result.getRemark() + " allow: " + result.getAllowType());
                username=result.getIdentifier();
                initview();
            }
        });


    }


    private void initfile(){
        String str=null;
        Date date=null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        date =new Date();
        str=format.format(date);
        imagename = str+".jpg";
        Log.d("什么鬼",imagename);
            file = new File(FileUtil.getCachePath(this), imagename);
            if (Build.VERSION.SDK_INT < 24) {
                fileUri = Uri.fromFile(file);
            }

    }

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(GoodsMain.this,"空的",Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };

    public void initview(){
        ip=getIPAddress(getApplicationContext());
        Log.d("userId",ip);

        mWebView= (WebView) findViewById(R.id.webview1);
        initWebView();
        initData();
//        mWebView.loadUrl("http://123.207.32.211:8099/shop/goodsMain.html");//搜索页
        mWebView.loadUrl("http://123.207.32.211:8099/shop/index.html");//主页
    }

    public void getGoodsId(String goodsId){
        this.goodsId=goodsId;
        Log.d("传过来的goodsId",goodsId);

        updateMysql(ip,username,goodsId);


    }
    public void initWebView(){
        mWebView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                return super.shouldOverrideUrlLoading(view, url);

            }


            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.d("error",description);
                Log.d("error",failingUrl);

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("网页开始加载了","网页开始加载了");
                if(first){
                    IInvokeJS invokeJS = mSimpleJavaJsBridge.createInvokJSCommand(IInvokeJS.class);
                    invokeJS.setCookie(username,new IJavaCallback2JS() {
                        @JavaCallback4JS
                        public void test(@ParamResponseStatus("msg")String statusMsg, @Param("msg") String msg) {
                        }

                    });
                }


            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                Log.d("网页加载完毕","网页加载完毕");

                if(goodsId!=null){

                    updatetohtml(postname);
                    Log.d("updatetohtml","我把用户名发过去啦");
                    Log.d("最后一步传的是什么",postname);
                    Log.d("最后一步传的是什么",goodsId);
                    postname=null;
                    goodsId=null;
                    first=false;

                }




            }

        });


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setBuiltInZoomControls(false);
        settings.setBlockNetworkImage(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setAppCacheEnabled(true);
        settings.setSaveFormData(true);
        settings.setDatabaseEnabled(true);

        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();

        settings.setPluginState(WebSettings.PluginState.ON);

        settings.setGeolocationDatabasePath(dir);

        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @SuppressWarnings("all")

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {

                super.onReceivedIcon(view, icon);

            }


            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);

            }


        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            if(mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
                first=false;
                postname=null;
                goodsId=null;
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    public void showdialog(){
        dialog=new UserDialog(this, R.style.userdialog, R.layout.userdialog,
                new UserDialog.LeaveMyDialogListener() {
                    @Override
                    public void onClick(View view) {
                        switch(view.getId()){
                            case R.id.usecamera:
                                dialog.dismiss();
                                postphoto();

                                //试一试第二种方式
                                break;
                            case R.id.usephoto:
                                dialog.dismiss();
                                postimage();

                                break;
                            case R.id.back:
                                dialog.dismiss();
                                break;


                            default:
                                break;
                        }
                    }
                });

        dialog.show();



    }

    public void postphoto() {
        initfile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    public void postimage() {
        initfile();
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_IMAGE);
    }

    private void compressWithLs(File file, String name1 ){

//        Luban.get(this)
//                .load(file)
//                .putGear(Luban.THIRD_GEAR)
//                .setFilename(name1.substring(0,name1.length()-4))
//                .setCompressListener(new OnCompressListener() {
//                    @Override
//                    public void onStart() {
//                    }
//
//                    @Override
//                    public void onSuccess(File file) {
//                        Log.i("path", file.getAbsolutePath());
//                        Log.d("success","compress success!!!!!!!!!!!!!");
//                        try {
//                            Toast.makeText(GoodsMain.this,"压缩成功",Toast.LENGTH_LONG).show();
//
////                            uploadFile(file.getAbsolutePath());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//                }).launch();


        try {
            uploadFile(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != -1) {
            return;
        }
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            Uri newUri;
            if (Build.VERSION.SDK_INT < 24) {
                newUri = Uri.parse("file:///" +FileUtil.getFilePath(this, data.getData()));
            } else {
                newUri = data.getData();
            }
            if (newUri != null) {
                startcrop(newUri);
            } else {
                Toast.makeText(this, "没有得到相册图片", Toast.LENGTH_LONG).show();
            }
        }


//                if (resultCode == RESULT_OK) {
//                    boolean isOri = data.getBooleanExtra("isOri", false);
//                    String path = data.getStringExtra("path");
//                    File file = new File(path);
//                    if (file.exists() && file.length() > 0) {
//                        if (file.length() > 1024 * 1024 * 10) {
//                            Toast.makeText(this, getString(R.string.chat_file_too_large), Toast.LENGTH_SHORT).show();
//                        } else {
//
//
////                        Message message = new ImageMessage(path,isOri);
////                        presenter.sendMessage(message.getMessage());
//                        }
//                    } else {
//                        Toast.makeText(this, getString(R.string.chat_file_not_exist), Toast.LENGTH_SHORT).show();
//                    }
//                }



        else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            startcrop(fileUri);
        }
        else if (requestCode == REQUEST_CODE_CROUP_PHOTO) {

            compressWithLs(file,imagename);
        }

    }

    private void uploadFile(final String uploadfile) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                httpclient.getParams().setParameter(
                        CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                HttpPost httppost = new HttpPost("http://123.207.32.211:8099/shop/upload.php");

                MultipartEntity entity = new MultipartEntity();

                File file = new File(uploadfile);
                FileBody fileBody = new FileBody(file);
                entity.addPart("file", fileBody);

                httppost.setEntity(entity);
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httppost);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    try {
                        Log.d("response", EntityUtils.toString(resEntity));
                        Log.d("我到了upload.php这一步了", "我到了upload.php这一步了");

                        updateimage(imagename);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("response", "error拉拉拉拉拉拉拉拉拉");
                }
            }
        }).start();
    }

    private void startcrop(Uri fileUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(fileUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
//        intent.putExtra("outputX", 400);//图片输出大小
//        intent.putExtra("outputY", 400);
        intent.putExtra("output", Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);

    }


    public void initData() {
        JavaInterfaces4JS javaInterfaces4JS = new JavaInterfaces4JS(this);
        mSimpleJavaJsBridge = new SimpleJavaJsBridge.Builder().addJavaInterface4JS(javaInterfaces4JS)
                .setWebView(mWebView)
                .setJSMethodName4Java("_JSNativeBridge._handleMessageFromNative")
                .setProtocol("niu","receive_msg").create();
    }

    /**
     * js调用native函数
     * @param result
     */
    public void setResult(String result){
        if(result != null){
//            Toast.makeText(GoodsMain.this,result,Toast.LENGTH_LONG).show();
        }
    }

    private String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }
    private String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    public void updateMysql(final String userId, final String userName, final String goodsId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://123.207.32.211:8099/shop/php/browseCloth.php");
                HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                Log.d("data",userName);
                params.add(new BasicNameValuePair("userId",userId));
                params.add(new BasicNameValuePair("userName",userName));//真坑，以后前后两个字符串都写一样的吧！
                params.add(new BasicNameValuePair("goodsId",goodsId));
                Log.d("为什么别的衣服没有",userId);
                Log.d("为什么别的衣服没有",userName);
                Log.d("为什么别的衣服没有",goodsId);
                try {
                    UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
                    httpPost.setEntity(entity);
                    httpClient.execute(httpPost);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    HttpResponse httpResponse=httpClient.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        final String response= EntityUtils.toString(entity,"utf-8");
                        Log.d("返回值",response);
                    }
                    getMysql(goodsId);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void getMysql(final String goodsId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://123.207.32.211:8099/shop/php/AcBroUser.php");
                HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("goodsId",goodsId));
                try {
                    UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
                    httpPost.setEntity(entity);
                    httpClient.execute(httpPost);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    HttpResponse httpResponse=httpClient.execute(httpPost);
                    Log.d("91","91");
                    if(httpResponse.getStatusLine().getStatusCode()==200) {
                        HttpEntity entity = httpResponse.getEntity();
                        Log.d("92", "92");
                        final String response = EntityUtils.toString(entity, "utf-8");
                        if (response == null) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                        else{
                        Log.d("浏览该商品的有",response);
                        postname=response;
                        String[] s = response.split("[,]");
                        for (int i = 0; i < s.length; i++) {
                            Log.d("用户名在这里呢", s[i]);


                        }
//                        if(postname!=null)
//                            postname = response.substring(0, response.length() - 2);

                    }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void updatetohtml(final String username){
        Log.d("mooooooooo",postname);

        IInvokeJS invokeJS = mSimpleJavaJsBridge.createInvokJSCommand(IInvokeJS.class);
        invokeJS.exam(username,goodsId,new IJavaCallback2JS() {
            @JavaCallback4JS
            public void test(@ParamResponseStatus("msg")String statusMsg, @Param("msg") String msg) {
            }

        });

    }

    public void updateimage(final String filename){

        Log.d("上传图片，调用你的getPhoto",username);
        Log.d("上传图片，调用你的getPhoto",filename);

        IInvokeJS invokeJS = mSimpleJavaJsBridge.createInvokJSCommand(IInvokeJS.class);
        invokeJS.getPhoto(filename,new IJavaCallback2JS() {
            @JavaCallback4JS
            public void test(@ParamResponseStatus("msg")String statusMsg, @Param("msg") String msg) {
                uploadlast(username,imagename);
            }

        });

    }
    public void uploadlast(final String username, final String filename){
        Log.d("最后一步了","最后一步了");
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://123.207.32.211:8099/shop/php/person.php");

                List<NameValuePair> params=new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username",username));
                params.add(new BasicNameValuePair("head",filename));
                try {
                    UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
                    httpPost.setEntity(entity);
                    httpClient.execute(httpPost);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    HttpResponse httpResponse=httpClient.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        Log.d("data","成功了");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void startchat(String name){
        ChatActivity.navToChat(this,name, TIMConversationType.C2C);
    }

    public void logout(){
        LoginBusiness.logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                    Toast.makeText(GoodsMain.this, getResources().getString(R.string.setting_logout_fail), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess() {
                    TlsBusiness.logout(UserInfo.getInstance().getId());
                    UserInfo.getInstance().setId(null);
                    MessageEvent.getInstance().clear();
                    FriendshipInfo.getInstance().clear();
                    GroupInfo.getInstance().clear();
                    Intent intent = new Intent(GoodsMain.this,SplashActivity.class);
                    finish();
                    startActivity(intent);


            }
        });
    }




}
