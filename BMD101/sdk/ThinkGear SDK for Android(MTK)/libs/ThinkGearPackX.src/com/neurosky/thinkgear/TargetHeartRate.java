package com.neurosky.thinkgear;

public class TargetHeartRate
{
  private int a;
  private double b;
  private double c;

  public int[] getTargetHeartRate(int paramInt, String paramString1, String paramString2)
  {
    if (paramString1.equals("Male"))
      this.a = (220 - paramInt);
    else if (paramString1.equals("Female"))
      this.a = (226 - paramInt);
    else
      return new int[] { -1, -1 };
    if (paramString2.equals("Light Exercise"))
    {
      this.b = (0.5D * this.a);
      this.c = (0.6D * this.a);
    }
    else if (paramString2.equals("Weight Loss"))
    {
      this.b = (0.6D * this.a);
      this.c = (0.7D * this.a);
    }
    else if (paramString2.equals("Aerobic"))
    {
      this.b = (0.7D * this.a);
      this.c = (0.8D * this.a);
    }
    else if (paramString2.equals("Conditioning"))
    {
      this.b = (0.8D * this.a);
      this.c = (0.9D * this.a);
    }
    else if (paramString2.equals("Athletic"))
    {
      this.b = (0.9D * this.a);
      this.c = (1.0D * this.a);
    }
    else
    {
      return new int[] { -1, -1 };
    }
    return new int[] { (int)Math.floor(this.b + 0.5D), (int)Math.floor(this.c + 0.5D) };
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TargetHeartRate
 * JD-Core Version:    0.6.0
 */