package com.neurosky.thinkgear;

import F;
import android.util.Log;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class EkgTemplate
{
  public String subjectName;
  public String fileName;
  public EkgEpoch[] templateArray;
  public int maxNumberOfTemplateElements = 100;
  public int lastTemplateIndex;
  public float score;
  public float meanDistance;

  public EkgTemplate(String paramString)
  {
    this.subjectName = paramString;
    this.templateArray = new EkgEpoch[this.maxNumberOfTemplateElements];
    this.lastTemplateIndex = 0;
  }

  public EkgTemplate()
  {
    this.subjectName = "";
    this.templateArray = new EkgEpoch[this.maxNumberOfTemplateElements];
    this.lastTemplateIndex = 0;
  }

  public boolean loadJson(String paramString)
  {
    if (paramString.length() == 0)
      return false;
    try
    {
      paramString = (JSONObject)(paramString = new JSONTokener(paramString)).nextValue();
      this.templateArray = new EkgEpoch[this.maxNumberOfTemplateElements];
      JSONArray localJSONArray = paramString.getJSONArray("templateArray");
      Log.v("TGDevice", "loadJson() a.length " + localJSONArray.length());
      for (int i = 0; i < localJSONArray.length(); i++)
      {
        this.templateArray[i] = new EkgEpoch(localJSONArray.getJSONArray(i));
        this.lastTemplateIndex = i;
      }
      this.subjectName = paramString.getString("subjectName");
    }
    catch (JSONException paramString)
    {
      Log.d("EkgParameters", "JSON exception: " + paramString.getLocalizedMessage());
      return false;
    }
    catch (ClassCastException localClassCastException)
    {
      return false;
    }
    return true;
  }

  public String toJSONString()
  {
    JSONObject localJSONObject = new JSONObject();
    JSONArray localJSONArray = new JSONArray();
    Log.v("TGDevice", "toJSONString() template array: " + this.templateArray.length);
    for (int i = 0; i < this.lastTemplateIndex; i++)
      localJSONArray.put(this.templateArray[i].toJSONArray());
    Log.v("TGDevice", "toJsonString() arraylength:" + localJSONArray.length());
    try
    {
      localJSONObject.accumulate("subjectName", this.subjectName);
      localJSONObject.accumulate("templateArray", localJSONArray);
    }
    catch (JSONException localJSONException2)
    {
      JSONException localJSONException1;
      (localJSONException1 = localJSONException2).printStackTrace();
    }
    return localJSONObject.toString();
  }

  public float[][] getValues()
  {
    float[][] arrayOfFloat = new float[this.lastTemplateIndex][this.templateArray[1].numberOfSamples];
    for (int i = 0; i < this.lastTemplateIndex; i++)
      arrayOfFloat[i] = this.templateArray[i].data;
    return arrayOfFloat;
  }

  public void addTemplate(int paramInt, float[] paramArrayOfFloat)
  {
    this.templateArray[this.lastTemplateIndex] = new EkgEpoch(paramInt, paramArrayOfFloat);
    this.lastTemplateIndex += 1;
  }

  public EkgTemplate(String paramString, float[][] paramArrayOfFloat)
  {
    this.lastTemplateIndex = 0;
    this.templateArray = new EkgEpoch[this.maxNumberOfTemplateElements];
    this.subjectName = paramString;
    for (paramString = 0; paramString < paramArrayOfFloat.length; paramString++)
      addTemplate(paramArrayOfFloat[0].length, paramArrayOfFloat[paramString]);
    this.meanDistance = computeDistance(this).mean();
  }

  public void addData(EkgEpoch paramEkgEpoch)
  {
    this.templateArray[this.lastTemplateIndex] = paramEkgEpoch;
    this.lastTemplateIndex += 1;
  }

  public void selectRows(int[] paramArrayOfInt)
  {
    Arrays.sort(paramArrayOfInt);
    for (int i = 0; i < paramArrayOfInt.length; i++)
      this.templateArray[i] = this.templateArray[paramArrayOfInt[i]];
    this.lastTemplateIndex = paramArrayOfInt.length;
  }

  public void addTemplate(EkgEpoch paramEkgEpoch)
  {
    this.templateArray[this.lastTemplateIndex] = paramEkgEpoch;
    this.lastTemplateIndex += 1;
  }

  public void rmTemplate(int paramInt)
  {
    for (paramInt = paramInt; paramInt < this.lastTemplateIndex - 1; paramInt++)
      copyTemplate(paramInt, paramInt + 1);
    this.lastTemplateIndex -= 1;
  }

  public boolean equal(EkgTemplate paramEkgTemplate)
  {
    if (this.lastTemplateIndex != paramEkgTemplate.lastTemplateIndex)
      return false;
    for (int i = 0; i < this.lastTemplateIndex; i++)
      if (this.templateArray[i].data[0] != paramEkgTemplate.templateArray[i].data[0])
        return false;
    return true;
  }

  public static String buildStrongTemplate(String[] paramArrayOfString)
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return null;
    EkgTemplate[] arrayOfEkgTemplate = new EkgTemplate[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      arrayOfEkgTemplate[i] = new EkgTemplate();
      arrayOfEkgTemplate[i].loadJson(paramArrayOfString[i]);
    }
    EkgTemplate localEkgTemplate;
    return (localEkgTemplate = buildStrongTemplate(arrayOfEkgTemplate)).toJSONString();
  }

  public static EkgTemplate buildStrongTemplate(EkgTemplate[] paramArrayOfEkgTemplate)
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return null;
    int i = paramArrayOfEkgTemplate[0].lastTemplateIndex;
    EkgTemplate localEkgTemplate = new EkgTemplate(paramArrayOfEkgTemplate[0].subjectName);
    if (i != paramArrayOfEkgTemplate.length)
      if (((paramArrayOfEkgTemplate[i] != null ? 1 : 0) | (paramArrayOfEkgTemplate[(i - 1)] == null ? 1 : 0)) != 0)
        throw new Exception("There must be as many element in the cell array as element in each template");
    Object localObject1 = new EkgTemplate("test");
    for (int j = 0; j < i; j++)
      for (k = 0; k < paramArrayOfEkgTemplate[j].lastTemplateIndex; k++)
        ((EkgTemplate)localObject1).addTemplate(paramArrayOfEkgTemplate[j].templateArray[k]);
    DistanceArray localDistanceArray = ((EkgTemplate)localObject1).computeDistance((EkgTemplate)localObject1);
    for (int k = 0; k < i; k++)
    {
      localObject1 = Matlab.linspace(k * i, (k + 1) * i - 1, i);
      Object localObject2 = Matlab.setdiff(localObject2 = Matlab.linspace(0, i * i - 1, i * i), localObject1);
      Arrays.sort(localObject2 = (float[])(localObject1 = (localObject1 = localDistanceArray.subArray(localObject1, localObject2)).colMean()).clone());
      localObject1 = Matlab.sortIndices(localObject1, localObject2);
      int m = (int)Math.ceil(i / 2.0D);
      localEkgTemplate.addData(paramArrayOfEkgTemplate[k].templateArray[localObject1[(m - 1)]]);
    }
    return (EkgTemplate)(EkgTemplate)localEkgTemplate;
  }

  public static EkgTemplate buildStrongTemplateNew(EkgTemplate[] paramArrayOfEkgTemplate)
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return null;
    int i = paramArrayOfEkgTemplate[0].lastTemplateIndex;
    EkgTemplate localEkgTemplate1 = new EkgTemplate(paramArrayOfEkgTemplate[0].subjectName);
    if (i != paramArrayOfEkgTemplate.length)
      if (((paramArrayOfEkgTemplate[i] != null ? 1 : 0) | (paramArrayOfEkgTemplate[(i - 1)] == null ? 1 : 0)) != 0)
        throw new Exception("There must be as many element in the cell array as element in each template");
    EkgTemplate localEkgTemplate2 = new EkgTemplate("test");
    for (int j = 0; j < i; j++)
      for (m = 0; m < paramArrayOfEkgTemplate[j].lastTemplateIndex; m++)
        localEkgTemplate2.addTemplate(paramArrayOfEkgTemplate[j].templateArray[m]);
    DistanceArray localDistanceArray = localEkgTemplate2.computeDistance(localEkgTemplate2);
    for (int m = 0; m < localEkgTemplate2.lastTemplateIndex; m++)
      localDistanceArray.array[m][m] = 1200.0F;
    float[] arrayOfFloat;
    Arrays.sort(paramArrayOfEkgTemplate = (float[])(arrayOfFloat = localDistanceArray.medianRow()).clone());
    paramArrayOfEkgTemplate = Matlab.sortIndices(arrayOfFloat, paramArrayOfEkgTemplate);
    for (int k = 0; k < i; k++)
      localEkgTemplate1.addData(localEkgTemplate2.templateArray[paramArrayOfEkgTemplate[k]]);
    return localEkgTemplate1;
  }

  public DistanceArray computeDistance(EkgTemplate paramEkgTemplate)
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return null;
    DistanceArray localDistanceArray = new DistanceArray(this.lastTemplateIndex, paramEkgTemplate.lastTemplateIndex);
    int i = 0;
    if (equal(paramEkgTemplate))
      i = 1;
    for (int j = 0; j < this.lastTemplateIndex; j++)
    {
      int k;
      if (i != 0)
        for (k = j + 1; k < paramEkgTemplate.lastTemplateIndex; k++)
          try
          {
            localDistanceArray.array[j][k] = (float)Math.sqrt(this.templateArray[j].subtract(paramEkgTemplate.templateArray[k]).square().mean());
            localDistanceArray.array[k][j] = localDistanceArray.array[j][k];
          }
          catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
          {
            Log.v("TGDevice", "ind1: " + j + " ind2: " + k);
            Log.v("TGDevice", "templateArray Length: " + this.templateArray.length);
          }
      else
        for (k = 0; k < paramEkgTemplate.lastTemplateIndex; k++)
          localDistanceArray.array[j][k] = (float)Math.sqrt(this.templateArray[j].subtract(paramEkgTemplate.templateArray[k]).square().mean());
    }
    return localDistanceArray;
  }

  public DistanceArray computeAutoCorr(EkgTemplate paramEkgTemplate)
  {
    DistanceArray localDistanceArray1 = new DistanceArray(this.lastTemplateIndex, paramEkgTemplate.lastTemplateIndex);
    DistanceArray localDistanceArray2 = new DistanceArray(this.lastTemplateIndex, paramEkgTemplate.lastTemplateIndex);
    EkgTemplate localEkgTemplate = new EkgTemplate();
    for (int i = 0; i < this.lastTemplateIndex; i++)
      localEkgTemplate.addData(this.templateArray[i].subtract(this.templateArray[i].mean()));
    int j;
    for (i = 0; i < this.lastTemplateIndex; i++)
      for (j = 0; j < paramEkgTemplate.lastTemplateIndex; j++)
      {
        float f = 0.0F;
        for (int k = 0; k < this.templateArray[0].numberOfSamples; k++)
          f += localEkgTemplate.templateArray[i].data[k] * localEkgTemplate.templateArray[j].data[k];
        localDistanceArray1.array[i][j] = (f / this.templateArray[0].numberOfSamples);
      }
    for (i = 0; i < this.lastTemplateIndex; i++)
      for (j = 0; j < paramEkgTemplate.lastTemplateIndex; j++)
        localDistanceArray1.array[i][j] /= (float)Math.sqrt(localDistanceArray1.array[i][i] * localDistanceArray1.array[j][j]);
    return localDistanceArray2;
  }

  public DistanceArray computeCorr(EkgTemplate paramEkgTemplate)
  {
    DistanceArray localDistanceArray = new DistanceArray(this.lastTemplateIndex, paramEkgTemplate.lastTemplateIndex);
    EkgTemplate localEkgTemplate1 = new EkgTemplate();
    EkgTemplate localEkgTemplate2 = new EkgTemplate();
    for (int i = 0; i < this.lastTemplateIndex; i++)
      localEkgTemplate1.addData(this.templateArray[i].subtract(this.templateArray[i].mean()));
    for (i = 0; i < paramEkgTemplate.lastTemplateIndex; i++)
      localEkgTemplate2.addData(paramEkgTemplate.templateArray[i].subtract(paramEkgTemplate.templateArray[i].mean()));
    for (i = 0; i < localEkgTemplate1.lastTemplateIndex; i++)
      for (paramEkgTemplate = 0; paramEkgTemplate < localEkgTemplate2.lastTemplateIndex; paramEkgTemplate++)
        localDistanceArray.array[i][paramEkgTemplate] = (float)localEkgTemplate1.templateArray[i].corrSimple(localEkgTemplate2.templateArray[paramEkgTemplate]);
    return localDistanceArray;
  }

  public void copyTemplate(int paramInt1, int paramInt2)
  {
    for (int i = 0; i < this.lastTemplateIndex; i++)
      this.templateArray[paramInt1] = this.templateArray[paramInt2].clone();
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EkgTemplate
 * JD-Core Version:    0.6.0
 */