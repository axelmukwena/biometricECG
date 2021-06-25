package com.neurosky.thinkgear;

import java.util.List;

public class LinearInterpolation
{
  private int a;
  private float b;
  private float[] c;
  private int d;

  public float[] interpolate(int[] paramArrayOfInt1, float[] paramArrayOfFloat, int[] paramArrayOfInt2)
  {
    this.a = 1;
    this.b = ((paramArrayOfFloat[1] - paramArrayOfFloat[0]) / (paramArrayOfInt1[1] - paramArrayOfInt1[0]));
    this.c = new float[paramArrayOfInt2.length];
    this.d = 0;
    while (this.d < paramArrayOfInt2.length)
    {
      if ((paramArrayOfInt2[this.d] > paramArrayOfInt1[this.a]) && (this.a < paramArrayOfInt1.length - 1))
      {
        this.a += 1;
        this.b = ((paramArrayOfFloat[this.a] - paramArrayOfFloat[(this.a - 1)]) / (paramArrayOfInt1[this.a] - paramArrayOfInt1[(this.a - 1)]));
      }
      this.c[this.d] = (paramArrayOfFloat[(this.a - 1)] + this.b * (paramArrayOfInt2[this.d] - paramArrayOfInt1[(this.a - 1)]));
      this.d += 1;
    }
    return this.c;
  }

  public float[] interpolate(List paramList1, List paramList2, int[] paramArrayOfInt)
  {
    this.a = 1;
    this.b = ((((Float)paramList2.get(1)).floatValue() - ((Float)paramList2.get(0)).floatValue()) / (((Integer)paramList1.get(1)).intValue() - ((Integer)paramList1.get(0)).intValue()));
    this.c = new float[paramArrayOfInt.length];
    this.d = 0;
    while (this.d < paramArrayOfInt.length)
    {
      if ((paramArrayOfInt[this.d] > ((Integer)paramList1.get(this.a)).intValue()) && (this.a < paramList1.size() - 1))
      {
        this.a += 1;
        this.b = ((((Float)paramList2.get(this.a)).floatValue() - ((Float)paramList2.get(this.a - 1)).floatValue()) / (((Integer)paramList1.get(this.a)).intValue() - ((Integer)paramList1.get(this.a - 1)).intValue()));
      }
      this.c[this.d] = (((Float)paramList2.get(this.a - 1)).floatValue() + this.b * (paramArrayOfInt[this.d] - ((Integer)paramList1.get(this.a - 1)).intValue()));
      this.d += 1;
    }
    return this.c;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.LinearInterpolation
 * JD-Core Version:    0.6.0
 */