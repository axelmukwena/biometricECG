package com.neurosky.thinkgear;

public class TGHrvBuffered
{
  private TGHrv a = new TGHrv();
  public int[] buffer;
  public int[] bufferTemp;
  public int bufferCount;
  public int bufferMidPos;
  public int adjustPeakLatency;
  public int epochLen;

  public TGHrvBuffered()
  {
    this.epochLen = 120;
    this.adjustPeakLatency = 20;
    this.buffer = new int[this.epochLen + 2 * this.adjustPeakLatency];
    this.bufferTemp = new int[this.epochLen + 2 * this.adjustPeakLatency];
    this.bufferCount = 0;
    this.bufferMidPos = (this.buffer.length / 2);
  }

  public TGHrvBuffered(int paramInt1, int paramInt2)
  {
    this.epochLen = paramInt1;
    this.adjustPeakLatency = paramInt2;
    this.buffer = new int[this.epochLen + 2 * this.adjustPeakLatency];
    this.bufferTemp = new int[this.epochLen + 2 * this.adjustPeakLatency];
    this.bufferCount = 0;
    this.bufferMidPos = (this.buffer.length / 2);
  }

  public void Reset()
  {
    this.a.Reset();
  }

  public int[] AddData(int paramInt)
  {
    System.arraycopy(this.buffer, 1, this.bufferTemp, 0, this.buffer.length - 1);
    this.bufferTemp[(this.bufferTemp.length - 1)] = paramInt;
    System.arraycopy(this.bufferTemp, 0, this.buffer, 0, this.buffer.length);
    this.bufferCount += 1;
    if (this.bufferCount >= this.buffer.length)
    {
      paramInt = this.a.AddData(this.buffer[this.bufferMidPos]);
      int i = this.buffer[this.bufferMidPos];
      int j = this.bufferMidPos;
      if (paramInt > 0)
      {
        for (paramInt = this.bufferMidPos - this.adjustPeakLatency; paramInt < this.bufferMidPos + this.adjustPeakLatency; paramInt++)
        {
          if (i >= this.buffer[paramInt])
            continue;
          i = this.buffer[paramInt];
          j = paramInt;
        }
        paramInt = new int[this.epochLen];
        System.arraycopy(this.bufferTemp, j - paramInt.length / 2, paramInt, 0, paramInt.length);
        return paramInt;
      }
      return null;
    }
    return null;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TGHrvBuffered
 * JD-Core Version:    0.6.0
 */