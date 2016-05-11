package doubled.wifidirecttest.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.peak.salut.SalutDevice;

import java.util.ArrayList;

import doubled.wifidirecttest.R;

/**
 * Created by Irfan Septiadi Putra on 11/05/2016.
 */
public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SalutDevice> listDevice = new ArrayList<SalutDevice>();

    public ListAdapter(Context ctx, ArrayList<SalutDevice> salutDevices){
        this.context = ctx;
        this.listDevice = salutDevices;
    }


    @Override
    public int getCount() {

        return listDevice.size();
    }

    @Override
    public Object getItem(int position) {
        return listDevice.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_devices, parent, false);
            holder.txtNama = (TextView) convertView.findViewById(R.id.txtNameDevice);
            convertView.setTag(holder);
        }
        else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtNama.setText(listDevice.get(position).deviceName);
        return convertView;
    }


    public static class ViewHolder {
        private TextView txtNama;
    }
}
