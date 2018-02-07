package com.balckhao.blackonvif;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.balckhao.blackonvif.onvif.FindDevicesThread;
import com.balckhao.blackonvif.onvif.GetDeviceInfoThread;
import com.balckhao.blackonvif.onvifBean.Device;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FindDevicesThread.FindDevicesListener,
        GetDeviceInfoThread.GetDeviceInfoCallBack {

    private DevicesAdapter adapter;
    private ArrayList<Device> devices;
    //当前选中的标识
    private int selectedIndex = -1;

    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView1);
        //加载动画
        loadingFragment = new LoadingFragment();
        //设备列表
        devices = new ArrayList<>();
        adapter = new DevicesAdapter(this, devices);
        listView.setAdapter(adapter);
        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadingFragment.show(getSupportFragmentManager(), "loading");
                selectedIndex = position;
                new GetDeviceInfoThread(devices.get(position), MainActivity.this, MainActivity.this).start();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                loadingFragment.show(getSupportFragmentManager(), "loading");
                new FindDevicesThread(this, this).start();
                break;
        }
    }

    @Override
    public void searchResult(ArrayList<Device> devices) {
        this.devices.clear();
        this.devices.addAll(devices);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingFragment.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void getDeviceInfoResult(boolean isSuccess, String errorMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingFragment.dismiss();
            }
        });
        if (isSuccess) {
            //搜索成功打印数据
            Log.e("MainActivity", devices.get(selectedIndex).toString());
        }
    }
}
