package com.neurosky.thinkgear;

public class EEGAlgorithmToolbox$Butter_EEG
{
  private EEGAlgorithmToolbox.Complex a = new EEGAlgorithmToolbox.Complex(0.0D, -1.0D);

  public EEGAlgorithmToolbox$Butter_EEG(EEGAlgorithmToolbox paramEEGAlgorithmToolbox)
  {
  }

  public EEGAlgorithmToolbox.Butter_Return ButterworthBandPassFilter(int paramInt, double[] paramArrayOfDouble)
  {
    double d1 = paramArrayOfDouble[0];
    double d2 = paramArrayOfDouble[1];
    double d5 = d2;
    double d3 = d1;
    double[] arrayOfDouble2 = paramInt;
    Object localObject1 = this;
    double[] arrayOfDouble10 = new double[2 * arrayOfDouble2];
    double[] arrayOfDouble11 = new double[2 * arrayOfDouble2];
    double d7 = Math.cos(3.141592653589793D * (d5 + d3) / 2.0D);
    double d6;
    double d8 = Math.sin(d6 = 3.141592653589793D * (d5 - d3) / 2.0D);
    double d9 = Math.cos(d6);
    double d10 = d8 * 2.0D * d9;
    double d11 = d9 * 2.0D * d9 - 1.0D;
    for (int j = 0; j < arrayOfDouble2; j++)
    {
      double d14;
      d15 = Math.sin(d14 = 3.141592653589793D * (2 * j + 1) / (2 * arrayOfDouble2));
      double d16 = Math.cos(d14);
      double d18 = 1.0D + d10 * d15;
      arrayOfDouble10[(2 * j)] = (d11 / d18);
      arrayOfDouble10[(2 * j + 1)] = (d10 * d16 / d18);
      arrayOfDouble11[(2 * j)] = (d7 * -2.0D * (d9 + d8 * d15) / d18);
      arrayOfDouble11[(2 * j + 1)] = (d7 * -2.0D * d8 * d16 / d18);
    }
    double[] arrayOfDouble3 = arrayOfDouble10;
    localObject1 = arrayOfDouble11;
    paramArrayOfDouble = arrayOfDouble2;
    double[] arrayOfDouble4;
    (arrayOfDouble4 = new double[4 * paramArrayOfDouble])[2] = arrayOfDouble3[0];
    arrayOfDouble4[3] = arrayOfDouble3[1];
    arrayOfDouble4[0] = localObject1[0];
    arrayOfDouble4[1] = localObject1[1];
    for (double[] arrayOfDouble5 = 1; arrayOfDouble5 < paramArrayOfDouble; arrayOfDouble5++)
    {
      arrayOfDouble4[(2 * (2 * arrayOfDouble5 + 1))] += arrayOfDouble3[(2 * arrayOfDouble5)] * arrayOfDouble4[(2 * (2 * arrayOfDouble5 - 1))] - arrayOfDouble3[(2 * arrayOfDouble5 + 1)] * arrayOfDouble4[(2 * (2 * arrayOfDouble5 - 1) + 1)];
      arrayOfDouble4[(2 * (2 * arrayOfDouble5 + 1) + 1)] += arrayOfDouble3[(2 * arrayOfDouble5)] * arrayOfDouble4[(2 * (2 * arrayOfDouble5 - 1) + 1)] + arrayOfDouble3[(2 * arrayOfDouble5 + 1)] * arrayOfDouble4[(2 * (2 * arrayOfDouble5 - 1))];
      for (int n = 2 * arrayOfDouble5; n > 1; n--)
      {
        arrayOfDouble4[(2 * n)] += localObject1[(2 * arrayOfDouble5)] * arrayOfDouble4[(2 * (n - 1))] - localObject1[(2 * arrayOfDouble5 + 1)] * arrayOfDouble4[(2 * (n - 1) + 1)] + arrayOfDouble3[(2 * arrayOfDouble5)] * arrayOfDouble4[(2 * (n - 2))] - arrayOfDouble3[(2 * arrayOfDouble5 + 1)] * arrayOfDouble4[(2 * (n - 2) + 1)];
        arrayOfDouble4[(2 * n + 1)] += localObject1[(2 * arrayOfDouble5)] * arrayOfDouble4[(2 * (n - 1) + 1)] + localObject1[(2 * arrayOfDouble5 + 1)] * arrayOfDouble4[(2 * (n - 1))] + arrayOfDouble3[(2 * arrayOfDouble5)] * arrayOfDouble4[(2 * (n - 2) + 1)] + arrayOfDouble3[(2 * arrayOfDouble5 + 1)] * arrayOfDouble4[(2 * (n - 2))];
      }
      arrayOfDouble4[2] += localObject1[(2 * arrayOfDouble5)] * arrayOfDouble4[0] - localObject1[(2 * arrayOfDouble5 + 1)] * arrayOfDouble4[1] + arrayOfDouble3[(2 * arrayOfDouble5)];
      arrayOfDouble4[3] += localObject1[(2 * arrayOfDouble5)] * arrayOfDouble4[1] + localObject1[(2 * arrayOfDouble5 + 1)] * arrayOfDouble4[0] + arrayOfDouble3[(2 * arrayOfDouble5 + 1)];
      arrayOfDouble4[0] += localObject1[(2 * arrayOfDouble5)];
      arrayOfDouble4[1] += localObject1[(2 * arrayOfDouble5 + 1)];
    }
    localObject1 = arrayOfDouble4;
    (arrayOfDouble3 = new double[2 * arrayOfDouble2 + 1])[1] = localObject1[0];
    arrayOfDouble3[0] = 1.0D;
    arrayOfDouble3[2] = localObject1[2];
    for (int m = 3; m <= 2 * arrayOfDouble2; m++)
      arrayOfDouble3[m] = localObject1[(2 * m - 2)];
    paramArrayOfDouble = arrayOfDouble3;
    double[] arrayOfDouble6 = paramArrayOfDouble;
    d5 = d2;
    double d4 = d1;
    arrayOfDouble2 = paramInt;
    localObject1 = this;
    double[] arrayOfDouble7 = new double[2 * arrayOfDouble2 + 1];
    for (int i1 = 0; i1 < arrayOfDouble7.length; i1++)
      arrayOfDouble7[i1] = i1;
    double[] arrayOfDouble8 = new double[2 * arrayOfDouble2 + 1];
    EEGAlgorithmToolbox.CVector localCVector = new EEGAlgorithmToolbox.CVector(((Butter_EEG)localObject1).b, 2 * arrayOfDouble2 + 1);
    double d15 = arrayOfDouble2;
    Object localObject3 = localObject1;
    int i5;
    double[] arrayOfDouble13;
    (arrayOfDouble13 = new double[(i5 = d15) + 1])[0] = 1.0D;
    arrayOfDouble13[1] = i5;
    int k = i5 / 2;
    for (m = 2; m <= k; m++)
    {
      arrayOfDouble13[m] = ((i5 - m + 1) * arrayOfDouble13[(m - 1)] / m);
      arrayOfDouble13[(i5 - m)] = arrayOfDouble13[m];
    }
    arrayOfDouble13[(i5 - 1)] = i5;
    arrayOfDouble13[i5] = 1.0D;
    double[] arrayOfDouble12;
    for (double d17 = 0; d17 <= d15; d17++)
    {
      if (d17 % 2 == 0)
        continue;
      arrayOfDouble12[d17] = (-arrayOfDouble12[d17]);
    }
    Object localObject2;
    for (int i2 = 0; i2 < arrayOfDouble2; i2++)
    {
      arrayOfDouble8[(2 * i2)] = localObject2[i2];
      arrayOfDouble8[(2 * i2 + 1)] = 0.0D;
    }
    arrayOfDouble8[(2 * arrayOfDouble2)] = localObject2[arrayOfDouble2];
    double[] arrayOfDouble9;
    (arrayOfDouble9 = new double[2])[0] = (4.0D * Math.tan(d4 * 3.141592653589793D / 2.0D));
    arrayOfDouble9[1] = (4.0D * Math.tan(d5 * 3.141592653589793D / 2.0D));
    d10 = 2.0D * Math.atan2(Math.sqrt(arrayOfDouble9[0] * arrayOfDouble9[1]), 4.0D);
    for (int i3 = 0; i3 < arrayOfDouble7.length; i3++)
      EEGAlgorithmToolbox.CVector.a(localCVector)[i3] = EEGAlgorithmToolbox.Complex.CExp(EEGAlgorithmToolbox.Complex.CMultipleValue(EEGAlgorithmToolbox.Complex.CMultipleValue(((Butter_EEG)localObject1).a, d10), arrayOfDouble7[i3]));
    double d12 = 0.0D;
    double d13 = 0.0D;
    for (int i = 0; i < arrayOfDouble7.length; i++)
    {
      d12 += EEGAlgorithmToolbox.Complex.CMultipleValue(EEGAlgorithmToolbox.CVector.a(localCVector)[i], arrayOfDouble8[i]).getReal();
      d13 += EEGAlgorithmToolbox.Complex.CMultipleValue(EEGAlgorithmToolbox.CVector.a(localCVector)[i], arrayOfDouble6[i]).getReal();
    }
    double[] arrayOfDouble1 = new double[arrayOfDouble8.length];
    for (int i4 = 0; i4 < arrayOfDouble7.length; i4++)
      arrayOfDouble1[i4] = (arrayOfDouble8[i4] * d13 / d12);
    paramInt = (localObject2 = (arrayOfDouble12 = arrayOfDouble13) == null ? null : arrayOfDouble12) == null ? null : arrayOfDouble1;
    return (EEGAlgorithmToolbox.Butter_Return)new EEGAlgorithmToolbox.Butter_Return(this.b, paramInt, paramArrayOfDouble);
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.Butter_EEG
 * JD-Core Version:    0.6.0
 */