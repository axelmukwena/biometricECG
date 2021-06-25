package com.neurosky.thinkgear;

import org.json.JSONObject;
import org.json.JSONTokener;

public class EkgParameters
{
  public int artifactStdThreshold = 2400;
  public int epochLen = 296;
  public int prePeakAmplitude = 2700;
  public int prePeakLatency = -15;
  public int postPeakLatency = 11;
  public int postPeakAmplitude = -1;
  public int epochValidMeanThreshold = 1700;
  public int epochValidMaxThreshold = 500;
  public int epochValidMaxModifier1 = 1000;
  public int epochValidMaxModifier2 = 10;
  public int templateNum = 5;
  public int templatesForDist = 5;
  public int templateMaxDistance = 435;
  public int templateDistThreshold = 50;
  public int templateDistStdDiff = 100;
  public int templateDistModifier1 = -1;
  public int templateDistModifier2 = -1;
  public int lineNoiseThreshold = 1000000;
  public int smoothing = 8;
  public int stanley = 0;
  public int verboseMatlab = 0;

  public EkgParameters()
  {
  }

  public EkgParameters(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16)
  {
    this.artifactStdThreshold = paramInt1;
    this.epochLen = paramInt2;
    this.prePeakAmplitude = paramInt4;
    this.prePeakLatency = paramInt5;
    this.postPeakLatency = paramInt7;
    this.postPeakAmplitude = paramInt6;
    this.epochValidMeanThreshold = paramInt8;
    this.epochValidMaxThreshold = paramInt9;
    this.epochValidMaxModifier1 = paramInt10;
    this.epochValidMaxModifier2 = paramInt11;
    this.templateNum = paramInt12;
    this.templateDistThreshold = paramInt13;
    this.templateDistStdDiff = paramInt14;
    this.templateDistModifier1 = paramInt15;
    this.templateDistModifier2 = paramInt16;
  }

  public EkgParameters(String paramString)
  {
    try
    {
      if ((paramString = (JSONObject)new JSONTokener(paramString).nextValue()).has("artifactStdThreshold"))
        this.artifactStdThreshold = paramString.getInt("artifactStdThreshold");
      if (paramString.has("epochLen"))
        this.epochLen = paramString.getInt("epochLen");
      if (paramString.has("prePeakAmplitude"))
        this.prePeakAmplitude = paramString.getInt("prePeakAmplitude");
      if (paramString.has("prePeakLatency"))
        this.prePeakLatency = paramString.getInt("prePeakLatency");
      if (paramString.has("PostPeakLatency"))
        this.postPeakLatency = paramString.getInt("PostPeakLatency");
      if (paramString.has("PostPeakAmplitude"))
        this.postPeakAmplitude = paramString.getInt("PostPeakAmplitude");
      if (paramString.has("epochValidMeanThreshold"))
        this.epochValidMeanThreshold = paramString.getInt("epochValidMeanThreshold");
      if (paramString.has("epochValidMaxThreshold"))
        this.epochValidMaxThreshold = paramString.getInt("epochValidMaxThreshold");
      if (paramString.has("epochValidMaxModifier1"))
        this.epochValidMaxModifier1 = paramString.getInt("epochValidMaxModifier1");
      if (paramString.has("epochValidMaxModifier2"))
        this.epochValidMaxModifier2 = paramString.getInt("epochValidMaxModifier2");
      if (paramString.has("templateNum"))
        this.templateNum = paramString.getInt("templateNum");
      if (paramString.has("templatesForDist"))
        this.templatesForDist = paramString.getInt("templatesForDist");
      if (paramString.has("templateDistThreshold"))
        this.templateDistThreshold = paramString.getInt("templateDistThreshold)");
      if (paramString.has("templateDistStdDiff"))
        this.templateDistStdDiff = paramString.getInt("templateDistStdDiff");
      if (paramString.has("templateDistModifier1"))
        this.templateDistModifier1 = paramString.getInt("templateDistModifier1");
      if (paramString.has("templateDistModifier2"))
        this.templateDistModifier2 = paramString.getInt("templateDistModifier2");
      if (paramString.has("lineNoiseThreshold"))
        this.lineNoiseThreshold = paramString.getInt("lineNoiseThreshold");
      if (paramString.has("smoothing"))
        this.smoothing = paramString.getInt("smoothing");
      if (paramString.has("stanley"))
        this.stanley = paramString.getInt("stanley");
      if (paramString.has("verboseMatlab"))
        this.verboseMatlab = paramString.getInt("verboseMatlab");
      return;
    }
    catch (org.json.JSONException paramString)
    {
    }
  }

  public void save()
  {
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EkgParameters
 * JD-Core Version:    0.6.0
 */