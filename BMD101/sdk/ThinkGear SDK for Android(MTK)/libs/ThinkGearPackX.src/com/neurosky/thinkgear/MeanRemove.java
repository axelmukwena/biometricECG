package com.neurosky.thinkgear;

public class MeanRemove
{
  private float[] a;
  private float b;
  private int c;

  public float[] removeMean(float[] paramArrayOfFloat)
  {
    this.a = new float[paramArrayOfFloat.length];
    this.b = 0.0F;
    this.c = 0;
    while (this.c < paramArrayOfFloat.length)
    {
      this.b += paramArrayOfFloat[this.c];
      this.c += 1;
    }
    this.b /= paramArrayOfFloat.length;
    this.c = 0;
    while (this.c < paramArrayOfFloat.length)
    {
      this.a[this.c] = (paramArrayOfFloat[this.c] - this.b);
      this.c += 1;
    }
    return this.a;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.MeanRemove
 * JD-Core Version:    0.6.0
 */