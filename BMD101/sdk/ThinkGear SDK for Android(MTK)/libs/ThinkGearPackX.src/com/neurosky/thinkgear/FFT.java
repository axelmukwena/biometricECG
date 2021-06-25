package com.neurosky.thinkgear;

public class FFT
{
  private float[] a;
  private float[] b;

  public FFTResult calculateFFT(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, int paramInt1, int paramInt2)
  {
    this.a = new float[paramInt2];
    this.b = new float[paramInt2];
    System.arraycopy(paramArrayOfFloat1, 0, this.a, 0, Math.min(paramArrayOfFloat1.length, paramInt2));
    System.arraycopy(paramArrayOfFloat2, 0, this.b, 0, Math.min(paramArrayOfFloat2.length, paramInt2));
    float[] arrayOfFloat4 = (int)(Math.log(this.a.length) / Math.log(2.0D));
    paramArrayOfFloat1 = 1;
    for (paramArrayOfFloat2 = 0; paramArrayOfFloat2 < arrayOfFloat4; paramArrayOfFloat2++)
      paramArrayOfFloat1 <<= 1;
    float[] arrayOfFloat2 = paramArrayOfFloat1 >> 1;
    float[] arrayOfFloat1 = 0;
    float f1;
    int i;
    for (paramArrayOfFloat2 = 0; paramArrayOfFloat2 < paramArrayOfFloat1 - 1; paramArrayOfFloat2++)
    {
      if (paramArrayOfFloat2 < arrayOfFloat1)
      {
        paramInt2 = this.a[paramArrayOfFloat2];
        f1 = this.b[paramArrayOfFloat2];
        this.a[paramArrayOfFloat2] = this.a[arrayOfFloat1];
        this.b[paramArrayOfFloat2] = this.b[arrayOfFloat1];
        this.a[arrayOfFloat1] = paramInt2;
        this.b[arrayOfFloat1] = f1;
      }
      paramInt2 = arrayOfFloat2;
      while (paramInt2 <= arrayOfFloat1)
      {
        arrayOfFloat1 -= paramInt2;
        paramInt2 >>= 1;
      }
      i += paramInt2;
    }
    float f2 = -1.0F;
    float f3 = 0.0F;
    float[] arrayOfFloat3 = 1;
    for (arrayOfFloat2 = 0; arrayOfFloat2 < arrayOfFloat4; arrayOfFloat2++)
    {
      f1 = arrayOfFloat3;
      arrayOfFloat3 <<= 1;
      float f6 = 1.0F;
      float f7 = 0.0F;
      for (i = 0; i < f1; i++)
      {
        paramArrayOfFloat2 = i;
        while (paramArrayOfFloat2 < paramArrayOfFloat1)
        {
          paramInt2 = paramArrayOfFloat2 + f1;
          float f4 = f6 * this.a[paramInt2] - f7 * this.b[paramInt2];
          float f5 = f6 * this.b[paramInt2] + f7 * this.a[paramInt2];
          this.a[paramArrayOfFloat2] -= f4;
          this.b[paramArrayOfFloat2] -= f5;
          this.a[paramArrayOfFloat2] += f4;
          this.b[paramArrayOfFloat2] += f5;
          paramArrayOfFloat2 += arrayOfFloat3;
        }
        paramArrayOfFloat2 = f6 * f2 - f7 * f3;
        f7 = f6 * f3 + f7 * f2;
        f6 = paramArrayOfFloat2;
      }
      f3 = (float)Math.sqrt((1.0D - f2) / 2.0D);
      if (paramInt1 == 1)
        f3 = -f3;
      f2 = (float)Math.sqrt((1.0D + f2) / 2.0D);
    }
    if (paramInt1 == -1)
      for (paramArrayOfFloat2 = 0; paramArrayOfFloat2 < paramArrayOfFloat1; paramArrayOfFloat2++)
      {
        this.a[paramArrayOfFloat2] /= paramArrayOfFloat1;
        this.b[paramArrayOfFloat2] /= paramArrayOfFloat1;
      }
    return new FFTResult(this.a, this.b);
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.FFT
 * JD-Core Version:    0.6.0
 */