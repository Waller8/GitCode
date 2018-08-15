package com.wll.assmain;

import org.itheima.game.DrawUtils;

import java.io.IOException;

public class GrassWall extends Element {

    private String wallPath = "TankWar/res/img/grass.gif";

    //墙的构造方法
    public GrassWall(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            //通过文件的大小获取墙的宽度与高度
            int[] size = DrawUtils.getSize(wallPath);
            this.width = size[0];
            this.height = size[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(){
        try {
            DrawUtils.draw(wallPath,x,y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
