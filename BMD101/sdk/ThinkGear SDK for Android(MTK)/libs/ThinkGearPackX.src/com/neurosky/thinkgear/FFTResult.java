package com.neurosky.thinkgear;

public class FFTResult
{
  protected float[] real;
  protected float[] imaginary;
  protected float[] power;

  public FFTResult(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    this.real = paramArrayOfFloat1;
    this.imaginary = paramArrayOfFloat2;
    this.power = a();
  }

  public float[] getReal()
  {
    return this.real;
  }

  public float[] getImaginary()
  {
    return this.imaginary;
  }

  public float[] getPower()
  {
    return this.power;
  }

  private float[] a()
  {
    this.power = new float[this.real.length];
    for (int i = 0; i < this.power.length; i++)
      this.power[i] = (float)Math.pow(Math.pow(Math.abs(this.real[i]), 2.0D) + Math.pow(Math.abs(this.imaginary[i]), 2.0D), 0.5D);
    return this.power;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.FFTResult
 * JD-Core Version:    0.6.0
 */