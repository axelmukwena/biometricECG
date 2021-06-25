package com.neurosky.thinkgear;

public class TGHrvArray
{
  private TGHrv a = new TGHrv();

  public void reset()
  {
    this.a.Reset();
  }

  public void reset(double paramDouble)
  {
    this.a.Reset(paramDouble);
  }

  public int[] processArray(int[] paramArrayOfInt)
  {
    int j = 0;
    int[] arrayOfInt1 = new int[60000];
    for (int k = 0; k < paramArrayOfInt.length; k++)
    {
      int i;
      if ((i = this.a.AddData(paramArrayOfInt[k])) <= 0)
        continue;
      arrayOfInt1[j] = k;
      j += 1;
    }
    int[] arrayOfInt2 = new int[j];
    System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, j);
    return arrayOfInt2;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TGHrvArray
 * JD-Core Version:    0.6.0
 */