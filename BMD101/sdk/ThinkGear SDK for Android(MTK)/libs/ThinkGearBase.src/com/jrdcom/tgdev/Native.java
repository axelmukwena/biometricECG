/*    */ package com.jrdcom.tgdev;
/*    */ 
/*    */ import java.io.FileDescriptor;
/*    */ 
/*    */ public class Native
/*    */ {
/*    */   public static native void JNI_close_node();
/*    */ 
/*    */   public static native int JNI_open_node();
/*    */ 
/*    */   public static native void SerialJNI_close();
/*    */ 
/*    */   public static native FileDescriptor SerialJNI_open(String paramString);
/*    */ 
/*    */   static
/*    */   {
/* 10 */     System.loadLibrary("CP_JNI_Cardiograph");
/*    */   }
/*    */ }

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearBase.jar
 * Qualified Name:     com.jrdcom.tgdev.Native
 * JD-Core Version:    0.6.0
 */