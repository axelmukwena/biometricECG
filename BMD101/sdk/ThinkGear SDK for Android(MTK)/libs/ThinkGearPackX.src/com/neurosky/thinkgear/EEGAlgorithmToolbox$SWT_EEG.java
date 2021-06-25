package com.neurosky.thinkgear;

import android.util.Log;

public class EEGAlgorithmToolbox$SWT_EEG
{
  private double[] a;
  private double[] b;
  private double[] c;
  private double[] d;
  private double[] e;
  private double[] f;
  private double[] g;
  private int h;
  private double[][] i;
  private double[][] j;
  private double[] k;
  private double[] l;
  private double[] m;
  private double[][] n;

  public EEGAlgorithmToolbox$SWT_EEG(EEGAlgorithmToolbox paramEEGAlgorithmToolbox)
  {
  }

  public double[][] SWT_Fun(double[] paramArrayOfDouble, int paramInt, String paramString)
  {
    this.h = paramArrayOfDouble.length;
    if (paramString != "coif3")
      Log.i("Warning", "No such wavelet type");
    this.a = new double[] { -3.459977283621256E-005D, -7.098330313814125E-005D, 0.0004662169601128863D, 0.001117518770891D, -0.00257451768875D, -0.009007976136662D, 0.015880544863616D, 0.034555027573062D, -0.082301927106886D, -0.071799821619312D, 0.428483476377619D, 0.793777222625621D, 0.405176902409617D, -0.061123390002673D, -0.065771911281856D, 0.023452696141836D, 0.007782596427325D, -0.003793512864491D };
    this.b = new double[] { 0.003793512864491D, 0.007782596427325D, -0.023452696141836D, -0.065771911281856D, 0.061123390002673D, 0.405176902409617D, -0.793777222625621D, 0.428483476377619D, 0.071799821619312D, -0.082301927106886D, -0.034555027573062D, 0.015880544863616D, 0.009007976136662D, -0.00257451768875D, -0.001117518770891D, 0.0004662169601128863D, 7.098330313814125E-005D, -3.459977283621256E-005D };
    this.i = new double[paramInt][this.h];
    this.j = new double[paramInt][this.h];
    paramString = 0;
    if (paramString < paramInt)
    {
      int i1 = this.a.length;
      int i2 = i1 / 2;
      double[] arrayOfDouble1 = paramArrayOfDouble;
      SWT_EEG localSWT_EEG = this;
      int i3;
      int i4 = (i3 = arrayOfDouble1.length) - i2 + 1;
      localSWT_EEG.k = new double[arrayOfDouble1.length + 2 * i2];
      int i5 = 0;
      localSWT_EEG.k[i5] = i4;
      i4 += 1;
      i5++;
      for (i5 = 0; i5 < i3; i5++)
        localSWT_EEG.k[(i5 + i2)] = (i5 + 1);
      for (i5 = 0; i5 < i2; i5++)
        localSWT_EEG.k[(i5 + i2 + i3)] = (i5 + 1);
      arrayOfDouble2 = new double[localSWT_EEG.k.length];
      for (i2 = 0; i2 < localSWT_EEG.k.length; i2++)
      {
        i3 = (int)localSWT_EEG.k[i2];
        arrayOfDouble2[i2] = arrayOfDouble1[(i3 - 1)];
      }
    }
  }

  private static double[] a(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    double[] arrayOfDouble = new double[paramArrayOfDouble1.length + paramArrayOfDouble2.length - 1];
    for (int i1 = 0; i1 < paramArrayOfDouble1.length; i1++)
      for (int i2 = 0; i2 < paramArrayOfDouble2.length; i2++)
        arrayOfDouble[(i1 + i2)] += paramArrayOfDouble1[i1] * paramArrayOfDouble2[i2];
    return arrayOfDouble;
  }

  private double[] a(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
  {
    this.l = new double[paramInt1];
    int i1 = 0;
    for (int i2 = paramInt2 - 1; i2 <= paramInt2 + paramInt1 - 2; i2++)
    {
      this.l[i1] = paramArrayOfDouble[i2];
      i1 += 1;
    }
    return this.l;
  }

  private double[] a(double[] paramArrayOfDouble)
  {
    int i1 = 2 * paramArrayOfDouble.length;
    this.m = new double[i1];
    int i2 = 0;
    int i3 = 0;
    while (i3 < i1)
    {
      this.m[i3] = paramArrayOfDouble[i2];
      i2 += 1;
      i3 += 2;
    }
    return this.m;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.SWT_EEG
 * JD-Core Version:    0.6.0
 */