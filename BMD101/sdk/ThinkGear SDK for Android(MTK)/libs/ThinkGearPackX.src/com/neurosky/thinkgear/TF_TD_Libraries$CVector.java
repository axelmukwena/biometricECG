package com.neurosky.thinkgear;

import android.util.Log;

public class TF_TD_Libraries$CVector
{
  private int a;
  private TF_TD_Libraries.Complex[] b;

  public TF_TD_Libraries$CVector(TF_TD_Libraries paramTF_TD_Libraries, int paramInt)
  {
    this.a = paramInt;
    this.b = new TF_TD_Libraries.Complex[paramInt];
    for (paramTF_TD_Libraries = 0; paramTF_TD_Libraries < paramInt; paramTF_TD_Libraries++)
      this.b[paramTF_TD_Libraries] = TF_TD_Libraries.Complex.CZero;
  }

  public TF_TD_Libraries$CVector(TF_TD_Libraries paramTF_TD_Libraries, TF_TD_Libraries.Complex[] paramArrayOfComplex)
  {
    this.a = paramArrayOfComplex.length;
    this.b = paramArrayOfComplex;
  }

  public TF_TD_Libraries.Complex setVector(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > this.a))
      Log.i("Exception:", "i is out of range");
    return this.b[paramInt];
  }

  public int GetVectorSize()
  {
    return this.a;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.CVector
 * JD-Core Version:    0.6.0
 */