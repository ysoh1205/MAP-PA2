package edu.skku.cs.pa2.DataModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;

import edu.skku.cs.pa2.MazeActivity;
import edu.skku.cs.pa2.R;

public class MazeGridviewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> items;
    private int mazeSize;

    public int[] getUserCoordinate() {
        return userCoordinate;
    }

    public void setUserCoordinate(int[] userCoordinate) {
        this.userCoordinate = userCoordinate;
    }

    public int[] userCoordinate;
    public int hintCoordinate;

    public int getUserDirection() {
        return USER_DIRECTION;
    }

    public void setUserDirection(int userDirection) {
        USER_DIRECTION = userDirection;
    }

    public int USER_DIRECTION;
    int flag=0;

    public MazeGridviewAdapter(Context mContext, ArrayList<Integer> items, int mazeSize) {
        this.mContext = mContext;
        this.items = items;
        this.mazeSize = mazeSize;
        this.hintCoordinate = -1;
    }

    @Override
    public int getCount() { return items.size(); }

    @Override
    public Object getItem(int i) { return items.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.maze_gridview, viewGroup,false);
        }

        if(getCount()!=0) {
            if(flag==0) {
                flag=1;
                return view;
            }
            ImageView cellImage = view.findViewById(R.id.cellImage);

            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) viewGroup.getLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) cellImage.getLayoutParams();

            float total_px = layoutParams.width / mazeSize;
            float margin_px = total_px/14;
            int[] wall = setMaze(i);

            marginLayoutParams.setMarginStart((int)(wall[0]*margin_px));
            marginLayoutParams.setMarginEnd((int)(wall[2]*margin_px));
            marginLayoutParams.topMargin=(int)(wall[1]*margin_px);
            marginLayoutParams.bottomMargin = (int)(wall[3]*margin_px);

            marginLayoutParams.width = (int)((total_px) - (marginLayoutParams.getMarginStart()+marginLayoutParams.getMarginEnd()));
            marginLayoutParams.height = (int)((total_px) - (marginLayoutParams.topMargin+marginLayoutParams.bottomMargin));

            ImageView elementImage = view.findViewById(R.id.elementImage);
            if(i == mazeSize*mazeSize-1) {
                elementImage.setImageResource(R.drawable.goal);
            }
            if(i == hintCoordinate) {
                elementImage.setImageResource(R.drawable.hint);
            }
            if(i == userCoordinate[0]*mazeSize + userCoordinate[1]) {
                elementImage.setImageResource(R.drawable.user);
                elementImage.setRotation((float)USER_DIRECTION);
                if(i == hintCoordinate){
                    hintCoordinate = -1;
                }
            }
        }

        return view;
    }

    public int[] setMaze(int i) {
        int status = items.get(i);
        int[] arr = {0,0,0,0};

        if(status%2 == 1) arr[2] = 1;
        status = status/2;
        if(status%2 == 1) arr[3] = 1;
        status = status/2;
        if(status%2 == 1) arr[0] = 1;
        status = status/2;
        if(status%2 == 1) arr[1] = 1;

        return arr;
    }

}
