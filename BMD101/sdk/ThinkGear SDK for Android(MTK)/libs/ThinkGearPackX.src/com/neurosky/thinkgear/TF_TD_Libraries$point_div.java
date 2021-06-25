package com.neurosky.thinkgear;

import D;

public class TF_TD_Libraries$point_div
{
  public int row;

  public TF_TD_Libraries$point_div(TF_TD_Libraries paramTF_TD_Libraries)
  {
  }

  public double[] div(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    Object localObject = new TF_TD_Libraries.Diff(this.a);
    this.row = ((TF_TD_Libraries.Diff)localObject).return_row(paramArrayOfDouble1, this.row);
    localObject = new double[this.row];
    for (int i = 0; i < this.row; i++)
      paramArrayOfDouble1[i] /= paramArrayOfDouble2[i];
    return (D)localObject;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.point_div
 * JD-Core Version:    0.6.0
 */