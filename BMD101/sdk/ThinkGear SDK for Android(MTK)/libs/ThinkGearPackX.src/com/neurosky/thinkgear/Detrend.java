package com.neurosky.thinkgear;

public class Detrend
{
  private float a;
  private float[] b;
  private float[] c;
  private float[] d;
  private float[] e;
  private float f;
  private float g;
  private int h;
  private int i;

  public float[] removeLinearTrend(float[] paramArrayOfFloat)
  {
    this.i = paramArrayOfFloat.length;
    this.d = new float[this.i];
    this.e = new float[this.i];
    this.h = 0;
    while (this.h < this.i)
    {
      this.e[this.h] = (this.h + 1);
      this.h += 1;
    }
    this.f = (float)((a(paramArrayOfFloat) * a(b(this.e)) - a(this.e) * a(a(this.e, paramArrayOfFloat))) / (this.i * a(b(this.e)) - Math.pow(a(this.e), 2.0D)));
    this.g = (float)((this.i * a(a(this.e, paramArrayOfFloat)) - a(this.e) * a(paramArrayOfFloat)) / (this.i * a(b(this.e)) - Math.pow(a(this.e), 2.0D)));
    this.h = 0;
    while (this.h < this.i)
    {
      this.d[this.h] = (float)(paramArrayOfFloat[this.h] - (this.f + this.g * (this.h + 1.0D)));
      this.h += 1;
    }
    return this.d;
  }

  private float a(float[] paramArrayOfFloat)
  {
    this.a = 0.0F;
    for (int j = 0; j < paramArrayOfFloat.length; j++)
      this.a += paramArrayOfFloat[j];
    return this.a;
  }

  private float[] a(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    this.b = new float[paramArrayOfFloat1.length];
    for (int j = 0; j < paramArrayOfFloat1.length; j++)
      this.b[j] = (paramArrayOfFloat1[j] * paramArrayOfFloat2[j]);
    return this.b;
  }

  private float[] b(float[] paramArrayOfFloat)
  {
    this.c = new float[paramArrayOfFloat.length];
    for (int j = 0; j < paramArrayOfFloat.length; j++)
      this.c[j] = (float)Math.pow(paramArrayOfFloat[j], 2.0D);
    return this.c;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.Detrend
 * JD-Core Version:    0.6.0
 */