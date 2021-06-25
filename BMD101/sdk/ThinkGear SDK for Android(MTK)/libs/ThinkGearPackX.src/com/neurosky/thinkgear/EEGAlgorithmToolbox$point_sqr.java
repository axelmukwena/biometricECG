package com.neurosky.thinkgear;

import D;

public class EEGAlgorithmToolbox$point_sqr
{
  public int row;

  public EEGAlgorithmToolbox$point_sqr(EEGAlgorithmToolbox paramEEGAlgorithmToolbox)
  {
  }

  public double[] sqr(double[] paramArrayOfDouble)
  {
    Object localObject = new EEGAlgorithmToolbox.Diff(this.a);
    this.row = ((EEGAlgorithmToolbox.Diff)localObject).return_row(paramArrayOfDouble, this.row);
    localObject = new double[this.row];
    for (int i = 0; i < this.row; i++)
      paramArrayOfDouble[i] *= paramArrayOfDouble[i];
    return (D)localObject;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.point_sqr
 * JD-Core Version:    0.6.0
 */