package com.neurosky.thinkgear;

public class TF_TD_Libraries$Diff
{
  private int a;

  public TF_TD_Libraries$Diff(TF_TD_Libraries paramTF_TD_Libraries)
  {
  }

  public int return_row(double[] paramArrayOfDouble, int paramInt)
  {
    return paramInt = paramArrayOfDouble.length;
  }

  public double[] applydiff(double[] paramArrayOfDouble)
  {
    new Diff(this.b);
    this.a = return_row(paramArrayOfDouble, this.a);
    double[] arrayOfDouble = new double[this.a];
    for (int i = 0; i < this.a; i++)
      if (i == 0)
        arrayOfDouble[i] = paramArrayOfDouble[i];
      else
        paramArrayOfDouble[i] -= paramArrayOfDouble[(i - 1)];
    return arrayOfDouble;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.Diff
 * JD-Core Version:    0.6.0
 */