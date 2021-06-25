package com.neurosky.thinkgear;

import android.util.Log;
import java.util.Arrays;

public class EnergyLevel
{
  private int[] a;
  private int b = 128;
  private int[] c = new int[this.b];
  private int[] d = new int[this.b];
  private float[] e = new float[this.b];
  private float[] f;
  private float[] g;
  private float[] h = new float[this.b];
  private float[] i = new float[this.b];
  private int[] j = new int[300];
  private int k = 0;
  private int l = 0;
  private int m = 0;
  private int n = 2;
  private float o;
  private float p;
  private float q;
  private float r;
  private float s;
  private FFT t = new FFT();
  private FFTResult u;
  private HanningWindow v = new HanningWindow(this.b);
  private int[] w;
  private float[] x;

  public int calculateEnergyLevel(int[] paramArrayOfInt, int paramInt)
  {
    this.a = new int[paramInt];
    this.a[0] = 0;
    for (int i1 = 1; i1 < paramInt; i1++)
      this.a[i1] = (this.a[(i1 - 1)] + paramArrayOfInt[i1]);
    if (this.a[(paramInt - 1)] < 63500)
      return -1;
    for (i1 = 0; i1 < this.b; i1++)
      this.c[i1] = (i1 * 1000 / 2);
    this.w = new int[paramArrayOfInt.length];
    for (i1 = 0; i1 < paramArrayOfInt.length; i1++)
      this.w[i1] = paramArrayOfInt[i1];
    for (i1 = 7; i1 < paramArrayOfInt.length - 1; i1++)
    {
      this.x = new float[] { paramArrayOfInt[(i1 - 7)], paramArrayOfInt[(i1 - 6)], paramArrayOfInt[(i1 - 5)], paramArrayOfInt[(i1 - 4)], paramArrayOfInt[(i1 - 3)], paramArrayOfInt[(i1 - 2)], paramArrayOfInt[(i1 - 1)] };
      if ((paramArrayOfInt[i1] <= 1.4D * a(this.x)) && (paramArrayOfInt[i1] >= 0.6D * a(this.x)))
        continue;
      this.w[i1] = (int)((paramArrayOfInt[(i1 + 1)] + paramArrayOfInt[(i1 - 1)]) / 2.0D);
    }
    this.d[0] = this.w[0];
    i1 = 1;
    paramArrayOfInt = this.d[0];
    for (paramInt = 1; paramInt < this.b; paramInt++)
    {
      while (this.a[i1] <= this.c[paramInt])
        i1++;
      this.d[paramInt] = (int)(this.w[(i1 - 1)] + (this.w[i1] - this.w[(i1 - 1)]) * (this.c[paramInt] - this.a[(i1 - 1)]) / this.w[i1]);
      paramArrayOfInt += this.d[paramInt];
    }
    this.m = (paramArrayOfInt / this.b);
    for (paramInt = 0; paramInt < this.b; paramInt++)
      this.e[paramInt] = (this.d[paramInt] - this.m);
    this.e = this.v.applyCoeffs(this.e);
    this.u = this.t.calculateFFT(this.e, this.i, 1, this.b);
    this.f = this.u.getReal();
    this.g = this.u.getImaginary();
    for (paramInt = 0; paramInt < this.b; paramInt++)
      this.h[paramInt] = (2.0F * (float)(Math.pow(this.f[paramInt], 2.0D) + Math.pow(this.g[paramInt], 2.0D)));
    this.p = 0.0F;
    this.q = 0.0F;
    for (paramInt = 0; paramInt < this.b; paramInt++)
    {
      this.o = (this.n / this.b * paramInt);
      if ((this.o >= 0.15D) && (this.o <= 0.4D))
        this.p += this.h[paramInt];
      if ((this.o < 0.04D) || (this.o > 0.15D))
        continue;
      this.q += this.h[paramInt];
    }
    this.r = (float)Math.min(10.0D, this.q / this.p);
    this.s = (float)(100.0D - 10.0D * this.r);
    this.s = (float)Math.floor(this.s + 0.5D);
    if (this.s == 0.0F)
      this.s = 1.0F;
    return (int)this.s;
  }

  public int addInterval(int paramInt1, int paramInt2)
  {
    if (paramInt2 == 200)
    {
      if (this.k >= 300)
      {
        Log.v("TGDevice", "Buffer reset: " + this.k + " " + this.l);
        this.k = 0;
        this.l = 0;
        this.j = new int[300];
        return 0;
      }
      this.j[(this.k++)] = paramInt1;
      this.l += paramInt1;
      if (this.l >= 75000)
      {
        paramInt1 = 0;
        for (paramInt2 = 0; paramInt1 < 5000; paramInt2++)
          paramInt1 += this.j[paramInt2];
        paramInt1 = new int[this.k - paramInt2];
        System.arraycopy(this.j, paramInt2, paramInt1, 0, paramInt1.length);
        paramInt1 = calculateEnergyLevel(paramInt1, paramInt1.length);
        this.k = 0;
        this.l = 0;
        return paramInt1;
      }
    }
    else
    {
      this.k = 0;
      this.l = 0;
      this.j = new int[300];
    }
    return 0;
  }

  private static float a(float[] paramArrayOfFloat)
  {
    Arrays.sort(paramArrayOfFloat);
    float f1 = (float)(paramArrayOfFloat.length / 2.0D);
    if (paramArrayOfFloat.length % 2 == 0)
      return (float)((paramArrayOfFloat[((int)f1 - 1)] + paramArrayOfFloat[(int)f1]) / 2.0D);
    return paramArrayOfFloat[(int)Math.floor(f1)];
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EnergyLevel
 * JD-Core Version:    0.6.0
 */