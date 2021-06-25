package com.neurosky.thinkgear;

public class EEGAlgorithmToolbox$IFFTResult
{
  protected double[] real;
  protected double[] imaginary;
  protected double[] power;

  public EEGAlgorithmToolbox$IFFTResult(EEGAlgorithmToolbox paramEEGAlgorithmToolbox, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    this.real = paramArrayOfDouble1;
    this.imaginary = paramArrayOfDouble2;
    this.power = a();
  }

  public double[] getReal()
  {
    return this.real;
  }

  public double[] getImaginary()
  {
    return this.imaginary;
  }

  public double[] getPower()
  {
    return this.power;
  }

  private double[] a()
  {
    this.power = new double[this.real.length];
    for (int i = 0; i < this.power.length; i++)
      this.power[i] = Math.pow(Math.pow(Math.abs(this.real[i]), 2.0D) + Math.pow(Math.abs(this.imaginary[i]), 2.0D), 0.5D);
    return this.power;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.IFFTResult
 * JD-Core Version:    0.6.0
 */