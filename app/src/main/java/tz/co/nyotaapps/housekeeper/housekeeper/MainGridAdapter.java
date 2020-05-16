package tz.co.nyotaapps.housekeeper.housekeeper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Valerio on 26/05/2016.
 */
public class MainGridAdapter extends ArrayAdapter<mObject> {

    Context mContext;
   ArrayList<mObject> mArray = new ArrayList<>();
    int sizex;

    public MainGridAdapter(Context context,ArrayList<mObject> objects, int size) {
        super(context,R.layout.single_gridview_item,objects);

        this.mContext = context;
        this.mArray = objects;
        this.sizex = size;

    }

    public class ViewHolder {

        TextView mText;
        ImageView mImage;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        mObject object = mArray.get(position);




        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_gridview_item, parent,false);
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, sizex));
            holder = new ViewHolder();
            holder.mText = (TextView) convertView.findViewById(R.id.url2);
            holder.mImage = (ImageView) convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);
        }else
        {
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, sizex));
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mText.setText(object.getCatUrl());
        //Picasso.with(mContext).load(object.getCatUrl()).fit().into(holder.mImage);
        //Picasso.get().load(url).placeholder(R.drawable.placeholder).into(thumbnail);
        Picasso.get().load(object.getCatUrl()).placeholder(R.drawable.placeholder).fit().into(holder.mImage);


        Log.w("TEST1" , "adapter " + object.getCatUrl());


        return convertView;

    }
}