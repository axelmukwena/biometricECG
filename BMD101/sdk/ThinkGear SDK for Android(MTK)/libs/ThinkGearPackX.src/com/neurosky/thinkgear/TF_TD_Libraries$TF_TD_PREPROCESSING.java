package com.neurosky.thinkgear;

public class TF_TD_Libraries$TF_TD_PREPROCESSING
{
  private double[] a;
  private double[] b;
  private double[] c;
  private double[] d;
  private double[] e;
  private double[] f;
  private double[] g;
  private TF_TD_Libraries.Butter_Return h;

  public TF_TD_Libraries$TF_TD_PREPROCESSING(TF_TD_Libraries paramTF_TD_Libraries)
  {
  }

  public double[] TF_TD_preprocessing(double[] paramArrayOfDouble, int paramInt)
  {
    paramInt = new TF_TD_Libraries.Detrend(this.i);
    this.a = paramInt.removeLinearTrend(paramArrayOfDouble);
    paramArrayOfDouble = new TF_TD_Libraries.Butter(this.i);
    this.g = new double[2];
    this.g[0] = 0.001953125D;
    this.g[1] = 0.17578125D;
    this.h = paramArrayOfDouble.ButterworthBandPassFilter(5, this.g);
    this.b = this.h.get_B();
    this.c = this.h.get_A();
    paramArrayOfDouble = new TF_TD_Libraries.filtfilt(this.i);
    this.d = paramArrayOfDouble.ZeroPhaseFiltering(this.b, this.c, this.a);
    paramArrayOfDouble = new TF_TD_Libraries.TF_TD_eyeblink_v2(this.i);
    this.e = paramArrayOfDouble.eyeblinkv2_fun(this.d);
    this.f = new double[this.e.length];
    for (paramArrayOfDouble = 0; paramArrayOfDouble < this.f.length; paramArrayOfDouble++)
      this.f[paramArrayOfDouble] = (this.e[paramArrayOfDouble] * 2.0D / 4096.0D / 2000.0D);
    return this.f;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.TF_TD_PREPROCESSING
 * JD-Core Version:    0.6.0
 */