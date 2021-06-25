package com.neurosky.thinkgear;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RespiratoryRate$CalcThread extends Thread
{
  private Handler a;
  private int b = 32768 - RespiratoryRate.a(this.A).length;
  private float[] c;
  private TGHrv d;
  private SplineInterpolation e;
  private Detrend f;
  private PSD g;
  private PSDResult h;
  private List i;
  private List j;
  private List k;
  private int[] l;
  private int m;
  private float[] n;
  private int o;
  private int p;
  private float q;
  private int r;
  private int s;
  private int t;
  private int[] u;
  private float[] v;
  private float[] w;
  private float[] x;
  private float y;
  private int z;

  public RespiratoryRate$CalcThread(RespiratoryRate paramRespiratoryRate, Handler paramHandler)
  {
    this.a = paramHandler;
    this.c = new float[this.b];
    this.d = new TGHrv();
    this.e = new SplineInterpolation();
    this.f = new Detrend();
    this.g = new PSD(256, 512, 8);
    this.i = new ArrayList();
    this.j = new ArrayList();
    this.k = new ArrayList();
  }

  public void run()
  {
    setName("TG-CalcRespRateThread: " + getName());
    try
    {
      while (!RespiratoryRate.b(this.A))
      {
        if (RespiratoryRate.c(this.A))
          calculate();
        sleep(1000L);
      }
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
    }
  }

  public float calculate()
  {
    Arrays.fill(this.c, 0.0F);
    int i2;
    for (int i1 = 0; i1 < this.b; i1++)
      for (i2 = 0; i2 < RespiratoryRate.a(this.A).length; i2++)
        this.c[i1] += RespiratoryRate.a(this.A)[i2] * RespiratoryRate.d(this.A)[(i1 + i2)];
    if (RespiratoryRate.b(this.A))
      return -1.0F;
    this.i.clear();
    this.d.Reset();
    for (i1 = 0; i1 < this.b; i1++)
    {
      this.m = this.d.AddData((int)this.c[i1]);
      if (this.m <= 0)
        continue;
      this.i.add(Integer.valueOf(i1));
    }
    if (RespiratoryRate.b(this.A))
      return -1.0F;
    this.j.clear();
    this.r = 0;
    for (i1 = 0; i1 < this.i.size(); i1++)
    {
      this.o = (((Integer)this.i.get(i1)).intValue() - 15);
      if (this.o < 0)
        this.o = 0;
      this.p = (((Integer)this.i.get(i1)).intValue() + 15);
      if (this.p > this.c.length - 1)
        this.p = (this.c.length - 1);
      this.q = this.c[this.o];
      for (i2 = this.o; i2 <= this.p; i2++)
      {
        if (this.c[i2] < this.q)
          continue;
        this.q = this.c[i2];
        this.r = i2;
      }
      this.j.add(Integer.valueOf(this.r));
    }
    if (this.j.size() < 25)
    {
      RespiratoryRate.a(this.A, false);
      Log.v("CalcRespRate", "not enough heart beats to calc respiration, skipping window");
      return -1.0F;
    }
    if (RespiratoryRate.b(this.A))
      return -1.0F;
    this.k.clear();
    for (i1 = 1; i1 < this.j.size(); i1++)
      this.k.add(Integer.valueOf(((Integer)this.j.get(i1)).intValue() - ((Integer)this.j.get(i1 - 1)).intValue()));
    this.j.remove(0);
    if (RespiratoryRate.b(this.A))
      return -1.0F;
    this.l = new int[this.k.size()];
    for (i1 = 0; i1 < this.k.size(); i1++)
      this.l[i1] = ((Integer)this.k.get(i1)).intValue();
    for (i1 = 7; i1 < this.k.size() - 1; i1++)
    {
      this.n = new float[] { ((Integer)this.k.get(i1 - 7)).intValue(), ((Integer)this.k.get(i1 - 6)).intValue(), ((Integer)this.k.get(i1 - 5)).intValue(), ((Integer)this.k.get(i1 - 4)).intValue(), ((Integer)this.k.get(i1 - 3)).intValue(), ((Integer)this.k.get(i1 - 2)).intValue(), ((Integer)this.k.get(i1 - 1)).intValue() };
      if ((((Integer)this.k.get(i1)).intValue() <= 1.4D * RespiratoryRate.a(this.A, this.n)) && (((Integer)this.k.get(i1)).intValue() >= 0.6D * RespiratoryRate.a(this.A, this.n)))
        continue;
      this.l[i1] = (int)((((Integer)this.k.get(i1 + 1)).intValue() + ((Integer)this.k.get(i1 - 1)).intValue()) / 2.0D);
    }
    if (RespiratoryRate.b(this.A))
      return -1.0F;
    this.k.clear();
    for (i1 = 0; i1 < this.l.length; i1++)
      this.k.add(Integer.valueOf(this.l[i1]));
    this.k.remove(0);
    this.k.remove(1);
    this.k.remove(2);
    this.k.remove(3);
    this.k.remove(4);
    this.k.remove(5);
    this.k.remove(6);
    this.k.remove(this.k.size() - 1);
    this.j.remove(0);
    this.j.remove(1);
    this.j.remove(2);
    this.j.remove(3);
    this.j.remove(4);
    this.j.remove(5);
    this.j.remove(6);
    this.j.remove(7);
    this.j.remove(this.j.size() - 1);
    if (RespiratoryRate.b(this.A))
      return -1.0F;
    this.s = (int)(Math.floor(((Integer)this.j.get(0)).intValue() / 64.0D) * 64.0D);
    this.t = (int)(Math.ceil(((Integer)this.j.get(this.j.size() - 1)).intValue() / 64.0D) * 64.0D);
    this.u = new int[this.t / 64 - this.s / 64 + 1];
    this.u[0] = this.s;
    for (i1 = 1; i1 < this.u.length; i1++)
      this.u[i1] = (this.u[(i1 - 1)] + 64);
    if (RespiratoryRate.b(this.A))
      return -1.0F;
    this.v = this.e.interpolate(this.j, this.k, this.u);
    if (this.v.length < 256)
    {
      RespiratoryRate.a(this.A, false);
      Log.v("CalcRespRate", "not enough data points for pWelch, skipping window");
      return -1.0F;
    }
    if (RespiratoryRate.b(this.A))
      return -1.0F;
    this.v = this.f.removeLinearTrend(this.v);
    this.h = this.g.pWelch(this.v);
    this.w = this.h.getPower();
    this.x = this.h.getFrequency();
    if (RespiratoryRate.b(this.A))
      return -1.0F;
    for (i1 = 0; i1 < this.w.length; i1++)
      this.w[i1] *= RespiratoryRate.e(this.A)[i1];
    this.y = 0.0F;
    this.z = 6;
    for (i1 = 6; i1 < 34; i1++)
    {
      if ((this.w[(i1 - 1)] >= this.w[i1]) || (this.w[(i1 + 1)] >= this.w[i1]) || (this.w[i1] < this.y))
        continue;
      this.y = this.w[i1];
      this.z = i1;
    }
    if (RespiratoryRate.b(this.A))
      return -1.0F;
    float f1 = (float)(Math.floor(this.x[this.z] * 60.0D * 100.0D + 0.5D) / 100.0D);
    this.a.obtainMessage(25, Float.valueOf(f1)).sendToTarget();
    RespiratoryRate.a(this.A, false);
    return f1;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.RespiratoryRate.CalcThread
 * JD-Core Version:    0.6.0
 */