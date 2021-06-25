using System;

using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Reflection;

using System.IO;
using System.IO.Ports;

using NeuroSky.ThinkGear;
using NeuroSky.ThinkGear.Algorithms;

namespace testprogram {
    class Program {

        static Connector connector;

        static bool golfZoneDemo = true;
        
        public static void Main(string[] args) {

            Assembly assembly = System.Reflection.Assembly.GetExecutingAssembly();
            if (assembly != null)
            {
                object[] customAttribute1 = assembly.GetCustomAttributes(typeof(AssemblyTitleAttribute), false);
                if ((customAttribute1 != null) && (customAttribute1.Length > 0))
                    Console.WriteLine(((AssemblyTitleAttribute)customAttribute1[0]).Title);
                object[] customAttribute2 = assembly.GetCustomAttributes(typeof(AssemblyCompanyAttribute), false);
                if ((customAttribute2 != null) && (customAttribute2.Length > 0))
                    Console.WriteLine(((AssemblyCompanyAttribute)customAttribute2[0]).Company);
                Console.WriteLine(assembly.GetName().Version.ToString());
            }
            AppDomain MyDomain = AppDomain.CurrentDomain;
            Assembly[] AssembliesLoaded = MyDomain.GetAssemblies();

            foreach (Assembly MyAssembly in AssembliesLoaded)
            {
                if (MyAssembly.FullName.Contains("ThinkGear"))
                    Console.WriteLine(MyAssembly.FullName);
            }


            Console.WriteLine("----------");
            if (golfZoneDemo) Console.WriteLine("Hello Golfer!");
            else Console.WriteLine("Hello EEG!");
            Console.WriteLine("----------");

            // Initialize a new Connector and add event handlers
            connector = new Connector();
            connector.DeviceConnected += new EventHandler(OnDeviceConnected);
            connector.DeviceConnectFail += new EventHandler(OnDeviceFail);
            connector.DeviceValidating += new EventHandler(OnDeviceValidating);

            // Scan for devices
            // add this one to scan 1st
            connector.ConnectScan("COM40");

            //start the mental effort and task familiarity calculations
            if (golfZoneDemo)
            {
                connector.setMentalEffortEnable(false);
                connector.setTaskFamiliarityEnable(false);
                connector.setBlinkDetectionEnabled(false);
                connector.setPositivityEnable(true); /* for demo purposes */
            }
            else
            {
                connector.enableTaskDifficulty(); //depricated
                connector.enableTaskFamiliarity(); //depricated

                connector.setMentalEffortRunContinuous(true);
                connector.setMentalEffortEnable(true);
                connector.setTaskFamiliarityRunContinuous(true);
                connector.setTaskFamiliarityEnable(true);

                connector.setBlinkDetectionEnabled(true);
            }
            
            Thread.Sleep(8 * 60 * 1000); // time to live for this program (8 min * 60 sec * 1000 ms)

            Console.WriteLine("----------");
            if (golfZoneDemo) Console.WriteLine("Time is up. Goodbye from Golf Zone sample program!");
            else Console.WriteLine("Time is up. Goodbye from EEG sample program!");
            Console.WriteLine("----------");

            // Close all open connections
            connector.Close();

            Thread.Sleep(10 * 1000); // delay long enough for a human to see the message (10 sec * 1000 ms)

            Environment.Exit(0);
        }

       
        /**
         * Called when a device is connected 
         */
        static void OnDeviceConnected(object sender, EventArgs e) {
            Connector.DeviceEventArgs de = (Connector.DeviceEventArgs)e;

            Console.WriteLine("Device found on: " + de.Device.PortName);

            de.Device.DataReceived += new EventHandler(OnDataReceived);
        }

        /**
         * Called when scanning fails
         */
        static void OnDeviceFail(object sender, EventArgs e) {
            Console.WriteLine("No devices found! :(");
        }

        /**
         * Called when each port is being validated
         */
        static void OnDeviceValidating(object sender, EventArgs e) {
            Console.WriteLine("Validating: ");
        }

        static byte rcv_poorSignal_last = 255; // start with impossible value
        static byte rcv_poorSignal;
        static byte rcv_poorSig_cnt = 0;

        /**
         * Called when data is received from a device
         */
        static void OnDataReceived(object sender, EventArgs e) {
            //Device d = (Device)sender;
            Device.DataEventArgs de = (Device.DataEventArgs)e;
            DataRow[] tempDataRowArray = de.DataRowArray;

            TGParser tgParser = new TGParser();
            tgParser.Read(de.DataRowArray);
            
            /* Loop through new parsed data */
            for (int i = 0; i < tgParser.ParsedData.Length; i++)
            {
                if (tgParser.ParsedData[i].ContainsKey("MSG_MODEL_IDENTIFIED"))
                {
                    Console.WriteLine("Model Identified");

                    connector.setMentalEffortRunContinuous(false);
                    connector.setMentalEffortEnable(false);
                    connector.setTaskFamiliarityRunContinuous(false);
                    connector.setTaskFamiliarityEnable(false);
                    connector.setPositivityEnable(false);
                    connector.setBlinkDetectionEnabled(false);

                    //
                    // the following are included to demonstrate the overide messages
                    //
                    connector.setRespirationRateEnable(true); // not allowed with EEG
                }
                if (tgParser.ParsedData[i].ContainsKey("MSG_ERR_CFG_OVERRIDE"))
                {
                    Console.WriteLine("ErrorConfigurationOverride: " + tgParser.ParsedData[i]["MSG_ERR_CFG_OVERRIDE"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("MSG_ERR_NOT_PROVISIONED"))
                {
                    Console.WriteLine("ErrorModuleNotProvisioned: " + tgParser.ParsedData[i]["MSG_ERR_NOT_PROVISIONED"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("TimeStamp"))
                {
                    //Console.WriteLine("TimeStamp");
                } if (tgParser.ParsedData[i].ContainsKey("Raw"))
                {
                    //Console.WriteLine("Raw: " + tgParser.ParsedData[i]["Raw"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("RawCh1"))
                {
                    //Console.WriteLine("RawCh1: " + tgParser.ParsedData[i]["RawCh1"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("RawCh2"))
                {
                    //Console.Write(", Raw Ch2:" + tgParser.ParsedData[i]["RawCh2"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("PoorSignal"))
                {
                    // NOTE: this doesn't work well with BMD sensors Dual Headband or CardioChip

                    rcv_poorSignal = (byte)tgParser.ParsedData[i]["PoorSignal"];
                    if (rcv_poorSignal != rcv_poorSignal_last || rcv_poorSig_cnt >= 30)
                    {
                        // when there is a change of state OR every 30 reports
                        rcv_poorSig_cnt = 0; // reset counter
                        rcv_poorSignal_last = rcv_poorSignal;
                        if (rcv_poorSignal == 0)
                        {
                            // signal is good, we are connected to a subject
                            Console.WriteLine("SIGNAL: we have good contact with the subject");
                        }
                        else
                        {
                            Console.WriteLine("SIGNAL: is POOR: " + rcv_poorSignal);
                        }
                    }
                    else rcv_poorSig_cnt++;
                }
                if (tgParser.ParsedData[i].ContainsKey("Attention"))
                {
                    if (tgParser.ParsedData[i]["Attention"] != 0) Console.WriteLine("Attention : " + tgParser.ParsedData[i]["Attention"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("Attention 1"))
                {
                    if (tgParser.ParsedData[i]["Attention 1"] != 0) Console.WriteLine("Attention 1: " + tgParser.ParsedData[i]["Attention 1"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("Attention 2"))
                {
                    if (tgParser.ParsedData[i]["Attention 2"] != 0) Console.WriteLine("Attention 2: " + tgParser.ParsedData[i]["Attention 2"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("Meditation"))
                {
                    if (tgParser.ParsedData[i]["Meditation"] != 0) Console.WriteLine("Meditation: " + tgParser.ParsedData[i]["Meditation"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("Meditation 1"))
                {
                    if (tgParser.ParsedData[i]["Meditation 1"] != 0) Console.WriteLine("Meditation 1: " + tgParser.ParsedData[i]["Meditation 1"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("Meditation 2"))
                {
                    if (tgParser.ParsedData[i]["Meditation 2"] != 0) Console.WriteLine("Meditation 2: " + tgParser.ParsedData[i]["Meditation 2"]);
                }
                if (tgParser.ParsedData[i].ContainsKey("Positivity"))
                {
                    Console.WriteLine("\t\tPositivity: " + tgParser.ParsedData[i]["Positivity"]);
                }
                if (!golfZoneDemo) // turn this off for the Golf Zone Demo
                {
                    if (tgParser.ParsedData[i].ContainsKey("BlinkStrength"))
                    {
                        Console.WriteLine("\t\tBlinkStrength: " + tgParser.ParsedData[i]["BlinkStrength"]);
                    }
                    if (tgParser.ParsedData[i].ContainsKey("MentalEffort"))
                    {
                        Console.WriteLine("\t\tMental Effort: " + tgParser.ParsedData[i]["MentalEffort"]);
                    }

                    if (tgParser.ParsedData[i].ContainsKey("TaskFamiliarity"))
                    {
                        Console.WriteLine("\t\tTask Familiarity: " + tgParser.ParsedData[i]["TaskFamiliarity"]);
                    }
                }
                if (golfZoneDemo)
                {
                    if (tgParser.ParsedData[i].ContainsKey("ReadyZone"))
                    {
                        byte zone = (byte) tgParser.ParsedData[i]["ReadyZone"];
                        Console.Write("\t\tGolfZone: ");
                        switch (zone)
                        {
                            case 9:
                                Console.WriteLine("9: Elite: you are the best, putt when you are ready");
                                break;
                            case 8:
                                Console.WriteLine("8:");
                                break;
                            case 7:
                                Console.WriteLine("7:");
                                break;
                            case 6:
                                Console.WriteLine("6:");
                                break;
                            case 5:
                                Console.WriteLine("5: Intermediate: you are good, relax and putt smoothly");
                                break;
                            case 4:
                                Console.WriteLine("4:");
                                break;
                            case 3:
                                Console.WriteLine("3:");
                                break;
                            case 2:
                                Console.WriteLine("2:");
                                break;
                            case 1:
                                Console.WriteLine("1: Beginner: take a breath and don't rush");
                                break;
                            case 0:
                                Console.WriteLine("0: Try to relax, focus on your target");
                                break;
                            default:
                                Console.WriteLine("OOPS: zone value out of range: " + zone);
                                break;
                        }
                    }
                }
            }
        }
    }
}
