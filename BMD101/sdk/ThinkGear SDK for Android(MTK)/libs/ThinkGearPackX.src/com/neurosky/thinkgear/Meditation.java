package com.neurosky.thinkgear;

public class Meditation
{
  public static int getZone(int paramInt, byte paramByte)
  {
    return paramInt = (int)Math.floor(0.85D * paramInt + 0.15D * (paramByte & 0xFF) + 0.5D);
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.Meditation
 * JD-Core Version:    0.6.0
 */