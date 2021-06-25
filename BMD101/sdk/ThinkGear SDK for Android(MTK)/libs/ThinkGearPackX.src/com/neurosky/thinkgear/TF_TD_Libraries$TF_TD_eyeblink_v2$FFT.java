package com.neurosky.thinkgear;

public class TF_TD_Libraries$TF_TD_eyeblink_v2$FFT
{
  private double[] a;
  private double[] b;

  public TF_TD_Libraries$TF_TD_eyeblink_v2$FFT(TF_TD_Libraries.TF_TD_eyeblink_v2 paramTF_TD_eyeblink_v2)
  {
  }

  public TF_TD_Libraries.TF_TD_eyeblink_v2.FFTResult calculateFFT(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, int paramInt1, int paramInt2)
  {
    this.a = new double[paramInt2];
    this.b = new double[paramInt2];
    System.arraycopy(paramArrayOfDouble1, 0, this.a, 0, Math.min(paramArrayOfDouble1.length, paramInt2));
    System.arraycopy(paramArrayOfDouble2, 0, this.b, 0, Math.min(paramArrayOfDouble2.length, paramInt2));
    double[] arrayOfDouble4 = (int)(Math.log(this.a.length) / Math.log(2.0D));
    paramArrayOfDouble1 = 1;
    for (paramArrayOfDouble2 = 0; paramArrayOfDouble2 < arrayOfDouble4; paramArrayOfDouble2++)
      paramArrayOfDouble1 <<= 1;
    double[] arrayOfDouble2 = paramArrayOfDouble1 >> 1;
    double[] arrayOfDouble1 = 0;
    int i;
    for (paramArrayOfDouble2 = 0; paramArrayOfDouble2 < paramArrayOfDouble1 - 1; paramArrayOfDouble2++)
    {
      if (paramArrayOfDouble2 < arrayOfDouble1)
      {
        double d3 = this.a[paramArrayOfDouble2];
        double d4 = this.b[paramArrayOfDouble2];
        this.a[paramArrayOfDouble2] = this.a[arrayOfDouble1];
        this.b[paramArrayOfDouble2] = this.b[arrayOfDouble1];
        this.a[arrayOfDouble1] = d3;
        this.b[arrayOfDouble1] = d4;
      }
      paramInt2 = arrayOfDouble2;
      while (paramInt2 <= arrayOfDouble1)
      {
        arrayOfDouble1 -= paramInt2;
        paramInt2 >>= 1;
      }
      i += paramInt2;
    }
    double d1 = -1.0D;
    double d2 = 0.0D;
    double[] arrayOfDouble3 = 1;
    for (arrayOfDouble2 = 0; arrayOfDouble2 < arrayOfDouble4; arrayOfDouble2++)
    {
      int j = arrayOfDouble3;
      arrayOfDouble3 <<= 1;
      double d7 = 1.0D;
      double d8 = 0.0D;
      for (i = 0; i < j; i++)
      {
        paramArrayOfDouble2 = i;
        while (paramArrayOfDouble2 < paramArrayOfDouble1)
        {
          paramInt2 = paramArrayOfDouble2 + j;
          double d5 = d7 * this.a[paramInt2] - d8 * this.b[paramInt2];
          double d6 = d7 * this.b[paramInt2] + d8 * this.a[paramInt2];
          this.a[paramArrayOfDouble2] -= d5;
          this.b[paramArrayOfDouble2] -= d6;
          this.a[paramArrayOfDouble2] += d5;
          this.b[paramArrayOfDouble2] += d6;
          paramArrayOfDouble2 += arrayOfDouble3;
        }
        double d9 = d7 * d1 - d8 * d2;
        d8 = d7 * d2 + d8 * d1;
        d7 = d9;
      }
      d2 = Math.sqrt((1.0D - d1) / 2.0D);
      if (paramInt1 == 1)
        d2 = -d2;
      d1 = Math.sqrt((d1 + 1.0D) / 2.0D);
    }
    if (paramInt1 == -1)
      for (paramArrayOfDouble2 = 0; paramArrayOfDouble2 < paramArrayOfDouble1; paramArrayOfDouble2++)
      {
        this.a[paramArrayOfDouble2] /= paramArrayOfDouble1;
        this.b[paramArrayOfDouble2] /= paramArrayOfDouble1;
      }
    return new TF_TD_Libraries.TF_TD_eyeblink_v2.FFTResult(this.c, this.a, this.b);
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.TF_TD_eyeblink_v2.FFT
 * JD-Core Version:    0.6.0
 */