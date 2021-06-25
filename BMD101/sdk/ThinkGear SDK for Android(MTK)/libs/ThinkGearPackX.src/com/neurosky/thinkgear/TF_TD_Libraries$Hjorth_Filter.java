package com.neurosky.thinkgear;

public class TF_TD_Libraries$Hjorth_Filter
{
  public TF_TD_Libraries$Hjorth_Filter(TF_TD_Libraries paramTF_TD_Libraries)
  {
  }

  public double[] Applyfilter(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3)
  {
    double d1 = paramArrayOfDouble2.length - 1;
    double d2;
    int i = (d2 = paramArrayOfDouble3.length - 1) + 1;
    double d3;
    if (paramArrayOfDouble2[0] != 1.0D)
      for (int j = 0; j < d1 + 1; j++)
      {
        d3 = paramArrayOfDouble2[0];
        paramArrayOfDouble1[j] /= d3;
        paramArrayOfDouble2[j] /= d3;
        paramArrayOfDouble2[0] = d3;
      }
    double[] arrayOfDouble = new double[i];
    if (d2 < d1)
      for (d3 = d2; d3 < d1; d3++)
      {
        paramArrayOfDouble3[d3] = 0.0D;
        d2 = d1;
      }
    for (double d4 = 0; d4 < d2 + 1; d4++)
      arrayOfDouble[d4] = 0.0D;
    paramArrayOfDouble1[0] *= paramArrayOfDouble3[0];
    for (d4 = 1; d4 < d1; d4++)
    {
      arrayOfDouble[d4] = 0.0D;
      for (double d6 = 0; d6 < d4 + 1; d6++)
        arrayOfDouble[d4] += paramArrayOfDouble1[d6] * paramArrayOfDouble3[(d4 - d6)];
      for (double d7 = 0; d7 < d4; d7++)
        arrayOfDouble[d4] -= paramArrayOfDouble2[(d7 + 1)] * arrayOfDouble[(d4 - d7 - 1)];
    }
    for (double d5 = d1; d5 < d2 + 1; d5++)
    {
      arrayOfDouble[d5] = 0.0D;
      for (int k = 0; k < d1 + 1; k++)
        arrayOfDouble[d5] += paramArrayOfDouble1[k] * paramArrayOfDouble3[(d5 - k)];
      for (double d8 = 0; d8 < d1; d8++)
        arrayOfDouble[d5] -= paramArrayOfDouble2[(d8 + 1)] * arrayOfDouble[(d5 - d8 - 1)];
    }
    return arrayOfDouble;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.Hjorth_Filter
 * JD-Core Version:    0.6.0
 */