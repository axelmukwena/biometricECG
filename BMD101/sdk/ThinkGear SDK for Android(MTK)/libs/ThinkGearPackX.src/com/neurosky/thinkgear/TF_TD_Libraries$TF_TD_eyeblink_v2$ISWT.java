package com.neurosky.thinkgear;

import android.util.Log;

public class TF_TD_Libraries$TF_TD_eyeblink_v2$ISWT
{
  private double[] a;
  private double[] b;
  private double[] c;
  private int d;
  private int e;
  private int f;
  private int g;
  private double[][] h;
  private double[] i;
  private double[] j;
  private double[] k;
  private double[] l;
  private double m;
  private double n;
  private double[] o;
  private double[] p;
  private double[] q;
  private double[] r;
  private double[] s;
  private double[] t;
  private double[] u;
  private double[] v;
  private double[] w;

  public TF_TD_Libraries$TF_TD_eyeblink_v2$ISWT(TF_TD_Libraries.TF_TD_eyeblink_v2 paramTF_TD_eyeblink_v2)
  {
  }

  public double[] ISWT_Fun(double[][] paramArrayOfDouble)
  {
    this.b = new double[] { -0.003793512864491D, 0.007782596427325D, 0.023452696141836D, -0.065771911281856D, -0.061123390002673D, 0.405176902409617D, 0.793777222625621D, 0.428483476377619D, -0.071799821619312D, -0.082301927106886D, 0.034555027573062D, 0.015880544863616D, -0.009007976136662D, -0.00257451768875D, 0.001117518770891D, 0.0004662169601128863D, -7.098330313814125E-005D, -3.459977283621256E-005D };
    this.c = new double[] { -3.459977283621256E-005D, 7.098330313814125E-005D, 0.0004662169601128863D, -0.001117518770891D, -0.00257451768875D, 0.009007976136662D, 0.015880544863616D, -0.034555027573062D, -0.082301927106886D, 0.071799821619312D, 0.428483476377619D, -0.793777222625621D, 0.405176902409617D, 0.061123390002673D, -0.065771911281856D, -0.023452696141836D, 0.007782596427325D, 0.003793512864491D };
    this.d = paramArrayOfDouble.length;
    Log.i("p = ", String.valueOf(this.d));
    this.e = (this.d - 1);
    this.h = new double[this.e][paramArrayOfDouble[0].length];
    for (int i1 = 0; i1 < this.e; i1++)
      for (int i2 = 0; i2 < paramArrayOfDouble[0].length; i2++)
        this.h[i1][i2] = paramArrayOfDouble[i1][i2];
    this.i = new double[paramArrayOfDouble[0].length];
    for (i1 = 0; i1 < paramArrayOfDouble[0].length; i1++)
      this.i[i1] = paramArrayOfDouble[(this.d - 1)][i1];
    this.e = this.h.length;
    this.f = this.h[0].length;
    for (i1 = this.e; i1 > 0; i1--)
    {
      this.m = Math.pow(2.0D, i1 - 1);
      this.n = this.m;
      double d1 = 1.0D;
      while (d1 <= this.n)
      {
        paramArrayOfDouble = 0;
        this.j = new double[(int)(Math.floor((this.f - d1) / this.m) + 1.0D)];
        double d2 = d1;
        while (d2 <= this.f)
        {
          this.j[paramArrayOfDouble] = d2;
          paramArrayOfDouble++;
          d2 += this.m;
        }
        this.g = this.j.length;
        paramArrayOfDouble = 0;
        this.k = new double[(int)(Math.floor((this.g - 1.0D) / 2.0D) + 1.0D)];
        int i5 = 0;
        while (i5 < this.g - 1)
        {
          this.k[paramArrayOfDouble] = this.j[i5];
          paramArrayOfDouble += 1;
          i5 += 2;
        }
        this.v = new double[this.k.length];
        for (i5 = 0; i5 < this.k.length; i5++)
        {
          i7 = (int)this.k[i5];
          this.v[i5] = this.i[(i7 - 1)];
        }
        this.w = new double[this.k.length];
        for (i5 = 0; i5 < this.k.length; i5++)
        {
          i7 = (int)this.k[i5];
          this.w[i5] = this.h[(i1 - 1)][(i7 - 1)];
        }
        double[] arrayOfDouble2 = a(this.v, this.w, this.b, this.c, this.g, 0.0D);
        paramArrayOfDouble = 0;
        this.l = new double[(int)(Math.floor((this.g - 2.0D) / 2.0D) + 1.0D)];
        int i7 = 1;
        while (i7 < this.g)
        {
          this.l[paramArrayOfDouble] = this.j[i7];
          paramArrayOfDouble += 1;
          i7 += 2;
        }
        double[] arrayOfDouble3 = new double[this.l.length];
        for (paramArrayOfDouble = 0; paramArrayOfDouble < this.l.length; paramArrayOfDouble++)
        {
          i3 = (int)this.l[paramArrayOfDouble];
          arrayOfDouble3[paramArrayOfDouble] = this.i[(i3 - 1)];
        }
        paramArrayOfDouble = new double[this.l.length];
        for (int i3 = 0; i3 < this.l.length; i3++)
        {
          int i8 = (int)this.l[i3];
          paramArrayOfDouble[i3] = this.h[(i1 - 1)][(i8 - 1)];
        }
        double[] arrayOfDouble1 = a(arrayOfDouble3, paramArrayOfDouble, this.b, this.c, this.g, -1.0D);
        double d3 = 0.5D;
        arrayOfDouble1 = a(arrayOfDouble2, arrayOfDouble1);
        paramArrayOfDouble = this;
        this.u = new double[arrayOfDouble1.length];
        for (int i6 = 0; i6 < arrayOfDouble1.length; i6++)
          paramArrayOfDouble.u[i6] = (arrayOfDouble1[i6] * 0.5D);
        double[] arrayOfDouble4 = paramArrayOfDouble.u;
        for (paramArrayOfDouble = 0; paramArrayOfDouble < this.j.length; paramArrayOfDouble++)
        {
          int i4 = (int)this.j[paramArrayOfDouble];
          this.i[(i4 - 1)] = arrayOfDouble4[paramArrayOfDouble];
        }
        d1 += 1.0D;
      }
    }
    this.a = new double[this.i.length];
    for (i1 = 0; i1 < this.i.length; i1++)
      this.a[i1] = this.i[i1];
    return this.a;
  }

  private double[] a(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3, double[] paramArrayOfDouble4, int paramInt, double paramDouble)
  {
    paramArrayOfDouble1 = a(paramArrayOfDouble1, paramArrayOfDouble3, paramInt);
    paramArrayOfDouble2 = a(paramArrayOfDouble2, paramArrayOfDouble4, paramInt);
    if (paramDouble == 0.0D)
    {
      this.r = a(paramArrayOfDouble1, paramArrayOfDouble2);
    }
    else
    {
      paramArrayOfDouble1 = a(paramArrayOfDouble1, paramArrayOfDouble2);
      this.r = new double[paramArrayOfDouble1.length];
      for (paramArrayOfDouble2 = 0; paramArrayOfDouble2 < paramArrayOfDouble1.length; paramArrayOfDouble2++)
        if (paramArrayOfDouble2 == 0)
          this.r[paramArrayOfDouble2] = paramArrayOfDouble1[(paramArrayOfDouble1.length - 1)];
        else
          this.r[paramArrayOfDouble2] = paramArrayOfDouble1[(paramArrayOfDouble2 - 1)];
    }
    return this.r;
  }

  private double[] a(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    this.t = new double[paramArrayOfDouble1.length];
    for (int i1 = 0; i1 < paramArrayOfDouble1.length; i1++)
      this.t[i1] = (paramArrayOfDouble1[i1] + paramArrayOfDouble2[i1]);
    return this.t;
  }

  private double[] a(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, int paramInt)
  {
    double[] arrayOfDouble1 = paramArrayOfDouble2.length;
    Object localObject = paramArrayOfDouble1;
    paramArrayOfDouble1 = this;
    int i1 = 2 * localObject.length;
    paramArrayOfDouble1.q = new double[i1];
    int i2 = 0;
    int i3 = 0;
    paramArrayOfDouble1.q[i3] = localObject[i2];
    i2 += 1;
    i3 += 2;
    i1 = arrayOfDouble1 / 2;
    localObject = paramArrayOfDouble1.q;
    paramArrayOfDouble1 = this;
    i3 = (i2 = localObject.length) - i1 + 1;
    paramArrayOfDouble1.o = new double[localObject.length + 2 * i1];
    int i4 = 0;
    paramArrayOfDouble1.o[i4] = i3;
    i3 += 1;
    i4++;
    for (i4 = 0; i4 < i2; i4++)
      paramArrayOfDouble1.o[(i4 + i1)] = (i4 + 1);
    for (i4 = 0; i4 < i1; i4++)
      paramArrayOfDouble1.o[(i4 + i1 + i2)] = (i4 + 1);
    double[] arrayOfDouble3 = new double[paramArrayOfDouble1.o.length];
    for (i1 = 0; i1 < paramArrayOfDouble1.o.length; i1++)
    {
      i2 = (int)paramArrayOfDouble1.o[i1];
      arrayOfDouble3[i1] = localObject[(i2 - 1)];
    }
    localObject = paramArrayOfDouble2;
    double[] arrayOfDouble2 = new double[(paramArrayOfDouble1 = arrayOfDouble3).length + localObject.length - 1];
    for (i3 = 0; i3 < paramArrayOfDouble1.length; i3++)
      for (int i5 = 0; i5 < localObject.length; i5++)
        arrayOfDouble2[(i3 + i5)] += paramArrayOfDouble1[i3] * localObject[i5];
    arrayOfDouble2 = arrayOfDouble1;
    i1 = paramInt;
    localObject = arrayOfDouble2;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.TF_TD_eyeblink_v2.ISWT
 * JD-Core Version:    0.6.0
 */