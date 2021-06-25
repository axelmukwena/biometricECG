package com.neurosky.thinkgear;

import android.util.Log;

public class Positivity
{
  private BlinkFilter a = new BlinkFilter(512);
  private FFT b = new FFT();
  private FFTResult c;
  private byte d = -56;
  private float[] e = new float[this.n];
  private float[] f = new float[this.n];
  private float[] g = new float[this.n];
  private float[] h = new float[this.p];
  private float[] i = new float[this.p];
  private float[] j = new float[this.p];
  private float[] k = new float[this.p];
  private float[] l = new float[this.p];
  private float[] m = new float[this.p];
  private short n = 5120;
  private short o = 0;
  private short p = 512;
  private double q;
  private int r;
  private double[] s = new double[6];
  private double[] t = new double[10];
  private double u;
  private double v;
  private double[] w = new double[6];
  private double[] x = new double[10];
  private double y;
  private double z;

  public double detect(byte paramByte, short paramShort1, short paramShort2)
  {
    if (paramByte == this.d)
    {
      System.arraycopy(this.e, 1, this.g, 0, this.n - 1);
      this.g[(this.n - 1)] = paramShort1;
      System.arraycopy(this.g, 0, this.e, 0, this.n);
      System.arraycopy(this.f, 1, this.g, 0, this.n - 1);
      this.g[(this.n - 1)] = paramShort2;
      System.arraycopy(this.g, 0, this.f, 0, this.n);
      this.o = (short)(this.o + 1);
      if (this.o == this.n)
      {
        Log.v("TGDevice", "Calculating positivity");
        this.o = (short)(this.p * 9);
        this.h = this.a.filter(paramByte, this.e);
        this.j = this.a.filter(paramByte, this.f);
        paramByte = this.p;
        paramShort1 = this.n / this.p;
        paramShort2 = new float[paramByte];
        this.v = 0.0D;
        this.z = 0.0D;
        for (short s1 = 0; s1 < paramShort1; s1++)
        {
          for (int i1 = 0; i1 < this.p; i1++)
          {
            this.i[i1] = this.h[(s1 * this.p + i1)];
            this.k[i1] = this.j[(s1 * this.p + i1)];
          }
          this.c = this.b.calculateFFT(this.i, paramShort2, 1, paramByte);
          this.l = this.c.getPower();
          this.c = this.b.calculateFFT(this.k, paramShort2, 1, paramByte);
          this.m = this.c.getPower();
          for (i1 = 8; i1 <= 13; i1++)
          {
            this.s[(i1 - 8)] = this.l[i1];
            this.w[(i1 - 8)] = this.m[i1];
          }
          this.t[s1] = a(this.s);
          this.x[s1] = a(this.w);
        }
        this.r = 0;
        while (this.r < this.t.length)
        {
          this.v += this.t[this.r];
          this.z += this.x[this.r];
          this.r += 1;
        }
        this.u = (this.v / this.t.length);
        this.y = (this.z / this.x.length);
        this.q = ((this.y - this.u) / (this.y + this.u) * 100.0D);
        if (this.q > 25.0D)
          this.q = 25.0D;
        else if (this.q < -25.0D)
          this.q = -25.0D;
        this.q *= 4.0D;
        return this.q;
      }
      return 200.0D;
    }
    this.e = new float[this.n];
    this.f = new float[this.n];
    this.o = 0;
    return 200.0D;
  }

  private static double a(double[] paramArrayOfDouble)
  {
    double d1 = paramArrayOfDouble[0];
    for (int i1 = 0; i1 < paramArrayOfDouble.length; i1++)
    {
      if (paramArrayOfDouble[i1] <= d1)
        continue;
      d1 = paramArrayOfDouble[i1];
    }
    return d1;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.Positivity
 * JD-Core Version:    0.6.0
 */