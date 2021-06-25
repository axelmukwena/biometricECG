package com.neurosky.thinkgear;

public class EEGAlgorithmToolbox$Diff_EEG
{
  private int a;

  public EEGAlgorithmToolbox$Diff_EEG(EEGAlgorithmToolbox paramEEGAlgorithmToolbox)
  {
  }

  public double[] applydiff(double[] paramArrayOfDouble)
  {
    new Diff_EEG(this.b);
    this.a = paramArrayOfDouble.length;
    double[] arrayOfDouble = new double[this.a - 1];
    for (int i = 1; i < this.a; i++)
      arrayOfDouble[(i - 1)] = (paramArrayOfDouble[i] - paramArrayOfDouble[(i - 1)]);
    return arrayOfDouble;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.Diff_EEG
 * JD-Core Version:    0.6.0
 */