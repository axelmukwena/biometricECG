package com.neurosky.thinkgear;

public final class TGHrv
{
  public boolean verbose = false;
  private int a;
  private int b;
  private int c;
  private int d;
  private int e;
  private int f;
  private int g;
  private int h;
  private int i;
  private int j;
  private int k;
  private int l;
  private int m;
  private int n;
  private int o;
  private int p;
  private int q;
  private int r;
  private boolean s;
  private boolean t;
  private boolean u;
  private int v;
  private int w;
  private int x;
  private int y;
  private int z;
  private int A;
  private int B;
  private int C;
  private int D;
  private int E;
  private int F;
  private int G;
  private int H;
  private int I;
  private int J;
  private double K;

  public TGHrv()
  {
    Reset();
  }

  public final void Reset(double paramDouble)
  {
    Reset();
    this.C = (int)paramDouble;
  }

  public final void Reset()
  {
    this.a = (this.b = this.c = this.d = this.e = this.f = this.g = this.h = this.i = this.j = this.k = this.l = this.m = this.n = this.o = this.p = 0);
    this.q = 0;
    this.r = 0;
    this.s = false;
    this.t = false;
    this.u = false;
    this.v = 512;
    this.w = 0;
    this.x = 0;
    this.y = 3000;
    this.z = 3000;
    this.A = 0;
    this.B = 0;
    this.C = 6;
    this.F = 0;
    this.D = 45;
    this.E = 70;
    this.J = 0;
    this.G = 0;
    this.H = 0;
    this.I = 153;
    this.K = 0.5D;
  }

  public final int AddData(int paramInt)
  {
    System.nanoTime();
    int i1 = -1;
    this.a = this.b;
    this.b = this.c;
    this.c = this.d;
    this.d = this.e;
    this.e = this.f;
    this.f = this.g;
    this.g = this.h;
    this.h = this.i;
    this.i = this.j;
    this.j = this.k;
    this.k = this.l;
    this.l = this.m;
    this.m = this.n;
    this.n = this.o;
    this.o = this.p;
    this.p = paramInt;
    this.x = (this.a - this.p);
    if (this.q == this.v << 1)
    {
      this.r += 2;
      this.q = 0;
      if (this.z > 3 * this.y / 2)
        this.y = (3 * this.y / 2);
      else
        this.y = this.z;
      this.y = Math.min(this.y, 6000);
      this.y = Math.max(this.y, 600);
      this.z = 0;
    }
    this.q += 1;
    if (this.x >= this.z)
      this.z = this.x;
    if (this.r < 2)
      this.y = this.z;
    if (this.r >= 0)
    {
      this.A = (this.y * this.D / 100);
      this.B = (this.y * this.E / 100);
      if ((!this.s) && (this.x >= this.A))
        this.s = true;
      if ((this.s) && (this.x <= this.B) && (!this.u))
        this.t = true;
      if ((this.t) && ((this.x >= this.A) || (-this.x >= 3 * this.A / 2 + 1)))
      {
        this.u = true;
        if (this.F < this.I)
        {
          this.t = false;
          if (this.H < 2)
            this.F = 0;
          this.w = 0;
        }
        if (this.F >= this.I)
        {
          this.t = false;
          this.w = this.F;
          this.F = 0;
        }
      }
      if ((this.F == 0) && (this.w != 0))
      {
        if (this.J == 0)
          this.J = this.w;
        else
          i1 = this.w;
        this.H += 1;
        if (this.w > 1500)
        {
          this.H = 0;
          this.G = 0;
          this.I = 153;
        }
        if (this.H >= 3)
          if (this.G == 0)
          {
            if ((this.w > 153) && (this.w < 1000))
              this.G = this.w;
          }
          else if ((this.w > 153) && (this.w < 5 * this.G / 2) && (this.w < 1024))
            this.G = ((this.w + this.C * this.G) / (this.C + 1));
        if ((this.H > 6) || ((this.G > 300) && (this.G < 400)))
          this.I = (int)Math.round(this.K * this.G);
      }
      if (this.u)
      {
        this.F += 1;
        if (this.x <= this.B)
          this.t = true;
      }
    }
    System.nanoTime();
    return i1;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TGHrv
 * JD-Core Version:    0.6.0
 */