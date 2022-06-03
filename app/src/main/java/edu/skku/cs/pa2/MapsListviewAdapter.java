package edu.skku.cs.pa2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.skku.cs.pa2.DataModel.MapInfo;

public class MapsListviewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MapInfo> mapInfos;

    public static final String EXT_MAZENAME = "Mazename";
    public static final String EXT_MAZESIZE = "Mazesize";

    public MapsListviewAdapter(Context mContext, ArrayList<MapInfo> mapInfos) {
        this.mContext = mContext;
        this.mapInfos = mapInfos;
    }

    @Override
    public int getCount() {
        return mapInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return mapInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.map_listview, viewGroup,false);
        }


        if(getCount()!=0) {
            TextView mazenameTextview = view.findViewById(R.id.mazenameTextview);
            TextView mazesizeTextview = view.findViewById(R.id.mazesizeTextview);
            Button startButton = view.findViewById(R.id.button_mazeStart);

            mazenameTextview.setText(mapInfos.get(i).getName());
            mazesizeTextview.setText(mapInfos.get(i).getSize());

            startButton.setOnClickListener(view1 -> {
                Intent intent = new Intent(mContext, MazeActivity.class);
                intent.putExtra(EXT_MAZENAME, mazenameTextview.getText().toString());
                intent.putExtra(EXT_MAZESIZE, mazesizeTextview.getText().toString());

                mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            });
        }

        return view;
    }
}
