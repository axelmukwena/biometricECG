package com.neurosky.thinkgear;

public class EEGAlgorithmToolbox$Bandpower_Return
{
  protected double[][] _bp;
  protected double[][] _rbp;
  protected double[][] _bs;

  public EEGAlgorithmToolbox$Bandpower_Return(EEGAlgorithmToolbox paramEEGAlgorithmToolbox, double[][] paramArrayOfDouble1, double[][] paramArrayOfDouble2, double[][] paramArrayOfDouble3)
  {
    this._bp = paramArrayOfDouble1;
    this._rbp = paramArrayOfDouble2;
    this._bs = paramArrayOfDouble3;
  }

  public double[][] get_bp()
  {
    return this._bp;
  }

  public double[][] get_rbp()
  {
    return this._rbp;
  }

  public double[][] get_bs()
  {
    return this._bs;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.Bandpower_Return
 * JD-Core Version:    0.6.0
 */