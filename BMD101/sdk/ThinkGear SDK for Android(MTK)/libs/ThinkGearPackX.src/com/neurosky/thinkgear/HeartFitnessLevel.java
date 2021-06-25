package com.neurosky.thinkgear;

public class HeartFitnessLevel
{
  private String a;

  public String getHeartFitnessLevel(int paramInt1, String paramString, int paramInt2)
  {
    if ((!paramString.equals("Male")) && (!paramString.equals("Female")))
      return "";
    if ((paramInt1 >= 18) && (paramInt1 <= 25))
    {
      if (paramString.equals("Male"))
      {
        if (paramInt2 <= 55)
          this.a = "Athlete";
        else if ((paramInt2 >= 56) && (paramInt2 <= 61))
          this.a = "Excelent";
        else if ((paramInt2 >= 62) && (paramInt2 <= 65))
          this.a = "Good";
        else if ((paramInt2 >= 66) && (paramInt2 <= 69))
          this.a = "Above Average";
        else if ((paramInt2 >= 70) && (paramInt2 <= 73))
          this.a = "Average";
        else if ((paramInt2 >= 74) && (paramInt2 <= 81))
          this.a = "Below Average";
        else
          this.a = "Poor";
      }
      else if (paramInt2 <= 60)
        this.a = "Athlete";
      else if ((paramInt2 >= 61) && (paramInt2 <= 65))
        this.a = "Excelent";
      else if ((paramInt2 >= 66) && (paramInt2 <= 69))
        this.a = "Good";
      else if ((paramInt2 >= 70) && (paramInt2 <= 73))
        this.a = "Above Average";
      else if ((paramInt2 >= 74) && (paramInt2 <= 78))
        this.a = "Average";
      else if ((paramInt2 >= 79) && (paramInt2 <= 84))
        this.a = "Below Average";
      else
        this.a = "Poor";
    }
    else if ((paramInt1 >= 26) && (paramInt1 <= 35))
    {
      if (paramString.equals("Male"))
      {
        if (paramInt2 <= 54)
          this.a = "Athlete";
        else if ((paramInt2 >= 55) && (paramInt2 <= 61))
          this.a = "Excelent";
        else if ((paramInt2 >= 62) && (paramInt2 <= 65))
          this.a = "Good";
        else if ((paramInt2 >= 66) && (paramInt2 <= 70))
          this.a = "Above Average";
        else if ((paramInt2 >= 71) && (paramInt2 <= 74))
          this.a = "Average";
        else if ((paramInt2 >= 75) && (paramInt2 <= 81))
          this.a = "Below Average";
        else
          this.a = "Poor";
      }
      else if (paramInt2 <= 59)
        this.a = "Athlete";
      else if ((paramInt2 >= 60) && (paramInt2 <= 64))
        this.a = "Excelent";
      else if ((paramInt2 >= 65) && (paramInt2 <= 68))
        this.a = "Good";
      else if ((paramInt2 >= 69) && (paramInt2 <= 72))
        this.a = "Above Average";
      else if ((paramInt2 >= 73) && (paramInt2 <= 76))
        this.a = "Average";
      else if ((paramInt2 >= 77) && (paramInt2 <= 82))
        this.a = "Below Average";
      else
        this.a = "Poor";
    }
    else if ((paramInt1 >= 36) && (paramInt1 <= 45))
    {
      if (paramString.equals("Male"))
      {
        if (paramInt2 <= 56)
          this.a = "Athlete";
        else if ((paramInt2 >= 57) && (paramInt2 <= 62))
          this.a = "Excelent";
        else if ((paramInt2 >= 63) && (paramInt2 <= 66))
          this.a = "Good";
        else if ((paramInt2 >= 67) && (paramInt2 <= 70))
          this.a = "Above Average";
        else if ((paramInt2 >= 71) && (paramInt2 <= 75))
          this.a = "Average";
        else if ((paramInt2 >= 76) && (paramInt2 <= 82))
          this.a = "Below Average";
        else
          this.a = "Poor";
      }
      else if (paramInt2 <= 59)
        this.a = "Athlete";
      else if ((paramInt2 >= 60) && (paramInt2 <= 64))
        this.a = "Excelent";
      else if ((paramInt2 >= 65) && (paramInt2 <= 69))
        this.a = "Good";
      else if ((paramInt2 >= 70) && (paramInt2 <= 73))
        this.a = "Above Average";
      else if ((paramInt2 >= 74) && (paramInt2 <= 78))
        this.a = "Average";
      else if ((paramInt2 >= 79) && (paramInt2 <= 84))
        this.a = "Below Average";
      else
        this.a = "Poor";
    }
    else if ((paramInt1 >= 46) && (paramInt1 <= 55))
    {
      if (paramString.equals("Male"))
      {
        if (paramInt2 <= 57)
          this.a = "Athlete";
        else if ((paramInt2 >= 58) && (paramInt2 <= 63))
          this.a = "Excelent";
        else if ((paramInt2 >= 64) && (paramInt2 <= 67))
          this.a = "Good";
        else if ((paramInt2 >= 68) && (paramInt2 <= 71))
          this.a = "Above Average";
        else if ((paramInt2 >= 72) && (paramInt2 <= 76))
          this.a = "Average";
        else if ((paramInt2 >= 77) && (paramInt2 <= 83))
          this.a = "Below Average";
        else
          this.a = "Poor";
      }
      else if (paramInt2 <= 60)
        this.a = "Athlete";
      else if ((paramInt2 >= 61) && (paramInt2 <= 65))
        this.a = "Excelent";
      else if ((paramInt2 >= 66) && (paramInt2 <= 69))
        this.a = "Good";
      else if ((paramInt2 >= 70) && (paramInt2 <= 73))
        this.a = "Above Average";
      else if ((paramInt2 >= 74) && (paramInt2 <= 77))
        this.a = "Average";
      else if ((paramInt2 >= 78) && (paramInt2 <= 83))
        this.a = "Below Average";
      else
        this.a = "Poor";
    }
    else if ((paramInt1 >= 56) && (paramInt1 <= 65))
    {
      if (paramString.equals("Male"))
      {
        if (paramInt2 <= 56)
          this.a = "Athlete";
        else if ((paramInt2 >= 57) && (paramInt2 <= 61))
          this.a = "Excelent";
        else if ((paramInt2 >= 62) && (paramInt2 <= 67))
          this.a = "Good";
        else if ((paramInt2 >= 68) && (paramInt2 <= 71))
          this.a = "Above Average";
        else if ((paramInt2 >= 72) && (paramInt2 <= 75))
          this.a = "Average";
        else if ((paramInt2 >= 76) && (paramInt2 <= 81))
          this.a = "Below Average";
        else
          this.a = "Poor";
      }
      else if (paramInt2 <= 59)
        this.a = "Athlete";
      else if ((paramInt2 >= 60) && (paramInt2 <= 64))
        this.a = "Excelent";
      else if ((paramInt2 >= 65) && (paramInt2 <= 68))
        this.a = "Good";
      else if ((paramInt2 >= 69) && (paramInt2 <= 73))
        this.a = "Above Average";
      else if ((paramInt2 >= 74) && (paramInt2 <= 77))
        this.a = "Average";
      else if ((paramInt2 >= 78) && (paramInt2 <= 83))
        this.a = "Below Average";
      else
        this.a = "Poor";
    }
    else if (paramString.equals("Male"))
    {
      if (paramInt2 <= 55)
        this.a = "Athlete";
      else if ((paramInt2 >= 56) && (paramInt2 <= 61))
        this.a = "Excelent";
      else if ((paramInt2 >= 62) && (paramInt2 <= 65))
        this.a = "Good";
      else if ((paramInt2 >= 66) && (paramInt2 <= 69))
        this.a = "Above Average";
      else if ((paramInt2 >= 70) && (paramInt2 <= 73))
        this.a = "Average";
      else if ((paramInt2 >= 74) && (paramInt2 <= 79))
        this.a = "Below Average";
      else
        this.a = "Poor";
    }
    else if (paramInt2 <= 59)
      this.a = "Athlete";
    else if ((paramInt2 >= 60) && (paramInt2 <= 64))
      this.a = "Excelent";
    else if ((paramInt2 >= 65) && (paramInt2 <= 68))
      this.a = "Good";
    else if ((paramInt2 >= 69) && (paramInt2 <= 72))
      this.a = "Above Average";
    else if ((paramInt2 >= 73) && (paramInt2 <= 76))
      this.a = "Average";
    else if ((paramInt2 >= 77) && (paramInt2 <= 84))
      this.a = "Below Average";
    else
      this.a = "Poor";
    return this.a;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.HeartFitnessLevel
 * JD-Core Version:    0.6.0
 */