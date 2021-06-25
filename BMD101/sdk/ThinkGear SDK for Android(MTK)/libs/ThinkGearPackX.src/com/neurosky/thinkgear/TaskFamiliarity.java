package com.neurosky.thinkgear;

public class TaskFamiliarity
{
  public static boolean isProvisioned()
  {
    return true;
  }

  public double TaskFamiliarity(short[] paramArrayOfShort)
  {
    double[] arrayOfDouble1 = { 4395.15617934498D, -26293.300251300119D, 24757.94323660191D, 4254936656547.5928D, -40377609470097.977D, -7907.6475620890142D };
    int[] arrayOfInt = { 25, 33, 17, 8, 27 };
    Object localObject1 = new double[28672];
    for (int k = 0; k < 28672; k++)
      localObject1[k] = paramArrayOfShort[k];
    Object localObject2 = new TF_TD_Libraries();
    Object tmp119_117 = localObject2;
    tmp119_117.getClass();
    paramArrayOfShort = (paramArrayOfShort = new TF_TD_Libraries.TF_TD_PREPROCESSING(tmp119_117)).TF_TD_preprocessing(localObject1, 512);
    Object tmp144_142 = localObject2;
    tmp144_142.getClass();
    double d1 = (paramArrayOfShort = (localObject1 = new TF_TD_Libraries.TF_TD_FEATUREEXTRACTION(tmp144_142)).TF_TD_featureextraction(paramArrayOfShort, 512, 2, 1)).get_hjorth_activity();
    double d2 = paramArrayOfShort.get_hjorth_mobility();
    double d3 = paramArrayOfShort.get_hjorth_complexity();
    localObject1 = paramArrayOfShort.get_bp_row_mean();
    paramArrayOfShort.get_rbp_row_mean();
    localObject2 = paramArrayOfShort.get_bp_activity();
    double[] arrayOfDouble2 = paramArrayOfShort.get_bp_mobility();
    paramArrayOfShort = paramArrayOfShort.get_bp_complexity();
    int n;
    double[] arrayOfDouble3 = new double[n = localObject1.length + 1 + 1 + 1 + localObject2.length + arrayOfDouble2.length + paramArrayOfShort.length];
    int i1 = 0;
    for (int i2 = 0; i2 < localObject1.length; i2++)
    {
      arrayOfDouble3[i1] = localObject1[i2];
      i1++;
    }
    arrayOfDouble3[i1] = d1;
    i1++;
    arrayOfDouble3[i1] = d2;
    i1++;
    arrayOfDouble3[i1] = d3;
    i1++;
    for (i2 = 0; i2 < localObject2.length; i2++)
    {
      arrayOfDouble3[i1] = localObject2[i2];
      i1++;
    }
    for (i2 = 0; i2 < arrayOfDouble2.length; i2++)
    {
      arrayOfDouble3[i1] = arrayOfDouble2[i2];
      i1++;
    }
    for (i2 = 0; i2 < paramArrayOfShort.length; i2++)
    {
      arrayOfDouble3[i1] = paramArrayOfShort[i2];
      i1++;
    }
    i2 = arrayOfInt[0] - 1;
    paramArrayOfShort = arrayOfInt[1] - 1;
    int j = arrayOfInt[2] - 1;
    int m = arrayOfInt[3] - 1;
    int i = arrayOfInt[4] - 1;
    double d4;
    return d4 = arrayOfDouble1[0] + arrayOfDouble1[1] * arrayOfDouble3[i2] + arrayOfDouble1[2] * arrayOfDouble3[paramArrayOfShort] + arrayOfDouble1[3] * arrayOfDouble3[j] + arrayOfDouble1[4] * arrayOfDouble3[m] + arrayOfDouble1[5] * arrayOfDouble3[i];
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TaskFamiliarity
 * JD-Core Version:    0.6.0
 */