package com.neurosky.thinkgear;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class EkgSenseManager
{
  private TGDevice b;
  public EkgSense ekgSenseObj;
  private float[] c;
  private float[] d;
  private int e;
  private boolean f = true;
  private int g = 0;
  private EkgTemplate[] h;
  public int learnCounter = 0;
  boolean a = true;
  private EkgSenseManager.EkgTimer i;
  public String learnName = "";
  private String j;
  private String k;
  private String l;
  private int m = 0;

  public EkgSenseManager(TGDevice paramTGDevice)
  {
    this.b = paramTGDevice;
    this.ekgSenseObj = new EkgSense(new EkgParameters());
    this.c = new float[this.ekgSenseObj.params.epochLen];
    this.d = new float[this.ekgSenseObj.params.epochLen];
    if ("mounted".equals(Environment.getExternalStorageState()))
    {
      this.j = (Environment.getExternalStorageDirectory() + "/Android/data/com.neurosky.thinkgear/files/EKG");
      this.k = (this.j + "/parameters");
      this.l = (this.j + "/templates");
    }
    else
    {
      this.j = (Environment.getDataDirectory() + "/com.neurosky.thinkgear/files/EKG");
      this.k = (this.j + "/parameters");
      this.l = (this.j + "/templates");
    }
    Log.v("TGDevice", "EKG template path: " + this.l);
    if (!(paramTGDevice = new File(this.l)).exists())
    {
      if (paramTGDevice.mkdirs())
      {
        Log.d("TGDevice", "Templates directory created successfully");
      }
      else
      {
        Log.d("TGDevice", "EKG Data CAN NOT be saved, can not create Templates directory");
        Log.d("TGDevice", "EKG Data CAN NOT be saved, check permissions in AndroidManifest.xml");
      }
    }
    else if (!paramTGDevice.canWrite())
    {
      Log.d("TGDevice", "EKG Data CAN NOT be saved, directory is Write Proctected");
      Log.d("TGDevice", "EKG Data CAN NOT be saved, check permissions in AndroidManifest.xml");
    }
    loadTemplates();
    this.h = new EkgTemplate[this.ekgSenseObj.params.templateNum];
    this.i = new EkgSenseManager.EkgTimer(this);
    this.i.start();
  }

  public void resetBuffer()
  {
    this.c = new float[this.ekgSenseObj.params.epochLen];
    this.e = 0;
  }

  public void addSample(int paramInt1, int paramInt2)
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return;
    if ((paramInt2 == 0) && (this.m == 0))
    {
      this.m = paramInt2;
      return;
    }
    if ((paramInt2 == 0) && (this.m == 200))
    {
      this.ekgSenseObj.currentData = new EkgTemplate();
      resetBuffer();
      this.a = true;
      this.m = paramInt2;
      return;
    }
    if ((paramInt2 == 200) && (this.m == 0))
    {
      this.f = true;
      this.g = 0;
      resetBuffer();
      this.m = paramInt2;
    }
    if ((this.f) && (this.g < 2048))
    {
      this.g += 1;
      return;
    }
    if ((this.f) && (this.g == 2048))
    {
      this.f = false;
      this.g = 0;
      loadTemplates();
    }
    System.arraycopy(this.c, 1, this.d, 0, this.c.length - 1);
    this.d[(this.d.length - 1)] = paramInt1;
    System.arraycopy(this.d, 0, this.c, 0, this.c.length);
    if (this.e < this.c.length)
      this.e += 1;
    this.m = paramInt2;
  }

  public boolean loadParameterFile(String paramString)
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return false;
    Object localObject = new File(this.k);
    paramString = new File(this.k, paramString + ".json");
    try
    {
      if (!((File)localObject).exists())
      {
        Log.d("TGDevice", "EKG folder does not exist.");
        return false;
      }
      if (!paramString.exists())
      {
        Log.d("TGDevice", "Parameter file not found.");
        return false;
      }
      localObject = "";
      paramString = new BufferedReader(new FileReader(paramString));
      do
        localObject = (String)localObject + paramString.readLine();
      while (paramString.ready());
      paramString.close();
      this.ekgSenseObj.params = new EkgParameters((String)localObject);
      Log.v("TGDevice", "EKG Parmeters loaded");
      return true;
    }
    catch (IOException localIOException)
    {
      Log.v("TGDevice", "WTF: " + localIOException);
    }
    return false;
  }

  public void loadTemplates()
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return;
    Object localObject1;
    if ((localObject1 = (localObject1 = new File(this.l)).list()) != null)
    {
      this.ekgSenseObj.reset();
      Object localObject2;
      int n = (localObject2 = localObject1).length;
      for (int i1 = 0; i1 < n; i1++)
      {
        String str;
        if (!(str = localObject2[i1]).endsWith(".json"))
          continue;
        try
        {
          BufferedReader localBufferedReader = new BufferedReader(new FileReader(new File(this.l, str)));
          localObject1 = "";
          EkgTemplate localEkgTemplate = new EkgTemplate();
          do
            localObject1 = (String)localObject1 + localBufferedReader.readLine();
          while (localBufferedReader.ready());
          if (localEkgTemplate.loadJson((String)localObject1))
          {
            this.ekgSenseObj.addTemplate(localEkgTemplate);
            Log.v("TGDevice", "Loaded template: " + str);
          }
          else
          {
            Log.v("TGDevice", "Template load failed: " + str);
          }
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          (localObject1 = localFileNotFoundException).printStackTrace();
        }
        catch (IOException localIOException)
        {
        }
      }
    }
  }

  public boolean addUser(String paramString)
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return false;
    if (!(localObject = new File(this.l)).canWrite())
    {
      Log.d("TGDevice", "EKG Data CAN NOT be saved, Write Proctected");
      return false;
    }
    Object localObject = new File(this.l, paramString + ".json");
    try
    {
      if (!((File)localObject).exists())
        ((File)localObject).createNewFile();
      localObject = new FileOutputStream((File)localObject, false);
      this.ekgSenseObj.currentData.subjectName = paramString;
      ((OutputStream)localObject).write(this.ekgSenseObj.currentData.toJSONString().getBytes());
      ((OutputStream)localObject).close();
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      (localObject = localFileNotFoundException).printStackTrace();
    }
    catch (IOException localIOException)
    {
      return false;
    }
    return true;
  }

  public boolean addUser(EkgTemplate paramEkgTemplate)
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return false;
    if (!(localObject = new File(this.l)).canWrite())
    {
      Log.d("TGDevice", "EKG Data CAN NOT be saved, Write Proctected");
      return false;
    }
    Object localObject = new File(this.l, paramEkgTemplate.subjectName + ".json");
    try
    {
      if (!((File)localObject).exists())
        ((File)localObject).createNewFile();
      (localObject = new FileOutputStream((File)localObject, false)).write(paramEkgTemplate.toJSONString().getBytes());
      ((OutputStream)localObject).close();
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      (paramEkgTemplate = localFileNotFoundException).printStackTrace();
    }
    catch (IOException localIOException)
    {
      return false;
    }
    return true;
  }

  public boolean deleteUser(String paramString)
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return false;
    File localFile1;
    if (!(localFile1 = new File(this.l)).canWrite())
    {
      Log.d("TGDevice", "EKG Data CAN NOT be deleted, Write Proctected");
      return false;
    }
    String[] arrayOfString;
    if ((arrayOfString = localFile1.list()) != null)
    {
      int n = (arrayOfString = arrayOfString).length;
      for (int i1 = 0; i1 < n; i1++)
      {
        String str;
        File localFile2;
        if ((!(str = arrayOfString[i1]).startsWith(paramString + ".json")) || (!(localFile2 = new File(localFile1, str)).delete()))
          continue;
        Log.v("TGDevice", "EKG Deleted file: " + str);
        loadTemplates();
        return true;
      }
    }
    return false;
  }

  public boolean saveData()
  {
    if (!TGDevice.ekgPersonalizationEnabled)
      return false;
    Object localObject;
    if (!(localObject = new File(this.l)).canWrite())
    {
      Log.d("TGDevice", "EKG Data CAN NOT be saved, Write Proctected");
      return false;
    }
    for (int n = 0; n < this.ekgSenseObj.lastTemplateInd; n++)
    {
      localObject = new File(this.l, this.ekgSenseObj.templates[n].subjectName + "2.json");
      try
      {
        if (!((File)localObject).exists())
          ((File)localObject).createNewFile();
        (localObject = new FileOutputStream((File)localObject)).write(this.ekgSenseObj.templates[n].toJSONString().getBytes());
        ((OutputStream)localObject).close();
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        (localObject = localFileNotFoundException).printStackTrace();
      }
      catch (IOException localIOException)
      {
      }
    }
    return true;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.EkgSenseManager
 * JD-Core Version:    0.6.0
 */