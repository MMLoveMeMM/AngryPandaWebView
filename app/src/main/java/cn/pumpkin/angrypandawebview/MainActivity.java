package cn.pumpkin.angrypandawebview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import cn.pumpkin.angrypandawebview.identity.ForeignParcel;
import cn.pumpkin.angrypandawebview.identity.IdentityParcel;
import cn.pumpkin.angrypandawebview.identity.IdentityRender;

import static cn.pumpkin.angrypandawebview.identity.IdentityRender.IDENTITY_CHINA;
import static cn.pumpkin.angrypandawebview.identity.IdentityRender.IDENTITY_FOREIGN;
import static cn.pumpkin.angrypandawebview.identity.IdentityRender.IDENTITY_KEY_CHINA;
import static cn.pumpkin.angrypandawebview.identity.IdentityRender.IDENTITY_KEY_FOREIGN;
import static cn.pumpkin.angrypandawebview.identity.IdentityRender.IDENTITY_KEY_TEST;
import static cn.pumpkin.angrypandawebview.identity.IdentityRender.IDENTITY_TEST;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private Button button;
    private Button button1;
    private Button button2;
    private Button button3;

    private IdentityRender mIdentityRender;

    private IdentityHandler mIdentityHandler = new IdentityHandler();

    private class IdentityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int what = msg.what;
            switch (what) {
                case IDENTITY_TEST:{
                    Bundle bundle = msg.getData();
                    IdentityParcel parcel = bundle.getParcelable(IDENTITY_KEY_TEST);
                    String identity = parcel.getAddress()+":"+parcel.getCardNo()+":" + parcel.getName();
                    callJS(identity);
                }
                break;
                case IDENTITY_FOREIGN: {
                    Bundle bundle = msg.getData();
                    ForeignParcel parcel = bundle.getParcelable(IDENTITY_KEY_FOREIGN);
                    String identity = parcel.getCardNo() + parcel.getName();
                    callJS(identity);
                }
                break;
                case IDENTITY_CHINA: {
                    Bundle bundle = msg.getData();
                    IdentityParcel parcel = bundle.getParcelable(IDENTITY_KEY_CHINA);
                    String identity = parcel.getCardNo() + parcel.getName();
                    callJS(identity);
                }
                break;
                default:
                    break;
            }


        }
    }

    public void callJS(String identity) {
        // String identity = "身份证号码,户籍地址,性别等";
        if (mWebView != null) {
            mWebView.evaluateJavascript("callShowJS('" + identity + "')", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Toast.makeText(MainActivity.this, "返回值" + value, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.web);

        mIdentityRender = new IdentityRender(getApplicationContext(), mIdentityHandler);

        WebSettings webSettings = mWebView.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/callJs.html");
        mWebView.addJavascriptInterface(new AndroidNative(this), "AndroidNative");

        /**
         * Android调用js那边的方法
         */
        button = (Button) findViewById(R.id.button);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 模拟假数据操作
                mIdentityRender.ReadIdentityTest();
                // 读取真实二代身份证,外国人永久居住证数据(不带指纹的)
                // mIdentityRender.ReadCardAllNoFinger();
                // 读取真实二代身份证,外国人永久居住证数据(带指纹的)
                // mIdentityRender.ReadCardAllWithFinger();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String identity = "身份证号码,户籍地址,性别等";
                mWebView.evaluateJavascript("callShowJS('" + identity + "')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Toast.makeText(MainActivity.this, "返回值" + value, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.evaluateJavascript("callSumJS(6,11)", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Toast.makeText(MainActivity.this, "返回值" + value, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 通过Handler发送消息
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {

                        // 注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法
                        mWebView.loadUrl("javascript:callJS()");
                    }
                });

            }
        });

        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        //设置响应js 的Alert()函数
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

        });

    }
}
