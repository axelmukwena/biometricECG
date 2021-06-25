package com.neurosky.thinkgear;

public class DistanceArray
{
  public float[][] array;
  public int numRows = 0;
  public int numCols = 0;

  public DistanceArray()
  {
    this.array = new float[30][30];
  }

  public DistanceArray(int paramInt1, int paramInt2)
  {
    this.array = new float[paramInt1][paramInt2];
    this.numRows = paramInt1;
    this.numCols = paramInt2;
  }

  public float[] getRow(int paramInt)
  {
    float[] arrayOfFloat = new float[this.numCols];
    for (int i = 0; i < this.numCols; i++)
      arrayOfFloat[i] = this.array[paramInt][i];
    return arrayOfFloat;
  }

  public DistanceArray subArray(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    DistanceArray localDistanceArray = new DistanceArray(paramArrayOfInt1.length, paramArrayOfInt2.length);
    for (int i = 0; i < paramArrayOfInt1.length; i++)
      for (int j = 0; j < paramArrayOfInt2.length; j++)
        localDistanceArray.array[i][j] = this.array[paramArrayOfInt1[i]][paramArrayOfInt2[j]];
    return localDistanceArray;
  }

  public float mean()
  {
    float f = 0.0F;
    for (int i = 0; i < this.numRows; i++)
      for (int j = 0; j < this.numCols; j++)
        f += this.array[i][j];
    return f / this.numRows / this.numCols;
  }

  public float max()
  {
    float f = 0.0F;
    for (int i = 0; i < this.numRows; i++)
      for (int j = 0; j < this.numCols; j++)
      {
        if (this.array[i][j] <= f)
          continue;
        f = this.array[i][j];
      }
    return f;
  }

  public float maxAbs()
  {
    float f = 0.0F;
    for (int i = 0; i < this.numRows; i++)
      for (int j = 0; j < this.numCols; j++)
      {
        if (Math.abs(this.array[i][j]) <= f)
          continue;
        f = Math.abs(this.array[i][j]);
      }
    return f;
  }

  public float meanNoDiagonal()
  {
    float f = 0.0F;
    int i = 0;
    for (int j = 0; j < this.numRows; j++)
      for (int k = 0; k < this.numCols; k++)
      {
        if (j == k)
          continue;
        f += this.array[j][k];
        i++;
      }
    return f / i;
  }

  public float medianNoDiagonal()
  {
    EkgEpoch localEkgEpoch = new EkgEpoch(this.numRows * this.numCols - this.numRows);
    int i = 0;
    for (int j = 0; j < this.numRows; j++)
      for (int k = 0; k < this.numCols; k++)
      {
        if (j == k)
          continue;
        localEkgEpoch.data[(i++)] = this.array[j][k];
      }
    return localEkgEpoch.median();
  }

  public float[] medianRow()
  {
    float[] arrayOfFloat = new float[this.numRows];
    for (int i = 0; i < this.numRows; i++)
    {
      EkgEpoch localEkgEpoch = new EkgEpoch(this.numCols);
      int j = 0;
      for (int k = 0; k < this.numCols; k++)
        localEkgEpoch.data[(j++)] = this.array[i][k];
      arrayOfFloat[i] = localEkgEpoch.median();
    }
    return arrayOfFloat;
  }

  public float[] colMean()
  {
    float[] arrayOfFloat = new float[this.numRows];
    for (int i = 0; i < this.numRows; i++)
      for (int j = 0; j < this.numCols; j++)
        arrayOfFloat[i] += this.array[i][j] / this.numCols;
    return arrayOfFloat;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.DistanceArray
 * JD-Core Version:    0.6.0
 */