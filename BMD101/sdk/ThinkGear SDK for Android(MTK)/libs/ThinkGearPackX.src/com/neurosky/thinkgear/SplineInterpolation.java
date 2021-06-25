package com.neurosky.thinkgear;

import java.util.List;

public class SplineInterpolation
{
  private SplineInterpolation.SplineCoefficients a = new SplineInterpolation.SplineCoefficients(this);
  private int b;
  private int c;
  private float[] d;
  private float[] e;
  private float[] f;
  private float[] g;
  private float[] h;
  private float i;
  private float[] j;
  private float[] k;
  private float[] l;
  private float m;
  private int n;
  private int o;

  public float[] interpolate(List paramList1, List paramList2, int[] paramArrayOfInt)
  {
    this.b = paramList1.size();
    this.j = new float[this.b + 1];
    this.k = new float[this.b + 1];
    for (int i1 = 0; i1 < paramList1.size(); i1++)
    {
      this.j[i1] = ((Integer)paramList1.get(i1)).intValue();
      this.k[i1] = ((Integer)paramList2.get(i1)).intValue();
    }
    this.a.clearAll();
    paramList2 = this;
    this.d = new float[paramList2.b];
    paramList2.e = new float[paramList2.b];
    paramList2.f = new float[paramList2.b];
    paramList2.g = new float[paramList2.b];
    paramList2.o = 0;
    while (paramList2.o < paramList2.b)
    {
      paramList2.f[paramList2.o] = 1.0F;
      paramList2.d[paramList2.o] = 1.0F;
      paramList2.e[paramList2.o] = 4.0F;
      paramList2.o += 1;
    }
    paramList2.e[0] = 2.0F;
    paramList2.e[(paramList2.b - 1)] = 2.0F;
    paramList2.o = 1;
    while (paramList2.o < paramList2.b - 1)
    {
      paramList2.g[paramList2.o] = (3.0F * (paramList2.k[(paramList2.o + 1)] - paramList2.k[(paramList2.o - 1)]));
      paramList2.o += 1;
    }
    paramList2.g[0] = (3.0F * (paramList2.k[1] - paramList2.k[0]));
    paramList2.g[(paramList2.b - 1)] = (3.0F * (paramList2.k[(paramList2.b - 1)] - paramList2.k[(paramList2.b - 2)]));
    List localList;
    (localList = paramList2).c = localList.d.length;
    localList.f[0] /= localList.e[0];
    localList.g[0] /= localList.e[0];
    localList.o = 1;
    while (localList.o < localList.c)
    {
      localList.i = (1.0F / (localList.e[localList.o] - localList.f[(localList.o - 1)] * localList.d[localList.o]));
      localList.f[localList.o] *= localList.i;
      localList.g[localList.o] = ((localList.g[localList.o] - localList.g[(localList.o - 1)] * localList.d[localList.o]) * localList.i);
      localList.o += 1;
    }
    localList.h = new float[localList.c + 1];
    localList.h[(localList.c - 1)] = localList.g[(localList.c - 1)];
    localList.o = (localList.c - 2);
    while (localList.o >= 0)
    {
      localList.h[localList.o] = (localList.g[localList.o] - localList.f[localList.o] * localList.h[(localList.o + 1)]);
      localList.o -= 1;
    }
    paramList2.o = 0;
    while (paramList2.o < paramList2.b)
    {
      paramList2.a.A.add(Float.valueOf(paramList2.k[paramList2.o]));
      paramList2.a.B.add(Float.valueOf(paramList2.h[paramList2.o]));
      paramList2.a.C.add(Float.valueOf(3.0F * (paramList2.k[(paramList2.o + 1)] - paramList2.k[paramList2.o]) - 2.0F * paramList2.h[paramList2.o] - paramList2.h[(paramList2.o + 1)]));
      paramList2.a.D.add(Float.valueOf(2.0F * (paramList2.k[paramList2.o] - paramList2.k[(paramList2.o + 1)]) + paramList2.h[paramList2.o] + paramList2.h[(paramList2.o + 1)]));
      paramList2.o += 1;
    }
    this.n = 0;
    this.l = new float[paramArrayOfInt.length - 1];
    for (i1 = 0; i1 < paramArrayOfInt.length - 1; i1++)
      for (paramList1 = this.n; paramList1 < this.j.length - 1; paramList1++)
      {
        if ((paramArrayOfInt[i1] > this.j[(paramList1 + 1)]) || (paramArrayOfInt[i1] < this.j[paramList1]))
          continue;
        this.m = ((paramArrayOfInt[i1] - this.j[paramList1]) / (this.j[(paramList1 + 1)] - this.j[paramList1]));
        float f1 = this.m;
        int i2 = paramList1;
        float tmp1109_1108 = (((Float)(paramList2 = this.a).A.get(i2)).floatValue() + ((Float)paramList2.B.get(i2)).floatValue() * f1 + ((Float)paramList2.C.get(i2)).floatValue() * f1 * f1 + ((Float)paramList2.D.get(i2)).floatValue() * f1 * f1 * f1);
        paramList2 = tmp1109_1108;
        this.l[i1] = tmp1109_1108;
        this.n = paramList1;
      }
    return this.l;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.SplineInterpolation
 * JD-Core Version:    0.6.0
 */