package com.neurosky.thinkgear;

public class TF_TD_Libraries$FeatureExtrationReturn
{
  protected double _hjorth_activity;
  protected double _hjorth_mobility;
  protected double _hjorth_complexity;
  protected double[] _bp_row_mean;
  protected double[] _rbp_row_mean;
  protected double[] _bp_activity;
  protected double[] _bp_mobility;
  protected double[] _bp_complexity;

  public TF_TD_Libraries$FeatureExtrationReturn(TF_TD_Libraries paramTF_TD_Libraries, double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3, double[] paramArrayOfDouble4, double[] paramArrayOfDouble5)
  {
    this._hjorth_activity = paramDouble1;
    this._hjorth_mobility = paramDouble2;
    this._hjorth_complexity = paramDouble3;
    this._bp_row_mean = paramArrayOfDouble1;
    this._rbp_row_mean = paramArrayOfDouble2;
    this._bp_activity = paramArrayOfDouble3;
    this._bp_mobility = paramArrayOfDouble4;
    this._bp_complexity = paramArrayOfDouble5;
  }

  public double get_hjorth_activity()
  {
    return this._hjorth_activity;
  }

  public double get_hjorth_mobility()
  {
    return this._hjorth_mobility;
  }

  public double get_hjorth_complexity()
  {
    return this._hjorth_complexity;
  }

  public double[] get_bp_row_mean()
  {
    return this._bp_row_mean;
  }

  public double[] get_rbp_row_mean()
  {
    return this._rbp_row_mean;
  }

  public double[] get_bp_activity()
  {
    return this._bp_activity;
  }

  public double[] get_bp_mobility()
  {
    return this._bp_mobility;
  }

  public double[] get_bp_complexity()
  {
    return this._bp_complexity;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_Libraries.FeatureExtrationReturn
 * JD-Core Version:    0.6.0
 */