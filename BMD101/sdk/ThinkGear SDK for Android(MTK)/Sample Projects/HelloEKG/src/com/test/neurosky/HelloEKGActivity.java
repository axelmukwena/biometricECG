package com.test.neurosky;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.neurosky.thinkgear.HeartRateAcceleration;
import com.neurosky.thinkgear.NeuroSkyHeartMeters;
import com.neurosky.thinkgear.TGDevice;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HelloEKGActivity extends Activity {
	BluetoothAdapter bluetoothAdapter;
	TGDevice tgDevice;
	
    int 						subjectContactQuality_last;
    int							subjectContactQuality_cnt;

	Button bt_connect,bt_disconnect,bt_clean;
	TextView tv_HeartRate,tv_HeartAge,tv_RespirationRate,tv_RelaxationLevel,tv_5minHeartAge,tv_rrInterval;
	TextView tv_Title;
	EditText et_age;	
	public int average_heartrate = 0;
	int len = 0;
	int tem_heartrate_difference = 0;
	int tem_sum = 0;//sum of heart rate difference
	int value = 0;//new point
	int tem_value = 0;
	private GraphicalView chart;
	private LinearLayout linear;
	private XYSeries hseries;
	//draw section
	XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	XYSeriesRenderer dxyrenderer = new XYSeriesRenderer(),hxyrenderer;
	 //make data store
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();      
    private int addX = -1, addY;    
    int[] xv = new int[50]; 
    int[] yv = new int[50]; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main);
        
        subjectContactQuality_last = -1; /* start with impossible value */
        subjectContactQuality_cnt = 200; /* start over the limit, so it gets reported the 1st time */

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
        	// Alert user that Bluetooth is not available
        	Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
        	///finish();
        	return;
        }else {
        	// create the TGDevice 
        	tgDevice = new TGDevice(bluetoothAdapter, handler);     	
        }
        
        bt_connect = (Button)findViewById(R.id.button1);
        bt_disconnect = (Button)findViewById(R.id.button2);
        bt_clean = (Button)findViewById(R.id.button3);
        tv_HeartRate = (TextView)findViewById(R.id.textView2);
        tv_HeartRate.setText("");
        tv_HeartAge = (TextView)findViewById(R.id.textView4);
        tv_HeartAge.setText("");
        tv_RespirationRate = (TextView)findViewById(R.id.textView6);
        tv_RespirationRate.setText("");
        tv_RelaxationLevel = (TextView)findViewById(R.id.textView8);
        tv_RelaxationLevel.setText("");
        tv_5minHeartAge = (TextView)findViewById(R.id.textView13);
        tv_5minHeartAge.setText("");
        tv_rrInterval = (TextView)findViewById(R.id.textView14);
        tv_rrInterval.setText( "" );
        tv_Title = (TextView)findViewById(R.id.textView11);
        tv_Title.setText( "NeuroSky: " + TGDevice.version + " " + TGDevice.build_title);

        et_age = (EditText)findViewById(R.id.editText1);

        
        bt_connect.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//clean screen
				tv_HeartRate.setText("");			
				tv_HeartAge.setText("");
				tv_RelaxationLevel.setText("");				
				tv_RespirationRate.setText("");
				tv_5minHeartAge.setText( "" );
				tv_rrInterval.setText( "" );
				//et_age.setText("");
				dataset.removeSeries(hseries);				
				hseries.clear();
				dataset.addSeries(hseries);
				chart.invalidate();
				
				//hide keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(HelloEKGActivity.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_age.getWindowToken(), 0);
				if(TextUtils.isEmpty(et_age.getText()))
            	{
            		Toast.makeText(HelloEKGActivity.this, "Please input age!", Toast.LENGTH_LONG).show();
            	}
				else
				{
					tgDevice.connect(true);
				}
								
			}
        });

        bt_disconnect.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tgDevice.close();
				//clean screen
				tv_HeartRate.setText("");			
				tv_HeartAge.setText("");
				tv_RelaxationLevel.setText("");				
				tv_RespirationRate.setText("");
				tv_5minHeartAge.setText( "" );
				tv_rrInterval.setText( "" );
				et_age.setText("");
				dataset.removeSeries(hseries);				
				hseries.clear();
				dataset.addSeries(hseries);
				chart.invalidate();
				
			}
        	
        });
        bt_clean.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tv_HeartRate.setText("");			
				tv_HeartAge.setText("");
				tv_RelaxationLevel.setText("");
				tv_RespirationRate.setText("");
				tv_5minHeartAge.setText( "" );
				tv_rrInterval.setText( "" );
				dataset.removeSeries(hseries);				
				hseries.clear();
				dataset.addSeries(hseries);
				chart.invalidate();
				
			}
        	
        });
        //setup the draw section        
        renderer.setPointSize(3);
        renderer.setZoomButtonsVisible(true);
        renderer.setShowGrid(true);
        renderer.setXAxisMax(50);
        renderer.setXAxisMin(0);
        renderer.setYAxisMax(150);
        renderer.setYAxisMin(0);
        renderer.setXLabels(10);
        renderer.setYLabels(10);
        renderer.setAxesColor(Color.BLACK);
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setApplyBackgroundColor(true);
        //set up heart rate
        hxyrenderer = new XYSeriesRenderer();
        hxyrenderer.setColor(Color.BLUE);
        hxyrenderer.setPointStyle(PointStyle.DIAMOND);
        renderer.addSeriesRenderer(hxyrenderer);
        hseries = new XYSeries("heartrate");
        dataset.addSeries(hseries);
        //setup the draw in screen
        linear = (LinearLayout)findViewById(R.id.linear1);
        chart = ChartFactory.getLineChartView(HelloEKGActivity.this, dataset, renderer);
        linear.addView(chart,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
    }
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
    //update live curve
    public void updateChart(XYSeries series,int newValue)
    {
    	dataset.removeSeries(series);
    	int length = series.getItemCount();
    	//only maximum 50 points
        if(length>=50)
        {
        	
        	for(int i = 0;i<length-1;i++)
        	{
        		xv[i] = (int)series.getX(i);
        		yv[i] = (int)series.getY(i+1);
        	}
        	series.clear();
        	addX = length-1; 
            addY = newValue;
            for(int j = 0;j<length-1;j++)
            {
            	series.add(xv[j], yv[j]);
            }
            series.add(addX, addY);
            dataset.addSeries(series); 
        }
        else
        {
        	addX = length;
        	addY = newValue;
            series.add(addX, addY);
            dataset.addSeries(series);
        }
        
        chart.invalidate(); 
    }
    /**
     * Handles messages from TGDevice
     */
    private final Handler handler = new Handler() {
        
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what) {
        	case TGDevice.MSG_MODEL_IDENTIFIED:
        		/*
        		 * now there is something connected,
        		 * time to set the configurations we need
        		 */
            	//tv.append("Model Identified\n");
            	Toast.makeText(HelloEKGActivity.this, "Model Identified",Toast.LENGTH_LONG).show();	
                tgDevice.setBlinkDetectionEnabled(true); // not allowed on EKG hardware, here to show the override message
                tgDevice.setRespirationRateEnable(true); 
        		break;

            case TGDevice.MSG_STATE_CHANGE:

                switch (msg.arg1) {
	                case TGDevice.STATE_IDLE:
	                    break;
	                case TGDevice.STATE_CONNECTING:	         	
	                	Toast.makeText(HelloEKGActivity.this, "Connecting",Toast.LENGTH_LONG).show();	                	
	                	break;		                    
	                case TGDevice.STATE_CONNECTED:
	                	Toast.makeText(HelloEKGActivity.this, "Connected",Toast.LENGTH_LONG).show();	                	
	                	tgDevice.start();
	                	tgDevice.inputAge = Integer.parseInt( et_age.getText().toString() );
	                	tgDevice.pass_seconds = 15;
	                    break;
	                case TGDevice.STATE_NOT_FOUND:
	                	Toast.makeText(HelloEKGActivity.this, "Can't find device",Toast.LENGTH_LONG).show();
	                	Toast.makeText(HelloEKGActivity.this, "Bluetooth devices must be paired 1st",Toast.LENGTH_LONG).show();
	                	break;
	                /*case TGDevice.STATE_NOT_PAIRED:
	                	tv.append("not paired\n");
	                	break;*/
	                case TGDevice.STATE_DISCONNECTED:
	                	Toast.makeText(HelloEKGActivity.this, "Disconnected",Toast.LENGTH_LONG).show();	             
                }

                break;
            case TGDevice.MSG_POOR_SIGNAL:
            	/* display signal quality when there is a change of state, or every 30 reports (seconds) */
            	if (subjectContactQuality_cnt >= 30 || msg.arg1 != subjectContactQuality_last) {
            		if (msg.arg1 == 200) { //200 is for BMD
	                	Toast.makeText(HelloEKGActivity.this, "SignalQuality: is Good",Toast.LENGTH_LONG).show();	
             		}
            		else {
	                	Toast.makeText(HelloEKGActivity.this, "SignalQuality: is POOR: " + msg.arg1,Toast.LENGTH_LONG).show();	
            		}
            		subjectContactQuality_cnt = 0;
            		subjectContactQuality_last = msg.arg1;
            	}
            	else subjectContactQuality_cnt++;
                break;
            case TGDevice.MSG_RAW_DATA:	  
                
            	break;
            case TGDevice.MSG_HEART_RATE:
            	tv_HeartRate.setText(msg.arg1+"");
            	updateChart(hseries,msg.arg1);
                break;
            case TGDevice.MSG_ATTENTION:

            	break;
            case TGDevice.MSG_MEDITATION:

            	break;
            case TGDevice.MSG_BLINK:
                
            	break;
            case TGDevice.MSG_RAW_COUNT:
                
            	break;
            case TGDevice.MSG_EKG_RRINT:
                tv_rrInterval.setText( msg.arg1+"");
            	break;
            case TGDevice.MSG_LOW_BATTERY:
            	Toast.makeText(getApplicationContext(), "Low battery!", Toast.LENGTH_SHORT).show();
            	break;
            case TGDevice.MSG_RAW_MULTI:

            	break;
            case TGDevice.MSG_RELAXATION:
            	tv_RelaxationLevel.setText(msg.arg1+"");
            	break;
            case TGDevice.MSG_RESPIRATION:
            	//print out about 64s after touching, then update per 10s
            	//Float r = (Float)msg.obj;
            	tv_RespirationRate.setText(String.valueOf(msg.obj));
            	//Toast.makeText(getApplicationContext(), "Resp Rate: "+String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
            	break;
            case TGDevice.MSG_HEART_AGE:
                tv_HeartAge.setText( msg.arg1+"" );
                break;
            case TGDevice.MSG_HEART_AGE_5MIN:
                tv_5minHeartAge.setText( msg.arg1+"" );
                break;
                
            case TGDevice.MSG_ERR_CFG_OVERRIDE:
                switch (msg.arg1) {
                case TGDevice.ERR_MSG_BLINK_DETECT:
                	Toast.makeText(getApplicationContext(), "Override: blinkDetect", Toast.LENGTH_SHORT).show();
                    break;
                case TGDevice.ERR_MSG_TASKFAMILIARITY:
                	Toast.makeText(getApplicationContext(), "Override: Familiarity", Toast.LENGTH_SHORT).show();
                    break;
                case TGDevice.ERR_MSG_TASKDIFFICULTY:
                	Toast.makeText(getApplicationContext(), "Override: Difficulty", Toast.LENGTH_SHORT).show();
                    break;
                case TGDevice.ERR_MSG_POSITIVITY:
                	Toast.makeText(getApplicationContext(), "Override: Positivity", Toast.LENGTH_SHORT).show();
                    break;
                case TGDevice.ERR_MSG_RESPIRATIONRATE:
                	Toast.makeText(getApplicationContext(), "Override: Resp Rate", Toast.LENGTH_SHORT).show();
                    break;
                default:
                	Toast.makeText(getApplicationContext(), "Override: code: "+msg.arg1+"", Toast.LENGTH_SHORT).show();
                    break;
                }
                break;
            case TGDevice.MSG_ERR_NOT_PROVISIONED:
                switch (msg.arg1) {
                case TGDevice.ERR_MSG_BLINK_DETECT:
                	Toast.makeText(getApplicationContext(), "No Support: blinkDetect", Toast.LENGTH_SHORT).show();
                    break;
                case TGDevice.ERR_MSG_TASKFAMILIARITY:
                	Toast.makeText(getApplicationContext(), "No Support: Familiarity", Toast.LENGTH_SHORT).show();
                    break;
                case TGDevice.ERR_MSG_TASKDIFFICULTY:
                	Toast.makeText(getApplicationContext(), "No Support: Difficulty", Toast.LENGTH_SHORT).show();
                    break;
                case TGDevice.ERR_MSG_POSITIVITY:
                	Toast.makeText(getApplicationContext(), "No Support: Positivity", Toast.LENGTH_SHORT).show();
                    break;
                case TGDevice.ERR_MSG_RESPIRATIONRATE:
                	Toast.makeText(getApplicationContext(), "No Support: Resp Rate", Toast.LENGTH_SHORT).show();
                    break;
                default:
                	Toast.makeText(getApplicationContext(), "No Support: code: "+msg.arg1+"", Toast.LENGTH_SHORT).show();
                    break;
                }
                break;
            default:
            	break;
        }
        }
    };
}
