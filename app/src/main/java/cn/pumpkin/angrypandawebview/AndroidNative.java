package cn.pumpkin.angrypandawebview;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * @author: zhibao.Liu
 * @version:
 * @date: 2019/3/27 12:08
 * @des: js 调用android这边的接口方法
 * @see {@link }
 */

public class AndroidNative {

    private static final String TAG = AndroidNative.class.getName();
    private Context mContext;
    public AndroidNative(Context ctx){
        mContext = ctx;
    }
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
        Log.d(TAG,"JS调用了Android的hello方法 : "+msg);
        Toast.makeText(mContext,"msg : "+msg,Toast.LENGTH_SHORT).show();
    }

}
