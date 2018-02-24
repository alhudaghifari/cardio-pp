package com.urbannightdev.cardiopp.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.urbannightdev.cardiopp.Constant;
import com.urbannightdev.cardiopp.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePage extends AppCompatActivity {

    @BindView(R.id.rellay_device)
    RelativeLayout rellayDevice;
    @BindView(R.id.linlay_main)
    LinearLayout linlayMain;
    @BindView(R.id.ivGraph)
    ImageView ivGraph;
    @BindView(R.id.ivBluetooth)
    ImageView ivBluetooth;
    @BindView(R.id.ivSubscription)
    ImageView ivSubscription;
    @BindView(R.id.scrollview)
    ScrollView scrollViewGraph;
    @BindView(R.id.linlay_toolbar)
    LinearLayout linlayToolbar;

    private Toolbar myToolbar;
    private ActionBar aksibar;

    /** BLUETOOTH PAGE **/

    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = Constant.this_app;

    private static final String KEY_NAMA_PARTNER = "namapartner";
    private static final String KEY_TELEPON = "telepon";

    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;

    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter mAdapter;
    private ArrayAdapter mAdapterPair;
    private ArrayAdapter mAdapterSaranKesehatan;
    private Handler bluetoothIn;

    private String address = "";
    private int handlerState = 1;
    private boolean isBtConnected = false;
    private boolean isBtOn = false;
    private boolean isBtChecked = false;

    private ArrayList mArrayListPaired;
    private ArrayList mArrayListSearched;
    private ArrayList arrayListSaranKesehatan;
    private ProgressDialog progress;

    private LinearLayout layoutwarning;
    private Switch aSwitch;
    private Button btnSearchBluetooth;
    private ListView pairedDevicelist;
    private ListView searchedDevicelist;
    private ListView listViewSaranKesehatan;
    private TextView bluetoothTextInfo;
    private ProgressBar spinner;

    private boolean isSend;

    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /** BLUETOOTH PAGE **/

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.redkuat));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setVisibility(View.GONE);
        setSupportActionBar(myToolbar);

        ButterKnife.bind(this);

        aksibar = HomePage.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        barChart = (BarChart) findViewById(R.id.barchart);

        inisialisasiDataBluetoothAwal();
        checkBluetoothAvailability();
        initializeListener();

        initializeDataSaranKesehatan();

        visualizeHistogram();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                linlayMain.setVisibility(View.VISIBLE);
                rellayDevice.setVisibility(View.GONE);
                myToolbar.setVisibility(View.GONE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w("HomePage", " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("HomePage", " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w("HomePage", " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w("HomePage", " onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("FragmentBluetooth", " onDestroy");
        HomePage.this.unregisterReceiver(mReceiver);
    }

    private void visualizeHistogram() {
        List<BarEntry> barEntries = new ArrayList<>();

        Random rand = new Random();

        int n;
        int mod;

        for (int i=0;i<100;i++) {
            n = rand.nextInt(50) + 1;
            mod = rand.nextInt(30) + 1;

            if (mod%2 == 1)
                n *= -1;
            barEntries.add(new BarEntry(i, n));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "ECG");

        barDataSet.setColor(Color.RED);

        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);

        barChart.invalidate();

    }

    private void initializeListener() {
        ivGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollViewGraph.setVisibility(View.VISIBLE);
                rellayDevice.setVisibility(View.GONE);
                linlayToolbar.setBackgroundColor(Color.WHITE);
                ivGraph.setImageDrawable(getResources().getDrawable(R.drawable.icons8linechart48_clicked, null));
                ivBluetooth.setImageDrawable(getResources().getDrawable(R.drawable.icons8_bluetooth_48, null));
                ivSubscription.setImageDrawable(getResources().getDrawable(R.drawable.icons8_user_48, null));
            }
        });

        ivBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollViewGraph.setVisibility(View.GONE);
                rellayDevice.setVisibility(View.VISIBLE);
                linlayToolbar.setBackgroundColor(Color.parseColor("#b90000"));
                pairedDevicesList();
                ivGraph.setImageDrawable(getResources().getDrawable(R.drawable.icons8_line_chart_48_white, null));
                ivBluetooth.setImageDrawable(getResources().getDrawable(R.drawable.icons8_bluetooth_48_clicked, null));
                ivSubscription.setImageDrawable(getResources().getDrawable(R.drawable.icons8_user_48_white, null));
            }
        });

        btnSearchBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

//                bluetoothInputHandler();
//
//                Handler bluteHandler = new Handler();
//                bluteHandler.post(handlerBluetooth);

                if (myBluetooth.isEnabled())
                    searchNearestBluetooth();
                else
                    showToast(getResources().getString(R.string.please_blute));
            }
        });

        //attach a listener to check for changes in state
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){

                    myBluetooth.enable();
                    isBtChecked = true;

                }else{
                    myBluetooth.disable();
                }
            }
        });
    }

    /** BAGIAN BLUETOOTH **/

    /**
     * Inisialisasi data awal diperlukan
     */
    public void inisialisasiDataBluetoothAwal() {
        isSend = false;
        layoutwarning = (LinearLayout) findViewById(R.id.linearwarning);
        btnSearchBluetooth = (Button) findViewById(R.id.btnSearch);
        pairedDevicelist = (ListView) findViewById(R.id.listViewPair);
        searchedDevicelist = (ListView) findViewById(R.id.listViewSearch);
        listViewSaranKesehatan = (ListView) findViewById(R.id.listSaranKesehatan);
        bluetoothTextInfo = (TextView) findViewById(R.id.blute_text_info);
        aSwitch = (Switch) findViewById(R.id.switch_blute);
        spinner = (ProgressBar) findViewById(R.id.progressbar);
        spinner.setVisibility(View.GONE);

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        HomePage.this.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    /**
     * cek apakah bluetooth tersedia
     * jika tersedia maka cek untuk dinyalakan
     */
    public void checkBluetoothAvailability() {

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth == null) {
            //Show a mensag. that thedevice has no bluetooth adapter
            Toast.makeText(HomePage.this, "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
        }
        else {
            if (myBluetooth.isEnabled()) {
                aSwitch.setChecked(true);
                showWarning(false);
            } else {
                aSwitch.setChecked(false);
                showWarning(true);
            }
        }
    }

    /**
     * method ini berfungsi untuk mencari bluetooth aktif yang terdekat
     */
    private void searchNearestBluetooth() {
        mArrayListSearched = new ArrayList();
        //search bluetooth
        myBluetooth.startDiscovery();
    }


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("onBtReceive : ", "1");
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                Log.w("btReceiver : ", "action_state_changed");
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    isBtOn = true;
                    showWarning(false);
                    showToast("Enabled");
                    isBtChecked = false;
                    pairedDevicesList();
                } else {
                    isBtOn = false;
                    showWarning(true);
                }

                if (!isBtChecked)
                    aSwitch.setChecked(isBtOn);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.w("btReceiver : ", "ACTION_DISCOVERY_STARTED");

                spinner.setVisibility(View.VISIBLE);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.w("btReceiver : ", "ACTION_DISCOVERY_Finished");
                spinner.setVisibility(View.GONE);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.w("btReceiver : ", "ACTION_found");
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Add the name and address to an array adapter to show in a ListView
                mArrayListSearched.add(device.getName() + "\n" + device.getAddress());

                mAdapterPair = new ArrayAdapter(HomePage.this,android.R.layout.simple_list_item_1, mArrayListSearched);
                searchedDevicelist.setAdapter(mAdapterPair);
                searchedDevicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

                Log.d("deviceName : ", "" + device.getName());
            }
        }
    };

    private void initializeDataSaranKesehatan() {
        arrayListSaranKesehatan = new ArrayList();
        arrayListSaranKesehatan.add("Halo, aku Dr. Pepe, personal assistant heart care anda.");
        arrayListSaranKesehatan.add("Disini aku bakalan kasih saran seputar kondisi jantung anda");
        arrayListSaranKesehatan.add("Kita akan membantu mengetahui kondisimu melalui ECG yaa");
        arrayListSaranKesehatan.add("Cintai jantungmu, sayangi hidup kita :)");


        mAdapterSaranKesehatan = new ArrayAdapter(HomePage.this, android.R.layout.simple_list_item_1, arrayListSaranKesehatan);
        listViewSaranKesehatan.setAdapter(mAdapterSaranKesehatan);
        listViewSaranKesehatan.setFocusable(false);
    }

    /**
     * method ini untuk menampilkan bluetooth yang telah di pairing sebelumnya
     * ke tampilan paired device
     */
    private void pairedDevicesList() {
        if (myBluetooth.isEnabled()) {
            pairedDevices = myBluetooth.getBondedDevices();
            mArrayListPaired = new ArrayList();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt : pairedDevices) {
                    mArrayListPaired.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
                }
            } else {
                //Toast.makeText(FragmentBluetooth.this.getActivity(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
            }

            mAdapter = new ArrayAdapter(HomePage.this.getBaseContext(), android.R.layout.simple_list_item_1, mArrayListPaired);
            pairedDevicelist.setAdapter(mAdapter);
            pairedDevicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
        }
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            address = info.substring(info.length() - 17);
            // Make an intent to start next activity.
            startBluetooth();
        }
    };

    /**
     * startBluetooth adalah method untuk melakukan handling pada pesan bluetooth yang masuk
     * jika ada pesan masuk maka bluetoothInputHandler() akan dipanggil
     * kemudian setelah semua pesan masuk maka smartphone akan mengirimkan data berupa totCounter
     * untuk melakukan sinkronisasi terhadap device.
     */
    public void startBluetooth() {
        new ConnectBT().execute(); //Call the class to connect

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {

                String readMessage = (String) msg.obj;// msg.arg1 = bytes from connect thread
                Log.d("pesannya : ",readMessage);

                if (readMessage.equals("1")) {
                    bluetoothInputHandler(readMessage);
                    Handler bluteHandler = new Handler();
                    bluteHandler.post(handlerBluetooth);
                }
            }
        };
    }

    /**
     * Runnable ini berfunsi untuk komunikasi dengan FragmentGraph
     * hal yang dikomunikasikan adalah pengiriman tanggal hari ini serta
     * banyaknya penekanan tombol yang dilakukan hari ini.
     */
    Runnable handlerBluetooth = new Runnable() {
        @Override
        public void run() {
        }
    };


    private void bluetoothInputHandler(String messageBt) {
        String message = "Halo, saya butuh bantuan karena vertigo saya kumat. " +
                "Mohon bisa datang ke Thamrin Nine Jakarta";

        if (!isSend) {
            Context _context = HomePage.this;
            pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

            String notelp = pref.getString(KEY_TELEPON, "085642582286");

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(notelp, null, message, null, null);
            isSend = true;
            showToast("Message Sent To : " + notelp);
        }

    }


    private class ConnectBT extends AsyncTask<Void, Void, Void> { // UI thread{
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            //show a progress dialog
            progress = ProgressDialog.show(HomePage.this, "Connecting...", "Please wait!");
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                showToast("Connection Failed. Is it a SPP Bluetooth? Try again.");
            }
            else {
                showToast("Connected.");
                isBtConnected = true;
                ConnectedThread mConnectedThread;
                mConnectedThread = new ConnectedThread(btSocket);
                mConnectedThread.start();

                //I send a character when resuming.beginning transmission to check device is connected
                //If it is not an exception will be thrown in the write method and finish() will be called
                mConnectedThread.write("x");
            }
            progress.dismiss();
        }
    }


    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    String readMessage = "";
                    bytes = mmInStream.read(buffer); //read bytes from input buffer
                    readMessage = new String(buffer, 0, bytes);
                    Log.d("inipesan : ", readMessage);

                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        // send data to device by bluetooth
        public void write(String input) {
            Log.d("1berhasil kirim ",input + "");
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
                Log.d("2berhasil kirim ",msgBuffer + "");
            } catch (IOException e) {
                //if you cannot write, close the application
                e.printStackTrace();
                Log.e("Syncronize :","total counter failed");
            }
        }
    }

    /** BAGIAN BLUETOOTH **/


    /**
     * method ini untuk menampilkan toast agar pemanggilan lebih sederhana
     * @param s pesan
     */
    private void showToast(String s) {
        Toast.makeText(HomePage.this,s,Toast.LENGTH_LONG).show();
    }

    /**
     * Method ini berfungsi untuk menampilkan peringatan saat bluetooth dalam
     * keadaan aktif / non aktif
     * @param status input status keaktifan bluetooth
     */
    private void showWarning(boolean status) {
        if (status) {
            layoutwarning.setVisibility(View.VISIBLE);
        } else {
            layoutwarning.setVisibility(View.GONE);
        }
    }
}
