package com.neurosky.thinkgear;

import java.util.ArrayList;
import java.util.List;

public class HeartRateAcceleration
{
  private long a = System.currentTimeMillis();
  private List b = new ArrayList();
  private int c;
  private int d = 0;
  private int e = 0;
  private int f;
  private int[] g = { -1, -1 };
  private int[] h = new int[2];

  public HeartRateAcceleration()
  {
    this.f = 10000;
  }

  public HeartRateAcceleration(int paramInt)
  {
    this.f = (paramInt * 1000);
  }

  public int[] getAcceleration(int paramInt, byte paramByte)
  {
    if (paramByte == 200)
    {
      this.b.add(Integer.valueOf(paramInt));
      if (System.currentTimeMillis() - this.a >= this.f)
      {
        this.e += 1;
        for (paramInt = 0; paramInt < this.b.size(); paramInt++)
          this.c += ((Integer)this.b.get(paramInt)).intValue();
        this.c = (int)(this.c / this.b.size());
        this.h[0] = this.c;
        if (this.e == 1)
          this.h[1] = 0;
        else
          this.h[1] = (this.c - this.d);
        this.d = this.c;
        this.b.clear();
        this.a = System.currentTimeMillis();
        return this.h;
      }
      return this.g;
    }
    this.e = 0;
    this.b.clear();
    this.a = System.currentTimeMillis();
    return this.g;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.HeartRateAcceleration
 * JD-Core Version:    0.6.0
 */