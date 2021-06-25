package com.neurosky.thinkgear;

public class PSDResult
{
  protected float[] power;
  protected float[] frequency;

  public PSDResult(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    this.power = paramArrayOfFloat2;
    this.frequency = paramArrayOfFloat1;
  }

  public float[] getPower()
  {
    return this.power;
  }

  public float[] getFrequency()
  {
    return this.frequency;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.PSDResult
 * JD-Core Version:    0.6.0
 */