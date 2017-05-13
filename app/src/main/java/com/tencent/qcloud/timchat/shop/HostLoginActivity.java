package com.tencent.qcloud.timchat.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.qcloud.timchat.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HostLoginActivity extends Activity {
    private Button login;
    private EditText username;
    private EditText password;
    private TextView register;
    String name;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_login);
        register= (TextView) findViewById(R.id.registerNewUser);
        login= (Button) findViewById(R.id.btn_login);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=username.getText().toString();
                pass=password.getText().toString();
                if(name!=null&&pass!=null){
                    httpclientClick(name,pass);//呵这个用HttpClient居然能成功

                }
                else
                    Toast.makeText(HostLoginActivity.this,"错误:用户名或密码为空", Toast.LENGTH_LONG).show();



            }
        });
        // 设置点击"注册新用户"事件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HostLoginActivity.this, HostRegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
//                finish();
            }
        });
    }
    public void httpclientClick(final String username, final String userpassword){
        new Thread(new Runnable() {
            @Override

            public void run() {
                Log.d("1","1");
                HttpClient httpClient=new DefaultHttpClient();
                Log.d("2","2");

                HttpPost httpPost=new HttpPost("http://123.207.32.211:8099/CUM/php/login.php");
                Log.d("3","3");

                HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
                Log.d("4","4");

                List<NameValuePair> params=new ArrayList<NameValuePair>();
                Log.d("5","5");

                params.add(new BasicNameValuePair("username",username));
                Log.d("6","6");

                params.add(new BasicNameValuePair("userpassword",userpassword));
                Log.d("7","7");

                Log.d("8","8");
                try {
                    UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
                    Log.d("9","9");

                    httpPost.setEntity(entity);
                    Log.d("10","10");

                    httpClient.execute(httpPost);
                    Log.d("11","11");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.d("12","12");
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    Log.d("13","13");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("14","14");
                }
                try {
                    HttpResponse httpResponse=httpClient.execute(httpPost);
                    Log.d("91","91");
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        Log.d("92","92");
                        final String response= EntityUtils.toString(entity,"utf-8");
                        Log.d("返回值",response);
                        if(response.indexOf("<META HTTP-EQUIV=\"refresh\"")!=-1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("success","成功");
                                    Intent intent=new Intent(HostLoginActivity.this,GoodsMain.class);
//                                    Intent intent=new Intent(HostLoginActivity.this,HomeActivity.class);
                                    intent.putExtra("username",username);
                                    startActivity(intent);
                                    finish();
                                }
                            });


                        }
                        else{
Log.d("failed","失败");                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
