package com.neurosky.thinkgear;

import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;

public class EkgEpoch
  implements Cloneable
{
  public int numberOfSamples;
  public float[] data;

  public EkgEpoch(int paramInt)
  {
    this.numberOfSamples = paramInt;
    this.data = new float[this.numberOfSamples];
  }

  public EkgEpoch(int paramInt, float[] paramArrayOfFloat)
  {
    this.numberOfSamples = paramInt;
    this.data = new float[this.numberOfSamples];
    System.arraycopy(paramArrayOfFloat, 0, this.data, 0, this.numberOfSamples);
  }

  public EkgEpoch(float[] paramArrayOfFloat)
  {
    this.numberOfSamples = paramArrayOfFloat.length;
    this.data = new float[this.numberOfSamples];
    System.arraycopy(paramArrayOfFloat, 0, this.data, 0, this.numberOfSamples);
  }

  public EkgEpoch(int[] paramArrayOfInt)
  {
    this.numberOfSamples = paramArrayOfInt.length;
    this.data = new float[this.numberOfSamples];
    for (int i = 0; i < paramArrayOfInt.length; i++)
      this.data[i] = paramArrayOfInt[i];
  }

  public EkgEpoch(EkgEpoch paramEkgEpoch)
  {
    this.numberOfSamples = paramEkgEpoch.numberOfSamples;
    this.data = new float[this.numberOfSamples];
    System.arraycopy(paramEkgEpoch.data, 0, this.data, 0, this.numberOfSamples);
  }

  public EkgEpoch(JSONArray paramJSONArray)
  {
    this.numberOfSamples = paramJSONArray.length();
    this.data = new float[this.numberOfSamples];
    try
    {
      for (int i = 0; i < this.numberOfSamples; i++)
        this.data[i] = (float)paramJSONArray.getDouble(i);
      return;
    }
    catch (JSONException localJSONException)
    {
    }
  }

  public JSONArray toJSONArray()
  {
    JSONArray localJSONArray = new JSONArray();
    for (float f : this.data)
      try
      {
        localJSONArray.put(f);
      }
      catch (JSONException localJSONException2)
      {
        JSONException localJSONException1;
        (localJSONException1 = localJSONException2).printStackTrace();
      }
    return localJSONArray;
  }

  public static float[] convolve(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float[] arrayOfFloat = new float[paramArrayOfFloat1.length + paramArrayOfFloat2.length - 1];
    for (int i = 0; i < paramArrayOfFloat2.length; i++)
      for (int j = 0; j < paramArrayOfFloat1.length; j++)
        arrayOfFloat[(i + j)] += paramArrayOfFloat2[i] * paramArrayOfFloat1[j];
    return arrayOfFloat;
  }

  public boolean detectHighTail()
  {
    int i = 0;
    int j;
    int k = (j = this.data.length / 2) + 1;
    if (exceedValue(this.data[(k - 1)], 0, j - j / 2))
      i = 1;
    if (exceedValue(this.data[(k - 1)], this.data.length - (j - j / 2), this.data.length))
      i = 1;
    return i;
  }

  public boolean detectHighTail(int paramInt)
  {
    int i = 0;
    paramInt += 1;
    int j;
    int k = (j = this.data.length / 2) + 1;
    if (paramInt < j)
    {
      if (exceedValue(this.data[(k - 1)], 0, j - paramInt))
        i = 1;
    }
    else if (exceedValue(this.data[(k - 1)], this.data.length - (j - (2 * j - paramInt)) - 1, this.data.length))
      i = 1;
    return i;
  }

  public float getLineNoiseAmplitude()
  {
    EkgEpoch localEkgEpoch = subtract(mean());
    float[] arrayOfFloat1 = new float[13];
    float[] arrayOfFloat2 = new float[13];
    for (int j = 0; j < 13; j++)
    {
      float f2 = (float)(2 * j * 3.141592653589793D / 13.0D);
      arrayOfFloat1[j] = (float)Math.cos(f2 * 2.0F);
      arrayOfFloat2[j] = (float)Math.sin(f2 * 2.0F);
    }
    float[] arrayOfFloat3 = convolve(localEkgEpoch.data, arrayOfFloat1);
    float[] arrayOfFloat4 = convolve(localEkgEpoch.data, arrayOfFloat2);
    float f1 = 0.0F;
    for (int i = 0; i < arrayOfFloat3.length; i++)
      f1 = (float)(f1 + Math.sqrt(arrayOfFloat3[i] * arrayOfFloat3[i] + arrayOfFloat4[i] * arrayOfFloat4[i]));
    return f1;
  }

  public EkgEpoch subtract(EkgEpoch paramEkgEpoch)
  {
    EkgEpoch localEkgEpoch = new EkgEpoch(this.numberOfSamples);
    for (int i = 0; i < this.numberOfSamples; i++)
      this.data[i] -= paramEkgEpoch.data[i];
    return localEkgEpoch;
  }

  public double corr(EkgEpoch paramEkgEpoch)
  {
    return (paramEkgEpoch = cov(paramEkgEpoch)).array[0][1] / Math.sqrt(paramEkgEpoch.array[0][0] * paramEkgEpoch.array[1][1]);
  }

  public double corrSimple(EkgEpoch paramEkgEpoch)
  {
    return (paramEkgEpoch = covSimple(paramEkgEpoch)).array[0][1] / Math.sqrt(paramEkgEpoch.array[0][0] * paramEkgEpoch.array[1][1]);
  }

  public DistanceArray cov(EkgEpoch paramEkgEpoch)
  {
    EkgEpoch localEkgEpoch = subtract(mean());
    paramEkgEpoch = paramEkgEpoch.subtract(paramEkgEpoch.mean());
    return paramEkgEpoch = localEkgEpoch.covSimple(paramEkgEpoch);
  }

  public DistanceArray covSimple(EkgEpoch paramEkgEpoch)
  {
    DistanceArray localDistanceArray = new DistanceArray(2, 2);
    for (int i = 0; i < this.numberOfSamples; i++)
    {
      int tmp27_26 = 0;
      float[] tmp27_25 = localDistanceArray.array[0];
      tmp27_25[tmp27_26] = (float)(tmp27_25[tmp27_26] + this.data[i] * this.data[i] / (this.numberOfSamples - 1));
    }
    for (i = 0; i < this.numberOfSamples; i++)
    {
      int tmp78_77 = 0;
      float[] tmp78_76 = localDistanceArray.array[1];
      tmp78_76[tmp78_77] = (float)(tmp78_76[tmp78_77] + this.data[i] * paramEkgEpoch.data[i] / (this.numberOfSamples - 1));
    }
    for (i = 0; i < this.numberOfSamples; i++)
    {
      int tmp129_128 = 1;
      float[] tmp129_127 = localDistanceArray.array[1];
      tmp129_127[tmp129_128] = (float)(tmp129_127[tmp129_128] + paramEkgEpoch.data[i] * paramEkgEpoch.data[i] / (this.numberOfSamples - 1));
    }
    localDistanceArray.array[0][1] = localDistanceArray.array[1][0];
    return localDistanceArray;
  }

  public EkgEpoch subEpoch(int paramInt1, int paramInt2)
  {
    EkgEpoch localEkgEpoch = new EkgEpoch(paramInt2 - paramInt1);
    for (int i = paramInt1; i < paramInt2; i++)
      localEkgEpoch.data[(i - paramInt1)] = this.data[i];
    return localEkgEpoch;
  }

  public EkgEpoch subEpoch2(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    EkgEpoch localEkgEpoch = new EkgEpoch(paramInt2 - paramInt1 + paramInt4 - paramInt3);
    int i = 0;
    for (paramInt1 = paramInt1; paramInt1 < paramInt2; paramInt1++)
      localEkgEpoch.data[(i++)] = this.data[paramInt1];
    for (paramInt1 = paramInt3; paramInt1 < paramInt4; paramInt1++)
      localEkgEpoch.data[(i++)] = this.data[paramInt1];
    return localEkgEpoch;
  }

  public EkgEpoch square()
  {
    EkgEpoch localEkgEpoch = new EkgEpoch(this.numberOfSamples);
    for (int i = 0; i < this.numberOfSamples; i++)
      this.data[i] *= this.data[i];
    return localEkgEpoch;
  }

  public EkgEpoch subtract(float paramFloat)
  {
    EkgEpoch localEkgEpoch = new EkgEpoch(this.numberOfSamples);
    for (int i = 0; i < this.numberOfSamples; i++)
      this.data[i] -= paramFloat;
    return localEkgEpoch;
  }

  public EkgEpoch diff()
  {
    EkgEpoch localEkgEpoch = new EkgEpoch(this.numberOfSamples - 1);
    for (int i = 0; i < localEkgEpoch.numberOfSamples; i++)
      localEkgEpoch.data[i] = (this.data[(i + 1)] - this.data[i]);
    return localEkgEpoch;
  }

  public boolean exceedValue(float paramFloat, int paramInt1, int paramInt2)
  {
    for (paramInt1 = paramInt1; paramInt1 < paramInt2; paramInt1++)
      if (this.data[paramInt1] > paramFloat)
        return true;
    return false;
  }

  public EkgEpoch smooth(int paramInt)
  {
    EkgEpoch localEkgEpoch = new EkgEpoch(this.numberOfSamples);
    if (Math.round(paramInt / 2.0F) == paramInt / 2.0F)
      paramInt--;
    for (int i = 0; i < localEkgEpoch.numberOfSamples; i++)
    {
      int j;
      int k = ((j = Math.min(Math.min(i, paramInt / 2), localEkgEpoch.numberOfSamples - i - 1)) << 1) + 1;
      for (int m = -j; m < j + 1; m++)
        localEkgEpoch.data[i] += this.data[(i + m)];
      localEkgEpoch.data[i] /= k;
    }
    return localEkgEpoch;
  }

  public float mean()
  {
    return sum() / this.numberOfSamples;
  }

  public float sum()
  {
    float f = 0.0F;
    for (int i = 0; i < this.numberOfSamples; i++)
      f += this.data[i];
    return f;
  }

  public float max()
  {
    float f = 0.0F;
    for (int i = 0; i < this.numberOfSamples; i++)
    {
      if (this.data[i] <= f)
        continue;
      f = this.data[i];
    }
    return f;
  }

  public float median()
  {
    float[] arrayOfFloat = new float[this.numberOfSamples];
    for (int i = 0; i < this.numberOfSamples; i++)
      arrayOfFloat[i] = this.data[i];
    Arrays.sort(arrayOfFloat);
    if (Math.floor(this.numberOfSamples / 2) * 2.0D == this.numberOfSamples)
      return (arrayOfFloat[((int)Math.ceil(this.numberOfSamples / 2) - 1)] + arrayOfFloat[(int)Math.ceil(this.numberOfSamples / 2)]) / 2.0F;
    return arrayOfFloat[(int)Math.ceil(this.numberOfSamples / 2)];
  }

  public EkgEpoch clone()
  {
    try
    {
      return (EkgEpoch)super.clone();
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public EkgEpoch sort()
  {
    EkgEpoch localEkgEpoch;
    Arrays.sort((localEkgEpoch = new EkgEpoch(this)).data);
    return localEkgEpoch;
  }

  public int[] sortIndices(EkgEpoch paramEkgEpoch)
  {
    int[] arrayOfInt = new int[this.numberOfSamples];
    for (int i = 0; i < this.numberOfSamples; i++)
      for (int j = 0; j < this.numberOfSamples; j++)
      {
        if (this.data[i] != paramEkgEpoch.data[j])
          continue;
        arrayOfInt[j] = i;
        break;
      }
    return arrayOfInt;
  }

  public float std()
  {
    float f1 = 0.0F;
    float f2 = mean();
    for (int i = 0; i < this.numberOfSamples; i++)
      f1 += (this.data[i] - f2) * (this.data[i] - f2);
    return (float)Math.sqrt(f1 / (this.numberOfSamples - 1));
  }

  public int find_heart_beats(int[] paramArrayOfInt, float paramFloat)
  {
    int[] arrayOfInt1 = Math.abs(paramArrayOfInt[0]);
    paramArrayOfInt = paramArrayOfInt[1];
    EkgEpoch localEkgEpoch1;
    EkgEpoch localEkgEpoch2 = (localEkgEpoch1 = subtract(median())).diff();
    int[] arrayOfInt2 = 0;
    float f1 = 0.0F;
    for (int[] arrayOfInt3 = arrayOfInt1; arrayOfInt3 < localEkgEpoch2.numberOfSamples - paramArrayOfInt; arrayOfInt3++)
    {
      if (((localEkgEpoch2.data[(arrayOfInt3 - 1)] > 0.0F ? 1 : 0) & (localEkgEpoch2.data[arrayOfInt3] < 0.0F ? 1 : 0)) == 0)
        continue;
      float f2 = localEkgEpoch1.data[arrayOfInt3] - localEkgEpoch1.data[(arrayOfInt3 - arrayOfInt1)] + (localEkgEpoch1.data[arrayOfInt3] - localEkgEpoch1.data[(arrayOfInt3 + paramArrayOfInt)]);
      float f3 = localEkgEpoch1.data[(arrayOfInt3 + 1)] - localEkgEpoch1.data[(arrayOfInt3 + 1 - arrayOfInt1)] + (localEkgEpoch1.data[(arrayOfInt3 + 1)] - localEkgEpoch1.data[(arrayOfInt3 + 1 + paramArrayOfInt)]);
      if (f2 > f1)
      {
        f1 = f2;
        arrayOfInt2 = arrayOfInt3;
      }
      if (f3 <= f1)
        continue;
      f1 = f3;
      arrayOfInt2 = arrayOfInt3 + 1;
    }
    if (f1 < paramFloat)
      return -1;
    if (((localEkgEpoch1.data[arrayOfInt2] - localEkgEpoch1.data[(arrayOfInt2 - arrayOfInt1)] < paramFloat / 2.8D ? 1 : 0) | (localEkgEpoch1.data[arrayOfInt2] - localEkgEpoch1.data[(arrayOfInt2 + paramArrayOfInt)] < paramFloat / 2.0F ? 1 : 0)) != 0)
      return -1;
    return arrayOfInt2;
  }

  public EkgEpoch detrend()
  {
    float[] arrayOfFloat = this.data;
    Object localObject = new float[this.numberOfSamples];
    int i = this.numberOfSamples;
    float f2 = 0.0F;
    float f3 = 0.0F;
    for (int j = 0; j < i; j++)
    {
      localObject[j] = j;
      f2 += localObject[j];
      f3 += arrayOfFloat[j];
    }
    float f4 = f2 / i;
    f2 = f3 / i;
    f3 = 0.0F;
    float f5 = 0.0F;
    for (int k = 0; k < i; k++)
    {
      f3 += (localObject[k] - f4) * (localObject[k] - f4);
      f5 += (localObject[k] - f4) * (arrayOfFloat[k] - f2);
    }
    float f6 = f5 / f3;
    float f1 = f2 - f6 * f4;
    localObject = new EkgEpoch(this.numberOfSamples);
    for (i = 0; i < ((EkgEpoch)localObject).numberOfSamples; i++)
      localObject.data[i] = (this.data[i] - f1 - f6 * i);
    return (EkgEpoch)localObject;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EkgEpoch
 * JD-Core Version:    0.6.0
 */