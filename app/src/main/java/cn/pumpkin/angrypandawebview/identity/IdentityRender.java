package cn.pumpkin.angrypandawebview.identity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;

import com.example.mtreader.NativeFunc;
import com.ivsign.android.IDCReader.IDCReaderSDK;

import cn.pumpkin.angrypandawebview.utils.ToolFun;

public class IdentityRender {

    private static final String TAG = IdentityRender.class.getName();

    public static final int IDENTITY_TEST = 0x09;
    public static final int IDENTITY_FOREIGN = 0x10;
    public static final int IDENTITY_CHINA = 0x11;

    public static final String IDENTITY_KEY_FOREIGN = "foreign";
    public static final String IDENTITY_KEY_CHINA = "china";
    public static final String IDENTITY_KEY_TEST = "test";


    private Context mContext;
    private Handler mHandler;
    private HashMap<String, byte[]> IDCMsg, FRIDCMsg;

    public IdentityRender(Context ctx, Handler handler) {
        mContext = ctx;
        mHandler = handler;
    }

    public void ReadIdentityTest(){
        IdentityParcel mIdentityParcel = new IdentityParcel();
        mIdentityParcel.setName("中国");
        mIdentityParcel.setSex("男");
        mIdentityParcel.setNation("汉");
        mIdentityParcel.setBirthday("1990-01-02");
        mIdentityParcel.setAddress("深圳福田");
        mIdentityParcel.setCardNo("1234567890");
        mIdentityParcel.setGrantDept("福田公安局");
        mIdentityParcel.setUserLifeBegin("2011-02-03");
        mIdentityParcel.setUserLifeEnd("2099-01-01");
        Message msg = new Message();
        msg.what = IDENTITY_TEST;
        Bundle bundle = new Bundle();
        bundle.putParcelable(IDENTITY_KEY_TEST, mIdentityParcel);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    public void ReadCardAllNoFinger() {
        int[] idcMsgLen = new int[2];
        byte[] idcMsg = new byte[4096];
        String StrBmpFilePath;
        long mStartTime = System.currentTimeMillis();
        long mendTime = 0;
        int isReadFinger = 0; //不读指纹
        int ret = NativeFunc.mt8IDCardReadAll(idcMsgLen, idcMsg, isReadFinger);
        if (ret == 0) {
            mendTime = System.currentTimeMillis();
            Log("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", 1);
            //显示photo
            byte[] wltData = null;
            try {
                wltData = AnysizeIDCMsg.CombinationWltData(idcMsgLen, idcMsg, isReadFinger);
            } catch (Exception ex) {
                Log("组合wlt数据失败,errMsg:\n" + ex.getMessage(), 0);
                return;
            }
            if (IDCReaderSDK.GetLoadSoState()) {
                ToolFun.initLicData(mContext, Environment.getExternalStorageDirectory() + "/wltlib");
                int st = IDCReaderSDK.Init();
                if (st == 0) {
                    int t = IDCReaderSDK.unpack(wltData, AnysizeIDCMsg.byLicData);
                    if (t == 1) {
                        StrBmpFilePath = Environment.getExternalStorageDirectory() + "/wltlib/zp.bmp";
                        FileInputStream fis;
                        try {
                            fis = new FileInputStream(StrBmpFilePath);

                            //Bitmap bmp = BitmapFactory.decodeStream(fis);
                            fis.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Log(StrBmpFilePath, 2); // 显示照片
                    } else {
                        Log("身份证照片解码库解码失败,ret = " + t, 0);
                    }
                } else {
                    Log("身份证照片解码库初始化失�?,请检�?< " + Environment.getExternalStorageDirectory() + "/wltlib/ > 目录下是否有照片解码库授权文�?!", 0);
                }
            } else {
                Log("未找到身份证照片解码库libwltdecode.so!", 0);
            }

            FRIDCMsg = AnysizeIDCMsg.anysizeIDCMsg(isReadFinger, idcMsgLen[0], idcMsg);
            String idType = "";
            try {
                idType = new String(FRIDCMsg.get("TypeID"), "UTF-16LE");

                if (idType.compareTo("I") == 0) {
                    ForeignParcel mForeignParcel = new ForeignParcel();
                    mForeignParcel.setForeignName(new String(FRIDCMsg.get("ForeignName"), "UTF-16LE"));
                    mForeignParcel.setSex(new String(FRIDCMsg.get("Sex")));
                    mForeignParcel.setCardNo(new String(FRIDCMsg.get("IDNum"), "UTF-16LE"));
                    mForeignParcel.setNation(new String(FRIDCMsg.get("CountryCode"), "UTF-16LE"));
                    mForeignParcel.setChinaName(new String(FRIDCMsg.get("ChinaName"), "UTF-16LE"));
                    mForeignParcel.setUserLifeBegin(new String(FRIDCMsg.get("DateStart"), "UTF-16LE"));
                    mForeignParcel.setUserLifeEnd(new String(FRIDCMsg.get("DateEnd"), "UTF-16LE"));
                    mForeignParcel.setBirthday(new String(FRIDCMsg.get("Birth"), "UTF-16LE"));
                    mForeignParcel.setIDVer(new String(FRIDCMsg.get("IDVer"), "UTF-16LE"));

                    Message msg = new Message();
                    msg.what = IDENTITY_FOREIGN;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(IDENTITY_KEY_FOREIGN, mForeignParcel);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);

                } else {
                    IdentityParcel mIdentityParcel = new IdentityParcel();
                    mIdentityParcel.setName(new String(FRIDCMsg.get("ChinaName"), "UTF-16LE"));
                    mIdentityParcel.setSex(new String(FRIDCMsg.get("Sex")));
                    if (idType.compareTo("J") != 0) {
                        mIdentityParcel.setNation(new String(FRIDCMsg.get("Nation")));
                    }
                    mIdentityParcel.setBirthday(new String(FRIDCMsg.get("Birth"), "UTF-16LE"));
                    mIdentityParcel.setAddress(new String(FRIDCMsg.get("Address"), "UTF-16LE"));
                    mIdentityParcel.setCardNo(new String(FRIDCMsg.get("IDNum"), "UTF-16LE"));
                    mIdentityParcel.setGrantDept(new String(FRIDCMsg.get("Issued"), "UTF-16LE"));
                    mIdentityParcel.setUserLifeBegin(new String(FRIDCMsg.get("DateStart"), "UTF-16LE"));
                    mIdentityParcel.setUserLifeEnd(new String(FRIDCMsg.get("DateEnd"), "UTF-16LE"));
                    Message msg = new Message();
                    msg.what = IDENTITY_CHINA;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(IDENTITY_KEY_CHINA, mIdentityParcel);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log("\n总用时：" + (mendTime - mStartTime) + "ms", 1);
            Log("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", 1);
        } else {
            Log("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", 0);
            Log("获取身份证信息失�?! ret:" + ret, 0);
        }
    }

    public void ReadCardAllWithFinger() {
        int[] idcMsgLen = new int[2];
        byte[] idcMsg = new byte[4096];
        String StrBmpFilePath;
        long mStartTime = System.currentTimeMillis();
        long mendTime = 0;
        int isReadFinger = 1; //读指�?
        int ret = NativeFunc.mt8IDCardReadAll(idcMsgLen, idcMsg, isReadFinger);
        if (ret == 0) {
            mendTime = System.currentTimeMillis();
            Log("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", 1);
            //显示photo
            byte[] wltData = null;
            try {
                wltData = AnysizeIDCMsg.CombinationWltData(idcMsgLen, idcMsg, isReadFinger);
            } catch (Exception ex) {
                Log("组合wlt数据失败,errMsg:\n" + ex.getMessage(), 0);
                return;
            }

            if (IDCReaderSDK.GetLoadSoState()) {
                ToolFun.initLicData(mContext, Environment.getExternalStorageDirectory() + "/wltlib");
                int st = IDCReaderSDK.Init();
                if (st == 0) {
                    int t = IDCReaderSDK.unpack(wltData, AnysizeIDCMsg.byLicData);
                    if (t == 1) {
                        StrBmpFilePath = Environment.getExternalStorageDirectory() + "/wltlib/zp.bmp";
                        FileInputStream fis;
                        try {
                            fis = new FileInputStream(StrBmpFilePath);

                            //Bitmap bmp = BitmapFactory.decodeStream(fis);
                            fis.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Log(StrBmpFilePath, 2); // 显示照片
                    } else {
                        Log("身份证照片解码库解码失败,ret = " + t, 0);
                    }
                } else {
                    Log("身份证照片解码库初始化失�?,请检�?< " + Environment.getExternalStorageDirectory() + "/wltlib/ > 目录下是否有照片解码库授权文�?!", 0);
                }
            } else {
                Log("未找到身份证照片解码库libwltdecode.so!", 0);
            }


            FRIDCMsg = AnysizeIDCMsg.anysizeIDCMsg(isReadFinger, idcMsgLen[0], idcMsg);
            String idType = "";
            try {
                idType = new String(FRIDCMsg.get("TypeID"), "UTF-16LE");

                if (idType.compareTo("I") == 0) {
                    ForeignParcel mForeignParcel = new ForeignParcel();
                    mForeignParcel.setForeignName(new String(FRIDCMsg.get("ForeignName"), "UTF-16LE"));
                    mForeignParcel.setSex(new String(FRIDCMsg.get("Sex")));
                    mForeignParcel.setCardNo(new String(FRIDCMsg.get("IDNum"), "UTF-16LE"));
                    mForeignParcel.setNation(new String(FRIDCMsg.get("CountryCode"), "UTF-16LE"));
                    mForeignParcel.setChinaName(new String(FRIDCMsg.get("ChinaName"), "UTF-16LE"));
                    mForeignParcel.setUserLifeBegin(new String(FRIDCMsg.get("DateStart"), "UTF-16LE"));
                    mForeignParcel.setUserLifeEnd(new String(FRIDCMsg.get("DateEnd"), "UTF-16LE"));
                    mForeignParcel.setBirthday(new String(FRIDCMsg.get("Birth"), "UTF-16LE"));
                    mForeignParcel.setIDVer(new String(FRIDCMsg.get("IDVer"), "UTF-16LE"));
                    Message msg = new Message();
                    msg.what = IDENTITY_FOREIGN;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(IDENTITY_KEY_FOREIGN, mForeignParcel);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } else {
                    IdentityParcel mIdentityParcel = new IdentityParcel();
                    mIdentityParcel.setName(new String(FRIDCMsg.get("ChinaName"), "UTF-16LE"));
                    mIdentityParcel.setSex(new String(FRIDCMsg.get("Sex")));
                    if (idType.compareTo("J") != 0) {
                        mIdentityParcel.setNation(new String(FRIDCMsg.get("Nation")));
                    }
                    mIdentityParcel.setBirthday(new String(FRIDCMsg.get("Birth"), "UTF-16LE"));
                    mIdentityParcel.setAddress(new String(FRIDCMsg.get("Address"), "UTF-16LE"));
                    mIdentityParcel.setCardNo(new String(FRIDCMsg.get("IDNum"), "UTF-16LE"));
                    mIdentityParcel.setGrantDept(new String(FRIDCMsg.get("Issued"), "UTF-16LE"));
                    mIdentityParcel.setUserLifeBegin(new String(FRIDCMsg.get("DateStart"), "UTF-16LE"));
                    mIdentityParcel.setUserLifeEnd(new String(FRIDCMsg.get("DateEnd"), "UTF-16LE"));
                    Message msg = new Message();
                    msg.what = IDENTITY_CHINA;
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(IDENTITY_KEY_CHINA, mIdentityParcel);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            Log("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", 0);
            Log("获取身份证信息失�?! ret:" + ret, 0);
        }
    }

    public void Log(String info, int mark) {

        Log.d(TAG,info);

    }

}
