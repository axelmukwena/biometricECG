package com.neurosky.thinkgear;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.Random;

public class EkgSenseManager$EkgTimer extends Thread
{
  private boolean a = true;

  public EkgSenseManager$EkgTimer(EkgSenseManager paramEkgSenseManager)
  {
  }

  public void run()
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return;
    long l1 = System.currentTimeMillis();
    Random localRandom = new Random();
    int i = 200;
    while (this.a)
    {
      Object localObject;
      if ((System.currentTimeMillis() - l1 > i) && (EkgSenseManager.a(this.b).ekgState != 0) && (EkgSenseManager.b(EkgSenseManager.a(this.b).ekgSenseMan) == EkgSenseManager.a(this.b).ekgSenseMan.ekgSenseObj.params.epochLen))
      {
        if (EkgSenseManager.a(this.b).ekgSenseMan.ekgSenseObj.processData(EkgSenseManager.c(EkgSenseManager.a(this.b).ekgSenseMan)))
          switch (EkgSenseManager.a(this.b).ekgState)
          {
          case 1:
            EkgSenseManager.a(this.b).ekgSenseMan.addUser(EkgSenseManager.a(this.b).ekgSenseMan.learnName);
            EkgSenseManager.a(this.b).ekgSenseMan.resetBuffer();
            EkgSenseManager.a(this.b).ekgSenseMan.loadTemplates();
            EkgSenseManager.a(this.b).ekgState = 0;
            EkgSenseManager.a(this.b).handler.obtainMessage(269, Integer.valueOf(0)).sendToTarget();
            Log.v("TGDevice", "Learn complete");
            break;
          case 2:
            if (!EkgSenseManager.a(this.b).ekgSenseMan.a)
              continue;
            EkgSenseManager.d(EkgSenseManager.a(this.b).ekgSenseMan)[EkgSenseManager.a(this.b).ekgSenseMan.learnCounter] = EkgSenseManager.a(this.b).ekgSenseMan.ekgSenseObj.currentData;
            EkgSenseManager.d(EkgSenseManager.a(this.b).ekgSenseMan)[EkgSenseManager.a(this.b).ekgSenseMan.learnCounter].subjectName = EkgSenseManager.a(this.b).ekgSenseMan.learnName;
            EkgSenseManager.a(this.b).ekgSenseMan.learnCounter += 1;
            Log.v("TGDevice", "Train step: " + EkgSenseManager.a(this.b).ekgSenseMan.learnCounter);
            if (EkgSenseManager.a(this.b).ekgSenseMan.learnCounter == EkgSenseManager.a(this.b).ekgSenseMan.ekgSenseObj.params.templateNum)
            {
              new EkgTemplate(EkgSenseManager.a(this.b).ekgSenseMan.learnName);
              localObject = null;
              try
              {
                Log.v("TGDevice", "Long learn complete");
                (localObject = EkgTemplate.buildStrongTemplateNew(EkgSenseManager.d(EkgSenseManager.a(this.b).ekgSenseMan))).subjectName = EkgSenseManager.a(this.b).ekgSenseMan.learnName;
                EkgSenseManager.a(this.b).ekgSenseMan.addUser((EkgTemplate)localObject);
                EkgSenseManager.a(this.b).ekgState = 0;
                EkgSenseManager.a(this.b).ekgSenseMan.learnName = "";
                EkgSenseManager.a(this.b).ekgSenseMan.loadTemplates();
                EkgSenseManager.a(this.b).handler.obtainMessage(269, Integer.valueOf(0)).sendToTarget();
                EkgSenseManager.a(this.b).ekgSenseMan.loadTemplates();
                this.b.a = false;
              }
              catch (Exception localException2)
              {
                Exception localException1;
                (localException1 = localException2).printStackTrace();
              }
              EkgSenseManager.a(this.b).ekgSenseMan.learnCounter = 0;
            }
            else
            {
              EkgSenseManager.a(this.b).handler.obtainMessage(270, EkgSenseManager.a(this.b).ekgSenseMan.learnCounter, 0).sendToTarget();
            }
            EkgSenseManager.a(this.b).ekgSenseMan.a = false;
            break;
          case 3:
            if (!this.b.a)
              continue;
            EkgSenseManager.a(this.b).handler.obtainMessage(268, EkgSenseManager.a(this.b).ekgSenseMan.ekgSenseObj.getClassificationResults()).sendToTarget();
          }
        i = 150 + localRandom.nextInt(101);
        long l2 = System.currentTimeMillis();
      }
      try
      {
        Thread.sleep(20L);
      }
      catch (InterruptedException localInterruptedException)
      {
        (localObject = localInterruptedException).printStackTrace();
      }
    }
  }

  public void cancel()
  {
    this.a = false;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EkgSenseManager.EkgTimer
 * JD-Core Version:    0.6.0
 */