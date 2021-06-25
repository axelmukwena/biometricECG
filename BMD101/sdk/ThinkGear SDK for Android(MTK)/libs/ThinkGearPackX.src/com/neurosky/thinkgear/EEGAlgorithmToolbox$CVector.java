package com.neurosky.thinkgear;

import android.util.Log;

public class EEGAlgorithmToolbox$CVector
{
  private int a;
  private EEGAlgorithmToolbox.Complex[] b;

  public EEGAlgorithmToolbox$CVector(EEGAlgorithmToolbox paramEEGAlgorithmToolbox, int paramInt)
  {
    this.a = paramInt;
    this.b = new EEGAlgorithmToolbox.Complex[paramInt];
    for (paramEEGAlgorithmToolbox = 0; paramEEGAlgorithmToolbox < paramInt; paramEEGAlgorithmToolbox++)
      this.b[paramEEGAlgorithmToolbox] = EEGAlgorithmToolbox.Complex.CZero;
  }

  public EEGAlgorithmToolbox$CVector(EEGAlgorithmToolbox paramEEGAlgorithmToolbox, EEGAlgorithmToolbox.Complex[] paramArrayOfComplex)
  {
    this.a = paramArrayOfComplex.length;
    this.b = paramArrayOfComplex;
  }

  public EEGAlgorithmToolbox.Complex setVector(int paramInt)
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
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.CVector
 * JD-Core Version:    0.6.0
 */