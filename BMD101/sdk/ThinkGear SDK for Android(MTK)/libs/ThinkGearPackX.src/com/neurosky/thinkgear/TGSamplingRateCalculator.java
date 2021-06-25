package com.neurosky.thinkgear;

public class TGSamplingRateCalculator
{
  private int a = 0;
  private int b = 0;
  private long c = System.currentTimeMillis();

  public int calcSamplingRate()
  {
    this.a += 1;
    if (System.currentTimeMillis() - this.c >= 2000L)
    {
      this.b = (int)(this.a / ((System.currentTimeMillis() - this.c) / 1000L));
      if ((this.b > 156) && (this.b < 356))
        this.b = 256;
      else if ((this.b > 412) && (this.b < 612))
        this.b = 512;
      else
        return 0;
      return this.b;
    }
    return 0;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TGSamplingRateCalculator
 * JD-Core Version:    0.6.0
 */