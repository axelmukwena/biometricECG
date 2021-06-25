package com.neurosky.thinkgear;

public class PSD
{
  private FFT a;
  private FFTResult b;
  private HammingWindow c;
  private int d;
  private int e;
  private int f;
  private float g = 0.5F;
  private float[] h;
  private float[] i;
  private float[] j;
  private float[] k;
  private float[] l;
  private float m;
  private float n;
  private float[] o;
  private int p;
  private int q;
  private int r;
  private int s;

  public PSD(int paramInt1, int paramInt2, int paramInt3)
  {
    this.p = paramInt3;
    this.e = paramInt1;
    this.q = paramInt2;
    this.j = new float[this.e];
    this.l = new float[this.q / 2 + 1];
    this.c = new HammingWindow(this.e);
    this.h = this.c.generateCoeffs(this.e);
    this.m = a(this.h);
    this.o = new float[this.q / 2 + 1];
    this.r = 0;
    while (this.r <= this.q / 2)
    {
      this.o[this.r] = (paramInt3 / this.q * this.r);
      this.r += 1;
    }
    this.a = new FFT();
  }

  public PSDResult pWelch(float[] paramArrayOfFloat)
  {
    this.d = paramArrayOfFloat.length;
    if (this.e > this.d)
      return new PSDResult(new float[] { -1.0F }, new float[] { -1.0F });
    this.f = ((this.d - (int)(this.e * this.g)) / (this.e - (int)(this.e * this.g)));
    this.i = new float[this.q / 2 + 1];
    this.r = 0;
    while (this.r < this.f)
    {
      System.arraycopy(paramArrayOfFloat, this.e / 2 * this.r, this.j, 0, this.e);
      this.j = this.c.applyCoeffs(this.j);
      this.b = this.a.calculateFFT(this.j, this.l, 1, this.q);
      this.k = this.b.getPower();
      this.s = 0;
      while (this.s < this.q / 2 + 1)
      {
        this.i[this.s] = (float)(this.i[this.s] + Math.pow(this.k[this.s], 2.0D));
        this.s += 1;
      }
      this.r += 1;
    }
    this.r = 1;
    while (this.r < this.i.length - 1)
    {
      this.i[this.r] *= 2.0F;
      this.r += 1;
    }
    this.n = (this.f * this.p * this.m);
    this.r = 0;
    while (this.r < this.i.length)
    {
      this.i[this.r] /= this.n;
      this.r += 1;
    }
    return new PSDResult(this.o, this.i);
  }

  private float a(float[] paramArrayOfFloat)
  {
    float f1 = 0.0F;
    this.r = 0;
    while (this.r < this.e)
    {
      f1 = (float)(f1 + Math.pow(paramArrayOfFloat[this.r], 2.0D));
      this.r += 1;
    }
    return f1;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.PSD
 * JD-Core Version:    0.6.0
 */