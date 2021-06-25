package com.neurosky.thinkgear;

import android.os.Handler;
import android.util.Log;

public class TF_TD_control
{
  private short[] a = new short[32768];
  private int b = 0;
  private int c = 0;
  private int d = 0;
  private int e = 0;
  private boolean f = false;
  private boolean g = false;
  private short[] h = new short[32768];
  private TF_TD_control.CalcThread i;
  private TGDevice j;
  private Handler k;
  private TaskFamiliarity l;
  private TaskDifficulty m;

  public TF_TD_control(TGDevice paramTGDevice)
  {
    if (paramTGDevice != null)
    {
      this.j = paramTGDevice;
      this.k = this.j.handler;
    }
    else
    {
      this.j = null;
      this.k = null;
    }
    this.l = new TaskFamiliarity();
    this.m = new TaskDifficulty();
  }

  public void killThread()
  {
    if (this.i != null)
    {
      this.g = true;
      this.i.interrupt();
      this.i = null;
      this.f = false;
    }
  }

  public static boolean isProvisioned()
  {
    return true;
  }

  public double updateTF_TD(short paramShort, int paramInt)
  {
    if (paramInt == 0)
    {
      this.a[(this.b++)] = paramShort;
      if (this.b >= 32768)
        this.b = 0;
      if (this.c < 32768)
        this.c += 1;
      this.d += 1;
      if ((this.c == 32768) && (this.d >= 30720))
      {
        this.d = 0;
        if (this.f)
        {
          this.e += 1;
        }
        else
        {
          System.arraycopy(this.a, this.b, this.h, 0, 32768 - this.b);
          System.arraycopy(this.a, 0, this.h, 32768 - this.b, 32768 - (32768 - this.b));
          this.f = true;
          if (this.i == null)
          {
            this.i = new TF_TD_control.CalcThread(this, this.k);
            this.i.setPriority(this.i.getPriority() - 1);
            this.i.start();
          }
          if (this.e > 0)
          {
            Log.d("TF_TD_calc", "HUMM: calc window count: " + this.e + 1);
            Log.d("TF_TD_calc", "HUMM: window size is: 30720");
            Log.d("TF_TD_calc", "HUMM: is the debugger in use?");
            this.e = 0;
          }
        }
        return -1.0D;
      }
      return -1.0D;
    }
    this.b = 0;
    this.c = 0;
    this.d = 0;
    this.e = 0;
    return -2.0D;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_control
 * JD-Core Version:    0.6.0
 */