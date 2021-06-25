package com.neurosky.thinkgear;

public class TGSleepStager
{
  private short a = 0;
  private short b = -1;
  private int c;
  private final int d;
  private final byte[] e = { 4, 4, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 };

  public TGSleepStager(int paramInt)
  {
    this.c = paramInt;
    this.d = (this.c * 30);
  }

  public byte Detect(int paramInt, short paramShort)
  {
    this.a = (short)(this.a + 1);
    if (this.a == this.d)
    {
      this.b = (short)(this.b + 1);
      this.a = 0;
      if (this.b < 115)
        return this.e[this.b];
      this.b = 0;
      return this.e[this.b];
    }
    return -1;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TGSleepStager
 * JD-Core Version:    0.6.0
 */