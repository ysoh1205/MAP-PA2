package edu.skku.cs.pa2;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class MazeStatus {
    public int mazeSize;
    public int[][] mazeData;
    public PossibleWay[][] possibleWays;
    public Coordinate[] movements = new Coordinate[4];
    public boolean[] visited;
    public boolean hintFlag;

    public int[] predecessor;
    public ArrayList<Coordinate> queue = new ArrayList<Coordinate>();
    public ArrayList<ArrayList<Coordinate>> allPaths = new ArrayList<ArrayList<Coordinate>>();

    public int[][] getMazeData() {
        return mazeData;
    }

    public void setMazeData(int[][] mazeData) {
        this.mazeData = mazeData;
    }

    public PossibleWay[][] getPossibleWays() {
        return possibleWays;
    }

    public void generatePossibleWays() {
        hintFlag = true;
        for(int i=0; i<4; i++) {
            movements[i] = new Coordinate(0,0);
        }
        movements[0].x=-1; movements[0].y=0; //Up
        movements[1].x=1; movements[1].y=0; //Down
        movements[2].x=0; movements[2].y=-1; //Left
        movements[3].x=0; movements[3].y=1; //Right

        mazeSize = mazeData.length;

        predecessor = new int[mazeSize*mazeSize];
        visited = new boolean[mazeSize*mazeSize];
        for(int i=0; i<mazeSize*mazeSize; i++) visited[i] = false;

        possibleWays = new PossibleWay[mazeSize][mazeSize];

        for(int i=0; i<mazeSize; i++) {
            for(int j=0; j<mazeSize; j++) {
                possibleWays[i][j] = new PossibleWay();
                int stat = mazeData[i][j];
                possibleWays[i][j].RIGHT = stat%2==0 ? true : false;
                stat=stat/2;
                possibleWays[i][j].DOWN = stat%2==0 ? true : false;
                stat=stat/2;
                possibleWays[i][j].LEFT = stat%2==0 ? true : false;
                stat=stat/2;
                possibleWays[i][j].UP = stat%2==0 ? true : false;
                stat=stat/2;
            }
        }
    }

    public int BFS(int[] current) {
        queue.add(new Coordinate(current[0], current[1]));
        visited[current[0]*mazeSize + current[1]] = true;

        while(!queue.isEmpty()) {
            Coordinate popped = queue.remove(0);
            for(int i=0; i<4; i++) {
                int nextX;
                int nextY;
                switch (i) {
                    case 0:
                        nextX = popped.x + movements[i].x;
                        nextY = popped.y + movements[i].y;
                        if(nextX >= 0){
                            if (possibleWays[popped.x][popped.y].UP && !visited[mazeSize * nextX + nextY]) {
                                queue.add(new Coordinate(nextX, nextY));
                                visited[mazeSize * nextX + nextY] = true;
                                predecessor[mazeSize * nextX + nextY] = mazeSize * popped.x + popped.y;
                            }
                        }
                        break;
                    case 1:
                        nextX = popped.x + movements[i].x;
                        nextY = popped.y + movements[i].y;
                        if(nextX < mazeSize) {
                            if (possibleWays[popped.x][popped.y].DOWN && !visited[mazeSize * nextX + nextY]) {
                                queue.add(new Coordinate(nextX, nextY));
                                visited[mazeSize * nextX + nextY] = true;
                                predecessor[mazeSize * nextX + nextY] = mazeSize * popped.x + popped.y;
                            }
                        }
                        break;
                    case 2:
                        nextX = popped.x + movements[i].x;
                        nextY = popped.y + movements[i].y;
                        if(nextY >= 0){
                            if (possibleWays[popped.x][popped.y].LEFT && !visited[mazeSize * nextX + nextY]) {
                                queue.add(new Coordinate(nextX, nextY));
                                visited[mazeSize * nextX + nextY] = true;
                                predecessor[mazeSize * nextX + nextY] = mazeSize * popped.x + popped.y;
                            }
                        }
                        break;
                    case 3:
                        nextX = popped.x + movements[i].x;
                        nextY = popped.y + movements[i].y;
                        if(nextY < mazeSize){
                            if (possibleWays[popped.x][popped.y].RIGHT && !visited[mazeSize * nextX + nextY]) {
                                queue.add(new Coordinate(nextX, nextY));
                                visited[mazeSize * nextX + nextY] = true;
                                predecessor[mazeSize * nextX + nextY] = mazeSize * popped.x + popped.y;
                            }
                        }
                        break;
                }
            }
        }


        int hint = mazeSize*mazeSize-1;
        while(predecessor[hint]!=mazeSize*current[0]+current[1]){
            hint = predecessor[hint];
        }

        return hint;
    }
}
