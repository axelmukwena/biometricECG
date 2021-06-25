package com.neurosky.thinkgear;

import android.util.Log;
import java.io.PrintStream;

public class EkgSense
{
  private final boolean a = TGDevice.ekgPersonalizationEnabled;
  public EkgParameters params;
  public EkgTemplate[] templates;
  public EkgTemplate currentData;
  public int lastTemplateInd = 0;
  public float lastEpochValue = 0.0F;
  public TGHrvBuffered buf;

  public EkgSense(EkgParameters paramEkgParameters)
  {
    this.params = paramEkgParameters;
    this.templates = new EkgTemplate[40];
    this.currentData = new EkgTemplate();
  }

  public void reset()
  {
    this.templates = new EkgTemplate[40];
    this.currentData = new EkgTemplate();
    this.lastTemplateInd = 0;
  }

  public void addTemplate(String paramString, float[][] paramArrayOfFloat)
  {
    this.templates[this.lastTemplateInd] = new EkgTemplate(paramString, paramArrayOfFloat);
    this.lastTemplateInd += 1;
  }

  public void addTemplate(EkgTemplate paramEkgTemplate)
  {
    this.templates[this.lastTemplateInd] = paramEkgTemplate;
    Log.v("EkgSense", "Loaded template: " + paramEkgTemplate.subjectName + " at index: " + this.lastTemplateInd);
    this.lastTemplateInd += 1;
  }

  public String getClassificationResults()
  {
    if (!this.a)
      return "Unknown";
    int i = 0;
    float f1 = 1000000.0F;
    for (int j = 0; j < this.lastTemplateInd; j++)
    {
      if (this.templates[j].score >= f1)
        continue;
      i = j;
      f1 = this.templates[j].score;
    }
    float f2 = 1000000.0F;
    for (int k = 0; k < this.lastTemplateInd; k++)
    {
      if (((this.templates[k].score < f2 ? 1 : 0) & (this.templates[k].score != f1 ? 1 : 0)) == 0)
        continue;
      f2 = this.templates[k].score;
    }
    if (f1 < this.params.templateMaxDistance)
      return this.templates[i].subjectName;
    return "Unknown";
  }

  public boolean processData(float[] paramArrayOfFloat)
  {
    System.nanoTime();
    if (!this.a)
      return false;
    int i = this.params.epochLen / 4;
    EkgEpoch localEkgEpoch1 = (paramArrayOfFloat = new EkgEpoch(paramArrayOfFloat)).subEpoch(i, this.params.epochLen - i);
    int[] arrayOfInt1;
    (arrayOfInt1 = new int[2])[0] = this.params.prePeakLatency;
    arrayOfInt1[1] = this.params.postPeakLatency;
    int j;
    float f1;
    if (((j = localEkgEpoch1.find_heart_beats(arrayOfInt1, this.params.prePeakAmplitude)) != -1) && ((f1 = localEkgEpoch1.data[j]) != this.lastEpochValue))
    {
      this.lastEpochValue = f1;
      if ((paramArrayOfFloat = paramArrayOfFloat.subEpoch(j, j + 2 * i)).detectHighTail(j))
        paramArrayOfFloat = null;
      if (paramArrayOfFloat == null)
      {
        if (this.params.verboseMatlab == 1)
          System.out.println("Epoch removed");
      }
      else if (this.params.verboseMatlab == 1)
        System.out.println("Epoch selected");
      if ((paramArrayOfFloat != null) && (paramArrayOfFloat.getLineNoiseAmplitude() < this.params.lineNoiseThreshold) && ((paramArrayOfFloat = (paramArrayOfFloat = (paramArrayOfFloat = paramArrayOfFloat.smooth(this.params.smoothing)).detrend()).subtract(paramArrayOfFloat.median())).std() < this.params.artifactStdThreshold))
      {
        if (this.params.stanley == 1)
          paramArrayOfFloat = (paramArrayOfFloat = paramArrayOfFloat.subEpoch2(57, 67, 81, 97)).subtract(paramArrayOfFloat.median());
        this.currentData.addData(paramArrayOfFloat);
        if (this.currentData.lastTemplateIndex > this.params.templateNum)
        {
          Object localObject1 = new float[(paramArrayOfFloat = this.currentData.computeDistance(this.currentData)).numRows];
          int[][] arrayOfInt = new int[paramArrayOfFloat.numRows][this.params.templatesForDist];
          for (j = 0; j < paramArrayOfFloat.numRows; j++)
          {
            EkgEpoch localEkgEpoch2 = (localObject2 = new EkgEpoch(paramArrayOfFloat.array[j])).sort();
            Object localObject2 = ((EkgEpoch)localObject2).sortIndices(localEkgEpoch2);
            localObject1[j] = localEkgEpoch2.subEpoch(0, this.params.templatesForDist).sum();
            for (int n = 0; n < this.params.templatesForDist; n++)
              arrayOfInt[j][n] = localObject2[n];
          }
          for (j = 0; j < paramArrayOfFloat.numRows; j++)
          {
            if (this.params.verboseMatlab != 1)
              continue;
            System.out.println("Sum correlation " + localObject1[j]);
          }
          j = 0;
          float f2 = localObject1[0];
          for (int m = 1; m < paramArrayOfFloat.numRows; m++)
          {
            if (localObject1[m] >= f2)
              continue;
            j = m;
            f2 = localObject1[m];
          }
          if (f2 < this.params.epochValidMeanThreshold)
          {
            float f4 = 0.0F;
            for (int k = 0; k < this.params.templateNum; k++)
            {
              float f5;
              if ((f5 = this.currentData.templateArray[arrayOfInt[j][k]].max()) <= f4)
                continue;
              f4 = f5;
            }
            float f3 = (f4 - this.params.epochValidMaxModifier1) / this.params.epochValidMaxModifier2;
            for (int i1 = 0; i1 < this.params.templatesForDist; i1++)
            {
              if (this.params.verboseMatlab != 1)
                continue;
              System.out.println("Corr selected " + paramArrayOfFloat.array[j][arrayOfInt[j][i1]]);
            }
            float f6 = paramArrayOfFloat.array[j][arrayOfInt[j][(this.params.templatesForDist - 1)]];
            if (this.params.templatesForDist == 3)
              f6 = Math.max(f6, paramArrayOfFloat.array[arrayOfInt[j][1]][arrayOfInt[j][2]]);
            paramArrayOfFloat = this.params.epochValidMaxThreshold + f3;
            if (this.params.verboseMatlab == 1)
              System.out.println("Max corr: " + f6 + ", threshold " + paramArrayOfFloat);
            if (f6 < this.params.epochValidMaxThreshold + f3)
            {
              this.currentData.selectRows(arrayOfInt[j]);
              this.currentData.computeDistance(this.currentData);
              for (paramArrayOfFloat = 0; paramArrayOfFloat < this.lastTemplateInd; paramArrayOfFloat++)
              {
                localObject1 = this.currentData.computeDistance(this.templates[paramArrayOfFloat]);
                this.templates[paramArrayOfFloat].score = ((DistanceArray)localObject1).mean();
              }
              System.nanoTime();
              return true;
            }
          }
        }
      }
    }
    System.nanoTime();
    return false;
  }

  public boolean processData2(int[] paramArrayOfInt)
  {
    System.nanoTime();
    int i = 0;
    for (int j = 0; j < paramArrayOfInt.length; j++)
    {
      Object localObject1;
      if ((localObject1 = this.buf.AddData(paramArrayOfInt[j])) == null)
        continue;
      if ((localObject1 = new EkgEpoch(localObject1)).detectHighTail())
        localObject1 = null;
      if ((localObject1 == null) || (((EkgEpoch)localObject1).getLineNoiseAmplitude() >= this.params.lineNoiseThreshold) || ((localObject1 = (localObject1 = (localObject1 = ((EkgEpoch)localObject1).smooth(this.params.smoothing)).detrend()).subtract(((EkgEpoch)localObject1).median())).std() >= this.params.artifactStdThreshold))
        continue;
      if (this.params.stanley == 1)
        localObject1 = (localObject1 = ((EkgEpoch)localObject1).subEpoch2(57, 67, 81, 97)).subtract(((EkgEpoch)localObject1).median());
      this.currentData.addData((EkgEpoch)localObject1);
      if (this.currentData.lastTemplateIndex <= this.params.templateNum)
        continue;
      float[] arrayOfFloat = new float[(localObject1 = this.currentData.computeDistance(this.currentData)).numRows];
      int[][] arrayOfInt = new int[((DistanceArray)localObject1).numRows][this.params.templatesForDist];
      for (int k = 0; k < ((DistanceArray)localObject1).numRows; k++)
      {
        EkgEpoch localEkgEpoch = (localObject2 = new EkgEpoch(localObject1.array[k])).sort();
        Object localObject2 = ((EkgEpoch)localObject2).sortIndices(localEkgEpoch);
        arrayOfFloat[k] = localEkgEpoch.subEpoch(0, this.params.templatesForDist).sum();
        for (int i1 = 0; i1 < this.params.templatesForDist; i1++)
          arrayOfInt[k][i1] = localObject2[i1];
      }
      for (k = 0; k < ((DistanceArray)localObject1).numRows; k++)
      {
        if (this.params.verboseMatlab != 1)
          continue;
        System.out.println("Sum correlation " + arrayOfFloat[k]);
      }
      k = 0;
      float f2 = arrayOfFloat[0];
      for (int n = 1; n < ((DistanceArray)localObject1).numRows; n++)
      {
        if (arrayOfFloat[n] >= f2)
          continue;
        k = n;
        f2 = arrayOfFloat[n];
      }
      if (f2 >= this.params.epochValidMeanThreshold)
        continue;
      float f4 = 0.0F;
      for (int m = 0; m < this.params.templateNum; m++)
      {
        float f5;
        if ((f5 = this.currentData.templateArray[arrayOfInt[k][m]].max()) <= f4)
          continue;
        f4 = f5;
      }
      float f3 = (f4 - this.params.epochValidMaxModifier1) / this.params.epochValidMaxModifier2;
      for (int i2 = 0; i2 < this.params.templatesForDist; i2++)
      {
        if (this.params.verboseMatlab != 1)
          continue;
        System.out.println("Corr selected " + localObject1.array[k][arrayOfInt[k][i2]]);
      }
      float f6 = localObject1.array[k][arrayOfInt[k][(this.params.templatesForDist - 1)]];
      if (this.params.templatesForDist == 3)
        f6 = Math.max(f6, localObject1.array[arrayOfInt[k][1]][arrayOfInt[k][2]]);
      float f1 = this.params.epochValidMaxThreshold + f3;
      if (this.params.verboseMatlab == 1)
        System.out.println("Max corr: " + f6 + ", threshold " + f1);
      if (f6 >= this.params.epochValidMaxThreshold + f3)
        continue;
      this.currentData.selectRows(arrayOfInt[k]);
      this.currentData.computeDistance(this.currentData);
      for (i = 0; i < this.lastTemplateInd; i++)
      {
        DistanceArray localDistanceArray = this.currentData.computeDistance(this.templates[i]);
        this.templates[i].score = localDistanceArray.mean();
      }
      System.nanoTime();
      i = 1;
    }
    return i;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EkgSense
 * JD-Core Version:    0.6.0
 */