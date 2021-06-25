package com.neurosky.thinkgear;

public class EEGAlgorithmToolbox$BandPower
{
  private double[][] a;
  private double[][] b;
  private double[][] c;
  private double[][] d = { { 1.0D, 4.0D }, { 4.0D, 8.0D }, { 8.0D, 11.0D }, { 11.0D, 14.0D }, { 14.0D, 25.0D }, { 25.0D, 36.0D }, { 36.0D, 40.0D }, { 40.0D, 44.0D } };

  public EEGAlgorithmToolbox$BandPower(EEGAlgorithmToolbox paramEEGAlgorithmToolbox)
  {
  }

  public EEGAlgorithmToolbox.Bandpower_Return Bandpower_fun(double[] paramArrayOfDouble, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = this.d.length;
    this.c = new double[i][paramArrayOfDouble.length];
    this.a = new double[i][(int)(Math.floor((paramArrayOfDouble.length - paramInt2 * paramInt1) / (paramInt3 * paramInt1)) + 1.0D)];
    int j = 0;
    if (j < i)
    {
      double d1 = this.d[j][0] / paramInt1 * 2.0D;
      double d2 = this.d[j][1] / paramInt1 * 2.0D;
      double[] arrayOfDouble4 = { d1, d2 };
      EEGAlgorithmToolbox.Butter localButter;
      EEGAlgorithmToolbox.Butter_Return localButter_Return;
      double[] arrayOfDouble5 = (localButter_Return = (localButter = new EEGAlgorithmToolbox.Butter(this.e)).ButterworthBandPassFilter(4, arrayOfDouble4)).get_B();
      double[] arrayOfDouble1 = localButter_Return.get_A();
      EEGAlgorithmToolbox.filtfilt localfiltfilt;
      arrayOfDouble1 = (localfiltfilt = new EEGAlgorithmToolbox.filtfilt(this.e)).ZeroPhaseFiltering(arrayOfDouble5, arrayOfDouble1, paramArrayOfDouble);
      for (int k = 0; k < arrayOfDouble1.length; k++)
        this.c[j][k] = arrayOfDouble1[k];
      double[] arrayOfDouble2 = new double[arrayOfDouble1.length];
      for (int m = 0; m < arrayOfDouble1.length; m++)
        arrayOfDouble2[m] = Math.pow(this.c[j][m], 2.0D);
      arrayOfDouble3 = new double[arrayOfDouble2.length];
      for (int n = 0; n < arrayOfDouble2.length; n++)
        if (Double.isNaN(arrayOfDouble2[n]))
          arrayOfDouble3[n] = 0.0D;
        else
          arrayOfDouble3[n] = arrayOfDouble2[n];
    }
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.BandPower
 * JD-Core Version:    0.6.0
 */