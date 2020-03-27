package com.example.luigidarco.myfit.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luigidarco.myfit.R;
import com.example.luigidarco.myfit.callbacks.RecyclerItemClick;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BleListAdapter extends RecyclerView.Adapter<BleListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BluetoothDevice> dataset;
    private RecyclerItemClick callback;

    public BleListAdapter(ArrayList<BluetoothDevice> devices, Context context, RecyclerItemClick callback) {
        this.context = context;
        this.dataset = devices;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ble_devices_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BluetoothDevice device = dataset.get(position);
        if (device.getName() == null) {
            holder.name.setText("Unknown");
        } else {
            holder.name.setText(device.getName());
        }
        holder.address.setText(device.getAddress());
        holder.itemView.setOnClickListener(l -> {
            callback.onClickItem(device);
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ble_device_name);
            address = itemView.findViewById(R.id.ble_device_address);

        }
    }
}
