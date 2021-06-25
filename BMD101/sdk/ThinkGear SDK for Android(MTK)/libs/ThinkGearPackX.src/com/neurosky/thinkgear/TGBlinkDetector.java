package com.neurosky.thinkgear;

public class TGBlinkDetector
{
  private short a = 0;
  private short b = 0;
  private short[] c = new short[512];
  private short[] d = new short[512];
  private int e = 0;
  public short blinkStart = -1;
  private short f = -1;
  private short g = -1;
  private short h = -1;
  private short i = -1;
  public short blinkEnd = -1;
  private short j = 0;
  private short k = 0;
  private short l = 0;
  private double m = 0.0D;

  public byte Detect(int paramInt, short paramShort)
  {
    if (paramInt < 51)
    {
      System.arraycopy(this.c, 1, this.d, 0, 511);
      this.d[511] = paramShort;
      System.arraycopy(this.d, 0, this.c, 0, 512);
      if (this.b < 512)
        this.b = (short)(this.b + 1);
      if (this.b > 511)
        switch (this.e)
        {
        case 0:
          if (paramShort > 230)
          {
            this.blinkStart = -1;
            this.g = -1;
            this.h = -1;
            this.i = -1;
            this.blinkEnd = -1;
            this.f = 511;
            this.j = paramShort;
            this.e = 1;
          }
          if (paramShort >= -200)
            break;
          this.blinkStart = -1;
          this.g = -1;
          this.h = -1;
          this.i = -1;
          this.blinkEnd = -1;
          this.f = 511;
          this.k = paramShort;
          this.e = 4;
          break;
        case 1:
          if ((511 - this.f > 120) || (this.f <= 0))
            this.e = 0;
          this.f = (short)(this.f - 1);
          if ((paramShort <= 230) && (this.c[510] > 230))
            this.g = 510;
          else
            this.g = (short)(this.g - 1);
          if (paramShort > this.j)
            this.j = paramShort;
          if (paramShort >= -200)
            break;
          this.h = 511;
          this.k = paramShort;
          if (this.h - this.g < 45)
          {
            this.e = 2;
            break;
          }
          this.e = 0;
          break;
        case 4:
          if ((511 - this.f > 120) || (this.f <= 0))
          {
            this.e = 0;
            return 0;
          }
          this.f = (short)(this.f - 1);
          if ((paramShort >= -200) && (this.c[510] < -200))
            this.g = 510;
          else
            this.g = (short)(this.g - 1);
          if (paramShort < this.k)
            this.k = paramShort;
          if (paramShort <= 230)
            break;
          this.h = 511;
          this.j = paramShort;
          if (this.h - this.g < 45)
          {
            this.e = 5;
            break;
          }
          this.e = 0;
          break;
        case 2:
          this.f = (short)(this.f - 1);
          this.g = (short)(this.g - 1);
          this.h = (short)(this.h - 1);
          if ((paramShort >= -200) && (this.c[510] < -200))
          {
            this.i = 510;
            this.e = 3;
          }
          else
          {
            this.i = (short)(this.i - 1);
          }
          if (paramShort < this.k)
            this.k = paramShort;
          if (511 - this.f <= 120)
            break;
          this.i = 511;
          this.e = 3;
          break;
        case 5:
          this.f = (short)(this.f - 1);
          this.g = (short)(this.g - 1);
          this.h = (short)(this.h - 1);
          if ((paramShort <= 230) && (this.c[510] > 230))
          {
            this.i = 510;
            this.e = 6;
          }
          else
          {
            this.i = (short)(this.i - 1);
          }
          if (paramShort > this.j)
            this.j = paramShort;
          if (511 - this.f <= 120)
            break;
          this.i = 511;
          this.e = 6;
          break;
        case 3:
          this.f = (short)(this.f - 1);
          this.g = (short)(this.g - 1);
          this.h = (short)(this.h - 1);
          if (paramShort < -200)
            this.e = 2;
          else
            this.i = (short)(this.i - 1);
          if ((511 - this.i > 25) || (paramShort > 33))
            this.blinkEnd = 511;
          if (this.blinkEnd <= 0)
            break;
          for (this.a = 0; this.a < 25; this.a = (short)(this.a + 1))
          {
            this.blinkStart = (short)(this.f - this.a);
            if (this.c[(this.f - this.a)] < 33)
              break;
          }
          this.l = (short)(this.j - this.k);
          if (this.l < 500)
          {
            this.e = 0;
            return 0;
          }
          this.m = 0.0D;
          for (this.a = this.blinkStart; this.a < this.blinkEnd + 1; this.a = (short)(this.a + 1))
            this.m += this.c[this.a];
          this.m /= (this.blinkEnd - this.blinkStart + 1);
          if (this.m < 0.0D)
            this.m = (-this.m);
          if (this.m > 200.0D)
          {
            this.e = 0;
            return 0;
          }
          if (this.blinkEnd - this.blinkStart < 50)
          {
            this.e = 0;
            return 0;
          }
          if ((this.c[this.blinkStart] > 230) || (this.c[this.blinkStart] < -200))
          {
            this.e = 0;
            return 0;
          }
          if ((this.c[this.blinkEnd] > 230) || (this.c[this.blinkEnd] < -200))
          {
            this.e = 0;
            return 0;
          }
          this.e = 0;
          return (byte)(this.l >> 4);
        case 6:
          this.f = (short)(this.f - 1);
          this.g = (short)(this.g - 1);
          this.h = (short)(this.h - 1);
          if (paramShort > 230)
            this.e = 5;
          else
            this.i = (short)(this.i - 1);
          if ((511 - this.i > 25) || (paramShort < 33))
            this.blinkEnd = 511;
          if (this.blinkEnd <= 0)
            break;
          for (this.a = 0; this.a < 25; this.a = (short)(this.a + 1))
          {
            this.blinkStart = (short)(this.f - this.a);
            if (this.c[(this.f - this.a)] > 33)
              break;
          }
          this.l = (short)(this.j - this.k);
          if (this.l < 500)
          {
            this.e = 0;
            return 0;
          }
          this.m = 0.0D;
          for (this.a = this.blinkStart; this.a < this.blinkEnd + 1; this.a = (short)(this.a + 1))
            this.m += this.c[this.a];
          this.m /= (this.blinkEnd - this.blinkStart + 1);
          if (this.m < 0.0D)
            this.m = (-this.m);
          if (this.m > 200.0D)
          {
            this.e = 0;
            return 0;
          }
          if (this.blinkEnd - this.blinkStart < 50)
          {
            this.e = 0;
            return 0;
          }
          if ((this.c[this.blinkStart] > 230) || (this.c[this.blinkStart] < -200))
          {
            this.e = 0;
            return 0;
          }
          if ((this.c[this.blinkEnd] > 230) || (this.c[this.blinkEnd] < -200))
          {
            this.e = 0;
            return 0;
          }
          this.e = 0;
          return (byte)(this.l >> 4);
        default:
          this.e = 0;
          break;
        }
    }
    else
    {
      this.b = 0;
      this.f = -1;
      this.g = -1;
      this.h = -1;
      this.i = -1;
      this.e = 0;
    }
    return 0;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TGBlinkDetector
 * JD-Core Version:    0.6.0
 */