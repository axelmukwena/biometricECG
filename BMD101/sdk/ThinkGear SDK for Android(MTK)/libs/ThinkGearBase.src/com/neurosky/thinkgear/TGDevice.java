/*      */ package com.neurosky.thinkgear;
/*      */ 
/*      */ import android.bluetooth.BluetoothAdapter;
/*      */ import android.bluetooth.BluetoothDevice;
/*      */ import android.bluetooth.BluetoothSocket;
/*      */ import android.os.Build.VERSION;
/*      */ import android.os.Environment;
/*      */ import android.os.Handler;
/*      */ import android.os.Message;
/*      */ import android.text.format.DateFormat;
/*      */ import android.util.Log;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ 
/*      */ public class TGDevice
/*      */ {
/*      */   public static final int STATE_IDLE = 0;
/*      */   public static final int STATE_CONNECTING = 1;
/*      */   public static final int STATE_CONNECTED = 2;
/*      */   public static final int STATE_DISCONNECTED = 3;
/*      */   public static final int STATE_NOT_FOUND = 4;
/*      */   public static final int STATE_ERR_NO_DEVICE = 5;
/*      */   public static final int STATE_ERR_BT_OFF = 6;
/*      */   public static final int MSG_STATE_CHANGE = 256;
/*      */   public static final int MSG_POOR_SIGNAL = 2;
/*      */   public static final int MSG_HEART_RATE = 3;
/*      */   public static final int MSG_ATTENTION = 4;
/*      */   public static final int MSG_MEDITATION = 5;
/*      */   public static final int MSG_EGO_TRIM = 8;
/*      */   public static final int MSG_ZONE = 14;
/*      */   public static final int MSG_BLINK = 22;
/*      */   public static final int MSG_RELAXATION = 24;
/*      */   public static final int MSG_RESPIRATION = 25;
/*      */   public static final int MSG_POSITIVITY = 26;
/*      */   public static final int MSG_FAMILIARITY = 27;
/*      */   public static final int MSG_DIFFICULTY = 28;
/*      */   public static final int MSG_RAW_DATA = 128;
/*      */   public static final int MSG_RAW_LIFECARE = 275;
/*      */   public static final int MSG_RAW_GAIN = 276;
/*      */   public static final int MSG_EEG_POWER = 131;
/*      */   public static final int MSG_RAW_MULTI = 145;
/*      */   public static final int MSG_THINKCAPBETA_RAW = 176;
/*      */   public static final int MSG_THINKCAP_RAW = 177;
/*      */   public static final int MSG_RAW_MULTI_NEW = 178;
/*      */   public static final int MSG_SLEEP_STAGE = 180;
/*      */   public static final int MSG_MULTI_SIGNAL_STATUS = 179;
/*      */   public static final int MSG_RAW_COUNT = 19;
/*      */   public static final int MSG_LOW_BATTERY = 20;
/*      */   public static final int MSG_EKG_IDENTIFIED = 268;
/*      */   public static final int MSG_EKG_TRAINED = 269;
/*      */   public static final int MSG_EKG_TRAIN_STEP = 270;
/*      */   public static final int MSG_EKG_RRINT = 271;
/*      */   public static final int MSG_HEART_AGE = 272;
/*      */   public static final int MSG_HEART_AGE_5MIN = 273;
/*      */   public static final int MSG_MODEL_IDENTIFIED = 800;
/*      */   public static final int MSG_ERR_CFG_OVERRIDE = 900;
/*      */   public static final int MSG_ERR_NOT_PROVISIONED = 901;
/*      */   public static final int ERR_MSG_BLINK_DETECT = 31;
/*      */   public static final int ERR_MSG_TASKFAMILIARITY = 32;
/*      */   public static final int ERR_MSG_TASKDIFFICULTY = 33;
/*      */   public static final int ERR_MSG_POSITIVITY = 34;
/*      */   public static final int ERR_MSG_RESPIRATIONRATE = 35;
/*   77 */   public int ekgState = 0;
/*      */   public static final boolean L = true;
/*   82 */   public static boolean ekgPersonalizationEnabled = true;
/*      */ 
/*   86 */   private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
/*      */   TGDevice myTGDevice;
/*      */   Handler handler;
/*   96 */   private boolean byteLogEnabled = false;
/*   97 */   private boolean logEnabled = false;
/*   98 */   private boolean rawEnabled = false;
/*      */   private boolean isReadable;
/*  101 */   private boolean isStart = false;
/*      */   private int state;
/*      */   private BluetoothAdapter btAdapter;
/*      */   private OutputStream output;
/*      */   private BluetoothDevice connectedDevice;
/*      */   private Set<BluetoothDevice> pairedDevices;
/*      */   private ConnectThread btConnectThread;
/*      */   private ConnectedThread btConnectedThread;
/*      */   private OutputStream logWriter;
/*      */   private OutputStream byteLogWriter;
/*  118 */   private boolean trimByteSent = false;
/*  119 */   private boolean isTrimByteNeeded = true;
/*  120 */   private boolean isBMD101 = false;
/*  121 */   private boolean isBMD100 = false;
/*  122 */   private boolean isIdentified = false;
/*      */ 
/*  124 */   private int isIdentified_pkt_cnt = 0;
/*  125 */   private boolean isFunctionEEG = false;
/*  126 */   private boolean isFunctionEKG = false;
/*  127 */   private boolean isManyChannel = false;
/*      */ 
/*  131 */   private boolean blinkEnabled = false;
/*      */ 
/*  140 */   private boolean respirationEnabled = false;
/*  141 */   private boolean familiarityEnabled = false;
/*  142 */   private boolean familiarityRunContinuous = false;
/*  143 */   private boolean difficultyEnabled = false;
/*  144 */   private boolean difficultyRunContinuous = false;
/*  145 */   private boolean positivityEnabled = false;
/*      */   public EkgSenseManager ekgSenseMan;
/*      */   private List<Integer> rrintBuffer;
/*      */   private List<Integer> rrintBufferForHeartAge;
/*      */   private List<Integer> inputRawBuffer;
/*      */   private StreamThread streamThread;
/*      */   private InputStream inputstream;
/*      */   public int inputAge;
/*      */   public int pass_seconds;
/*  166 */   private static int pass_raw_counter = 0;
/*      */   public static final int version = 29;
/*      */   public static final int minorversion = 0;
/*      */   public static final String build_title = "2013-09-12 SDK Release";
/*      */ 
/*      */   public TGDevice(BluetoothAdapter btAdapter, Handler handler)
/*      */   {
/*  184 */     this.btAdapter = btAdapter;
/*  185 */     this.handler = handler;
/*  186 */     this.myTGDevice = this;
/*  187 */     this.state = 0;
/*      */ 
/*  189 */     if (ekgPersonalizationEnabled) {
/*  190 */       this.ekgSenseMan = new EkgSenseManager(this);
/*      */     }
/*  192 */     this.rrintBuffer = new ArrayList();
/*  193 */     this.rrintBufferForHeartAge = new ArrayList();
/*  194 */     this.inputRawBuffer = new ArrayList();
/*      */ 
/*  197 */     logTheVersion();
/*      */ 
/*  205 */     if (this.btAdapter.getState() == 10) {
/*  206 */       setState(6);
/*  207 */       this.state = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public TGDevice(InputStream s, OutputStream o, Handler handler)
/*      */   {
/*  242 */     this.state = 0;
/*      */ 
/*  248 */     if (s == null) {
/*  249 */       Log.v("TGDevice", "Invalid input stream");
/*      */     }
/*  251 */     this.inputstream = s;
/*      */ 
/*  253 */     if (o != null) {
/*  254 */       this.output = o;
/*      */     }
/*      */ 
/*  257 */     this.handler = handler;
/*  258 */     this.myTGDevice = this;
/*      */ 
/*  261 */     if (ekgPersonalizationEnabled) {
/*  262 */       this.ekgSenseMan = new EkgSenseManager(this);
/*      */     }
/*  264 */     this.rrintBuffer = new ArrayList();
/*  265 */     this.rrintBufferForHeartAge = new ArrayList();
/*  266 */     this.inputRawBuffer = new ArrayList();
/*      */ 
/*  268 */     logTheVersion();
/*      */   }
/*      */ 
/*      */   public static int getVersion()
/*      */   {
/*  289 */     return 29;
/*      */   }
/*      */   public static int getMinorVersion() {
/*  292 */     return 0;
/*      */   }
/*      */ 
/*      */   public static String getTitle() {
/*  296 */     return "2013-09-12 SDK Release";
/*      */   }
/*      */ 
/*      */   private static void logTheVersion() {
/*  300 */     Log.d("TGDevice", "Initialized. Version: " + getVersion());
/*  301 */     if (getVersion() != TGconfig.getVersion())
/*  302 */       Log.w("TGDevice", "OOPS!! TG-SDK internal Version mismatch: TGDevice: " + getVersion() + " TGconfig: " + TGconfig.getVersion() + "\n");
/*      */   }
/*      */ 
/*      */   public synchronized void connect(BluetoothDevice btDevice)
/*      */   {
/*  312 */     connect(btDevice, false);
/*      */   }
/*      */ 
/*      */   public synchronized void connect(BluetoothDevice btDevice, boolean rawEnabled)
/*      */   {
/*  321 */     if (this.btAdapter.getState() == 10) {
/*  322 */       setState(6);
/*  323 */       return;
/*      */     }
/*      */ 
/*  326 */     setStart(false);
/*  327 */     this.rawEnabled = rawEnabled;
/*      */     Set d;
/*  328 */     (
/*  329 */       d = new HashSet())
/*  329 */       .add(btDevice);
/*      */ 
/*  331 */     if (this.btConnectThread != null) {
/*  332 */       this.btConnectThread.cancel();
/*  333 */       this.btConnectThread = null;
/*      */     }
/*      */ 
/*  336 */     this.btConnectThread = new ConnectThread(d);
/*  337 */     this.btConnectThread.start();
/*  338 */     setState(1);
/*  339 */     Log.i("TGDevice", "BT Connecting to: " + btDevice.getName() + ", " + btDevice.getAddress() + " with raw enabled: " + rawEnabled);
/*      */   }
/*      */ 
/*      */   public synchronized void connect(boolean rawEnabled)
/*      */   {
/*  348 */     if (this.btAdapter.getState() == 10) {
/*  349 */       setState(6);
/*  350 */       return;
/*      */     }
/*      */ 
/*  353 */     if (this.btAdapter != null) {
/*  354 */       setStart(false);
/*  355 */       this.rawEnabled = rawEnabled;
/*  356 */       this.pairedDevices = this.btAdapter.getBondedDevices();
/*      */ 
/*  358 */       if (this.btConnectThread != null)
/*      */       {
/*  361 */         this.btConnectThread.cancel();
/*  362 */         this.btConnectThread = null;
/*      */       }
/*  364 */       if (this.btConnectedThread != null)
/*      */       {
/*  367 */         this.btConnectedThread.cancel();
/*  368 */         this.btConnectedThread = null;
/*      */       }
/*      */ 
/*  371 */       if (this.pairedDevices.isEmpty())
/*      */       {
/*  373 */         setState(5); return;
/*      */       }
/*  375 */       this.btConnectThread = new ConnectThread(this.pairedDevices);
/*  376 */       this.btConnectThread.start();
/*  377 */       setState(1);
/*      */     }
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public synchronized void connectUart(boolean rawEnabled)
/*      */   {
/*  388 */     connectStream(rawEnabled);
/*      */   }
/*      */ 
/*      */   public synchronized void connectStream(boolean paramBoolean)
/*      */   {
/*  398 */     if (this.streamThread != null) {
/*  399 */       this.streamThread.cancel();
/*  400 */       this.streamThread = null;
/*      */     }
/*      */ 
/*  403 */     this.streamThread = new StreamThread(this.inputstream);
/*  404 */     this.streamThread.start();
/*  405 */     setState(2);
/*  406 */     setReadable(true);
/*      */   }
/*      */ 
/*      */   private synchronized void connected(BluetoothSocket btSocket) {
/*  410 */     if (this.btConnectThread != null) {
/*  411 */       this.btConnectThread = null;
/*      */     }
/*  413 */     if (this.btConnectedThread != null) {
/*  414 */       this.btConnectedThread.cancel();
/*  415 */       this.btConnectedThread = null;
/*      */     }
/*      */ 
/*  418 */     setReadable(true);
/*      */ 
/*  420 */     this.btConnectedThread = new ConnectedThread(btSocket, this.rawEnabled);
/*  421 */     this.btConnectedThread.start();
/*  422 */     setState(2);
/*      */   }
/*      */ 
/*      */   public synchronized void close()
/*      */   {
/*  433 */     if (this.btConnectThread != null) {
/*  434 */       this.btConnectThread.cancel();
/*  435 */       this.btConnectThread = null;
/*      */     }
/*  437 */     if (this.btConnectedThread != null) {
/*  438 */       this.btConnectedThread.cancel();
/*  439 */       this.btConnectedThread = null;
/*      */     }
/*  441 */     if (this.streamThread != null) {
/*  442 */       this.streamThread.cancel();
/*  443 */       this.streamThread = null;
/*      */     }
/*      */ 
/*  451 */     stopLog();
/*  452 */     setState(3);
/*  453 */     Log.i("TGDevice", "Closing connections");
/*      */   }
/*      */ 
/*      */   public synchronized void start()
/*      */   {
/*  460 */     Log.d("TGDevice", "Starting stream");
/*  461 */     setStart(true);
/*      */   }
/*      */ 
/*      */   public synchronized void stop()
/*      */   {
/*  468 */     Log.d("TGDevice", "Stopping stream");
/*  469 */     setStart(false);
/*  470 */     setState(0);
/*      */   }
/*      */ 
/*      */   public BluetoothDevice getConnectedDevice()
/*      */   {
/*  478 */     return this.connectedDevice;
/*      */   }
/*      */ 
/*      */   private synchronized void setState(int s)
/*      */   {
/*  501 */     this.state = s;
/*  502 */     this.handler.obtainMessage(256, this.state, 0).sendToTarget();
/*      */   }
/*      */ 
/*      */   public synchronized int getState()
/*      */   {
/*  510 */     return this.state;
/*      */   }
/*      */ 
/*      */   private synchronized void setReadable(boolean readable) {
/*  514 */     this.isReadable = readable;
/*      */   }
/*      */ 
/*      */   private synchronized boolean getReadable() {
/*  518 */     return this.isReadable;
/*      */   }
/*      */ 
/*      */   private synchronized void setStart(boolean start) {
/*  522 */     this.isStart = start;
/*      */   }
/*      */ 
/*      */   private synchronized boolean getStart() {
/*  526 */     return this.isStart;
/*      */   }
/*      */ 
/*      */   public boolean startLog(String path, String fileName)
/*      */   {
/*  537 */     if (this.logEnabled) {
/*  538 */       return false;
/*      */     }
/*  540 */     String state = Environment.getExternalStorageState();
/*      */ 
/*  542 */     if ("mounted".equals(state))
/*      */     {
/*  546 */       if (path == "")
/*  547 */         path = Environment.getExternalStorageDirectory() + "/Android/data/com.neurosky.thinkgear/files";
/*  548 */       if (fileName == "") {
/*  549 */         fileName = DateFormat.format("yyyy-MM-dd-hhmmss", new Date()).toString() + ".txt";
/*      */       }
/*  551 */       File dir = new File(path);
/*  552 */       File file = new File(path, fileName);
/*      */       try
/*      */       {
/*  556 */         if ((!dir.exists()) && 
/*  557 */           (dir.mkdirs())) {
/*  558 */           Log.d("TGDevice", "Log directory created successfully");
/*      */         }
/*      */ 
/*  562 */         if (!file.exists()) {
/*  563 */           file.createNewFile();
/*      */         }
/*      */ 
/*  566 */         if (file.exists()) {
/*  567 */           this.logWriter = new FileOutputStream(file);
/*  568 */           this.logEnabled = true;
/*      */ 
/*  570 */           Log.i("TGDevice", "Logging enabled: " + path + fileName);
/*      */         }
/*      */       } catch (IOException e) {
/*  573 */         Log.v("TGDevice", "WTF: " + e + " path: " + path);
/*  574 */         return false;
/*      */       }
/*  576 */       return true;
/*      */     }
/*  578 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean startByteLog(String path, String fileName)
/*      */   {
/*  584 */     if (this.byteLogEnabled) {
/*  585 */       return false;
/*      */     }
/*  587 */     String state = Environment.getExternalStorageState();
/*      */ 
/*  589 */     if ("mounted".equals(state))
/*      */     {
/*  593 */       if (path == "")
/*  594 */         path = Environment.getExternalStorageDirectory() + "/Android/data/com.neurosky.thinkgear/files";
/*  595 */       if (fileName == "") {
/*  596 */         fileName = DateFormat.format("yyyy-MM-dd-hhmmss", new Date()).toString() + ".txt";
/*      */       }
/*  598 */       File dir = new File(path);
/*  599 */       File file = new File(path, fileName);
/*      */       try
/*      */       {
/*  603 */         if ((!dir.exists()) && 
/*  604 */           (dir.mkdirs())) {
/*  605 */           Log.d("TGDevice", "Log directory created successfully");
/*      */         }
/*      */ 
/*  609 */         if (!file.exists()) {
/*  610 */           file.createNewFile();
/*      */         }
/*      */ 
/*  613 */         if (file.exists()) {
/*  614 */           this.byteLogWriter = new FileOutputStream(file);
/*  615 */           this.byteLogEnabled = true;
/*      */ 
/*  617 */           Log.i("TGDevice", " Byte Logging enabled: " + path + fileName);
/*      */         }
/*      */       } catch (IOException e) {
/*  620 */         Log.v("TGDevice", "WTF: " + e + " path: " + path);
/*  621 */         return false;
/*      */       }
/*  623 */       return true;
/*      */     }
/*  625 */     return false;
/*      */   }
/*      */ 
/*      */   public void stopLog()
/*      */   {
/*      */     IOException localIOException1;
/*  633 */     if (this.logEnabled) {
/*  634 */       this.logEnabled = false;
/*      */       try {
/*  636 */         this.logWriter.close();
/*  637 */         Log.i("TGDevice", "Logging disabled"); } catch (IOException localIOException2) {
/*  638 */         (
/*  640 */           localIOException1 = 
/*  641 */           localIOException2).printStackTrace();
/*      */       }
/*      */     }
/*  643 */     if (this.byteLogEnabled) {
/*  644 */       this.byteLogEnabled = false;
/*      */       try {
/*  646 */         this.byteLogWriter.close();
/*  647 */         Log.i("TGDevice", "Logging disabled");
/*      */ 
/*  651 */         return;
/*      */       }
/*      */       catch (IOException localIOException3)
/*      */       {
/*  648 */         (
/*  650 */           localIOException1 = 
/*  651 */           localIOException3).printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean setBlinkDetectionEnabled(boolean enable_request)
/*      */   {
/* 1062 */     if (enable_request) {
/* 1063 */       if (this.isIdentified) {
/* 1064 */         if ((this.isFunctionEKG) || (this.isManyChannel)) {
/* 1065 */           this.handler.obtainMessage(900, 31, 0).sendToTarget();
/* 1066 */           Log.v("TGDevice", "OVERRIDE: BlinkDetect");
/*      */         }
/*      */         else
/*      */         {
/* 1070 */           this.blinkEnabled = true; break label73;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1078 */         this.blinkEnabled = true; break label73;
/*      */       }
/*      */     }
/*      */ 
/* 1082 */     this.blinkEnabled = false;
/*      */ 
/* 1084 */     label73: return this.blinkEnabled;
/*      */   }
/*      */ 
/*      */   public boolean getBlinkDetectionEnabled()
/*      */   {
/* 1091 */     return this.blinkEnabled;
/*      */   }
/*      */ 
/*      */   public boolean setTaskFamiliarityEnable(boolean enable_request)
/*      */   {
/* 1104 */     if (enable_request) {
/* 1105 */       if ((!TaskFamiliarity.isProvisioned()) || (!TF_TD_control.isProvisioned()))
/*      */       {
/* 1109 */         this.handler.obtainMessage(901, 32, 0).sendToTarget();
/*      */       }
/* 1112 */       else if (this.isIdentified) {
/* 1113 */         if ((this.isFunctionEKG) || (this.isManyChannel))
/*      */         {
/* 1119 */           this.handler.obtainMessage(900, 32, 0).sendToTarget();
/* 1120 */           Log.v("TGDevice", "OVERRIDE: Familiarity");
/*      */         }
/*      */         else
/*      */         {
/* 1124 */           this.familiarityEnabled = true; break label104;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1132 */         this.familiarityEnabled = true; break label104;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1148 */     this.familiarityEnabled = false;
/*      */ 
/* 1150 */     label104: return this.familiarityEnabled;
/*      */   }
/*      */ 
/*      */   public boolean getTaskFamiliarityEnable()
/*      */   {
/* 1157 */     return this.familiarityEnabled;
/*      */   }
/*      */ 
/*      */   public boolean setTaskFamiliarityRunContinuous(boolean enable_request)
/*      */   {
/* 1172 */     this.familiarityRunContinuous = enable_request;
/* 1173 */     return this.familiarityRunContinuous;
/*      */   }
/*      */ 
/*      */   public boolean getTaskFamiliarityRunContinuous()
/*      */   {
/* 1180 */     return this.familiarityRunContinuous;
/*      */   }
/*      */ 
/*      */   public boolean setTaskDifficultyEnable(boolean enable_request)
/*      */   {
/* 1193 */     if (enable_request) {
/* 1194 */       if ((!TaskDifficulty.isProvisioned()) || (!TF_TD_control.isProvisioned()))
/*      */       {
/* 1198 */         this.handler.obtainMessage(901, 33, 0).sendToTarget();
/*      */       }
/* 1201 */       else if (this.isIdentified) {
/* 1202 */         if ((this.isFunctionEKG) || (this.isManyChannel))
/*      */         {
/* 1208 */           this.handler.obtainMessage(900, 33, 0).sendToTarget();
/* 1209 */           Log.v("TGDevice", "OVERRIDE: Difficulty");
/*      */         }
/*      */         else
/*      */         {
/* 1213 */           this.difficultyEnabled = true; break label104;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1221 */         this.difficultyEnabled = true; break label104;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1237 */     this.difficultyEnabled = false;
/*      */ 
/* 1239 */     label104: return this.difficultyEnabled;
/*      */   }
/*      */ 
/*      */   public boolean getTaskDifficultyEnable()
/*      */   {
/* 1246 */     return this.difficultyEnabled;
/*      */   }
/*      */ 
/*      */   public boolean setTaskDifficultyRunContinuous(boolean enable_request)
/*      */   {
/* 1261 */     this.difficultyRunContinuous = enable_request;
/* 1262 */     return this.difficultyRunContinuous;
/*      */   }
/*      */ 
/*      */   public boolean getTaskDifficultyRunContinuous()
/*      */   {
/* 1269 */     return this.difficultyRunContinuous;
/*      */   }
/*      */ 
/*      */   public boolean setPositivityEnable(boolean enable_request)
/*      */   {
/* 1287 */     if (enable_request) {
/* 1288 */       Log.v("TGDevice", "OVERRIDE: Positivity");
/*      */     }
/* 1290 */     this.positivityEnabled = false;
/* 1291 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean getPositivityEnable()
/*      */   {
/* 1298 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean setRespirationRateEnable(boolean enable_request)
/*      */   {
/* 1311 */     if (enable_request) {
/* 1312 */       if (!RespiratoryRate.isProvisioned())
/*      */       {
/* 1316 */         this.handler.obtainMessage(901, 35, 0).sendToTarget();
/*      */       }
/* 1319 */       else if (this.isIdentified) {
/* 1320 */         if ((this.isFunctionEEG) || (this.isManyChannel)) {
/* 1321 */           this.handler.obtainMessage(900, 35, 0).sendToTarget();
/* 1322 */           Log.v("TGDevice", "OVERRIDE: Respire Rate");
/*      */         }
/*      */         else
/*      */         {
/* 1326 */           this.respirationEnabled = true; break label98;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1334 */         this.respirationEnabled = true; break label98;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1341 */     this.respirationEnabled = false;
/*      */ 
/* 1343 */     label98: return this.respirationEnabled;
/*      */   }
/*      */ 
/*      */   public boolean getRespirationRateEnable()
/*      */   {
/* 1350 */     return this.respirationEnabled;
/*      */   }
/*      */ 
/*      */   public double getHeartAge()
/*      */   {
/* 2709 */     if (this.rrintBuffer.size() == 0) {
/* 2710 */       return -1.0D;
/*      */     }
/*      */ 
/* 2713 */     Integer[] t = new Integer[this.rrintBuffer.size()];
/*      */     double d;
/* 2715 */     return d = NeuroSkyHeartMeters.calculateHeartAgeRaw((Integer[])this.rrintBuffer.toArray(t));
/*      */   }
/*      */ 
/*      */   public int getHeartRisk()
/*      */   {
/* 2721 */     if (this.rrintBuffer.size() == 0) {
/* 2722 */       return -1;
/*      */     }
/*      */ 
/* 2725 */     Integer[] t = new Integer[this.rrintBuffer.size()];
/*      */ 
/* 2727 */     return t = NeuroSkyHeartMeters.calculateHeartRiskAware((Integer[])this.rrintBuffer.toArray(t));
/*      */   }
/*      */ 
/*      */   public int EKGloadParams(String name)
/*      */   {
/* 2740 */     if (!ekgPersonalizationEnabled) {
/* 2741 */       return -1;
/*      */     }
/* 2743 */     if (name.length() == 0) {
/* 2744 */       Log.v("TGDevice", "EKG Mode: Indentificiation");
/* 2745 */       this.ekgSenseMan.ekgSenseObj.params = new EkgParameters();
/* 2746 */       return 0;
/*      */     }
/* 2748 */     Log.v("TGDevice", "EKG Mode: Authentication");
/* 2749 */     this.ekgSenseMan.loadParameterFile(name);
/* 2750 */     return 1;
/*      */   }
/*      */ 
/*      */   public void EKGstartDetection()
/*      */   {
/* 2756 */     if (!ekgPersonalizationEnabled) {
/* 2757 */       return;
/*      */     }
/* 2759 */     this.ekgSenseMan.resetBuffer();
/* 2760 */     this.ekgSenseMan.ekgSenseObj.currentData = new EkgTemplate();
/* 2761 */     this.ekgState = 3;
/*      */   }
/*      */ 
/*      */   public void EKGstopDetection() {
/* 2765 */     if (!ekgPersonalizationEnabled) {
/* 2766 */       return;
/*      */     }
/* 2768 */     this.ekgState = 0;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public void EKGstartTraining(String userName)
/*      */   {
/* 2776 */     if (!ekgPersonalizationEnabled) {
/* 2777 */       return;
/*      */     }
/* 2779 */     this.ekgState = 1;
/* 2780 */     this.ekgSenseMan.learnName = userName;
/*      */   }
/*      */ 
/*      */   public void EKGstartLongTraining(String userName) {
/* 2784 */     if (!ekgPersonalizationEnabled) {
/* 2785 */       return;
/*      */     }
/*      */ 
/* 2792 */     this.ekgState = 2;
/* 2793 */     this.ekgSenseMan.ekgSenseObj.reset();
/* 2794 */     this.ekgSenseMan.learnName = userName;
/* 2795 */     this.ekgSenseMan.learnCounter = 0;
/*      */   }
/*      */ 
/*      */   public void EKGstopTraining() {
/* 2799 */     if (!ekgPersonalizationEnabled) {
/* 2800 */       return;
/*      */     }
/* 2802 */     this.ekgState = 0;
/* 2803 */     this.ekgSenseMan.learnName = "";
/*      */   }
/*      */ 
/*      */   public boolean EKGdeleteUser(String name) {
/* 2807 */     if (!ekgPersonalizationEnabled) {
/* 2808 */       return false;
/*      */     }
/* 2810 */     return this.ekgSenseMan.deleteUser(name);
/*      */   }
/*      */ 
/*      */   private class TGParser
/*      */   {
/*      */     private TGDevice tgDevice;
/*      */     private Handler handler;
/* 1406 */     private boolean hwHeartRateEnabled = false;
/* 1407 */     private boolean rawCountEnabled = false;
/* 1408 */     private boolean sineWaveEnabled = false;
/*      */ 
/* 1413 */     private boolean sleepStageEnabled = false;
/* 1414 */     private boolean relaxationEnabled = true;
/*      */     private boolean rawEnabled;
/* 1418 */     private boolean positivityEnabled = false;
/* 1419 */     private boolean timeBasedIntervalEnabled = true;
/* 1420 */     private boolean calculationEnabled = false;
/*      */     private int parserState;
/*      */     private int payloadLength;
/*      */     private int payloadBytesReceived;
/*      */     private int payloadSum;
/*      */     private int checksum;
/* 1428 */     private int rawCount = 0;
/* 1429 */     private byte[] payload = new byte[256];
/* 1430 */     private int[] highlow = new int[2];
/*      */ 
/* 1432 */     private int[] sine = new int[512];
/* 1433 */     private final double[] coeff = { -9.744044235842301E-005D, -0.0001158802706427D, -0.000168411968763922D, -0.000220230946712853D, -0.000261497617618105D, -0.000280928519745322D, -0.000266503975879537D, -0.000207456105101242D, -9.60607754678275E-005D, 7.06479844998841E-005D, 0.000289279202398974D, 0.000549359171310485D, 0.000833627498869421D, 0.00111885157865328D, 0.001378119500004D, 0.00158354624155544D, 0.0017098129169296D, 0.001737409328845D, 0.00165575166183813D, 0.00146506438032912D, 0.00117746477759056D, 0.000816034117695199D, 0.000412844215447331D, 5.24895417994034E-006D, -0.000368302018467704D, -0.000673193852496632D, -0.000882833781720179D, -0.000982353338460868D, -0.000970341482030938D, -0.00085926049580429D, -0.000673493754692806D, -0.000446146123317908D, -0.00021418266270264D, -1.34624881903889E-005D, 0.000126516426775505D, 0.00018646630096872D, 0.000160107470556599D, 5.45352349753129E-005D, -0.000110987588308181D, -0.00030850987471925D, -0.000505715502523139D, -0.000671317451493218D, -0.000779750742743636D, -0.000815365828849824D, -0.000774523301469025D, -0.000666185490192942D, -0.000510005871163562D, -0.0003331379540844D, -0.00016540091389957D, -3.45312065837977E-005D, 3.86308033064396E-005D, 4.32881937489408E-005D, -1.94228353791984E-005D, -0.000137026795703571D, -0.00028776702659654D, -0.000444674887956967D, -0.000580032844849327D, -0.00067039277429577D, -0.000700350679062249D, -0.000665360053537445D, -0.0005721865322903D, -0.000437843807718034D, -0.000286293068422389D, -0.000144403231408916D, -3.69617246459944E-005D, 1.74584888808446E-005D, 9.98070420727893E-006D, -5.7277674807016E-005D, -0.000171410449954226D, -0.00031130364123457D, -0.000451218621755097D, -0.000565653686021656D, -0.000633691820405254D, -0.000642981223594934D, -0.00059171601394605D, -0.000489172991016836D, -0.000353799232879D, -0.000210096448291764D, -8.39458869442319E-005D, 1.8664996724834E-006D, 3.20306720584621E-005D, 1.28683042804457E-006D, -8.43683732439765E-005D, -0.00020897351185555D, -0.00034926493686228D, -0.000479138906517235D, -0.000574195668379932D, -0.00061644929580642D, -0.00059743143780257D, -0.000520018323470526D, -0.000397735963103583D, -0.00025255457870159D, -0.00011066638257936D, 2.08912857755044E-006D, 6.52221969033149E-005D, 6.71888260706398E-005D, 7.8132740985481E-006D, -0.00010181688682969D, -0.00024108962877764D, -0.00038382933877727D, -0.000502871887665066D, -0.000575313621310307D, -0.0005865691429630631D, -0.00053337358360414D, -0.000424271089806838D, -0.000278364015955548D, -0.00012164768632281D, 1.75247095049829E-005D, 0.00011390885230493D, 0.000149870978174831D, 0.00011897116401627D, 2.70624688829392E-005D, -0.000108442383216512D, -0.000261904280504075D, -0.000403915938898268D, -0.000506955565217396D, -0.000550312187666917D, -0.000524244802280253D, -0.000431708214571715D, -0.000288174788977175D, -0.00011868367820605D, 4.64904244119377E-005D, 0.000177638742212356D, 0.000250946901093621D, 0.0002532312989996D, 0.000184316204896415D, 5.74523943528654E-005D, -0.000103198447100559D, -0.000266713802736679D, -0.000401313076289361D, -0.000480063170756953D, -0.000486191446429637D, -0.000416139661135309D, -0.00028062372139007D, -0.00010261048558795D, 8.65917343263471E-005D, 0.00025328544795083D, 0.000367434261574489D, 0.000408543598287131D, 0.000369410870378961D, 0.000257871342545317D, 9.533246490044179E-005D, -8.68245287093865E-005D, -0.000253184359458119D, -0.000370652605605774D, -0.000414892907150544D, -0.000374794893114052D, -0.000254954924592149D, -7.48344639902692E-005D, 0.000134455506846221D, 0.000336015790834792D, 0.000493770262973607D, 0.000579439273810329D, 0.000577738539851612D, 0.00048963925032985D, 0.00033221821078459D, 0.000135904911184024D, -6.12246546286886E-005D, -0.00022025664244296D, -0.000308920034466907D, -0.00030752285342762D, -0.000213033893593434D, -3.96953994968743E-005D, 0.0001831805086828D, 0.000416678727825662D, 0.00061922033166568D, 0.000754513329299535D, 0.0007982248319367D, 0.000742921251348084D, 0.0005995122812886211D, 0.000395685490536059D, 0.00017077055677482D, -3.12520579316764E-005D, -0.00017012293532712D, -0.000216702229081013D, -0.000158799772313772D, -3.51663112894297E-006D, 0.000223593185867299D, 0.000483230524813043D, 0.00072930778548967D, 0.000917680849208474D, 0.00101425160569053D, 0.00100170797231571D, 0.000882824811935392D, 0.000680444821457835D, 0.000433304948104277D, 0.00018921170876054D, -3.97963442099545E-006D, -0.00010714149456976D, -9.75382510211861E-005D, 2.68551602843451E-005D, 0.0002464637750764D, 0.00052364554256276D, 0.000809281106655558D, 0.00105200093732304D, 0.00120759179058643D, 0.00124758803351198D, 0.0011646592200388D, 0.000974496584720574D, 0.000713031741195615D, 0.000430193817156114D, 0.000180569079914348D, 1.34194308769052E-005D, -3.68288426928038E-005D, 4.29148478210438E-005D, 0.000241435399013574D, 0.000525127275789142D, 0.000843769833829344D, 0.0011398772011145D, 0.00135918714255885D, 0.00146107399744823D, 0.00142619655393134D, 0.0012606355500263D, 0.000994960964833502D, 0.00067905255766096D, 0.000372855678084063D, 0.00013546675715985D, 1.3849309144843E-005D, 3.4012343541018E-005D, 0.000195625958665316D, 0.000471854489811893D, 0.000813805297511983D, 0.00115954087382408D, 0.0014453235756532D, 0.00161772585583283D, 0.00164363911682995D, 0.0015169763177431D, 0.0012600546655286D, 0.0009200184348714271D, 0.00056019737374658D, 0.0002486418319228D, 4.52472361597781E-005D, -9.44217447750788E-006D, 9.76836786773469E-005D, 0.000349587730781208D, 0.000701646118277693D, 0.00108976947205013D, 0.0014419630109892D, 0.00169189034302864D, 0.0017912450103479D, 0.00171926753558527D, 0.0014868999734513D, 0.00113538873580102D, 0.00072884065824089D, 0.000342704502465947D, 4.9611695770945E-005D, -9.41422652072306E-005D, -5.95892868598273E-005D, 0.000149210026530823D, 0.000495251264637447D, 0.000915185152311649D, 0.0013306426175546D, 0.00166288067239408D, 0.0018474051307332D, 0.0018464663763184D, 0.00165640715611924D, 0.00130904009040423D, 0.00086606793263505D, 0.000408117020837066D, 1.9730705769468E-005D, -0.000226295960430978D, -0.00028293202316019D, -0.000137996600502361D, 0.000182850004397D, 0.000620484195280941D, 0.00109274642643676D, 0.0015096647586277D, 0.00179008298063751D, 0.00187719780614157D, 0.0017494651968717D, 0.0014253553239755D, 0.000960366107007573D, 0.000437330250865943D, -4.88633815893146E-005D, -0.000408761057604636D, -0.000575386700363658D, -0.000517038609916063D, -0.00024395462268853D, 0.00019308850622077D, 0.00071152581767356D, 0.0012121573011249D, 0.00159738209371754D, 0.00178971139213456D, 0.0017465127588879D, 0.0014687260488199D, 0.00100124647033075D, 0.000425335294937135D, -0.0001562085758773D, -0.00063788478252317D, -0.000931420112254212D, -0.0009825044704947681D, -0.00078169289925272D, -0.000366775820220804D, 0.000183416169919086D, 0.00076329696472705D, 0.00125980163155078D, 0.0015736565179956D, 0.00163813734628643D, 0.00143228621248031D, 0.00098547625008281D, 0.00037285572048885D, -0.00029815298661902D, -0.000907384243766655D, -0.0013444420969517D, -0.00152949276953303D, -0.00142891708711331D, -0.0010622794183398D, -0.000499669263582827D, 0.000150755304146288D, 0.00076234087929536D, 0.00121357089114537D, 0.00141088442354037D, 0.0013068736477961D, 0.00090986693888556D, 0.00028343883620657D, -0.0004647202435967D, -0.00120218142834308D, -0.0017963806183181D, -0.00213943521160232D, -0.0021689857825021D, -0.001880603296009D, -0.0013297508884025D, -0.00062228291826615D, 0.0001044703719224D, 0.00070710096654096D, 0.00106288579277491D, 0.0010933599394102D, 0.000779759007376738D, 0.00016773156740448D, -0.000640203673959743D, -0.00150281291712562D, -0.0022661330701053D, -0.00279210409544492D, -0.00298494810943401D, -0.00280985876267867D, -0.00230077814015934D, -0.00155516788603045D, -0.000716946449325894D, 4.98425162577E-005D, 0.000591427811460621D, 0.000793553359180197D, 0.000603626371741223D, 4.18882442035887E-005D, -0.000801113927599773D, -0.0017801280527642D, -0.00272159333227661D, -0.0034556681717152D, -0.00384837663407466D, -0.0038274287920596D, -0.00339713383295239D, -0.0026389926345583D, -0.0016979707959775D, -0.000756362553657179D, -3.02801614322377E-007D, 0.00041530312463846D, 0.000396486626536007D, -7.075822572287791E-005D, -0.000916299236533598D, -0.00199725651943966D, -0.00312350225351387D, -0.0040923984060442D, -0.00472678969339786D, -0.00490881515896487D, -0.0046034450441617D, -0.00386674837933375D, -0.00283736136973274D, -0.0017120595076341D, -0.000710028821381479D, -3.19766104447796E-005D, 0.00017800520584401D, -0.000139079612299567D, -0.00094420432887318D, -0.00210539663183083D, -0.00342058739997302D, -0.00465401417839112D, -0.00557995156612952D, -0.00602539931306045D, -0.00590398183887164D, -0.0052341754236042D, -0.00413837160798384D, -0.00282220492576368D, -0.00153786268861382D, -0.000537603813784902D, -2.62833612978657E-005D, -0.000121501341208779D, -0.000829341117202341D, -0.00204036227034438D, -0.00354738366789472D, -0.00508198134809249D, -0.00636358411753227D, -0.0071519845067431D, -0.00729374687426444D, -0.00675336186413425D, -0.00562315333865641D, -0.00410925127068627D, -0.0024958874386246D, -0.00109388518637658D, -0.000182849419019012D, 4.25002186405121E-005D, -0.000488598112186092D, -0.00170597869344896D, -0.0034077424198901D, -0.00529439806145769D, -0.00702249784257682D, -0.00826802752262929D, -0.00878816295614779D, -0.008469513806873051D, -0.00735360701803667D, -0.00563389951885155D, -0.00362421333211422D, -0.0017034003463387D, -0.00024607843228828D, 0.0004482204930907D, 0.000214038601094618D, -0.000944144342204741D, -0.00284688065681757D, -0.00516735944741822D, -0.00748779521095554D, -0.00937485221013864D, -0.010460832484825D, -0.010515440991872D, -0.00949451455636883D, -0.00755555220454338D, -0.00503604893255608D, -0.00239708312458895D, -0.000141510749016328D, 0.0012791104762028D, 0.0015517714092001D, 0.00056704834171846D, -0.00155130143843582D, -0.00445908005673155D, -0.00764634029517581D, -0.010525560871907D, -0.012538539508786D, -0.013262846112682D, -0.012498154071149D, -0.010315320218553D, -0.0070574010076081D, -0.0032898835571324D, 0.000293142491633399D, 0.00299195454090684D, 0.00423206494064704D, 0.00367942048382208D, 0.00131776653753629D, -0.00252808679982519D, -0.00723063563671066D, -0.0119562940256244D, -0.0158063443723257D, -0.0179793574883495D, -0.0179272006989418D, -0.0154760392566849D, -0.010888368074868D, -0.00485085240159456D, 0.00161504006609049D, 0.0073095562625733D, 0.011055891678871D, 0.01191479251933D, 0.00937681513265424D, 0.00349822751127974D, -0.00504625235976391D, -0.015008409965934D, -0.0247086729108038D, -0.0322590319016573D, -0.0358365930989115D, -0.0339679171620155D, -0.0257807599622063D, -0.01118286220878D, 0.0090629046641984D, 0.0333830906953785D, 0.0595562582455106D, 0.0849741290731021D, 0.106966442254914D, 0.123144409178258D, 0.13171399483958D, 0.13171399483958D, 0.123144409178258D, 0.106966442254914D, 0.0849741290731021D, 0.0595562582455106D, 0.0333830906953785D, 0.0090629046641984D, -0.01118286220878D, -0.0257807599622063D, -0.0339679171620155D, -0.0358365930989115D, -0.0322590319016573D, -0.0247086729108038D, -0.015008409965934D, -0.00504625235976391D, 0.00349822751127974D, 0.00937681513265424D, 0.01191479251933D, 0.011055891678871D, 0.0073095562625733D, 0.00161504006609049D, -0.00485085240159456D, -0.010888368074868D, -0.0154760392566849D, -0.0179272006989418D, -0.0179793574883495D, -0.0158063443723257D, -0.0119562940256244D, -0.00723063563671066D, -0.00252808679982519D, 0.00131776653753629D, 0.00367942048382208D, 0.00423206494064704D, 0.00299195454090684D, 0.000293142491633399D, -0.0032898835571324D, -0.0070574010076081D, -0.010315320218553D, -0.012498154071149D, -0.013262846112682D, -0.012538539508786D, -0.010525560871907D, -0.00764634029517581D, -0.00445908005673155D, -0.00155130143843582D, 0.00056704834171846D, 0.0015517714092001D, 0.0012791104762028D, -0.000141510749016328D, -0.00239708312458895D, -0.00503604893255608D, -0.00755555220454338D, -0.00949451455636883D, -0.010515440991872D, -0.010460832484825D, -0.00937485221013864D, -0.00748779521095554D, -0.00516735944741822D, -0.00284688065681757D, -0.000944144342204741D, 0.000214038601094618D, 0.0004482204930907D, -0.00024607843228828D, -0.0017034003463387D, -0.00362421333211422D, -0.00563389951885155D, -0.00735360701803667D, -0.008469513806873051D, -0.00878816295614779D, -0.00826802752262929D, -0.00702249784257682D, -0.00529439806145769D, -0.0034077424198901D, -0.00170597869344896D, -0.000488598112186092D, 4.25002186405121E-005D, -0.000182849419019012D, -0.00109388518637658D, -0.0024958874386246D, -0.00410925127068627D, -0.00562315333865641D, -0.00675336186413425D, -0.00729374687426444D, -0.0071519845067431D, -0.00636358411753227D, -0.00508198134809249D, -0.00354738366789472D, -0.00204036227034438D, -0.000829341117202341D, -0.000121501341208779D, -2.62833612978657E-005D, -0.000537603813784902D, -0.00153786268861382D, -0.00282220492576368D, -0.00413837160798384D, -0.0052341754236042D, -0.00590398183887164D, -0.00602539931306045D, -0.00557995156612952D, -0.00465401417839112D, -0.00342058739997302D, -0.00210539663183083D, -0.00094420432887318D, -0.000139079612299567D, 0.00017800520584401D, -3.19766104447796E-005D, -0.000710028821381479D, -0.0017120595076341D, -0.00283736136973274D, -0.00386674837933375D, -0.0046034450441617D, -0.00490881515896487D, -0.00472678969339786D, -0.0040923984060442D, -0.00312350225351387D, -0.00199725651943966D, -0.000916299236533598D, -7.075822572287791E-005D, 0.000396486626536007D, 0.00041530312463846D, -3.02801614322377E-007D, -0.000756362553657179D, -0.0016979707959775D, -0.0026389926345583D, -0.00339713383295239D, -0.0038274287920596D, -0.00384837663407466D, -0.0034556681717152D, -0.00272159333227661D, -0.0017801280527642D, -0.000801113927599773D, 4.18882442035887E-005D, 0.000603626371741223D, 0.000793553359180197D, 0.000591427811460621D, 4.98425162577E-005D, -0.000716946449325894D, -0.00155516788603045D, -0.00230077814015934D, -0.00280985876267867D, -0.00298494810943401D, -0.00279210409544492D, -0.0022661330701053D, -0.00150281291712562D, -0.000640203673959743D, 0.00016773156740448D, 0.000779759007376738D, 0.0010933599394102D, 0.00106288579277491D, 0.00070710096654096D, 0.0001044703719224D, -0.00062228291826615D, -0.0013297508884025D, -0.001880603296009D, -0.0021689857825021D, -0.00213943521160232D, -0.0017963806183181D, -0.00120218142834308D, -0.0004647202435967D, 0.00028343883620657D, 0.00090986693888556D, 0.0013068736477961D, 0.00141088442354037D, 0.00121357089114537D, 0.00076234087929536D, 0.000150755304146288D, -0.000499669263582827D, -0.0010622794183398D, -0.00142891708711331D, -0.00152949276953303D, -0.0013444420969517D, -0.000907384243766655D, -0.00029815298661902D, 0.00037285572048885D, 0.00098547625008281D, 0.00143228621248031D, 0.00163813734628643D, 0.0015736565179956D, 0.00125980163155078D, 0.00076329696472705D, 0.000183416169919086D, -0.000366775820220804D, -0.00078169289925272D, -0.0009825044704947681D, -0.000931420112254212D, -0.00063788478252317D, -0.0001562085758773D, 0.000425335294937135D, 0.00100124647033075D, 0.0014687260488199D, 0.0017465127588879D, 0.00178971139213456D, 0.00159738209371754D, 0.0012121573011249D, 0.00071152581767356D, 0.00019308850622077D, -0.00024395462268853D, -0.000517038609916063D, -0.000575386700363658D, -0.000408761057604636D, -4.88633815893146E-005D, 0.000437330250865943D, 0.000960366107007573D, 0.0014253553239755D, 0.0017494651968717D, 0.00187719780614157D, 0.00179008298063751D, 0.0015096647586277D, 0.00109274642643676D, 0.000620484195280941D, 0.000182850004397D, -0.000137996600502361D, -0.00028293202316019D, -0.000226295960430978D, 1.9730705769468E-005D, 0.000408117020837066D, 0.00086606793263505D, 0.00130904009040423D, 0.00165640715611924D, 0.0018464663763184D, 0.0018474051307332D, 0.00166288067239408D, 0.0013306426175546D, 0.000915185152311649D, 0.000495251264637447D, 0.000149210026530823D, -5.95892868598273E-005D, -9.41422652072306E-005D, 4.9611695770945E-005D, 0.000342704502465947D, 0.00072884065824089D, 0.00113538873580102D, 0.0014868999734513D, 0.00171926753558527D, 0.0017912450103479D, 0.00169189034302864D, 0.0014419630109892D, 0.00108976947205013D, 0.000701646118277693D, 0.000349587730781208D, 9.76836786773469E-005D, -9.44217447750788E-006D, 4.52472361597781E-005D, 0.0002486418319228D, 0.00056019737374658D, 0.0009200184348714271D, 0.0012600546655286D, 0.0015169763177431D, 0.00164363911682995D, 0.00161772585583283D, 0.0014453235756532D, 0.00115954087382408D, 0.000813805297511983D, 0.000471854489811893D, 0.000195625958665316D, 3.4012343541018E-005D, 1.3849309144843E-005D, 0.00013546675715985D, 0.000372855678084063D, 0.00067905255766096D, 0.000994960964833502D, 0.0012606355500263D, 0.00142619655393134D, 0.00146107399744823D, 0.00135918714255885D, 0.0011398772011145D, 0.000843769833829344D, 0.000525127275789142D, 0.000241435399013574D, 4.29148478210438E-005D, -3.68288426928038E-005D, 1.34194308769052E-005D, 0.000180569079914348D, 0.000430193817156114D, 0.000713031741195615D, 0.000974496584720574D, 0.0011646592200388D, 0.00124758803351198D, 0.00120759179058643D, 0.00105200093732304D, 0.000809281106655558D, 0.00052364554256276D, 0.0002464637750764D, 2.68551602843451E-005D, -9.75382510211861E-005D, -0.00010714149456976D, -3.97963442099545E-006D, 0.00018921170876054D, 0.000433304948104277D, 0.000680444821457835D, 0.000882824811935392D, 0.00100170797231571D, 0.00101425160569053D, 0.000917680849208474D, 0.00072930778548967D, 0.000483230524813043D, 0.000223593185867299D, -3.51663112894297E-006D, -0.000158799772313772D, -0.000216702229081013D, -0.00017012293532712D, -3.12520579316764E-005D, 0.00017077055677482D, 0.000395685490536059D, 0.0005995122812886211D, 0.000742921251348084D, 0.0007982248319367D, 0.000754513329299535D, 0.00061922033166568D, 0.000416678727825662D, 0.0001831805086828D, -3.96953994968743E-005D, -0.000213033893593434D, -0.00030752285342762D, -0.000308920034466907D, -0.00022025664244296D, -6.12246546286886E-005D, 0.000135904911184024D, 0.00033221821078459D, 0.00048963925032985D, 0.000577738539851612D, 0.000579439273810329D, 0.000493770262973607D, 0.000336015790834792D, 0.000134455506846221D, -7.48344639902692E-005D, -0.000254954924592149D, -0.000374794893114052D, -0.000414892907150544D, -0.000370652605605774D, -0.000253184359458119D, -8.68245287093865E-005D, 9.533246490044179E-005D, 0.000257871342545317D, 0.000369410870378961D, 0.000408543598287131D, 0.000367434261574489D, 0.00025328544795083D, 8.65917343263471E-005D, -0.00010261048558795D, -0.00028062372139007D, -0.000416139661135309D, -0.000486191446429637D, -0.000480063170756953D, -0.000401313076289361D, -0.000266713802736679D, -0.000103198447100559D, 5.74523943528654E-005D, 0.000184316204896415D, 0.0002532312989996D, 0.000250946901093621D, 0.000177638742212356D, 4.64904244119377E-005D, -0.00011868367820605D, -0.000288174788977175D, -0.000431708214571715D, -0.000524244802280253D, -0.000550312187666917D, -0.000506955565217396D, -0.000403915938898268D, -0.000261904280504075D, -0.000108442383216512D, 2.70624688829392E-005D, 0.00011897116401627D, 0.000149870978174831D, 0.00011390885230493D, 1.75247095049829E-005D, -0.00012164768632281D, -0.000278364015955548D, -0.000424271089806838D, -0.00053337358360414D, -0.0005865691429630631D, -0.000575313621310307D, -0.000502871887665066D, -0.00038382933877727D, -0.00024108962877764D, -0.00010181688682969D, 7.8132740985481E-006D, 6.71888260706398E-005D, 6.52221969033149E-005D, 2.08912857755044E-006D, -0.00011066638257936D, -0.00025255457870159D, -0.000397735963103583D, -0.000520018323470526D, -0.00059743143780257D, -0.00061644929580642D, -0.000574195668379932D, -0.000479138906517235D, -0.00034926493686228D, -0.00020897351185555D, -8.43683732439765E-005D, 1.28683042804457E-006D, 3.20306720584621E-005D, 1.8664996724834E-006D, -8.39458869442319E-005D, -0.000210096448291764D, -0.000353799232879D, -0.000489172991016836D, -0.00059171601394605D, -0.000642981223594934D, -0.000633691820405254D, -0.000565653686021656D, -0.000451218621755097D, -0.00031130364123457D, -0.000171410449954226D, -5.7277674807016E-005D, 9.98070420727893E-006D, 1.74584888808446E-005D, -3.69617246459944E-005D, -0.000144403231408916D, -0.000286293068422389D, -0.000437843807718034D, -0.0005721865322903D, -0.000665360053537445D, -0.000700350679062249D, -0.00067039277429577D, -0.000580032844849327D, -0.000444674887956967D, -0.00028776702659654D, -0.000137026795703571D, -1.94228353791984E-005D, 4.32881937489408E-005D, 3.86308033064396E-005D, -3.45312065837977E-005D, -0.00016540091389957D, -0.0003331379540844D, -0.000510005871163562D, -0.000666185490192942D, -0.000774523301469025D, -0.000815365828849824D, -0.000779750742743636D, -0.000671317451493218D, -0.000505715502523139D, -0.00030850987471925D, -0.000110987588308181D, 5.45352349753129E-005D, 0.000160107470556599D, 0.00018646630096872D, 0.000126516426775505D, -1.34624881903889E-005D, -0.00021418266270264D, -0.000446146123317908D, -0.000673493754692806D, -0.00085926049580429D, -0.000970341482030938D, -0.000982353338460868D, -0.000882833781720179D, -0.000673193852496632D, -0.000368302018467704D, 5.24895417994034E-006D, 0.000412844215447331D, 0.000816034117695199D, 0.00117746477759056D, 0.00146506438032912D, 0.00165575166183813D, 0.001737409328845D, 0.0017098129169296D, 0.00158354624155544D, 0.001378119500004D, 0.00111885157865328D, 0.000833627498869421D, 0.000549359171310485D, 0.000289279202398974D, 7.06479844998841E-005D, -9.60607754678275E-005D, -0.000207456105101242D, -0.000266503975879537D, -0.000280928519745322D, -0.000261497617618105D, -0.000220230946712853D, -0.000168411968763922D, -0.0001158802706427D, -9.744044235842301E-005D };
/*      */     private int filtered;
/* 1434 */     private float[] eegBuffer_filtered = new float[948];
/*      */ 
/* 1441 */     private float[] tempeegBuffer_filtered = new float[948];
/*      */ 
/* 1444 */     private int bufferCounter = 0;
/*      */ 
/* 1448 */     private Integer[] rrInter_calcBuf_70 = new Integer[70];
/* 1449 */     private Integer[] rrInter_calcBuf_180 = new Integer['´'];
/*      */     private TGBlinkDetector blink;
/*      */     private TGSamplingRateCalculator sratecalc;
/*      */     private TGHrv hrv;
/*      */     private RespiratoryRate respiratory;
/*      */     private EnergyLevel energy;
/*      */     private Positivity positivity;
/*      */     private double[] vl;
/*      */     private TF_TD_control tf_td_control;
/* 1450 */     private int poorSignal = 255;
/*      */ 
/* 1465 */     private int rrint = 0;
/* 1466 */     private int samplingRate = 0;
/* 1467 */     private long timeLastInterval = -1L;
/*      */     private long timeInt;
/*      */     private Zone zone;
/* 1470 */     private int attention = 0;
/* 1471 */     private int meditation = 0;
/*      */     private int parser_err_log_suppression;
/*      */     private int dualchannel_raw_data;
/*      */     private int dualchannel_timestamp;
/*      */     private int dualchannel_sig_quality;
/*      */ 
/*      */     public TGParser(TGDevice tg, boolean rawDataEnable)
/*      */     {
/* 1482 */       if (tg != null) {
/* 1483 */         this.tgDevice = tg;
/* 1484 */         this.handler = this.tgDevice.handler;
/*      */       }
/*      */       else {
/* 1487 */         this.tgDevice = null;
/* 1488 */         this.handler = null;
/*      */       }
/* 1490 */       this.rawEnabled = rawDataEnable;
/* 1491 */       this.parserState = 1;
/* 1492 */       this.blink = new TGBlinkDetector();
/*      */ 
/* 1494 */       this.respiratory = new RespiratoryRate(this.tgDevice);
/* 1495 */       this.energy = new EnergyLevel();
/* 1496 */       this.sratecalc = new TGSamplingRateCalculator();
/* 1497 */       this.hrv = new TGHrv();
/* 1498 */       this.positivity = new Positivity();
/* 1499 */       this.vl = new double[20];
/*      */ 
/* 1504 */       this.tf_td_control = new TF_TD_control(this.tgDevice);
/* 1505 */       this.zone = new Zone();
/*      */ 
/* 1511 */       TGDevice.access$1102(TGDevice.this, false);
/* 1512 */       TGDevice.access$1202(TGDevice.this, 0);
/* 1513 */       TGDevice.access$1302(TGDevice.this, false);
/* 1514 */       TGDevice.access$1402(TGDevice.this, false);
/* 1515 */       TGDevice.access$1502(TGDevice.this, false);
/* 1516 */       TGDevice.access$1602(TGDevice.this, false);
/* 1517 */       TGDevice.access$1702(TGDevice.this, true);
/* 1518 */       TGDevice.access$1802(TGDevice.this, false);
/* 1519 */       TGDevice.access$1902(TGDevice.this, false);
/* 1520 */       TGDevice.access$2002(TGDevice.this, false);
/*      */ 
/* 1522 */       this.parser_err_log_suppression = 0;
/* 1523 */       this.dualchannel_raw_data = 0;
/* 1524 */       this.dualchannel_timestamp = 0;
/* 1525 */       this.dualchannel_sig_quality = 0;
/*      */     }
/*      */ 
/*      */     public final int parseByte(byte currByte)
/*      */     {
/* 1554 */       switch (this.parserState)
/*      */       {
/*      */       case 1:
/* 1558 */         if ((currByte & 0xFF) == 170) {
/* 1559 */           this.parserState = 2;
/*      */         }
/*      */         else
/*      */         {
/* 1563 */           if (!TGDevice.this.logEnabled) break; TGDevice.access$2200(TGDevice.this, "Waiting for sync"); } break;
/*      */       case 2:
/* 1569 */         if ((currByte & 0xFF) == 170) {
/* 1570 */           this.parserState = 3;
/*      */         } else {
/* 1572 */           this.parserState = 1;
/* 1573 */           if (!TGDevice.this.logEnabled) break; TGDevice.access$2200(TGDevice.this, "Bad 2nd sync byte"); } break;
/*      */       case 3:
/* 1579 */         this.payloadLength = (currByte & 0xFF);
/* 1580 */         if (this.payloadLength < 170) {
/* 1581 */           this.payloadBytesReceived = 0;
/* 1582 */           this.payloadSum = 0;
/* 1583 */           this.parserState = 4;
/* 1584 */         } else if (this.payloadLength == 170) {
/* 1585 */           this.parserState = 2;
/*      */         } else {
/* 1587 */           this.parserState = 1;
/*      */         }
/* 1589 */         break;
/*      */       case 4:
/* 1593 */         this.payload[(this.payloadBytesReceived++)] = currByte;
/* 1594 */         this.payloadSum += (currByte & 0xFF);
/* 1595 */         if (this.payloadBytesReceived != this.payloadLength) break;
/* 1596 */         this.parserState = 5; break;
/*      */       case 5:
/* 1604 */         this.checksum = (currByte & 0xFF);
/* 1605 */         if (this.checksum == ((this.payloadSum ^ 0xFFFFFFFF) & 0xFF))
/*      */         {
/* 1607 */           if (this.handler != null) parsePacketPayload();
/* 1608 */           this.parserState = 1;
/* 1609 */           return 1;
/* 1610 */         }if ((this.payloadBytesReceived == 8) && (this.payload[0] == -77))
/*      */         {
/* 1618 */           if (this.handler != null) parsePacketPayload();
/* 1619 */           this.parserState = 1;
/* 1620 */           return 1;
/* 1621 */         }if (this.checksum == 170)
/*      */         {
/* 1625 */           this.parserState = 2;
/* 1626 */           return -2;
/*      */         }
/*      */ 
/* 1630 */         Log.d("TGDevice", "Checksum failed. Recieved: " + this.checksum + ". Calculated: " + ((this.payloadSum ^ 0xFFFFFFFF) & 0xFF));
/* 1631 */         Log.d("TGDevice", "pay zero: " + this.payload[0] + ". len: " + this.payloadBytesReceived);
/* 1632 */         if (TGDevice.this.logEnabled) TGDevice.access$2200(TGDevice.this, "Checksum failed. Received: " + this.checksum + ". Calculated: " + ((this.payloadSum ^ 0xFFFFFFFF) & 0xFF));
/* 1633 */         this.parserState = 1;
/* 1634 */         return -2;
/*      */       }
/*      */ 
/* 1639 */       return this.parserState;
/*      */     }
/*      */ 
/*      */     public final int parseByte(byte[] buffer, int byte_cnt)
/*      */     {
/* 1645 */       int current_state = 1;
/*      */ 
/* 1647 */       if (byte_cnt > buffer.length) {
/* 1648 */         Log.d("TGDevice", "BUG: buffer length exceeded, using buffer limit");
/* 1649 */         if (TGDevice.this.logEnabled) TGDevice.access$2200(TGDevice.this, "BUG: buffer length exceeded, using buffer limit");
/* 1650 */         byte_cnt = buffer.length;
/*      */       }
/*      */ 
/* 1653 */       for (int i = 0; i < byte_cnt; i++)
/*      */       {
/* 1655 */         current_state = parseByte(buffer[i]);
/*      */       }
/* 1657 */       return current_state;
/*      */     }
/*      */ 
/*      */     private int applyFilter(float[] data)
/*      */     {
/* 1666 */       int length = data.length;
/* 1667 */       int result = 0;
/*      */ 
/* 1669 */       for (int i = 0; i < length; i++) {
/* 1670 */         result += (int)(data[i] * this.coeff[i]);
/*      */       }
/* 1672 */       return result;
/*      */     }
/*      */ 
/*      */     private void parsePacketPayload()
/*      */     {
/* 1688 */       int i = 0;
/* 1689 */       int j = 0;
/*      */ 
/* 1691 */       i = 0; int k = 0; int m = 0; int n = 0; int i1 = 0; int i2 = 0; int i3 = 0; int i4 = 0;
/* 1692 */       i = 0;
/*      */       int code;
/*      */       int valueLength;
/* 1695 */       if (!TGDevice.this.isIdentified) {
/* 1696 */         byte savedTrimByteValue = 0;
/*      */ 
/* 1718 */         TGDevice.access$1208(TGDevice.this);
/*      */ 
/* 1723 */         int index = 0;
/* 1724 */         while (index < this.payloadLength)
/*      */         {
/* 1736 */           code = this.payload[index] & 0xFF;
/* 1737 */           index++;
/*      */ 
/* 1740 */           if (code > 127) {
/* 1741 */             valueLength = this.payload[index] & 0xFF;
/* 1742 */             index++;
/*      */           } else {
/* 1744 */             valueLength = 1;
/*      */           }
/*      */ 
/* 1747 */           switch (code)
/*      */           {
/*      */           case 178:
/* 1756 */             TGDevice.access$1102(TGDevice.this, true);
/* 1757 */             TGDevice.access$1302(TGDevice.this, true);
/* 1758 */             TGDevice.access$1802(TGDevice.this, true);
/* 1759 */             TGDevice.access$1702(TGDevice.this, false);
/* 1760 */             TGDevice.access$2002(TGDevice.this, true);
/* 1761 */             Log.v("TGDevice", "BMD101 Dual Detected");
/* 1762 */             index += valueLength;
/* 1763 */             break;
/*      */           case 8:
/* 1776 */             if (!TGDevice.this.trimByteSent) {
/* 1777 */               savedTrimByteValue = (byte)this.payload[index];
/*      */             }
/* 1779 */             index += valueLength;
/* 1780 */             break;
/*      */           case 133:
/* 1783 */             if ((this.payload[(index + 2)] & 0xF0) == 240) {
/* 1784 */               TGDevice.access$1102(TGDevice.this, true);
/* 1785 */               TGDevice.access$1402(TGDevice.this, true);
/* 1786 */               TGDevice.access$1902(TGDevice.this, true);
/* 1787 */               TGDevice.access$1702(TGDevice.this, true);
/* 1788 */               Log.v("TGDevice", "BMD100 Detected");
/*      */             } else {
/* 1790 */               TGDevice.access$1102(TGDevice.this, true);
/* 1791 */               TGDevice.access$1302(TGDevice.this, true);
/* 1792 */               TGDevice.access$1902(TGDevice.this, true);
/* 1793 */               TGDevice.access$1702(TGDevice.this, false);
/* 1794 */               Log.v("TGDevice", "BMD101 Detected");
/*      */             }
/* 1796 */             index += valueLength;
/* 1797 */             break;
/*      */           case 144:
/*      */           case 177:
/* 1808 */             TGDevice.access$1102(TGDevice.this, true);
/* 1809 */             TGDevice.access$2302(TGDevice.this, true);
/* 1810 */             TGDevice.access$1802(TGDevice.this, true);
/* 1811 */             Log.v("TGDevice", "ThinkCap Detected");
/* 1812 */             index += valueLength;
/* 1813 */             break;
/*      */           default:
/* 1818 */             index += valueLength;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1825 */         if ((!TGDevice.this.isIdentified) && (TGDevice.this.isIdentified_pkt_cnt >= 768))
/*      */         {
/* 1839 */           TGDevice.access$1102(TGDevice.this, true);
/* 1840 */           TGDevice.access$1502(TGDevice.this, true);
/* 1841 */           TGDevice.access$1802(TGDevice.this, true);
/* 1842 */           TGDevice.access$1902(TGDevice.this, false);
/* 1843 */           TGDevice.access$1702(TGDevice.this, false);
/* 1844 */           Log.v("TGDevice", "TGAM Detected");
/*      */         }
/* 1846 */         if (TGDevice.this.isIdentified)
/*      */         {
/* 1851 */           if (TGDevice.this.isTrimByteNeeded) {
/* 1852 */             byte[] trimByte = { -86, -86, 4, 3, 64, -7, 0, savedTrimByteValue };
/* 1853 */             TGDevice.access$2400(TGDevice.this, trimByte);
/* 1854 */             Log.v("TGDevice", "Sent trim byte: " + savedTrimByteValue);
/* 1855 */             TGDevice.access$1602(TGDevice.this, true);
/*      */           }
/*      */ 
/* 1864 */           if ((TGDevice.this.isFunctionEEG) && 
/* 1865 */             (TGDevice.ekgPersonalizationEnabled)) {
/* 1866 */             TGDevice.ekgPersonalizationEnabled = false;
/* 1867 */             Log.v("TGDevice", "Disabling ekgPersonalization");
/*      */           }
/* 1869 */           TGDevice.this.setRespirationRateEnable(TGDevice.this.respirationEnabled);
/*      */ 
/* 1879 */           TGDevice.this.setPositivityEnable(false);
/* 1880 */           TGDevice.this.setTaskDifficultyEnable(TGDevice.this.difficultyEnabled);
/* 1881 */           TGDevice.this.setTaskFamiliarityEnable(TGDevice.this.familiarityEnabled);
/* 1882 */           TGDevice.this.setBlinkDetectionEnabled(TGDevice.this.blinkEnabled);
/*      */ 
/* 1887 */           this.handler.obtainMessage(800, 0, 0).sendToTarget();
/*      */         }
/* 1889 */         return;
/*      */       }
/*      */ 
/* 1898 */       int index = 0;
/* 1899 */       while (index < this.payloadLength)
/*      */       {
/* 1911 */         code = this.payload[index] & 0xFF;
/* 1912 */         index++;
/*      */ 
/* 1915 */         if (code > 127) {
/* 1916 */           valueLength = this.payload[index] & 0xFF;
/* 1917 */           index++;
/*      */         } else {
/* 1919 */           valueLength = 1;
/*      */         }
/*      */         label2175: TGRawMulti m;
/* 1924 */         switch (code)
/*      */         {
/*      */         case 1:
/* 1927 */           this.handler.obtainMessage(20).sendToTarget();
/* 1928 */           break;
/*      */         case 2:
/* 1933 */           if (this.rawCountEnabled) {
/* 1934 */             this.handler.obtainMessage(19, this.rawCount, 0).sendToTarget();
/* 1935 */             TGDevice.access$2200(TGDevice.this, "Raw Count: " + this.rawCount);
/* 1936 */             this.rawCount = 0;
/*      */           }
/*      */ 
/* 1940 */           if ((TGDevice.this.isBMD100) || ((TGDevice.this.isBMD101) && (TGDevice.this.isFunctionEKG)))
/*      */           {
/* 1945 */             if ((this.poorSignal == 200) && ((this.payload[index] & 0xFF) == 0)) {
/* 1946 */               this.hrv.Reset();
/* 1947 */               TGDevice.this.rrintBuffer.clear();
/* 1948 */               TGDevice.this.rrintBufferForHeartAge.clear();
/* 1949 */               TGDevice.this.inputRawBuffer.clear();
/* 1950 */               TGDevice.access$3202(0);
/* 1951 */               this.calculationEnabled = false;
/*      */ 
/* 1953 */               for (int k = 0; k < 948; k++)
/*      */               {
/* 1955 */                 this.tempeegBuffer_filtered[k] = 0.0F;
/* 1956 */                 this.eegBuffer_filtered[k] = 0.0F;
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1962 */           this.poorSignal = (this.payload[index] & 0xFF);
/* 1963 */           this.handler.obtainMessage(2, this.poorSignal, 0).sendToTarget();
/* 1964 */           if (TGDevice.this.logEnabled) TGDevice.access$2200(TGDevice.this, "02 01 " + String.format("%02X", new Object[] { Integer.valueOf(this.payload[index] & 0xFF) }) + " PoorSignal: " + this.poorSignal);
/*      */ 
/* 1968 */           index += valueLength;
/* 1969 */           break;
/*      */         case 4:
/* 1973 */           this.attention = (this.payload[index] & 0xFF);
/*      */ 
/* 1975 */           if (this.poorSignal == 0)
/*      */           {
/* 1980 */             this.handler.obtainMessage(4, this.attention, 0).sendToTarget();
/*      */           }
/*      */ 
/* 1983 */           if (TGDevice.this.logEnabled) TGDevice.access$2200(TGDevice.this, "04 01 " + String.format("%02X", new Object[] { Integer.valueOf(this.payload[index] & 0xFF) }) + " Attention: " + (this.payload[index] & 0xFF));
/* 1984 */           index += valueLength;
/* 1985 */           break;
/*      */         case 5:
/* 1988 */           this.meditation = (this.payload[index] & 0xFF);
/*      */ 
/* 1990 */           if (this.poorSignal == 0)
/*      */           {
/* 1995 */             this.handler.obtainMessage(5, this.meditation, 0).sendToTarget();
/*      */ 
/* 1999 */             if (this.zone.update((byte)this.attention, (byte)this.meditation).booleanValue())
/*      */             {
/* 2003 */               int z = this.zone.getZone();
/* 2004 */               this.handler.obtainMessage(14, z, 0).sendToTarget();
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 2011 */             this.zone.reset();
/*      */           }
/* 2013 */           if (TGDevice.this.logEnabled) TGDevice.access$2200(TGDevice.this, "05 01 " + String.format("%02X", new Object[] { Integer.valueOf(this.payload[index] & 0xFF) }) + " Meditation: " + (this.payload[index] & 0xFF));
/* 2014 */           index += valueLength;
/*      */ 
/* 2017 */           break;
/*      */         case 3:
/* 2020 */           index += valueLength;
/*      */ 
/* 2025 */           break;
/*      */         case 19:
/* 2028 */           if (this.dualchannel_timestamp++ == 0) {
/* 2029 */             Log.v("TGParser", "dual channel timestamp: ignoring ");
/*      */           }
/* 2031 */           else if (this.dualchannel_timestamp >= 75000) {
/* 2032 */             this.dualchannel_timestamp = 0;
/*      */           }
/* 2034 */           index += valueLength;
/* 2035 */           break;
/*      */         case 7:
/* 2038 */           index += valueLength;
/* 2039 */           break;
/*      */         case 128:
/* 2044 */           this.highlow[0] = (this.payload[index] & 0xFF);
/* 2045 */           this.highlow[1] = (this.payload[(index + 1)] & 0xFF);
/*      */           int raw;
/* 2047 */           if ((
/* 2047 */             raw = (this.highlow[0] << 8) + this.highlow[1]) > 
/* 2047 */             32768) raw -= 65536;
/*      */ 
/* 2051 */           if (TGDevice.ekgPersonalizationEnabled)
/*      */           {
/* 2054 */             TGDevice.this.ekgSenseMan.addSample(raw, this.poorSignal);
/*      */           }
/*      */ 
/* 2059 */           System.arraycopy(this.eegBuffer_filtered, 1, this.tempeegBuffer_filtered, 0, 947);
/* 2060 */           this.tempeegBuffer_filtered[947] = raw;
/* 2061 */           System.arraycopy(this.tempeegBuffer_filtered, 0, this.eegBuffer_filtered, 0, 948);
/* 2062 */           this.bufferCounter += 1;
/*      */ 
/* 2064 */           if (this.bufferCounter >= 948)
/*      */           {
/* 2067 */             this.filtered = applyFilter(this.eegBuffer_filtered);
/*      */ 
/* 2070 */             this.bufferCounter = 947;
/*      */           }
/*      */ 
/* 2075 */           if (this.rawEnabled)
/*      */           {
/* 2078 */             this.handler.obtainMessage(128, raw, 0).sendToTarget();
/*      */ 
/* 2085 */             if (TGDevice.this.logEnabled) {
/* 2086 */               TGDevice.access$2200(TGDevice.this, "80 02 " + String.format("%02X", new Object[] { Integer.valueOf(this.payload[index] & 0xFF) }) + " " + String.format("%02X", new Object[] { Integer.valueOf(this.payload[(index + 1)] & 0xFF) }) + " Raw: " + raw);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2095 */           if (this.rawCountEnabled) this.rawCount += 1;
/*      */ 
/* 2101 */           if ((TGDevice.this.blinkEnabled) && (TGDevice.this.isFunctionEEG))
/*      */           {
/*      */             int b;
/* 2121 */             if ((
/* 2121 */               b = this.blink.Detect(this.poorSignal, (short)raw)) > 0)
/*      */             {
/* 2122 */               this.handler.obtainMessage(22, b, 0).sendToTarget();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2128 */           if (((TGDevice.this.isBMD100) || ((TGDevice.this.isBMD101) && (TGDevice.this.isFunctionEKG))) && (this.poorSignal == 200))
/*      */           {
/* 2133 */             TGDevice.access$3208();
/* 2134 */             if (TGDevice.pass_raw_counter >= TGDevice.this.pass_seconds << 9)
/*      */             {
/* 2136 */               this.calculationEnabled = true;
/*      */             }
/*      */ 
/* 2155 */             this.rrint = this.hrv.AddData(this.filtered);
/*      */ 
/* 2157 */             if (this.rrint > 0) {
/* 2158 */               if (this.timeBasedIntervalEnabled)
/*      */               {
/* 2160 */                 if (this.timeLastInterval == -1L) {
/* 2161 */                   this.timeLastInterval = System.currentTimeMillis();
/*      */                 }
/*      */                 else
/*      */                 {
/* 2165 */                   this.timeInt = (System.currentTimeMillis() - this.timeLastInterval);
/* 2166 */                   if ((this.timeInt > -2147483648L) && (this.timeInt < 2147483647L)) {
/* 2167 */                     this.rrint = (int)this.timeInt;
/*      */                   }
/*      */                   else {
/* 2170 */                     this.rrint = (int)Math.round(this.rrint / 0.512D);
/*      */                   }
/* 2172 */                   this.timeLastInterval = System.currentTimeMillis(); break label2175;
/*      */                 }
/*      */               }
/*      */ 
/* 2176 */               this.rrint = (int)Math.round(this.rrint / 0.512D);
/*      */ 
/* 2179 */               this.handler.obtainMessage(271, this.rrint, 0).sendToTarget();
/*      */               int heartAgeFiveMin;
/* 2181 */               if (this.calculationEnabled)
/*      */               {
/* 2184 */                 TGDevice.this.rrintBuffer.add(Integer.valueOf(this.rrint));
/* 2185 */                 TGDevice.this.rrintBufferForHeartAge.add(Integer.valueOf(this.rrint));
/*      */ 
/* 2189 */                 if (TGDevice.this.rrintBufferForHeartAge.size() == 70)
/*      */                 {
/* 2192 */                   for (int i = 0; i < 70; i++)
/*      */                   {
/* 2194 */                     this.rrInter_calcBuf_70[i] = ((Integer)TGDevice.access$3000(TGDevice.this).get(i));
/*      */                   }
/*      */ 
/* 2198 */                   if (TGDevice.this.inputAge > 0)
/*      */                   {
/* 2202 */                     int heartAge = (int)NeuroSkyHeartMeters.calculateHeartAge(this.rrInter_calcBuf_70, TGDevice.this.inputAge, 16);
/* 2203 */                     this.handler.obtainMessage(272, heartAge, 0).sendToTarget();
/*      */                   }
/*      */ 
/* 2206 */                   TGDevice.this.rrintBufferForHeartAge.clear();
/*      */                 }
/*      */ 
/* 2210 */                 if (TGDevice.this.rrintBuffer.size() == 300)
/*      */                 {
/* 2213 */                   for (int i = 120; i < 300; i++)
/*      */                   {
/* 2215 */                     this.rrInter_calcBuf_180[(i - 120)] = ((Integer)TGDevice.access$2900(TGDevice.this).get(i));
/*      */                   }
/*      */ 
/* 2218 */                   if (TGDevice.this.inputAge > 0)
/*      */                   {
/* 2222 */                     heartAgeFiveMin = (int)NeuroSkyHeartMeters.calculateHeartAge(this.rrInter_calcBuf_180, TGDevice.this.inputAge, 8);
/* 2223 */                     this.handler.obtainMessage(273, heartAgeFiveMin, 0).sendToTarget();
/*      */                   }
/*      */ 
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/* 2234 */               double heartRate = Math.round(heartAgeFiveMin = 60.0D / (this.rrint / 1000.0D));
/*      */ 
/* 2235 */               this.handler.obtainMessage(3, (int)heartRate, 0).sendToTarget();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2253 */           if (((TGDevice.this.isBMD100) || ((TGDevice.this.isBMD101) && (TGDevice.this.isFunctionEKG))) && (this.rrint > 0) && (this.relaxationEnabled) && (this.calculationEnabled))
/*      */           {
/*      */             int e;
/* 2264 */             if ((
/* 2264 */               e = this.energy.addInterval(this.rrint, this.poorSignal)) > 0)
/*      */             {
/* 2265 */               this.handler.obtainMessage(24, e, 0).sendToTarget();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2271 */           if (((TGDevice.this.isBMD100) || ((TGDevice.this.isBMD101) && (TGDevice.this.isFunctionEKG))) && (TGDevice.this.respirationEnabled))
/*      */           {
/* 2273 */             this.respiratory.calculateRespiratoryRate((short)raw, this.poorSignal);
/*      */           }
/*      */ 
/* 2284 */           if ((TGDevice.this.difficultyEnabled) || (TGDevice.this.familiarityEnabled))
/*      */           {
/* 2360 */             this.tf_td_control.updateTF_TD((short)raw, this.poorSignal);
/*      */           }
/*      */ 
/* 2364 */           index += valueLength;
/* 2365 */           break;
/*      */         case 131:
/* 2368 */           if (valueLength == 24) {
/* 2369 */             int delta = getEEGPowerValue(this.payload[index], this.payload[(index + 1)], this.payload[(index + 2)]);
/* 2370 */             int theta = getEEGPowerValue(this.payload[(index + 3)], this.payload[(index + 4)], this.payload[(index + 5)]);
/* 2371 */             int lowAlpha = getEEGPowerValue(this.payload[(index + 6)], this.payload[(index + 7)], this.payload[(index + 8)]);
/* 2372 */             int highAlpha = getEEGPowerValue(this.payload[(index + 9)], this.payload[(index + 10)], this.payload[(index + 11)]);
/* 2373 */             int lowBeta = getEEGPowerValue(this.payload[(index + 12)], this.payload[(index + 13)], this.payload[(index + 14)]);
/* 2374 */             int highBeta = getEEGPowerValue(this.payload[(index + 15)], this.payload[(index + 16)], this.payload[(index + 17)]);
/* 2375 */             int lowGammma = getEEGPowerValue(this.payload[(index + 18)], this.payload[(index + 19)], this.payload[(index + 20)]);
/* 2376 */             int midGamma = getEEGPowerValue(this.payload[(index + 21)], this.payload[(index + 22)], this.payload[(index + 23)]);
/*      */ 
/* 2378 */             TGEegPower data = new TGEegPower(delta, theta, lowAlpha, highAlpha, lowBeta, highBeta, lowGammma, midGamma);
/* 2379 */             this.handler.obtainMessage(131, data).sendToTarget();
/* 2380 */             if (TGDevice.this.logEnabled) {
/* 2381 */               TGDevice.access$2200(TGDevice.this, "83 18 " + String.format("%02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X %02X", new Object[] { Byte.valueOf(this.payload[index]), Byte.valueOf(this.payload[(index + 1)]), Byte.valueOf(this.payload[(index + 2)]), Byte.valueOf(this.payload[(index + 3)]), Byte.valueOf(this.payload[(index + 4)]), Byte.valueOf(this.payload[(index + 5)]), Byte.valueOf(this.payload[(index + 6)]), Byte.valueOf(this.payload[(index + 7)]), Byte.valueOf(this.payload[(index + 8)]), Byte.valueOf(this.payload[(index + 9)]), Byte.valueOf(this.payload[(index + 10)]), Byte.valueOf(this.payload[(index + 11)]), Byte.valueOf(this.payload[(index + 12)]), Byte.valueOf(this.payload[(index + 13)]), Byte.valueOf(this.payload[(index + 14)]), Byte.valueOf(this.payload[(index + 15)]), Byte.valueOf(this.payload[(index + 16)]), Byte.valueOf(this.payload[(index + 17)]), Byte.valueOf(this.payload[(index + 18)]), Byte.valueOf(this.payload[(index + 19)]), Byte.valueOf(this.payload[(index + 20)]), Byte.valueOf(this.payload[(index + 21)]), Byte.valueOf(this.payload[(index + 22)]), Byte.valueOf(this.payload[(index + 23)]) }));
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2388 */           index += valueLength;
/* 2389 */           break;
/*      */         case 144:
/* 2392 */           (
/* 2394 */             m = new TGRawMulti()).ch1 = 
/* 2394 */             ((this.payload[index] << 8) + this.payload[(index + 1)]);
/* 2395 */           if (m.ch1 > 32768) m.ch1 -= 65536;
/*      */ 
/* 2397 */           if (valueLength > 3) {
/* 2398 */             m.ch2 = ((this.payload[(index + 3)] << 8) + this.payload[(index + 4)]);
/* 2399 */             if (m.ch2 > 32768) m.ch2 -= 65536;
/*      */ 
/* 2401 */             if (valueLength > 6) {
/* 2402 */               m.ch3 = ((this.payload[(index + 6)] << 8) + this.payload[(index + 7)]);
/* 2403 */               if (m.ch3 > 32768) m.ch3 -= 65536;
/*      */ 
/* 2405 */               if (valueLength > 9) {
/* 2406 */                 m.ch4 = ((this.payload[(index + 9)] << 8) + this.payload[(index + 10)]);
/* 2407 */                 if (m.ch4 > 32768) m.ch4 -= 65536;
/*      */ 
/* 2409 */                 if (valueLength > 12) {
/* 2410 */                   m.ch5 = ((this.payload[(index + 12)] << 8) + this.payload[(index + 13)]);
/* 2411 */                   if (m.ch5 > 32768) m.ch5 -= 65536;
/*      */ 
/* 2413 */                   if (valueLength > 15) {
/* 2414 */                     m.ch6 = ((this.payload[(index + 15)] << 8) + this.payload[(index + 16)]);
/* 2415 */                     if (m.ch6 > 32768) m.ch6 -= 65536;
/*      */ 
/* 2417 */                     if (valueLength > 18) {
/* 2418 */                       m.ch7 = ((this.payload[(index + 18)] << 8) + this.payload[(index + 19)]);
/* 2419 */                       if (m.ch7 > 32768) m.ch7 -= 65536;
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/* 2427 */           this.handler.obtainMessage(145, m).sendToTarget();
/*      */ 
/* 2437 */           break;
/*      */         case 177:
/* 2441 */           (
/* 2442 */             m = new TGRawMulti()).timeStamp1 = 
/* 2442 */             ((short)this.payload[index] & 0xFF);
/*      */ 
/* 2444 */           m.ch1 = (short)((this.payload[(index + 1)] << 8) + this.payload[(index + 2)]);
/* 2445 */           if (m.ch1 > 32768) m.ch1 -= 65536;
/*      */ 
/* 2447 */           m.ch2 = (short)((this.payload[(index + 3)] << 8) + this.payload[(index + 4)]);
/* 2448 */           if (m.ch2 > 32768) m.ch2 -= 65536;
/*      */ 
/* 2450 */           m.ch3 = ((this.payload[(index + 5)] << 8) + this.payload[(index + 6)]);
/* 2451 */           if (m.ch3 > 32768) m.ch3 -= 65536;
/*      */ 
/* 2453 */           m.ch4 = ((this.payload[(index + 7)] << 8) + this.payload[(index + 8)]);
/* 2454 */           if (m.ch4 > 32768) m.ch4 -= 65536;
/*      */ 
/* 2456 */           m.ch5 = ((this.payload[(index + 9)] << 8) + this.payload[(index + 10)]);
/* 2457 */           if (m.ch5 > 32768) m.ch5 -= 65536;
/*      */ 
/* 2459 */           m.ch6 = ((this.payload[(index + 11)] << 8) + this.payload[(index + 12)]);
/* 2460 */           if (m.ch6 > 32768) m.ch6 -= 65536;
/*      */ 
/* 2462 */           m.ch7 = ((this.payload[(index + 13)] << 8) + this.payload[(index + 14)]);
/* 2463 */           if (m.ch7 > 32768) m.ch7 -= 65536;
/*      */ 
/* 2465 */           m.ch8 = ((this.payload[(index + 15)] << 8) + this.payload[(index + 16)]);
/* 2466 */           if (m.ch8 > 32768) m.ch8 -= 65536;
/*      */ 
/* 2468 */           this.handler.obtainMessage(177, m).sendToTarget();
/* 2469 */           break;
/*      */         case 178:
/* 2474 */           if (this.dualchannel_raw_data++ == 0)
/* 2475 */             Log.v("TGParser", "dual channel raw data: ignoring ");
/* 2476 */           else if (this.dualchannel_raw_data >= 150000) {
/* 2477 */             this.dualchannel_raw_data = 0;
/*      */           }
/*      */ 
/* 2480 */           (
/* 2482 */             m = new TGRawMulti()).ch1 = 
/* 2482 */             ((this.payload[index] << 8) + this.payload[(index + 1)]);
/* 2483 */           if (m.ch1 > 32768) m.ch1 -= 65536;
/*      */ 
/* 2485 */           if (valueLength > 2) {
/* 2486 */             m.ch2 = ((this.payload[(index + 2)] << 8) + this.payload[(index + 3)]);
/* 2487 */             if (m.ch2 > 32768) m.ch2 -= 65536;
/*      */ 
/* 2489 */             if (valueLength > 4) {
/* 2490 */               m.ch3 = ((this.payload[(index + 4)] << 8) + this.payload[(index + 5)]);
/* 2491 */               if (m.ch3 > 32768) m.ch3 -= 65536;
/*      */ 
/* 2493 */               if (valueLength > 6) {
/* 2494 */                 m.ch4 = ((this.payload[(index + 6)] << 8) + this.payload[(index + 7)]);
/* 2495 */                 if (m.ch4 > 32768) m.ch4 -= 65536;
/*      */ 
/* 2497 */                 if (valueLength > 8) {
/* 2498 */                   m.ch5 = ((this.payload[(index + 8)] << 8) + this.payload[(index + 9)]);
/* 2499 */                   if (m.ch5 > 32768) m.ch5 -= 65536;
/*      */ 
/* 2501 */                   if (valueLength > 10) {
/* 2502 */                     m.ch6 = ((this.payload[(index + 10)] << 8) + this.payload[(index + 11)]);
/* 2503 */                     if (m.ch6 > 32768) m.ch6 -= 65536;
/*      */ 
/* 2505 */                     if (valueLength > 12) {
/* 2506 */                       m.ch7 = ((this.payload[(index + 12)] << 8) + this.payload[(index + 13)]);
/* 2507 */                       if (m.ch7 > 32768) m.ch7 -= 65536;
/*      */ 
/* 2509 */                       if (valueLength > 14) {
/* 2510 */                         m.ch8 = ((this.payload[(index + 14)] << 8) + this.payload[(index + 15)]);
/* 2511 */                         if (m.ch8 > 32768) m.ch8 -= 65536;
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/* 2519 */           this.handler.obtainMessage(178, m).sendToTarget();
/*      */ 
/* 2521 */           if ((TGDevice.this.difficultyEnabled) || (TGDevice.this.familiarityEnabled))
/*      */           {
/* 2547 */             int poorSignal_adjusted = this.poorSignal;
/* 2548 */             if ((TGDevice.this.isBMD100) || (TGDevice.this.isBMD101))
/*      */             {
/* 2552 */               if (this.poorSignal == 200) poorSignal_adjusted = 0;
/*      */             }
/* 2554 */             this.tf_td_control.updateTF_TD((short)m.ch1, poorSignal_adjusted);
/*      */           }
/*      */ 
/* 2558 */           index += valueLength;
/* 2559 */           break;
/*      */         case 8:
/* 2575 */           index += valueLength;
/* 2576 */           break;
/*      */         case 132:
/*      */         case 176:
/* 2580 */           index += valueLength;
/* 2581 */           break;
/*      */         case 133:
/* 2595 */           index += valueLength;
/* 2596 */           break;
/*      */         case 179:
/* 2598 */           if (this.dualchannel_sig_quality++ == 0)
/* 2599 */             Log.v("TGParser", "dual channel signal quality: receiving");
/* 2600 */           else if (this.dualchannel_sig_quality >= 300) {
/* 2601 */             this.dualchannel_sig_quality = 0;
/*      */           }
/*      */ 
/* 2604 */           int signal1 = this.payload[index] & 0xFF;
/* 2605 */           int signal2 = this.payload[(index + 1)] & 0xFF;
/*      */ 
/* 2609 */           this.handler.obtainMessage(179, signal1, signal2).sendToTarget();
/*      */ 
/* 2611 */           if ((signal1 == 200) && (signal2 == 200)) {
/* 2612 */             this.poorSignal = 200;
/*      */           }
/*      */           else {
/* 2615 */             this.poorSignal = 0;
/*      */           }
/* 2617 */           index += valueLength;
/* 2618 */           break;
/*      */         default:
/* 2627 */           if (this.parser_err_log_suppression++ == 0) {
/* 2628 */             Log.v("TGParser", "Unknown code: " + m);
/*      */           }
/* 2630 */           else if (this.parser_err_log_suppression > 10) {
/* 2631 */             Log.v("TGParser", "10 Unknown code messages suppressed");
/* 2632 */             this.parser_err_log_suppression = 0;
/*      */           }
/*      */ 
/* 2635 */           index += valueLength;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private static int getEEGPowerValue(byte highOrderByte, byte middleOrderByte, byte lowOrderByte)
/*      */     {
/* 2653 */       return (highOrderByte << 16) + (middleOrderByte << 8) + lowOrderByte & 0xFFFFFF;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class StreamThread extends Thread
/*      */   {
/*      */     private TGDevice.TGParser parser;
/*      */     private InputStream input;
/*      */     private BufferedInputStream buff;
/*  969 */     private int availableBytes = 0;
/*      */     private byte[] buffer;
/*      */ 
/*      */     public StreamThread(InputStream i)
/*      */     {
/*  974 */       this.parser = new TGDevice.TGParser(TGDevice.this, TGDevice.this.myTGDevice, true);
/*  975 */       this.input = i;
/*  976 */       this.buff = new BufferedInputStream(this.input);
/*      */     }
/*      */ 
/*      */     public final void run() {
/*  980 */       int i = 0;
/*      */ 
/*  984 */       if (this.input == null)
/*      */       {
/*  986 */         Log.v("TGDevice", "UART ERROR");
/*      */       }
/*      */       try
/*      */       {
/*      */         do
/*      */         {
/*      */           int byte_cnt;
/*  991 */           if (TGDevice.this.getStart()) {
/*  992 */             this.availableBytes = this.buff.available();
/*      */ 
/* 1012 */             Log.i("TGDevice", "Stream received buffer length: " + String.valueOf(this.availableBytes));
/* 1013 */             this.buffer = new byte[this.availableBytes];
/* 1014 */             byte_cnt = this.buff.read(this.buffer, 0, this.availableBytes);
/* 1015 */             this.parser.parseByte(this.buffer, byte_cnt);
/*      */           }
/*      */ 
/*      */           try
/*      */           {
/* 1020 */             Thread.sleep(1L, 500000);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException2)
/*      */           {
/*      */             InterruptedException localInterruptedException1;
/* 1021 */             (
/* 1023 */               localInterruptedException1 = 
/* 1024 */               localInterruptedException2).printStackTrace();
/*      */           }
/*      */         }
/* 1026 */         while (TGDevice.this.getReadable());
/*      */ 
/* 1028 */         this.input.close();
/*      */ 
/* 1033 */         return;
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1032 */         TGDevice.this.setState(5);
/*      */       }
/*      */     }
/*      */ 
/*      */     public final void cancel()
/*      */     {
/* 1038 */       TGDevice.this.setReadable(false);
/*      */       try {
/* 1040 */         this.input.close();
/*      */       }
/*      */       catch (IOException localIOException2)
/*      */       {
/*      */         IOException localIOException1;
/* 1041 */         (
/* 1043 */           localIOException1 = 
/* 1044 */           localIOException2).printStackTrace();
/*      */       }
/* 1045 */       Log.v("TGDevice", "StreamThread ending");
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ConnectedThread extends Thread
/*      */   {
/*      */     private BluetoothSocket btSocket;
/*      */     private InputStream inStream;
/*      */     private TGDevice.TGParser parser;
/*      */ 
/*      */     public ConnectedThread(BluetoothSocket s, boolean rawEnabled)
/*      */     {
/*  872 */       this.btSocket = s;
/*      */ 
/*  874 */       this.parser = new TGDevice.TGParser(TGDevice.this, TGDevice.this.myTGDevice, rawEnabled);
/*      */     }
/*      */ 
/*      */     public final void run()
/*      */     {
/*  885 */       buffer = new byte[1024];
/*  886 */       int i = 0;
/*  887 */       Log.v("TGDevice", "Begin BT ConnectedThread: " + this.btSocket.getRemoteDevice().getAddress());
/*      */ 
/*  891 */       setName("TG-BT-ConnectedThread: " + this.btSocket.getRemoteDevice().getAddress());
/*      */       try
/*      */       {
/*  894 */         this.inStream = this.btSocket.getInputStream();
/*  895 */         TGDevice.access$502(TGDevice.this, this.btSocket.getOutputStream());
/*      */         do
/*      */         {
/*  898 */           if (!TGDevice.this.getStart())
/*      */           {
/*      */             continue;
/*      */           }
/*      */ 
/*  916 */           int buff_cnt = this.inStream.read(buffer, 0, buffer.length);
/*  917 */           this.parser.parseByte(buffer, buff_cnt);
/*      */         }
/*  919 */         while (TGDevice.this.getReadable());
/*      */ 
/*  921 */         this.inStream.close();
/*  922 */         TGDevice.this.output.close();
/*      */         try
/*      */         {
/*  930 */           this.btSocket.close();
/*      */ 
/*  932 */           return;
/*      */         } catch (IOException localIOException1) {
/*  934 */           return;
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException2)
/*      */       {
/*  925 */         TGDevice.this.setState(3);
/*      */         try
/*      */         {
/*  930 */           this.btSocket.close();
/*      */ 
/*  932 */           return; } catch (IOException localIOException3) { return;
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/*      */         try
/*      */         {
/*  930 */           this.btSocket.close(); } catch (IOException localIOException4) {
/*      */         }
/*      */       }
/*  932 */       throw buffer;
/*      */     }
/*      */ 
/*      */     public final void cancel()
/*      */     {
/*      */       try
/*      */       {
/*  943 */         Log.v("TGDevice", "ConnectedThreadEnding: " + this.btSocket.getRemoteDevice().getAddress());
/*  944 */         if (this.parser.respiratory != null) this.parser.respiratory.killThread();
/*      */ 
/*  947 */         if (this.parser.tf_td_control != null) this.parser.tf_td_control.killThread();
/*      */ 
/*  950 */         this.inStream.close();
/*  951 */         TGDevice.this.output.close();
/*  952 */         this.btSocket.close();
/*      */       }
/*      */       catch (IOException localIOException2)
/*      */       {
/*      */         IOException localIOException1;
/*  954 */         (
/*  956 */           localIOException1 = 
/*  957 */           localIOException2).printStackTrace();
/*      */       }
/*      */ 
/*  960 */       TGDevice.this.setReadable(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ConnectThread extends Thread
/*      */   {
/*  681 */     private int SCAN_TIMEOUT = 5000;
/*      */     private Set<BluetoothDevice> btDevices;
/*      */     private BluetoothSocket btSocket;
/*      */     private TGDevice.TGParser parser;
/*      */     private InputStream inStream;
/*      */     private int apiVersion;
/*      */     private int retry_needed;
/*      */ 
/*      */     public ConnectThread()
/*      */     {
/*  691 */       this.btDevices = devices;
/*  692 */       this.apiVersion = Integer.valueOf(Build.VERSION.SDK).intValue();
/*      */ 
/*  694 */       this.parser = new TGDevice.TGParser(TGDevice.this, null, false);
/*      */     }
/*      */ 
/*      */     public final void run()
/*      */     {
/*  699 */       Log.v("TGDevice", "Begin BT ConnectThread");
/*  700 */       setName("TG-BT-ConnectThread");
/*      */ 
/*  702 */       if (TGDevice.this.btAdapter.isDiscovering()) {
/*  703 */         TGDevice.this.btAdapter.cancelDiscovery();
/*      */       }
/*  705 */       if (this.btDevices.size() == 0) {
/*  706 */         TGDevice.this.setState(5);
/*  707 */         return;
/*      */       }
/*      */ 
/*  710 */       for (BluetoothDevice d : this.btDevices) {
/*  711 */         this.retry_needed = 0;
/*      */         do {
/*  713 */           Log.d("TGDevice", "BT connect attemp: " + d.getName() + ", " + d.getAddress());
/*      */           try
/*      */           {
/*  721 */             if ((this.apiVersion >= 5) && (this.apiVersion < 10))
/*  722 */               this.btSocket = d.createRfcommSocketToServiceRecord(TGDevice.uuid);
/*  723 */             else if (this.apiVersion >= 10)
/*  724 */               this.btSocket = d.createInsecureRfcommSocketToServiceRecord(TGDevice.uuid);
/*      */           } catch (IOException e) {
/*  726 */             Log.d("TGDevice", "Error creating socket: " + e);
/*  727 */             continue;
/*      */           }
/*      */           try
/*      */           {
/*  731 */             this.btSocket.connect();
/*      */           } catch (IOException localIOException1) {
/*  733 */             Log.d("TGDevice", "BT connect UNABLE: " + d.getName() + ", " + d.getAddress());
/*      */ 
/*  750 */             continue;
/*      */           }
/*      */           try
/*      */           {
/*  754 */             this.inStream = this.btSocket.getInputStream();
/*      */           }
/*      */           catch (IOException localIOException2) {
/*  757 */             Log.d("TGDevice", "Could not get input stream");
/*      */           }
/*  759 */           int pState = 0;
/*  760 */           byte[] buffer = new byte[1];
/*  761 */           long start = System.currentTimeMillis();
/*      */           do
/*      */           {
/*      */             try
/*      */             {
/*  774 */               if (this.inStream.available() > 0) {
/*  775 */                 this.inStream.read(buffer);
/*  776 */                 pState = this.parser.parseByte(buffer[0]);
/*      */               } else {
/*      */                 try {
/*  779 */                   Log.v("TGDevice", "BT connect: no bytes, sleep a second");
/*  780 */                   Thread.sleep(1000L);
/*      */                 }
/*      */                 catch (InterruptedException localInterruptedException2)
/*      */                 {
/*      */                   InterruptedException localInterruptedException1;
/*  781 */                   (
/*  783 */                     localInterruptedException1 = 
/*  784 */                     localInterruptedException2).printStackTrace();
/*      */                 }
/*      */               }
/*      */             }
/*      */             catch (IOException localIOException3) {
/*  788 */               Log.v("TGDevice", "BT pstate: exception");
/*      */             }
/*      */           }
/*  790 */           while ((pState < 5) && (System.currentTimeMillis() - start < this.SCAN_TIMEOUT));
/*      */ 
/*  792 */           if (pState == 5) {
/*  793 */             TGDevice.access$302(TGDevice.this, d);
/*  794 */             Log.d("TGDevice", "BT connect CNCTED: " + d.getName() + ", " + d.getAddress());
/*      */ 
/*  797 */             TGDevice.this.connected(this.btSocket);
/*      */ 
/*  799 */             return;
/*      */           }
/*  801 */           Log.d("TGDevice", "BT connect INVALD: " + d.getName() + ", " + d.getAddress() + " NOT working.");
/*  802 */           Log.d("TGDevice", "BT connect INVALD: no valid packet received within: " + this.SCAN_TIMEOUT + "ms");
/*  803 */           this.retry_needed += 1;
/*      */           try {
/*  805 */             this.btSocket.close(); } catch (IOException localIOException4) {
/*      */           }
/*      */         }
/*  807 */         while (this.retry_needed == 1);
/*      */       }
/*      */ 
/*  815 */       TGDevice.this.setState(4);
/*      */     }
/*      */ 
/*      */     public final void cancel()
/*      */     {
/*      */       try {
/*  821 */         if (this.btSocket != null) {
/*  822 */           this.btSocket.close();
/*      */         }
/*  824 */         return;
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Users\Chris\Desktop\ThinkGear SDK for Android(MTK)\libs\ThinkGearBase.jar
 * Qualified Name:     com.neurosky.thinkgear.TGDevice
 * JD-Core Version:    0.6.0
 */