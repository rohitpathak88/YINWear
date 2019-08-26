package com.android.yinwear.ui.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.yinwear.R;
import com.android.yinwear.core.db.entity.DeviceDetail;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.RecyclerViewHolder> {

    private List<DeviceDetail> dataSource;

    public interface AdapterCallback {
        void onItemClicked(Integer menuPosition);
    }

    private AdapterCallback callback;

    private String drawableIcon;
    private Context context;


    public DevicesAdapter(Context context, List<DeviceDetail> dataArgs, AdapterCallback callback) {
        this.context = context;
        this.dataSource = dataArgs;
        this.callback = callback;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_devices, parent, false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        return recyclerViewHolder;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout menuContainer;
        TextView menuItem;
        TextView txtStatus;
        ImageView menuIcon;

        public RecyclerViewHolder(View view) {
            super(view);
            menuContainer = view.findViewById(R.id.menu_container);
            menuItem = view.findViewById(R.id.menu_item);
            menuIcon = view.findViewById(R.id.icon);
            txtStatus = view.findViewById(R.id.txt_status);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        DeviceDetail data_provider = dataSource.get(position);

        holder.menuItem.setText(data_provider.getName());
        holder.txtStatus.setText(data_provider.getPairing_status());
//        if ("UNPAIRED".equalsIgnoreCase(data_provider.getPairing_status())) {
//            holder.txtStatus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "device clicked", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        holder.menuIcon.setImageResource(data_provider.getImage());
//        if ("UNPAIRED".equalsIgnoreCase(data_provider.getPairing_status())) {
            holder.menuContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    if (callback != null) {
                        callback.onItemClicked(position);
                    }
                }
            });
//        }
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}
