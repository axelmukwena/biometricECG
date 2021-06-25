package com.neurosky.thinkgear;

import android.os.Handler;
import android.util.Log;

public class RespiratoryRate
{
  private final float[] a = { -0.091831F, -0.011071F, -0.011676F, -0.012308F, -0.012902F, -0.013514F, -0.014087F, -0.014688F, -0.01523F, -0.015806F, -0.016282F, -0.0168501F, -0.017316F, -0.017691F, -0.018164F, -0.018572F, -0.018938F, -0.019236F, -0.019519F, -0.019759F, -0.019981F, -0.0201654F, -0.0203217F, -0.0204582F, -0.0205138F, 0.97947F, -0.0205138F, -0.0204582F, -0.0203217F, -0.0201654F, -0.019981F, -0.019759F, -0.019519F, -0.019236F, -0.018938F, -0.018572F, -0.018164F, -0.017691F, -0.017316F, -0.0168501F, -0.016282F, -0.015806F, -0.01523F, -0.014688F, -0.014087F, -0.013514F, -0.012902F, -0.012308F, -0.011676F, -0.011071F, -0.091831F };
  private final float[] b = { 0.0F, 0.010309F, 0.022097F, 0.0345172F, 0.0473661F, 0.0605437F, 0.0739892F, 0.0876617F, 0.101532F, 0.115576F, 0.129778F, 0.144123F, 0.158599F, 0.173197F, 0.187907F, 0.202723F, 0.217638F, 0.232646F, 0.247743F, 0.262924F, 0.278186F, 0.293524F, 0.308935F, 0.324416F, 0.339965F, 0.355579F, 0.371255F, 0.386992F, 0.402787F, 0.418639F, 0.434546F, 0.450505F, 0.466516F, 0.482578F, 0.498688F, 0.514845F, 0.531049F, 0.547298F, 0.563591F, 0.579927F, 0.596304F, 0.612723F, 0.629182F, 0.64568F, 0.662216F, 0.678791F, 0.695402F, 0.712049F, 0.728731F, 0.745449F, 0.7622F, 0.778985F, 0.795803F, 0.812654F, 0.829536F, 0.846449F, 0.863394F, 0.880368F, 0.897373F, 0.914406F, 0.931469F, 0.94856F, 0.965679F, 0.982826F, 1.0F, 1.0172F, 1.03443F, 1.05168F, 1.06896F, 1.08627F, 1.1036F, 1.12095F, 1.13833F, 1.15573F, 1.17316F, 1.19061F, 1.20808F, 1.22558F, 1.2431F, 1.26064F, 1.27821F, 1.29579F, 1.3134F, 1.33103F, 1.34868F, 1.36635F, 1.38405F, 1.40176F, 1.41949F, 1.43725F, 1.45502F, 1.47281F, 1.49063F, 1.50846F, 1.52631F, 1.54418F, 1.56207F, 1.57998F, 1.5979F, 1.61585F, 1.63381F, 1.65179F, 1.66979F, 1.68781F, 1.70584F, 1.72389F, 1.74196F, 1.76005F, 1.77815F, 1.79627F, 1.8144F, 1.83256F, 1.85072F, 1.86891F, 1.88711F, 1.90533F, 1.92356F, 1.94181F, 1.96007F, 1.97835F, 1.99665F, 2.01496F, 2.03328F, 2.05162F, 2.06998F, 2.08835F, 2.10673F, 2.12513F, 2.14355F, 2.16198F, 2.18042F, 2.19887F, 2.21735F, 2.23583F, 2.25433F, 2.27284F, 2.29137F, 2.30991F, 2.32846F, 2.34703F, 2.36561F, 2.3842F, 2.40281F, 2.42143F, 2.44006F, 2.45871F, 2.47737F, 2.49604F, 2.51472F, 2.53342F, 2.55213F, 2.57085F, 2.58958F, 2.60833F, 2.62709F, 2.64586F, 2.66464F, 2.68344F, 2.70225F, 2.72106F, 2.7399F, 2.75874F, 2.77759F, 2.79646F, 2.81534F, 2.83423F, 2.85313F, 2.87204F, 2.89096F, 2.9099F, 2.92884F, 2.9478F, 2.96677F, 2.98575F, 3.00474F, 3.02374F, 3.04275F, 3.06177F, 3.0808F, 3.09985F, 3.1189F, 3.13797F, 3.15704F, 3.17613F, 3.19523F, 3.21433F, 3.23345F, 3.25258F, 3.27172F, 3.29086F, 3.31002F, 3.32919F, 3.34837F, 3.36756F, 3.38676F, 3.40596F, 3.42518F, 3.44441F, 3.46365F, 3.4829F, 3.50215F, 3.52142F, 3.5407F, 3.55998F, 3.57928F, 3.59858F, 3.6179F, 3.63722F, 3.65655F, 3.67589F, 3.69525F, 3.71461F, 3.73398F, 3.75335F, 3.77274F, 3.79214F, 3.81155F, 3.83096F, 3.85039F, 3.86982F, 3.88926F, 3.90871F, 3.92817F, 3.94764F, 3.96712F, 3.9866F, 4.0061F, 4.0256F, 4.04511F, 4.06463F, 4.08416F, 4.1037F, 4.12324F, 4.1428F, 4.16236F, 4.18193F, 4.20151F, 4.2211F, 4.24069F, 4.2603F, 4.27991F, 4.29953F, 4.31916F, 4.33879F, 4.35844F, 4.37809F, 4.39775F, 4.41742F, 4.4371F, 4.45678F, 4.47647F, 4.49617F, 4.51588F, 4.5356F, 4.55532F, 4.57505F, 4.59479F };
  private short[] c = new short[32768];
  private int d = 0;
  private int e = 0;
  private int f = 0;
  private int g = 0;
  private boolean h = false;
  private boolean i = false;
  private short[] j = new short[32768];
  private RespiratoryRate.CalcThread k;
  private TGDevice l;
  private Handler m;

  public RespiratoryRate(TGDevice paramTGDevice)
  {
    if (paramTGDevice != null)
    {
      this.l = paramTGDevice;
      this.m = this.l.handler;
      return;
    }
    this.l = null;
    this.m = null;
  }

  public void killThread()
  {
    if (this.k != null)
    {
      this.i = true;
      this.k.interrupt();
      this.k = null;
      this.h = false;
    }
  }

  public static boolean isProvisioned()
  {
    return true;
  }

  public float calculateRespiratoryRate(short paramShort, int paramInt)
  {
    if (paramInt >= 200)
    {
      this.c[(this.d++)] = paramShort;
      if (this.d >= 32768)
        this.d = 0;
      if (this.e < 32768)
        this.e += 1;
      this.f += 1;
      if ((this.e == 32768) && (this.f >= 5120))
      {
        this.f = 0;
        if (this.h)
        {
          this.g += 1;
        }
        else
        {
          System.arraycopy(this.c, this.d, this.j, 0, 32768 - this.d);
          System.arraycopy(this.c, 0, this.j, 32768 - this.d, 32768 - (32768 - this.d));
          this.h = true;
          if (this.k == null)
          {
            this.k = new RespiratoryRate.CalcThread(this, this.m);
            this.k.setPriority(this.k.getPriority() - 1);
            this.k.start();
          }
          if (this.g > 0)
          {
            Log.d("RespRate", "HUMM: resp calc window count: " + this.g + 1);
            Log.d("RespRate", "HUMM: window size is: 5120");
            Log.d("RespRate", "HUMM: is the debugger in use?");
            this.g = 0;
          }
        }
        return -1.0F;
      }
      return -1.0F;
    }
    this.d = 0;
    this.e = 0;
    this.f = 0;
    this.g = 0;
    return -2.0F;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.RespiratoryRate
 * JD-Core Version:    0.6.0
 */