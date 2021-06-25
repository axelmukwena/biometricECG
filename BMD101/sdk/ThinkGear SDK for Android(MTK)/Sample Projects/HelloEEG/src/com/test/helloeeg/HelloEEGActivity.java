package com.test.helloeeg;

import android.app.Activity;
import android.bluetooth.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.neurosky.thinkgear.*;

public class HelloEEGActivity extends Activity {
    
    private static final String TAG = "HelloEEG";

    BluetoothAdapter            bluetoothAdapter;
    TGDevice                    tgDevice;
    int 						subjectContactQuality_last;
    int							subjectContactQuality_cnt;

    final boolean               rawEnabled = true;
    
	double task_famil_baseline, task_famil_cur, task_famil_change;
	boolean task_famil_first;
	double task_diff_baseline, task_diff_cur, task_diff_change;
	boolean task_diff_first;

    ScrollView                  sv;
    TextView                    tv;
    Button                      b;


    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );
        
        sv = (ScrollView)findViewById( R.id.scrollView1 );
        tv = (TextView)findViewById( R.id.textView1 );
        tv.setText( "" );
        tv.append( "Android version: " + Integer.valueOf(android.os.Build.VERSION.SDK) + "\n" );
        
        subjectContactQuality_last = -1; /* start with impossible value */
        subjectContactQuality_cnt = 200; /* start over the limit, so it gets reported the 1st time */


        // Check if Bluetooth is available on the Android device
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if( bluetoothAdapter == null ) {            

        	// Alert user that Bluetooth is not available
        	Toast.makeText( this, "Bluetooth not available", Toast.LENGTH_LONG ).show();
        	//finish();
        	return;

        } else {
            
        	// create the TGDevice 	
        	tgDevice = new TGDevice(bluetoothAdapter, handler);
        } 

        tv.append("NeuroSky: " + TGDevice.version + " " + TGDevice.build_title);
        tv.append("\n" );
        
    	task_famil_baseline = task_famil_cur = task_famil_change = 0.0;
    	task_famil_first = true;
    	task_diff_baseline = task_diff_cur = task_diff_change = 0.0;
    	task_diff_first = true;
    	
        b = (Button)findViewById(R.id.button1);
        b.setOnClickListener( new OnClickListener(){

            public void onClick( View v ) {
                // TODO Auto-generated method stub
                tgDevice.connect( true );
            }
            
        } );
    } 
	/* end onCreate() */
    
  //turn off app when touch return button of phone
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event)
    {
    	if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
    	{
    		tgDevice.close();
    		this.finish();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        //if (!bluetoothAdapter.isEnabled()) {
          //  Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableIntent, 1);
        //}
    }

    @Override
    public void onPause() {
    	// tgDevice.close();
        super.onPause();
    }
    
    @Override
    public void onStop() {
        tgDevice.close();
        super.onStop();

    }
    
    @Override
    public void onDestroy() {
    	//tgDevice.close();
        super.onDestroy();
    }
    
    /**
     * Handles messages from TGDevice
     */
    final Handler handler = new Handler() {
        @Override
        public void handleMessage( Message msg ) {

            switch( msg.what ) {
            	case TGDevice.MSG_MODEL_IDENTIFIED:
            		/*
            		 * now there is something connected,
            		 * time to set the configurations we need
            		 */
                	tv.append("Model Identified\n");
                    tgDevice.setBlinkDetectionEnabled(true);
                    tgDevice.setTaskDifficultyRunContinuous(true);
                    tgDevice.setTaskDifficultyEnable(true);
                    tgDevice.setTaskFamiliarityRunContinuous(true);
                    tgDevice.setTaskFamiliarityEnable(true);
                    tgDevice.setRespirationRateEnable(true); /// not allowed on EEG hardware, here to show the override message
            		break;
            	
                case TGDevice.MSG_STATE_CHANGE:
    
                    switch( msg.arg1 ) {
    	                case TGDevice.STATE_IDLE:
    	                    break;
    	                case TGDevice.STATE_CONNECTING:       	
    	                	tv.append( "Connecting...\n" );
    	                	break;	
                        case TGDevice.STATE_CONNECTED:
                            tv.append( "Connected.\n" );
                            tgDevice.start();
                            break;
    	                case TGDevice.STATE_NOT_FOUND:
    	                	tv.append( "Could not connect to any of the paired BT devices.  Turn them on and try again.\n" );
    	                	tv.append( "Bluetooth devices must be paired 1st\n" );
    	                	break;
                        case TGDevice.STATE_ERR_NO_DEVICE:
                            tv.append( "No Bluetooth devices paired.  Pair your device and try again.\n" );
                            break;
    	                case TGDevice.STATE_ERR_BT_OFF:
    	                    tv.append( "Bluetooth is off.  Turn on Bluetooth and try again." );
    	                    break;

    	                case TGDevice.STATE_DISCONNECTED:
    	                	tv.append( "Disconnected.\n" );
                    } /* end switch on msg.arg1 */

                    break;
    
                case TGDevice.MSG_POOR_SIGNAL:
                	/* display signal quality when there is a change of state, or every 30 reports (seconds) */
                	if (subjectContactQuality_cnt >= 30 || msg.arg1 != subjectContactQuality_last) {
                		if (msg.arg1 == 0) tv.append( "SignalQuality: is Good: " + msg.arg1 + "\n" );
                		else tv.append( "SignalQuality: is POOR: " + msg.arg1 + "\n" );

                		subjectContactQuality_cnt = 0;
                		subjectContactQuality_last = msg.arg1;
                	}
                	else subjectContactQuality_cnt++;
                    break;
                
                case TGDevice.MSG_RAW_DATA:	  
                	/* Handle raw EEG/EKG data here */
                	break;

                
                case TGDevice.MSG_ATTENTION:
                    //tv.append( "Attention: " + msg.arg1 + "\n" );
                	break;
                
                case TGDevice.MSG_MEDITATION:
                    //tv.append( "Meditation: " + msg.arg1 + "\n" );
                	break;
                	
	            case TGDevice.MSG_EEG_POWER:
	            	TGEegPower e = (TGEegPower)msg.obj;
	            	tv.append("delta: " + e.delta + " theta: " + e.theta + " alpha1: " + e.lowAlpha + " alpha2: " + e.highAlpha + "\n");
	            	break;
	            	
                case TGDevice.MSG_FAMILIARITY:                    
                   	task_famil_cur = (Double) msg.obj;
                	if (task_famil_first) {
                		task_famil_first = false;
                	}
                	else {
                		/*
                		 * calculate the percentage change from the previous sample
                		 */
                		task_famil_change = calcPercentChange(task_famil_baseline,task_famil_cur);
                		if (task_famil_change > 500.0 || task_famil_change < -500.0 ) {
                			tv.append( "     Familiarity: excessive range\n" );
                			//Log.i( "familiarity: ", "excessive range" );
                		}
                		else {
                			tv.append( "     Familiarity: " + task_famil_change + " %\n" );
                			//Log.i( "familiarity: ", String.valueOf( task_famil_change ) + "%" );
                		}
                	}
                	task_famil_baseline = task_famil_cur;
                	break;
                case TGDevice.MSG_DIFFICULTY:
                   	task_diff_cur = (Double) msg.obj;
                	if (task_diff_first) {
                		task_diff_first = false;
                	}
                	else {
                		/*
                		 * calculate the percentage change from the previous sample
                		 */
                		task_diff_change = calcPercentChange(task_diff_baseline,task_diff_cur);
                		if (task_diff_change > 500.0 || task_diff_change < -500.0 ) {
                			tv.append( "     Difficulty: excessive range %\n" );
                			//Log.i("difficulty: ", "excessive range" );
                		}
                		else {
                			tv.append( "     Difficulty: " +  task_diff_change + " %\n" );
                			//Log.i( "difficulty: ", String.valueOf( task_diff_change ) + "%" );
                		}
                	}
                	task_diff_baseline = task_diff_cur;
                	break;
                	
                case TGDevice.MSG_ZONE:
	                switch (msg.arg1) {
	                case 3:
	                    tv.append( "          Zone: Elite\n" );
	                	break;
	                case 2:
	                    tv.append( "          Zone: Intermediate\n" );
	                	break;
	                case 1:
	                    tv.append( "          Zone: Beginner\n" );
	                	break;
	                default:
	                case 0:
	                    tv.append( "          Zone: relax and try to focus\n" );
	                	break;
                    }
                    break;
                    
                case TGDevice.MSG_BLINK:
                	tv.append( "Blink: " + msg.arg1 + "\n" );
                	break;
    
                case TGDevice.MSG_ERR_CFG_OVERRIDE:
                    switch (msg.arg1) {
                    case TGDevice.ERR_MSG_BLINK_DETECT:
                    	tv.append("Override: blinkDetect"+"\n");
                    	Toast.makeText(getApplicationContext(), "Override: blinkDetect", Toast.LENGTH_SHORT).show();
                        break;
                    case TGDevice.ERR_MSG_TASKFAMILIARITY:
                    	tv.append("Override: Familiarity"+"\n");
                    	Toast.makeText(getApplicationContext(), "Override: Familiarity", Toast.LENGTH_SHORT).show();
                        break;
                    case TGDevice.ERR_MSG_TASKDIFFICULTY:
                    	tv.append("Override: Difficulty"+"\n");
                    	Toast.makeText(getApplicationContext(), "Override: Difficulty", Toast.LENGTH_SHORT).show();
                        break;
                    case TGDevice.ERR_MSG_POSITIVITY:
                    	tv.append("Override: Positivity"+"\n");
                    	Toast.makeText(getApplicationContext(), "Override: Positivity", Toast.LENGTH_SHORT).show();
                        break;
                    case TGDevice.ERR_MSG_RESPIRATIONRATE:
                    	tv.append("Override: Resp Rate"+"\n");
                    	Toast.makeText(getApplicationContext(), "Override: Resp Rate", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    	tv.append("Override: code: "+msg.arg1+"\n");
                    	Toast.makeText(getApplicationContext(), "Override: code: "+msg.arg1+"", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    break;
                case TGDevice.MSG_ERR_NOT_PROVISIONED:
                    switch (msg.arg1) {
                    case TGDevice.ERR_MSG_BLINK_DETECT:
                    	tv.append("No Support: blinkDetect"+"\n");
                    	Toast.makeText(getApplicationContext(), "No Support: blinkDetect", Toast.LENGTH_SHORT).show();
                        break;
                    case TGDevice.ERR_MSG_TASKFAMILIARITY:
                    	tv.append("No Support: Familiarity"+"\n");
                    	Toast.makeText(getApplicationContext(), "No Support: Familiarity", Toast.LENGTH_SHORT).show();
                        break;
                    case TGDevice.ERR_MSG_TASKDIFFICULTY:
                    	tv.append("No Support: Difficulty"+"\n");
                    	Toast.makeText(getApplicationContext(), "No Support: Difficulty", Toast.LENGTH_SHORT).show();
                        break;
                    case TGDevice.ERR_MSG_POSITIVITY:
                    	tv.append("No Support: Positivity"+"\n");
                    	Toast.makeText(getApplicationContext(), "No Support: Positivity", Toast.LENGTH_SHORT).show();
                        break;
                    case TGDevice.ERR_MSG_RESPIRATIONRATE:
                    	tv.append("No Support: Resp Rate"+"\n");
                    	Toast.makeText(getApplicationContext(), "No Support: Resp Rate", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                    	tv.append("No Support: code: "+msg.arg1+"\n");
                    	Toast.makeText(getApplicationContext(), "No Support: code: "+msg.arg1+"", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    break;
                default:
                	break;
                	
        	} /* end switch on msg.what */
            
        	sv.fullScroll( View.FOCUS_DOWN );
        	
        } /* end handleMessage() */
        
    }; /* end Handler */
    
    private double calcPercentChange(double baseline, double current) {
    	double change;
    	
		if (baseline == 0.0) baseline = 1.0; //don't allow divide by zero
		/*
		 * calculate the percentage change
		 */
		change = current - baseline;
		change = (change / baseline) * 1000.0 + 0.5;
		change = Math.floor(change) / 10.0;
		return(change);
    }
    
    /**
     * This method is called when the user clicks on the "Connect" button.
     * 
     * @param view
     */
    public void doStuff(View view) {
    	if( tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED ) {
    	    
    		tgDevice.connect( rawEnabled );
    	}
    	
    } /* end doStuff() */
    
} /* end HelloEEGActivity() */
