package com.wll.assmain;

import com.wll.interf.Blockable;
import com.wll.interf.Destroyable;
import com.wll.interf.Hitable;
import org.itheima.game.DrawUtils;

import java.io.IOException;
/*
砖墙类:
     属性: 坐标,宽高,血量
     行为: 画
 */
public class BrickWall extends Element implements Blockable,Hitable,Destroyable{

    private int blood =3;
    private String wallPath = "TankWar/res/img/wall.gif";

//墙的构造方法
    public BrickWall(int x, int y) {
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

    public Blast showBlast() {
        blood --;
        return new Blast(x,y,width,height,blood);
    }


    @Override
    public boolean isDestroy() {
        return blood > 0 ? false : true;
    }
}
