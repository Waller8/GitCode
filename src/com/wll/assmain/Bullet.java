package com.wll.assmain;

import com.wll.interf.*;
import org.itheima.game.CollsionUtils;
import org.itheima.game.DrawUtils;
import org.itheima.game.SoundUtils;

import java.io.IOException;

//子弹类
public class Bullet extends Element implements Destroyable{
    private int speed = 6;

    //定义四种子弹的方向
    private String path1 = "TankWar/res/img/bullet_u.gif";
    private String path2 = "TankWar/res/img/bullet_d.gif";
    private String path3 = "TankWar/res/img/bullet_l.gif";
    private String path4 = "TankWar/res/img/bullet_r.gif";

    //定义子弹的方向
    private Direction direction;
    private int blood = 1; //子弹的血量


    //    子弹的构造方法
    public Bullet(Tank mt) {
        int tx = mt.x;   //坦克的x坐标
        int ty = mt.y;   //坦克的y坐标
        int tw = mt.width;   //坦克的宽度
        int th = mt.height;   //坦克的高度
        this.direction = mt.direction;//子弹的方向与坦克方向一致

        //这里要根据坦克的方向去判断决定子弹的方向
        switch (mt.direction) {
            case UP:
                try {
                    int[] size = DrawUtils.getSize(path1);
                    //子弹的x y宽度与长度
                    this.width = size[0];
                    this.height = size[1];
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //根据计算得出子弹的x坐标,this代表子弹
                this.x = tx + (tw - this.width) / 2;   //子弹的x坐标是坦克的x坐标加上 坦克宽度减去子弹宽度的一半
                this.y = ty - this.height;    //子弹的y坐标是坦克的y坐标减去子弹的高度
                break;
            case DOWN:
                try {
                    int[] size = DrawUtils.getSize(path2);
                    //子弹的宽度与长度
                    this.width = size[0];
                    this.height = size[1];
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //根据计算得出子弹的x坐标,this代表子弹
                this.x = tx + (tw - this.width) / 2;   //子弹的x坐标是 坦克的x坐标加上 坦克宽度减去子弹宽度的一半
                this.y = ty + th;    //子弹的y坐标是 坦克的y坐标加上坦克的高度
                break;
            case LEFT:
                try {
                    int[] size = DrawUtils.getSize(path3);
                    //子弹的宽度与长度
                    this.width = size[0];
                    this.height = size[1];
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //根据计算得出子弹的x坐标,this代表子弹
                this.x = tx - this.width;
                this.y = ty + (th - this.height) / 2;
                break;
            case RIGHT:
                try {
                    int[] size = DrawUtils.getSize(path4);
                    //子弹的宽度与长度
                    this.width = size[0];
                    this.height = size[1];
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //根据计算得出子弹的x坐标,this代表子弹
                this.x = tx + tw;
                this.y = ty + (th - this.height) / 2;
                break;
        }

        try {
            SoundUtils.play("TankWar/res/snd/hit.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw() {
        //根据不同的方向,绘制不同的子弹
        switch (this.direction) {
            case UP:
                try {
                    DrawUtils.draw(path1, x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;  //break不能省略,因为case具有穿透性.
            case DOWN:
                try {
                    DrawUtils.draw(path2, x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case LEFT:
                try {
                    DrawUtils.draw(path3, x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case RIGHT:
                try {
                    DrawUtils.draw(path4, x, y);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /*
    子弹移动的方法
     */
    public void move() {

        switch (direction) {
            case UP:
                y -= speed;
                break;  //break不能省略
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
        }

    }

    @Override
    public boolean isDestroy() {
        if(x < 0 || x > Config.WIDTH  || y < 0 || y > Config.HEIGHT){
            return true;
        }
        return false;
    }


    public boolean cheakAttack(Hitable hit) {
        Element e = (Element)hit;
        //假设子弹是第一个矩形,坦克是第二个矩形
        int x1 = this.x;
        int y1 = this.y;
        int w1 = this.width;
        int h1 = this.height;

        int x2 = e.x;
        int y2 = e.y;
        int w2 = e.width;
        int h2 = e.height;

        boolean res = CollsionUtils.isCollsionWithRect(x1, y1, w1, h1, x2, y2, w2, h2);
        return res;
    }
}
