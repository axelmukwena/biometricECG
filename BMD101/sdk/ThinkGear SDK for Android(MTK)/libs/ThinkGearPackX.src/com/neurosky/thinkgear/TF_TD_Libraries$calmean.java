package com.neurosky.thinkgear;

public class TF_TD_Libraries$calmean
{
  private int a;

  public TF_TD_Libraries$calmean(TF_TD_Libraries paramTF_TD_Libraries)
  {
  }

  public double mean(double[] paramArrayOfDouble)
  {
    TF_TD_Libraries.Diff localDiff = new TF_TD_Libraries.Diff(this.b);
    this.a = localDiff.return_row(paramArrayOfDouble, this.a);
    double d = 0.0D;
    for (int i = 0; i < this.a; i++)
      d += paramArrayOfDouble[i];
    return d /= this.a;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.calmean
 * JD-Core Version:    0.6.0
 */