package com.neurosky.thinkgear;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TF_TD_control$CalcThread extends Thread
{
  private Handler a;

  public TF_TD_control$CalcThread(TF_TD_control paramTF_TD_control, Handler paramHandler)
  {
    this.a = paramHandler;
  }

  public void run()
  {
    setName("TG-TF_TD_CalcThread: " + getName());
    try
    {
      while (!TF_TD_control.a(this.b))
      {
        if (TF_TD_control.b(this.b))
          calcTF_TD();
        sleep(1000L);
      }
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
    }
  }

  public void calcTF_TD()
  {
    if (TF_TD_control.c(this.b).getTaskFamiliarityEnable())
    {
      double d2 = TF_TD_control.e(this.b).TaskFamiliarity(TF_TD_control.d(this.b));
      if (TF_TD_control.a(this.b))
        return;
      this.a.obtainMessage(27, Double.valueOf(d2)).sendToTarget();
      Log.v("TaskFamiliarity", "familiarity_index: " + d2);
    }
    if (TF_TD_control.a(this.b))
      return;
    if (TF_TD_control.c(this.b).getTaskDifficultyEnable())
    {
      double d1 = TF_TD_control.f(this.b).TaskDifficulty(TF_TD_control.d(this.b));
      if (TF_TD_control.a(this.b))
        return;
      this.a.obtainMessage(28, Double.valueOf(d1)).sendToTarget();
      Log.v("TaskDifficulty", "difficulty_index: " + d1);
    }
    if (TF_TD_control.a(this.b))
      return;
    if ((TF_TD_control.c(this.b).getTaskFamiliarityEnable()) && (!TF_TD_control.c(this.b).getTaskFamiliarityRunContinuous()))
      TF_TD_control.c(this.b).setTaskFamiliarityEnable(false);
    if ((TF_TD_control.c(this.b).getTaskDifficultyEnable()) && (!TF_TD_control.c(this.b).getTaskDifficultyRunContinuous()))
      TF_TD_control.c(this.b).setTaskDifficultyEnable(false);
    TF_TD_control.a(this.b, false);
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.TF_TD_control.CalcThread
 * JD-Core Version:    0.6.0
 */