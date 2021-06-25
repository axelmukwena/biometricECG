package com.neurosky.thinkgear;

public class Zone
{
  private byte a;
  private byte b;
  private byte c;
  private byte d;
  private byte e;
  private static byte f = -1;
  private static byte g = 3;
  private static byte h = 2;
  private static byte i = 1;
  private static byte j = 0;

  public Zone()
  {
    reset();
  }

  public void reset()
  {
    this.b = (this.a = 0);
    this.c = 0;
    this.e = f;
    this.d = 0;
  }

  public byte getZone()
  {
    return this.e;
  }

  public Boolean update(byte paramByte1, byte paramByte2)
  {
    Boolean localBoolean = Boolean.valueOf(false);
    this.b = this.a;
    this.a = paramByte1;
    this.c = paramByte2;
    if ((this.a >= 82) && (this.b >= 82))
    {
      if (this.e != g)
      {
        this.e = g;
        this.d = this.c;
        localBoolean = Boolean.valueOf(true);
      }
      else if (this.c < this.d - 4)
      {
        this.e = (byte)(this.e - 1);
        this.d = this.c;
        localBoolean = Boolean.valueOf(true);
      }
    }
    else if ((this.a >= 67) && (this.b >= 67))
    {
      if (this.e != h)
      {
        this.e = h;
        this.d = this.c;
        localBoolean = Boolean.valueOf(true);
      }
      else if (this.c < this.d - 4)
      {
        this.e = (byte)(this.e - 1);
        this.d = this.c;
        localBoolean = Boolean.valueOf(true);
      }
    }
    else if ((this.a >= 53) && (this.b >= 53))
    {
      if (this.e != i)
      {
        this.e = i;
        this.d = this.c;
        localBoolean = Boolean.valueOf(true);
      }
      else if (this.c < this.d - 4)
      {
        this.e = (byte)(this.e - 1);
        this.d = this.c;
        localBoolean = Boolean.valueOf(true);
      }
    }
    else if (this.e != 0)
    {
      this.e = 0;
      this.d = 0;
      localBoolean = Boolean.valueOf(true);
    }
    return localBoolean;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.Zone
 * JD-Core Version:    0.6.0
 */