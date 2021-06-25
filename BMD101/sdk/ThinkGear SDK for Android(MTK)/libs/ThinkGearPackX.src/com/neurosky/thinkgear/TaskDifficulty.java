package com.neurosky.thinkgear;

public class TaskDifficulty
{
  public static boolean isProvisioned()
  {
    return true;
  }

  public double TaskDifficulty(short[] paramArrayOfShort)
  {
    double[] arrayOfDouble1 = { 11.845260043490633D, -0.381357581804967D, 0.8494898743668881D, 0.204202722295334D };
    int[] arrayOfInt = { 29, 15, 20 };
    Object localObject1 = new double[28160];
    for (int j = 0; j < 28160; j++)
      localObject1[j] = paramArrayOfShort[j];
    Object localObject2 = new TF_TD_Libraries();
    Object tmp96_94 = localObject2;
    tmp96_94.getClass();
    paramArrayOfShort = (paramArrayOfShort = new TF_TD_Libraries.TF_TD_PREPROCESSING(tmp96_94)).TF_TD_preprocessing(localObject1, 512);
    Object tmp121_119 = localObject2;
    tmp121_119.getClass();
    double d1 = (paramArrayOfShort = (localObject1 = new TF_TD_Libraries.TF_TD_FEATUREEXTRACTION(tmp121_119)).TF_TD_featureextraction(paramArrayOfShort, 512, 2, 1)).get_hjorth_activity();
    double d2 = paramArrayOfShort.get_hjorth_mobility();
    double d3 = paramArrayOfShort.get_hjorth_complexity();
    localObject1 = paramArrayOfShort.get_bp_row_mean();
    paramArrayOfShort.get_bp_row_mean();
    localObject2 = paramArrayOfShort.get_bp_activity();
    double[] arrayOfDouble2 = paramArrayOfShort.get_bp_mobility();
    paramArrayOfShort = paramArrayOfShort.get_bp_complexity();
    int k;
    double[] arrayOfDouble3 = new double[k = localObject1.length + 1 + 1 + 1 + localObject2.length + arrayOfDouble2.length + paramArrayOfShort.length];
    int m = 0;
    for (int n = 0; n < localObject1.length; n++)
    {
      arrayOfDouble3[m] = localObject1[n];
      m++;
    }
    arrayOfDouble3[m] = d1;
    m++;
    arrayOfDouble3[m] = d2;
    m++;
    arrayOfDouble3[m] = d3;
    m++;
    for (n = 0; n < localObject2.length; n++)
    {
      arrayOfDouble3[m] = localObject2[n];
      m++;
    }
    for (n = 0; n < arrayOfDouble2.length; n++)
    {
      arrayOfDouble3[m] = arrayOfDouble2[n];
      m++;
    }
    for (n = 0; n < paramArrayOfShort.length; n++)
    {
      arrayOfDouble3[m] = paramArrayOfShort[n];
      m++;
    }
    for (n = 0; n < arrayOfDouble3.length; n++)
      arrayOfDouble3[n] = Math.log10(arrayOfDouble3[n]);
    n = arrayOfInt[0] - 1;
    paramArrayOfShort = arrayOfInt[1] - 1;
    int i = arrayOfInt[2] - 1;
    double d4;
    return d4 = arrayOfDouble1[0] + arrayOfDouble1[1] * arrayOfDouble3[n] + arrayOfDouble1[2] * arrayOfDouble3[paramArrayOfShort] + arrayOfDouble1[3] * arrayOfDouble3[i];
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TaskDifficulty
 * JD-Core Version:    0.6.0
 */