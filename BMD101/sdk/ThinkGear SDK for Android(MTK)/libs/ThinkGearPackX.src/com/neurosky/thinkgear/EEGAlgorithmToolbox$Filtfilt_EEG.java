package com.neurosky.thinkgear;

public class EEGAlgorithmToolbox$Filtfilt_EEG
{
  public EEGAlgorithmToolbox$Filtfilt_EEG(EEGAlgorithmToolbox paramEEGAlgorithmToolbox)
  {
  }

  public double[] ZeroPhaseFiltering(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3)
  {
    double d = paramArrayOfDouble2.length;
  }

  private static double[] a(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3, double[] paramArrayOfDouble4)
  {
    double[] arrayOfDouble1 = new double[paramArrayOfDouble3.length];
    for (int i = 0; i < paramArrayOfDouble3.length; i++)
      for (int j = 0; ((i >= j ? 1 : 0) & (j < paramArrayOfDouble4.length ? 1 : 0)) != 0; j++)
        arrayOfDouble1[i] += paramArrayOfDouble1[j] * paramArrayOfDouble3[(i - j)] - paramArrayOfDouble2[j] * arrayOfDouble1[(i - j)];
    double[] arrayOfDouble2;
    (arrayOfDouble2 = new double[arrayOfDouble1.length])[0] = (arrayOfDouble1[0] + paramArrayOfDouble4[0]);
    int m;
    for (int k = 1; k < paramArrayOfDouble4.length; k++)
    {
      double d1 = 0.0D;
      double d2 = 0.0D;
      for (m = 0; ((k >= m ? 1 : 0) & (m < paramArrayOfDouble4.length ? 1 : 0)) != 0; m++)
        d1 += paramArrayOfDouble1[m] * paramArrayOfDouble3[(k - m)];
      for (m = 1; ((k >= m ? 1 : 0) & (m < paramArrayOfDouble4.length ? 1 : 0)) != 0; m++)
        d2 += paramArrayOfDouble2[m] * arrayOfDouble2[(k - m)];
      arrayOfDouble2[k] = (d1 - d2 + paramArrayOfDouble4[k]);
    }
    for (k = paramArrayOfDouble4.length; k < arrayOfDouble1.length; k++)
    {
      for (m = 0; m < paramArrayOfDouble4.length + 1; m++)
        arrayOfDouble2[k] += paramArrayOfDouble1[m] * paramArrayOfDouble3[(k - m)];
      for (m = 0; m < paramArrayOfDouble4.length; m++)
        arrayOfDouble2[k] -= paramArrayOfDouble2[(m + 1)] * arrayOfDouble2[(k - m - 1)];
    }
    return arrayOfDouble2;
  }

  private static double[] a(double[] paramArrayOfDouble)
  {
    double[] arrayOfDouble = new double[paramArrayOfDouble.length];
    for (int i = 0; i < paramArrayOfDouble.length; i++)
      arrayOfDouble[i] = paramArrayOfDouble[(paramArrayOfDouble.length - 1 - i)];
    return arrayOfDouble;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.Filtfilt_EEG
 * JD-Core Version:    0.6.0
 */