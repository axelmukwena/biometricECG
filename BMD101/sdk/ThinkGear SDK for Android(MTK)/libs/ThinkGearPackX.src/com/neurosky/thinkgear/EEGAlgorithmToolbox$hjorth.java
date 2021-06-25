package com.neurosky.thinkgear;

import android.util.Log;

public class EEGAlgorithmToolbox$hjorth
{
  private double[] a;
  private double[] b;
  private double[] c;
  private double d;
  private double e;
  private double f;
  private double g;
  private double h;
  private double i;

  public EEGAlgorithmToolbox$hjorth(EEGAlgorithmToolbox paramEEGAlgorithmToolbox)
  {
  }

  public EEGAlgorithmToolbox.Hjorth_Return hjorth_fun(double[] paramArrayOfDouble, int paramInt)
  {
    this.a = paramArrayOfDouble;
    Object localObject1 = new EEGAlgorithmToolbox.Diff(this.j);
    this.b = ((EEGAlgorithmToolbox.Diff)localObject1).applydiff(this.a);
    this.c = ((EEGAlgorithmToolbox.Diff)localObject1).applydiff(this.b);
    localObject1 = new double[paramArrayOfDouble = paramArrayOfDouble.length];
    Object localObject2 = new double[paramArrayOfDouble];
    double[] arrayOfDouble = new double[paramArrayOfDouble];
    for (Object localObject3 = 0; localObject3 < paramArrayOfDouble; localObject3++)
    {
      localObject1[localObject3] = this.a[localObject3];
      localObject2[localObject3] = this.b[localObject3];
      arrayOfDouble[localObject3] = this.c[localObject3];
      if (Double.isNaN(localObject1[localObject3]))
        localObject1[localObject3] = 0.0D;
      else
        localObject1[localObject3] = 1.0D;
      if (Double.isNaN(localObject2[localObject3]))
        localObject2[localObject3] = 0.0D;
      else
        localObject2[localObject3] = 1.0D;
      if (Double.isNaN(arrayOfDouble[localObject3]))
        arrayOfDouble[localObject3] = 0.0D;
      else
        arrayOfDouble[localObject3] = 1.0D;
    }
    if (paramInt == 0)
    {
      paramArrayOfDouble = (localObject3 = new EEGAlgorithmToolbox.point_sqr(this.j)).sqr(this.a);
      paramInt = ((EEGAlgorithmToolbox.point_sqr)localObject3).sqr(this.b);
      localObject1 = ((EEGAlgorithmToolbox.point_sqr)localObject3).sqr(this.c);
      localObject2 = new EEGAlgorithmToolbox.calmean(this.j);
      this.d = ((EEGAlgorithmToolbox.calmean)localObject2).mean(paramArrayOfDouble);
      this.e = ((EEGAlgorithmToolbox.calmean)localObject2).mean(paramInt);
      this.f = ((EEGAlgorithmToolbox.calmean)localObject2).mean(localObject1);
      this.g = this.d;
      this.h = Math.sqrt(this.e / this.d);
      this.i = Math.sqrt(this.f / this.e);
    }
    else
    {
      Log.i("Warning", "UC is not correctly assigned");
    }
    return (EEGAlgorithmToolbox.Hjorth_Return)(EEGAlgorithmToolbox.Hjorth_Return)(EEGAlgorithmToolbox.Hjorth_Return)new EEGAlgorithmToolbox.Hjorth_Return(this.j, this.g, this.h, this.i);
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.hjorth
 * JD-Core Version:    0.6.0
 */