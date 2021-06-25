package com.neurosky.thinkgear;

public class TGEegPower
{
  public int delta = 0;
  public int theta = 0;
  public int lowAlpha = 0;
  public int highAlpha = 0;
  public int lowBeta = 0;
  public int highBeta = 0;
  public int lowGamma = 0;
  public int midGamma = 0;

  public TGEegPower(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    this.delta = paramInt1;
    this.theta = paramInt2;
    this.lowAlpha = paramInt3;
    this.highAlpha = paramInt4;
    this.lowBeta = paramInt5;
    this.highBeta = paramInt6;
    this.lowGamma = paramInt7;
    this.midGamma = paramInt8;
  }

  public TGEegPower()
  {
    this(0, 0, 0, 0, 0, 0, 0, 0);
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TGEegPower
 * JD-Core Version:    0.6.0
 */