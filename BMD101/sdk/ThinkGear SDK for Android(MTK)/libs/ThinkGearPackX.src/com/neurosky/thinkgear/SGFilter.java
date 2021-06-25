package com.neurosky.thinkgear;

public class SGFilter
{
  private int a;
  private int b;
  private int c;
  private int d;
  private float[][] e;
  private float[] f;

  public float[] filterData(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    this.a = paramInt2;
    this.b = paramInt1;
    this.c = ((this.b - 1) / 2);
    this.e = new float[this.b][this.b];
    paramInt1 = this;
    paramInt2 = new float[this.a + 1][paramInt1.a + 1];
    float[][] arrayOfFloat1 = new float[paramInt1.b][paramInt1.a + 1];
    float[][] arrayOfFloat2 = new float[paramInt1.b][paramInt1.a + 1];
    arrayOfFloat3 = new float[paramInt1.b][paramInt1.b];
    for (int i = -paramInt1.c; i <= paramInt1.c; i++)
    {
      int m = 0;
      arrayOfFloat1[(i + paramInt1.c)][m] = (float)Math.pow(i, m);
      m++;
    }
    for (int j = 0; j <= paramInt1.a; j++)
      for (n = 0; n <= paramInt1.a; n++)
        for (int i1 = 0; i1 < paramInt1.b; i1++)
          paramInt2[j][n] += arrayOfFloat1[i1][n] * arrayOfFloat1[i1][j];
    int n = paramInt1.a + 1;
    int k = paramInt2;
    paramInt2 = paramInt1;
    float[][] arrayOfFloat4 = new float[n][n];
    float f1 = (float)(1.0D / paramInt2.a(k, n));
    float[][] arrayOfFloat5 = new float[n - 1][n - 1];
    for (int i3 = 0; i3 < n; i3++)
      for (int i4 = 0; i4 < n; i4++)
      {
        a(k, arrayOfFloat5, i3, i4, n);
        arrayOfFloat4[i4][i3] = (f1 * paramInt2.a(arrayOfFloat5, n - 1));
        if ((i4 + i3) % 2 != 1)
          continue;
        arrayOfFloat4[i4][i3] = (-arrayOfFloat4[i4][i3]);
      }
    Object localObject = arrayOfFloat4;
    int i2;
    for (n = 0; n <= paramInt1.a; n++)
      for (i2 = 0; i2 < paramInt1.b; i2++)
        for (paramInt2 = 0; paramInt2 <= paramInt1.a; paramInt2++)
          arrayOfFloat2[i2][n] += arrayOfFloat1[i2][paramInt2] * localObject[paramInt2][n];
    for (n = 0; n < paramInt1.b; n++)
      for (i2 = 0; i2 < paramInt1.b; i2++)
        for (paramInt2 = 0; paramInt2 <= paramInt1.a; paramInt2++)
          arrayOfFloat3[n][i2] += arrayOfFloat2[n][paramInt2] * arrayOfFloat1[i2][paramInt2];
  }

  private static int a(float[][] paramArrayOfFloat1, float[][] paramArrayOfFloat2, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    int j = 0;
    for (int k = 0; k < paramInt3; k++)
    {
      if (k == paramInt1)
        continue;
      i = 0;
      for (int m = 0; m < paramInt3; m++)
      {
        if (m == paramInt2)
          continue;
        paramArrayOfFloat2[j][i] = paramArrayOfFloat1[k][m];
        i++;
      }
      j++;
    }
    return 1;
  }

  private float a(float[][] paramArrayOfFloat, int paramInt)
  {
    if (paramInt == 1)
      return paramArrayOfFloat[0][0];
    float f1 = 0.0F;
    float[][] arrayOfFloat = new float[paramInt - 1][paramInt - 1];
    for (int i = 0; i < paramInt; i++)
    {
      a(paramArrayOfFloat, arrayOfFloat, 0, i, paramInt);
      f1 = (float)(f1 + (i % 2 == 1 ? -1.0D : 1.0D) * paramArrayOfFloat[0][i] * a(arrayOfFloat, paramInt - 1));
    }
    return f1;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.SGFilter
 * JD-Core Version:    0.6.0
 */