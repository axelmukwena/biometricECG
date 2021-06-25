package com.neurosky.thinkgear;

import android.util.Log;

public final class TGconfig
{
  public static final boolean TGstubs = false;
  public static final int version = 29;
  public static final int minorversion = 0;
  public static final String builder = "davidlordemann";
  public static final String build_title = "2013-09-12 SDK Release";

  public static int getVersion()
  {
    return 29;
  }

  public static int getMinorVersion()
  {
    return 0;
  }

  public static String getTitle()
  {
    return "2013-09-12 SDK Release";
  }

  public static void logNonProvisioned(String paramString)
  {
    Log.w("ThinkGear-SDK", "called but not provisioned: " + paramString + " is not provided in this configuration");
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TGconfig
 * JD-Core Version:    0.6.0
 */