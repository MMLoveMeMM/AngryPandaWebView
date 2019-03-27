package com.ivsign.android.IDCReader;

import android.util.Log;

public class IDCReaderSDK {
	private static final String TAG = "IDCReaderSDK";
	private static boolean LOAD_SO_FLAG = false;
	public static String strWltPath = "file:///android_asset";
	public static int Init() {
		return wltInit(strWltPath + "/wltlib");
		//return wltInit(Environment.getExternalStorageDirectory() + "/wltlib");
	}

	public static int unpack(byte[] wltdata, byte[] licdata) {
		return wltGetBMP(wltdata, licdata);
	}

	public static native int wltInit(String workPath);

	public static native int wltGetBMP(byte[] wltdata, byte[] licdata);

	/*
	 * this is used to load the 'wltdecode' library on application
	 */
	static {
		try {
			System.loadLibrary("wltdecode");
			LOAD_SO_FLAG = true;
		} catch (Throwable ex) {
			LOAD_SO_FLAG = false;
			Log.e(TAG, ex.toString());
		}

	}

	public static boolean GetLoadSoState() {
		return LOAD_SO_FLAG;
	}
}
