package com.neurosky.thinkgear;

import java.util.ArrayList;
import java.util.List;

public class BlinkFilter
{
  private TGBlinkDetector a = new TGBlinkDetector();
  private SGFilter b = new SGFilter();
  private List c = new ArrayList();
  private int d;
  private int e;
  private int f;
  private int g;
  private int h;
  private float[] i;
  private float j;
  private int k;
  private float[] l;
  private float[] m;
  private int n;

  public BlinkFilter(int paramInt)
  {
    this.d = paramInt;
  }

  public float[] filter(byte paramByte, float[] paramArrayOfFloat)
  {
    this.l = new float[paramArrayOfFloat.length];
    System.arraycopy(paramArrayOfFloat, 0, this.l, 0, paramArrayOfFloat.length);
    this.n = 0;
    this.c.clear();
    this.a.Detect(200, 200);
    for (int i1 = 0; i1 < paramArrayOfFloat.length; i1++)
    {
      if (this.a.Detect(paramByte, (short)(int)paramArrayOfFloat[i1]) != 0)
        if (this.a.blinkStart < this.a.blinkEnd)
        {
          if (this.n - (this.a.blinkEnd - this.a.blinkStart - 1) < 0)
            this.c.add(Integer.valueOf(0));
          else
            this.c.add(Integer.valueOf(this.n - (this.a.blinkEnd - this.a.blinkStart - 1)));
          this.c.add(Integer.valueOf(this.n));
        }
        else
        {
          this.c.add(Integer.valueOf(this.n - (this.a.blinkStart + (512 - this.a.blinkEnd))));
          this.c.add(Integer.valueOf(this.n));
        }
      this.n += 1;
    }
    for (i1 = 0; i1 < this.c.size() / 2; i1++)
    {
      this.e = ((Integer)this.c.get(i1 << 1)).intValue();
      this.f = ((Integer)this.c.get((i1 << 1) + 1)).intValue();
      this.h = 5;
      this.g = (61 * (this.d / 256));
      if (this.g % 2 == 0)
        this.g += 1;
      if (this.f - this.e < this.g)
      {
        this.j = ((this.f - this.e) / 2);
        if ((int)this.j > this.j)
          this.j = ((int)this.j - 1);
        else if ((int)this.j < this.j)
          this.j = (int)this.j;
        this.g = (((int)this.j << 1) + 1);
      }
      if (this.g <= this.h)
        this.h = (this.g - 1);
      if ((this.e == 0) || (this.f == 0))
        this.k = (this.f - this.e + 2);
      else
        this.k = (this.f - this.e + 1);
      this.i = new float[this.k];
      if ((this.e > 0) && (this.f < paramArrayOfFloat.length - 1))
        this.i = a(paramArrayOfFloat[(this.e - 1)], paramArrayOfFloat[(this.f + 1)], this.k);
      else if ((this.e == 0) && (this.f < paramArrayOfFloat.length - 1))
        this.i = a(paramArrayOfFloat[this.e], paramArrayOfFloat[(this.f + 1)], this.k);
      else if ((this.e > 0) && (this.f == paramArrayOfFloat.length - 1))
        this.i = a(paramArrayOfFloat[(this.e - 1)], paramArrayOfFloat[this.f], this.k);
      else
        this.i = a(paramArrayOfFloat[this.e], paramArrayOfFloat[this.f], this.k);
      if (this.g % 2 == 0)
        this.g += 1;
      this.m = new float[this.k];
      System.arraycopy(paramArrayOfFloat, this.e, this.m, 0, this.k);
      this.m = this.b.filterData(this.m, this.g, this.h);
      for (paramByte = 0; paramByte < this.m.length; paramByte++)
        this.l[(this.e + paramByte)] = (paramArrayOfFloat[(this.e + paramByte)] - this.m[paramByte] + this.i[paramByte]);
    }
    return this.l;
  }

  private static float[] a(float paramFloat1, float paramFloat2, int paramInt)
  {
    float[] arrayOfFloat = new float[paramInt];
    paramFloat2 = (paramFloat2 - paramFloat1) / (paramInt - 1);
    arrayOfFloat[0] = paramFloat1;
    for (paramFloat1 = 1; paramFloat1 < paramInt; paramFloat1++)
      arrayOfFloat[paramFloat1] = (arrayOfFloat[(paramFloat1 - 1)] + paramFloat2);
    return arrayOfFloat;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.BlinkFilter
 * JD-Core Version:    0.6.0
 */