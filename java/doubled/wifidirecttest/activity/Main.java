package doubled.wifidirecttest.activity;

import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;

import doubled.wifidirecttest.R;


public class Main extends AppCompatActivity implements SalutDataCallback, View.OnClickListener{

    /*
        This simple activity demonstrates how to use the Salut library from a host and client perspective.
     */

    public static final String TAG = "SalutTestApp";
    public SalutDataReceiver dataReceiver;
    public SalutServiceData serviceData;
    public Salut network;
    public Button hostingBtn;
    public Button discoverBtn;
    public Button startBtn;
    public boolean isHost = false;
    public boolean isClient = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        hostingBtn = (Button) findViewById(R.id.btnHost);
        discoverBtn = (Button) findViewById(R.id.btnDiscover);
        startBtn = (Button) findViewById(R.id.btnStart);

        hostingBtn.setOnClickListener(this);
        discoverBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);

        dataReceiver = new SalutDataReceiver(this, this);
        /*Populate the details for our awesome service. */
        serviceData = new SalutServiceData("testAwesomeService", 60606, "HOST");

        /*Create an instance of the Salut class, with all of the necessary data from before.
        * We'll also provide a callback just in case a device doesn't support WiFi Direct, which
        * Salut will tell us about before we start trying to use methods.*/
        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                // wiFiFailureDiag.show();
                // OR
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDiscover:
                    if(!isHost && isClient == false){
                        setAsClient();
                        isClient = true;
                        discoverBtn.setText("Stop Client");
                    }else if(isClient){
                        isClient = false;
                        network.stopServiceDiscovery(true);
                        discoverBtn.setText("Start Client");
                    }

                break;
            case R.id.btnHost:
                if(!isClient && isHost == false){
                    setAsHost();
                    isHost = true;
                    hostingBtn.setText("Stop Client");
                }else if(isHost){
                    isHost = false;
                    network.stopNetworkService(true);
                    hostingBtn.setText("Start Client");
                }
                break;
            case R.id.btnStart:
                getList();
                break;
            default:
                break;
        }
    }

    private void setAsHost(){
        network.startNetworkService(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice salutDevice) {
                Log.w(TAG, salutDevice.readableName + " has connected!");
                getList();
            }
        });
    }

    private void setAsClient(){
        network.discoverWithTimeout(
                new SalutCallback() {
                    @Override
                    public void call() {
                        Log.w(TAG, "Look at all these devices! " + network.foundDevices.toString());
                        getList();
                    }
                }, new SalutCallback() {
                    @Override
                    public void call() {
                        Log.w(TAG, "Bummer, we didn't find anyone. ");
                    }
                },10*1000);
    }


    @Override
    public void onDataReceived(Object o) {

    }

    public void getList(){
        if(network.isConnectedToAnotherDevice){
            Log.w("Device",""+network.foundDevices.size());
        }
    }
}

