package com.neurosky.thinkgear;

public class EEGAlgorithmToolbox$Complex
{
  private double a;
  private double b;
  public static Complex CZero = new Complex(0.0D, 0.0D);
  public static Complex CInfinity = new Complex((1.0D / 0.0D), (1.0D / 0.0D));

  public double getReal()
  {
    return this.a;
  }

  public double getImag()
  {
    return this.b;
  }

  public EEGAlgorithmToolbox$Complex(double paramDouble1, double paramDouble2)
  {
    this.a = paramDouble1;
    this.b = paramDouble2;
  }

  public static Complex divideOperation(Complex paramComplex, double paramDouble)
  {
    if (paramDouble == 0.0D)
      return CInfinity;
    double d1 = paramComplex.a / paramDouble;
    double d2 = paramComplex.b / paramDouble;
    return new Complex(d1, d2);
  }

  public static Complex CMultipleValue(Complex paramComplex, double paramDouble)
  {
    return new Complex(paramComplex.a * paramDouble, paramComplex.b * paramDouble);
  }

  public static double CNorm(Complex paramComplex)
  {
    return Math.sqrt(paramComplex.a * paramComplex.a + paramComplex.b * paramComplex.b);
  }

  public static Complex CExp(Complex paramComplex)
  {
    double d1 = paramComplex.a;
    double d2 = paramComplex.b;
    double d3 = Math.exp(d1);
    return new Complex(d3 * Math.cos(d2), d3 * Math.sin(d2));
  }

  public static Complex CLog(Complex paramComplex)
  {
    return new Complex(Math.log(CNorm(paramComplex)), Math.atan2(paramComplex.b, paramComplex.a));
  }

  public static Complex CSqrt(Complex paramComplex)
  {
    return CExp(divideOperation(CLog(paramComplex), 2.0D));
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EEGAlgorithmToolbox.Complex
 * JD-Core Version:    0.6.0
 */