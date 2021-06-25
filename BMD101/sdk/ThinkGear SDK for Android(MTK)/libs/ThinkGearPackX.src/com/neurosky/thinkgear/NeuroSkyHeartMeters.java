package com.neurosky.thinkgear;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class NeuroSkyHeartMeters
{
  public static int initRindex = 5;
  public static boolean verbose = false;
  public static boolean agelimits = true;
  private static String a;
  private static Integer[] b;
  private static Integer[] c;

  public static double calculateHeartAge(Integer[] paramArrayOfInteger, int paramInt1, int paramInt2)
  {
    long l1 = System.nanoTime();
    b = new Integer[paramArrayOfInteger.length];
    for (int i = 0; i < paramArrayOfInteger.length; i++)
      b[i] = Integer.valueOf(paramArrayOfInteger[i].intValue());
    for (i = 7; i < paramArrayOfInteger.length - 1; i++)
    {
      c = new Integer[] { paramArrayOfInteger[(i - 7)], paramArrayOfInteger[(i - 6)], paramArrayOfInteger[(i - 5)], paramArrayOfInteger[(i - 4)], paramArrayOfInteger[(i - 3)], paramArrayOfInteger[(i - 2)], paramArrayOfInteger[(i - 1)] };
      if ((paramArrayOfInteger[i].intValue() <= 1.5D * a(c)) && (paramArrayOfInteger[i].intValue() >= 0.6D * a(c)))
        continue;
      b[i] = Integer.valueOf((int)((paramArrayOfInteger[(i + 1)].intValue() + paramArrayOfInteger[(i - 1)].intValue()) / 2.0D));
    }
    paramArrayOfInteger = new Integer[b.length - 8];
    for (i = 7; i < b.length - 1; i++)
      paramArrayOfInteger[(i - 7)] = b[i];
    double d1 = calculateHeartAgeRaw(paramArrayOfInteger);
    double d2 = paramInt1 - d1;
    long l2 = System.nanoTime();
    if (verbose)
      System.out.println("Heart age: time in nanoseconds: " + (l2 - l1));
    return paramInt1 - d2 / paramInt2;
  }

  private static int a(Integer[] paramArrayOfInteger)
  {
    Arrays.sort(paramArrayOfInteger);
    float f = (float)(paramArrayOfInteger.length / 2.0D);
    if (paramArrayOfInteger.length % 2 == 0)
      return (int)((paramArrayOfInteger[((int)f - 1)].intValue() + paramArrayOfInteger[(int)f].intValue()) / 2.0D);
    return paramArrayOfInteger[(int)Math.floor(f)].intValue();
  }

  public static void resetHeartAge(String paramString)
  {
    if ("mounted".equals(Environment.getExternalStorageState()))
      a = Environment.getExternalStorageDirectory() + "/Android/data/com.neurosky.thinkgear/files/EKG/age";
    else
      a = Environment.getDataDirectory() + "/com.neurosky.thinkgear/files/EKG/age";
    File localFile = new File(a);
    paramString = new File(a, paramString + ".json");
    try
    {
      if (!localFile.exists())
        localFile.mkdirs();
      if (!paramString.exists())
      {
        paramString.createNewFile();
      }
      else
      {
        paramString.delete();
        paramString.createNewFile();
        return;
      }
    }
    catch (IOException localIOException)
    {
    }
  }

  public static int progressHeartAge(String paramString)
  {
    paramString = a(paramString);
    int i = 0;
    if (paramString == null)
      return -1;
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramString));
      paramString = "";
      do
        paramString = paramString + localBufferedReader.readLine();
      while (localBufferedReader.ready());
      localBufferedReader.close();
      i = (paramString = (paramString = (JSONObject)new JSONTokener(paramString).nextValue()).getJSONArray("rrIntervalInMS")).length();
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      (paramString = localFileNotFoundException).printStackTrace();
    }
    catch (IOException paramString)
    {
    }
    catch (JSONException paramString)
    {
    }
    return i;
  }

  public static double calculateHeartAge(Integer[] paramArrayOfInteger, int paramInt, String paramString)
  {
    System.nanoTime();
    if ((localObject = a(paramString)) == null)
      resetHeartAge(paramString);
    if (verbose)
      System.out.println("Values for byte array");
    Object localObject = b(paramString);
    for (int i = 0; i < localObject.length; i++)
    {
      if (!verbose)
        continue;
      System.out.println("Value " + Integer.toString(i) + ":" + Integer.toString(localObject[i]));
    }
    double d1 = calculateHeartAgeRaw(paramArrayOfInteger);
    paramArrayOfInteger = new byte[localObject.length + 1];
    for (int j = 0; j < localObject.length; j++)
      paramArrayOfInteger[j] = localObject[j];
    paramArrayOfInteger[(paramArrayOfInteger.length - 1)] = (byte)(int)d1;
    a(paramString, paramArrayOfInteger);
    if (verbose)
      System.out.println("Values for byte array2");
    for (j = 0; j < paramArrayOfInteger.length; j++)
    {
      if (!verbose)
        continue;
      System.out.println("Value " + Integer.toString(j) + ":" + Integer.toString(paramArrayOfInteger[j]));
    }
    double d2 = 0.0D;
    for (int k = 0; k < paramArrayOfInteger.length; k++)
      d2 += paramArrayOfInteger[k];
    d2 /= paramArrayOfInteger.length;
    double d3 = paramInt - d2;
  }

  public static double calculateHeartAgeRaw(Integer[] paramArrayOfInteger)
  {
    long l1 = System.nanoTime();
    double d1 = calculateSDNN(paramArrayOfInteger);
    double d2 = (85.0D - d1) / 0.57D;
    long l2 = System.nanoTime();
    if (verbose)
      System.out.println("Heart age uncorected: time in nanoseconds: " + (l2 - l1));
    if (agelimits)
    {
      if (d2 > 99.0D)
        d2 = 99.0D;
      if (d2 < 10.0D)
        d2 = 10.0D;
    }
    return d2;
  }

  public static int calculateHeartRiskAware(String paramString)
  {
    if ((paramString = b(paramString)).length == 0)
      return -1;
    double[] arrayOfDouble = new double[paramString.length];
    double d = 0.0D;
    for (int i = initRindex; i < paramString.length; i++)
    {
      arrayOfDouble[i] = (85.0D - 0.57D * paramString[i]);
      d += arrayOfDouble[i] / paramString.length;
    }
    if (d >= 30.0D)
      i = 0;
    else if (d >= 20.0D)
      i = 1;
    else if (d >= 10.0D)
      i = 2;
    else
      i = 3;
    return i;
  }

  public static int calculateHeartRiskAware(Integer[] paramArrayOfInteger)
  {
    long l1 = System.nanoTime();
    double d;
    if ((d = calculateSDNN(paramArrayOfInteger)) >= 30.0D)
      paramArrayOfInteger = 0;
    else if (d >= 20.0D)
      paramArrayOfInteger = 1;
    else if (d >= 10.0D)
      paramArrayOfInteger = 2;
    else
      paramArrayOfInteger = 3;
    long l2 = System.nanoTime();
    if (verbose)
      System.out.println("Heart risk: time in nanoseconds: " + (l2 - l1));
    return paramArrayOfInteger;
  }

  public static double calculateSDNN(Integer[] paramArrayOfInteger)
  {
    long l1 = System.nanoTime();
    double d1 = 0.0D;
    for (int i = initRindex; i < paramArrayOfInteger.length; i++)
      d1 += paramArrayOfInteger[i].intValue();
    double d2 = d1 / (paramArrayOfInteger.length - initRindex);
    double d3 = 0.0D;
    for (int j = initRindex; j < paramArrayOfInteger.length; j++)
      d3 += (paramArrayOfInteger[j].intValue() - d2) * (paramArrayOfInteger[j].intValue() - d2);
    double d4;
    double d5 = Math.sqrt(d4 = d3 / (paramArrayOfInteger.length - initRindex - 1));
    long l2 = System.nanoTime();
    if (verbose)
      System.out.println("SDNN: time in nanoseconds: " + (l2 - l1));
    return d5;
  }

  public static int calculateRelaxationLevel(int[] paramArrayOfInt)
  {
    long l1 = System.nanoTime();
    EnergyLevel localEnergyLevel;
    paramArrayOfInt = (localEnergyLevel = new EnergyLevel()).calculateEnergyLevel(paramArrayOfInt, paramArrayOfInt.length);
    long l2 = System.nanoTime();
    if (verbose)
      System.out.println("Energy level: time in nanoseconds: " + (l2 - l1));
    return paramArrayOfInt;
  }

  public static double calculateMult()
  {
    long l1 = System.nanoTime();
    double d = 3.141592653589793D;
    for (int i = 1; i < 100000; i++)
      d *= 3.141592653589793D;
    long l2 = System.nanoTime();
    if (verbose)
      System.out.println("Mult: time in nanoseconds: " + (l2 - l1));
    return d;
  }

  public static double calculateMult3()
  {
    return 3.141592653589793D;
  }

  public static double calculateMult2()
  {
    long l1 = System.nanoTime();
    long l2 = System.nanoTime();
    if (verbose)
      System.out.println("Mult: time in nanoseconds: " + (l2 - l1));
    return (1.0D / 0.0D);
  }

  private static File a(String paramString)
  {
    if ("mounted".equals(Environment.getExternalStorageState()))
      a = Environment.getExternalStorageDirectory() + "/Android/data/com.neurosky.thinkgear/files/EKG/age";
    else
      a = Environment.getDataDirectory() + "/com.neurosky.thinkgear/files/EKG/age";
    File localFile = new File(a);
    paramString = new File(a, paramString + ".json");
    try
    {
      if (!localFile.exists())
        localFile.mkdirs();
      if (!paramString.exists())
        paramString.createNewFile();
    }
    catch (IOException localIOException)
    {
      return null;
    }
    return paramString;
  }

  private static boolean a(String paramString, byte[] paramArrayOfByte)
  {
    if ((paramString = a(paramString)) == null)
      return false;
    String str = null;
    try
    {
      paramString = new FileOutputStream(paramString, false);
      str = a(paramArrayOfByte);
      paramString.write(str.getBytes());
      paramString.close();
      return true;
    }
    catch (IOException localIOException)
    {
      (paramString = localIOException).printStackTrace();
    }
    return false;
  }

  private static byte[] b(String paramString)
  {
    String str = "";
    paramString = a(paramString);
    byte[] arrayOfByte = { 0 };
    if (paramString == null)
      return arrayOfByte;
    try
    {
      paramString = new BufferedReader(new FileReader(paramString));
      do
        str = str + paramString.readLine();
      while (paramString.ready());
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      (paramString = localFileNotFoundException).printStackTrace();
    }
    catch (IOException localIOException)
    {
    }
    return arrayOfByte = c(str);
  }

  private static byte[] c(String paramString)
  {
    byte[] arrayOfByte = new byte[1];
    try
    {
      arrayOfByte = new byte[(paramString = (paramString = (JSONObject)(paramString = new JSONTokener(paramString)).nextValue()).getJSONArray("templateArray")).length()];
      for (int i = 0; i < paramString.length(); i++)
        arrayOfByte[i] = (byte)paramString.getInt(i);
    }
    catch (JSONException paramString)
    {
    }
    catch (java.lang.ClassCastException paramString)
    {
    }
    return arrayOfByte;
  }

  private static String a(byte[] paramArrayOfByte)
  {
    JSONObject localJSONObject = new JSONObject();
    JSONArray localJSONArray = new JSONArray();
    Log.v("TGDevice", "toJSONString() template array: " + paramArrayOfByte.length);
    for (int i = 0; i < paramArrayOfByte.length; i++)
      localJSONArray.put(paramArrayOfByte[i]);
    Log.v("TGDevice", "toJsonString() arraylength:" + localJSONArray.length());
    try
    {
      localJSONObject.accumulate("rrIntervalInMS", localJSONArray);
    }
    catch (JSONException localJSONException2)
    {
      JSONException localJSONException1;
      (localJSONException1 = localJSONException2).printStackTrace();
    }
    return localJSONObject.toString();
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.NeuroSkyHeartMeters
 * JD-Core Version:    0.6.0
 */