/*    */ package com.neurosky.thinkgear;
/*    */ 
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStreamWriter;
/*    */ 
/*    */ public class FileIO
/*    */ {
/*    */   public static void writeFile(String name, byte[] Bytes)
/*    */   {
/*    */     try
/*    */     {
/*    */       byte[] Bytes;
/* 18 */       fos = new FileOutputStream(name);
/* 19 */       BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(fos));
/* 20 */       fos.write(Bytes);
/* 21 */       buf.write(",");
/* 22 */       fos.close();
/* 23 */       buf.flush();
/*    */ 
/* 28 */       return;
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/* 25 */       (
/* 27 */         fos = 
/* 28 */         localException).getMessage();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static boolean exists(String paramString) {
/* 32 */     return new File("test.txt").exists();
/*    */   }
/*    */ 
/*    */   public static void createEmptyFile(String name)
/*    */   {
/*    */     try
/*    */     {
/* 39 */       (
/* 40 */         name = new FileOutputStream(name))
/* 40 */         .close();
/*    */ 
/* 45 */       return;
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/* 42 */       (
/* 44 */         name = 
/* 45 */         localException).getMessage();
/*    */     }
/*    */   }
/*    */ 
/*    */   public static byte[] readFile(String name)
/*    */   {
/* 50 */     int bufferlen = 0;
/* 51 */     byte[] buffer = new byte[1024];
/*    */     try
/*    */     {
/* 54 */       in = new FileInputStream(name);
/* 55 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 56 */       int i = 0;
/*    */       int len;
/* 57 */       while ((len = in.read(buffer)) != -1)
/*    */       {
/* 59 */         out.write(buffer, 0, len);
/* 60 */         bufferlen = len;
/*    */       }
/*    */ 
/* 63 */       out.close();
/* 64 */       in.close();
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/*    */       FileInputStream in;
/* 66 */       (
/* 68 */         in = 
/* 69 */         localIOException).printStackTrace();
/*    */     }
/* 70 */     byte[] data = new byte[bufferlen];
/* 71 */     for (int index = 0; index < data.length; index++)
/* 72 */       data[index] = buffer[index];
/* 73 */     return data;
/*    */   }
/*    */ }

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearBase.jar
 * Qualified Name:     com.neurosky.thinkgear.FileIO
 * JD-Core Version:    0.6.0
 */