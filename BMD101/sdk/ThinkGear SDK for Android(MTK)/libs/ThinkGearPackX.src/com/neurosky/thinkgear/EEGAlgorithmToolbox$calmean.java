package com.neurosky.thinkgear;

public class EEGAlgorithmToolbox$calmean
{
  private int a;

  public EEGAlgorithmToolbox$calmean(EEGAlgorithmToolbox paramEEGAlgorithmToolbox)
  {
  }

  public double mean(double[] paramArrayOfDouble)
  {
    EEGAlgorithmToolbox.Diff localDiff = new EEGAlgorithmToolbox.Diff(this.b);
    this.a = localDiff.return_row(paramArrayOfDouble, this.a);
    double d = 0.0D;
    for (int i = 0; i < this.a; i++)
      d += paramArrayOfDouble[i];
    return d /= this.a;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.calmean
 * JD-Core Version:    0.6.0
 */