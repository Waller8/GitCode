package com.wll.assmain;

import com.wll.interf.Destroyable;
import org.itheima.game.DrawUtils;
import org.itheima.game.SoundUtils;

import java.io.IOException;

public class Blast extends Element implements Destroyable{
    //爆炸效果类

    //定义数组存放爆炸效果图的路径
    private String[] arr = {
            "TankWar/res/img/blast_1.gif",
            "TankWar/res/img/blast_2.gif",
            "TankWar/res/img/blast_3.gif",
            "TankWar/res/img/blast_4.gif",
            "TankWar/res/img/blast_5.gif",
            "TankWar/res/img/blast_6.gif",
            "TankWar/res/img/blast_7.gif",
            "TankWar/res/img/blast_8.gif"
    };
    private int index = 0;
    private int len = arr.length;   //定义变量记录数组长度.
    private boolean flag = false;

    public Blast(int qx, int qy, int qw, int qh,int blood) {    //传入的是墙的坐标跟宽,高
        try {
            int[] size = DrawUtils.getSize("TankWar/res/img/blast_1.gif");
            this.width = size[0];
            this.height = size[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.x = qx - (this.width - qw) / 2;
        this.y = qy - (this.height - qh) / 2;

        if(blood > 0){
            len = 4;
        }

        try {
            SoundUtils.play("TankWar/res/snd/blast.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw() {
        try {
           if(index > len -1){
               index = 0;
               flag = true;
           }
            DrawUtils.draw(arr[index], x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
        index++;
    }

    public boolean isDestroy(){
        if (flag == true){
            return true;
        }
        return false;
    }

}
