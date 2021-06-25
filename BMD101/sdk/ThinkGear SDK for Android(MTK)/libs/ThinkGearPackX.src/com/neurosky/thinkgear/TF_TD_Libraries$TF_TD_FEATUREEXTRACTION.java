package com.neurosky.thinkgear;

public class TF_TD_Libraries$TF_TD_FEATUREEXTRACTION
{
  public TF_TD_Libraries$TF_TD_FEATUREEXTRACTION(TF_TD_Libraries paramTF_TD_Libraries)
  {
  }

  public TF_TD_Libraries.FeatureExtrationReturn TF_TD_featureextraction(double[] paramArrayOfDouble, int paramInt1, int paramInt2, int paramInt3)
  {
    Object localObject;
    double d1 = (localObject = (localObject = new TF_TD_Libraries.hjorth(this.a)).hjorth_fun(paramArrayOfDouble, 0)).get_activity();
    double d2 = ((TF_TD_Libraries.Hjorth_Return)localObject).get_mobility();
    double d3 = ((TF_TD_Libraries.Hjorth_Return)localObject).get_complexity();
    paramInt1 = (paramArrayOfDouble = (localObject = new TF_TD_Libraries.BandPower(this.a)).Bandpower_fun(paramArrayOfDouble, paramInt1, paramInt2, paramInt3)).get_bp();
    paramInt2 = paramArrayOfDouble.get_rbp();
    paramArrayOfDouble = paramArrayOfDouble.get_bs();
    paramInt3 = paramInt1.length;
    int i = paramInt1[0].length;
    double[] arrayOfDouble1 = new double[paramInt3];
    for (int j = 0; j < paramInt3; j++)
    {
      double d4 = 0.0D;
      for (k = 0; k < i; k++)
        d4 += paramInt1[j][k];
      arrayOfDouble1[j] = (d4 / i);
    }
    j = paramInt2.length;
    int m = paramInt2[0].length;
    double[] arrayOfDouble3 = new double[j];
    for (int k = 0; k < j; k++)
    {
      double d5 = 0.0D;
      for (paramInt1 = 0; paramInt1 < m; paramInt1++)
        d5 += paramInt2[k][paramInt1];
      arrayOfDouble3[k] = (d5 / m);
    }
    for (k = 0; k < arrayOfDouble1.length; k++)
    {
      if (!Double.isInfinite(arrayOfDouble1[k]))
        continue;
      arrayOfDouble1[k] = 0.0D;
    }
    for (k = 0; k < arrayOfDouble1.length; k++)
    {
      if (!Double.isNaN(arrayOfDouble1[k]))
        continue;
      arrayOfDouble1[k] = 0.0D;
    }
    for (k = 0; k < arrayOfDouble3.length; k++)
    {
      if (!Double.isInfinite(arrayOfDouble3[k]))
        continue;
      arrayOfDouble3[k] = 0.0D;
    }
    for (k = 0; k < arrayOfDouble3.length; k++)
    {
      if (!Double.isNaN(arrayOfDouble3[k]))
        continue;
      arrayOfDouble3[k] = 0.0D;
    }
    double[] arrayOfDouble2 = paramArrayOfDouble;
    int i1;
    for (int n = 0; n < arrayOfDouble2.length; n++)
      for (i1 = 0; i1 < arrayOfDouble2[0].length; i1++)
      {
        if (!Double.isInfinite(arrayOfDouble2[n][i1]))
          continue;
        arrayOfDouble2[n][i1] = 0.0D;
      }
    for (n = 0; n < arrayOfDouble2.length; n++)
      for (i1 = 0; i1 < arrayOfDouble2[0].length; i1++)
      {
        if (!Double.isNaN(arrayOfDouble2[n][i1]))
          continue;
        arrayOfDouble2[n][i1] = 0.0D;
      }
    double[] arrayOfDouble4 = new double[arrayOfDouble2.length];
    double[] arrayOfDouble5 = new double[arrayOfDouble2.length];
    paramInt1 = new double[arrayOfDouble2.length];
    for (paramArrayOfDouble = 0; paramArrayOfDouble < arrayOfDouble2.length; paramArrayOfDouble++)
    {
      paramInt2 = new double[arrayOfDouble2[0].length];
      for (paramInt3 = 0; paramInt3 < arrayOfDouble2[0].length; paramInt3++)
        paramInt2[paramInt3] = arrayOfDouble2[paramArrayOfDouble][paramInt3];
      paramInt2 = (paramInt3 = new TF_TD_Libraries.hjorth(this.a)).hjorth_fun(paramInt2, 0);
      arrayOfDouble4[paramArrayOfDouble] = paramInt2.get_activity();
      arrayOfDouble5[paramArrayOfDouble] = paramInt2.get_mobility();
      paramInt1[paramArrayOfDouble] = paramInt2.get_complexity();
    }
    for (paramArrayOfDouble = 0; paramArrayOfDouble < arrayOfDouble4.length; paramArrayOfDouble++)
    {
      if (!Double.isInfinite(arrayOfDouble4[paramArrayOfDouble]))
        continue;
      arrayOfDouble4[paramArrayOfDouble] = 0.0D;
    }
    for (paramArrayOfDouble = 0; paramArrayOfDouble < arrayOfDouble5.length; paramArrayOfDouble++)
    {
      if (!Double.isNaN(arrayOfDouble5[paramArrayOfDouble]))
        continue;
      arrayOfDouble5[paramArrayOfDouble] = 0.0D;
    }
    for (paramArrayOfDouble = 0; paramArrayOfDouble < paramInt1.length; paramArrayOfDouble++)
    {
      if (!Double.isNaN(paramInt1[paramArrayOfDouble]))
        continue;
      paramInt1[paramArrayOfDouble] = 0.0D;
    }
    return (TF_TD_Libraries.FeatureExtrationReturn)new TF_TD_Libraries.FeatureExtrationReturn(this.a, d1, d2, d3, arrayOfDouble1, arrayOfDouble3, arrayOfDouble4, arrayOfDouble5, paramInt1);
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.TF_TD_FEATUREEXTRACTION
 * JD-Core Version:    0.6.0
 */