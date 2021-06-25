package com.neurosky.thinkgear;

import I;
import java.io.PrintStream;
import java.util.Arrays;

public class Matlab
{
  public static int[] sortIndices(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    int[] arrayOfInt = new int[paramArrayOfFloat1.length];
    for (int i = 0; i < paramArrayOfFloat1.length; i++)
      for (int j = 0; j < paramArrayOfFloat1.length; j++)
      {
        if (paramArrayOfFloat1[i] != paramArrayOfFloat2[j])
          continue;
        arrayOfInt[j] = i;
        break;
      }
    return arrayOfInt;
  }

  public static float[] linspace(float paramFloat1, float paramFloat2, int paramInt)
  {
    float[] arrayOfFloat = new float[paramInt];
    for (int i = 0; i < paramInt; i++)
      arrayOfFloat[i] = (paramFloat1 + (paramFloat2 - paramFloat1) / (paramInt - 1) * i);
    return arrayOfFloat;
  }

  public static int[] linspace(int paramInt1, int paramInt2, int paramInt3)
  {
    paramInt2 = new int[(paramInt1 = linspace(paramInt1, paramInt2, paramInt3)).length];
    for (int i = 0; i < paramInt3; i++)
      paramInt2[i] = Math.round(paramInt1[i]);
    return paramInt2;
  }

  public static int[] setdiff(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int[] arrayOfInt1 = new int[paramArrayOfInt1.length];
    paramArrayOfInt2 = (int[])paramArrayOfInt2.clone();
    int i = 0;
    Arrays.sort(paramArrayOfInt2);
    for (int j = 0; j < paramArrayOfInt1.length; j++)
    {
      arrayOfInt1[j] = Arrays.binarySearch(paramArrayOfInt2, paramArrayOfInt1[j]);
      if (arrayOfInt1[j] < 0)
        continue;
      i++;
    }
    int[] arrayOfInt2 = new int[paramArrayOfInt1.length - i];
    paramArrayOfInt2 = 0;
    for (i = 0; i < paramArrayOfInt1.length; i++)
    {
      if (arrayOfInt1[i] >= 0)
        continue;
      arrayOfInt2[(paramArrayOfInt2++)] = paramArrayOfInt1[i];
    }
    return arrayOfInt2;
  }

  public static void disp(float[][] paramArrayOfFloat)
  {
    for (int i = 0; i < paramArrayOfFloat.length; i++)
    {
      for (int j = 0; j < paramArrayOfFloat[0].length; j++)
        System.out.format("\t%3.2f", new Object[] { Float.valueOf(paramArrayOfFloat[i][j]) });
      System.out.println("");
    }
  }

  public static void disp(float[] paramArrayOfFloat)
  {
    for (int i = 0; i < paramArrayOfFloat.length; i++)
      System.out.format("\t%3.2f", new Object[] { Float.valueOf(paramArrayOfFloat[i]) });
    System.out.println("");
  }

  public static void disp(int[][] paramArrayOfInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      for (int j = 0; j < paramArrayOfInt[0].length; j++)
        System.out.format("\t%d", new Object[] { Integer.valueOf(paramArrayOfInt[i][j]) });
      System.out.println("");
    }
  }

  public static void disp(int[] paramArrayOfInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i++)
      System.out.format("\t%d", new Object[] { Integer.valueOf(paramArrayOfInt[i]) });
    System.out.println("");
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.Matlab
 * JD-Core Version:    0.6.0
 */