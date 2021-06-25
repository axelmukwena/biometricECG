package com.neurosky.thinkgear;

public class QRSdetect1
{
  public static int[] QRSdetectEKG(int[] paramArrayOfInt)
  {
    int[] arrayOfInt1 = new int[(int)(paramArrayOfInt.length / 360.0D * 3.0D)];
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int j = 0;
    int i4;
    int i5;
    int i6;
    int i7;
    int i8;
    int i9;
    int i10;
    int i11;
    int i = i11 = i10 = i9 = i8 = i7 = i6 = i5 = i4 = paramArrayOfInt[0];
    int i12 = 0;
    for (int i13 = 0; i13 < paramArrayOfInt.length; i13++)
    {
      int i3;
      i = (i3 = paramArrayOfInt[i13]) + 4 * i4 + i5 * 6 + 4 * i6 + i7 - i8 - 4 * i9 - i10 * 6 - 4 * i11 - i;
      if (i13 % 720 == 0)
        if (n == 0)
        {
          if (j -= (j >> 4) < 0)
            j = 0;
        }
        else if ((n >= 5) && (j += (j >> 4) > 0))
          j = 0;
      if ((n == 0) && (Math.abs(i) > j))
      {
        n = 1;
        i2 = 57;
        k = i > 0 ? 1 : -1;
        i1 = i13;
      }
      if (n != 0)
      {
        if (i * k < -j)
        {
          k = -k;
          n++;
          i2 = n > 4 ? 72 : 57;
        }
        else if ((i * k > j) && (Math.abs(i) > m))
        {
          m = Math.abs(i);
        }
        if (i2-- < 0)
        {
          if ((2 <= n) && (n <= 4))
          {
            if (j += ((m >> 2) - j >> 3) < 0)
              j = 0;
            else if (j > 0)
              j = 0;
            arrayOfInt1[i12] = i1;
            i12++;
          }
          else if (n >= 5)
          {
            arrayOfInt1[i12] = (-i1);
            i12++;
          }
          n = 0;
        }
      }
      i = i11;
      i11 = i10;
      i10 = i9;
      i9 = i8;
      i8 = i7;
      i7 = i6;
      i6 = i5;
      i5 = i4;
      i4 = i3;
    }
    int[] arrayOfInt2 = new int[i12];
    System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, i12);
    return arrayOfInt2;
  }
}

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearPackX.jar
 * Qualified Name:     com.neurosky.thinkgear.QRSdetect1
 * JD-Core Version:    0.6.0
 */