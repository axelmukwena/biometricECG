package com.neurosky.thinkgear;

public class TF_TD_Libraries$Detrend
{
  private double a;
  private double[] b;
  private double[] c;
  private double[] d;
  private double[] e;
  private double f;
  private double g;
  private int h;
  private int i;

  public TF_TD_Libraries$Detrend(TF_TD_Libraries paramTF_TD_Libraries)
  {
  }

  public double[] removeLinearTrend(double[] paramArrayOfDouble)
  {
    this.i = paramArrayOfDouble.length;
    this.d = new double[this.i];
    this.e = new double[this.i];
    this.h = 0;
    while (this.h < this.i)
    {
      this.e[this.h] = (this.h + 1);
      this.h += 1;
    }
    this.f = ((a(paramArrayOfDouble) * a(b(this.e)) - a(this.e) * a(a(this.e, paramArrayOfDouble))) / (this.i * a(b(this.e)) - Math.pow(a(this.e), 2.0D)));
    this.g = ((this.i * a(a(this.e, paramArrayOfDouble)) - a(this.e) * a(paramArrayOfDouble)) / (this.i * a(b(this.e)) - Math.pow(a(this.e), 2.0D)));
    this.h = 0;
    while (this.h < this.i)
    {
      this.d[this.h] = (paramArrayOfDouble[this.h] - (this.f + this.g * (this.h + 1.0D)));
      this.h += 1;
    }
    return this.d;
  }

  private double a(double[] paramArrayOfDouble)
  {
    this.a = 0.0D;
    for (int j = 0; j < paramArrayOfDouble.length; j++)
      this.a += paramArrayOfDouble[j];
    return this.a;
  }

  private double[] a(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    this.b = new double[paramArrayOfDouble1.length];
    for (int j = 0; j < paramArrayOfDouble1.length; j++)
      this.b[j] = (paramArrayOfDouble1[j] * paramArrayOfDouble2[j]);
    return this.b;
  }

  private double[] b(double[] paramArrayOfDouble)
  {
    this.c = new double[paramArrayOfDouble.length];
    for (int j = 0; j < paramArrayOfDouble.length; j++)
      this.c[j] = Math.pow(paramArrayOfDouble[j], 2.0D);
    return this.c;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.Detrend
 * JD-Core Version:    0.6.0
 */