package com.neurosky.thinkgear;

import D;

public class TF_TD_Libraries$point_sqr
{
  public int row;

  public TF_TD_Libraries$point_sqr(TF_TD_Libraries paramTF_TD_Libraries)
  {
  }

  public double[] sqr(double[] paramArrayOfDouble)
  {
    Object localObject = new TF_TD_Libraries.Diff(this.a);
    this.row = ((TF_TD_Libraries.Diff)localObject).return_row(paramArrayOfDouble, this.row);
    localObject = new double[this.row];
    for (int i = 0; i < this.row; i++)
      paramArrayOfDouble[i] *= paramArrayOfDouble[i];
    return (D)localObject;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.point_sqr
 * JD-Core Version:    0.6.0
 */