package com.neurosky.thinkgear;

public class HammingWindow
{
  private float[] a;
  private float[] b;

  public HammingWindow(int paramInt)
  {
    this.a = generateCoeffs(paramInt);
  }

  public float[] generateCoeffs(int paramInt)
  {
    float[] arrayOfFloat = new float[paramInt];
    for (int i = 0; i < paramInt; i++)
      arrayOfFloat[i] = (float)(0.54D - 0.46D * Math.cos(6.283185307179586D * i / (paramInt - 1)));
    return arrayOfFloat;
  }

  public float[] applyCoeffs(float[] paramArrayOfFloat)
  {
    this.b = new float[paramArrayOfFloat.length];
    for (int i = 0; i < paramArrayOfFloat.length; i++)
      this.b[i] = (paramArrayOfFloat[i] * this.a[i]);
    return this.b;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.HammingWindow
 * JD-Core Version:    0.6.0
 */