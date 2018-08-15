package com.wll.assmain;

import com.wll.interf.Blockable;
import com.wll.interf.Config;
import com.wll.interf.Direction;
import com.wll.interf.Moveable;
import org.itheima.game.CollsionUtils;
import org.itheima.game.DrawUtils;

import java.io.IOException;
import java.util.Random;

/*
 * 应该将坦克定义为类(MyTank).
 * 属性: 坐标,宽,高,血量,移动速度,移动方向
 * 行为: 画(坦克炮口应随向转动),移动
 */
public class EnemyTank extends Tank implements Moveable,Blockable{
    private int blood = 5;  //坦克血量
    private int speed = 3; //坦克每次移动的距离

    public Direction baddir;   //记录坦克不能移动的方向.
    //定义四个坦克,炮口朝向不同.
    private String path1 = "TankWar/res/img/enemy_1_u.gif";
    private String path2 = "TankWar/res/img/enemy_1_d.gif";
    private String path3 = "TankWar/res/img/enemy_1_l.gif";
    private String path4 = "TankWar/res/img/enemy_1_r.gif";

       private long lastTime; //按键之后最后一颗发射子弹的时间
    private long nowTime; //将要按键发射子弹的时间
    private int minSpeed;//坦克移动的最小间距

    //坦克的构造方法
    public EnemyTank(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            //通过文件的大小获取墙的宽度与高度
            int[] size = DrawUtils.getSize(path1);
            this.width = size[0];
            this.height = size[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Direction getRandom() {
        int num = new Random().nextInt(4);
        switch (num) {
            case 0:
               return Direction.UP;
            case 1:
                return Direction.DOWN;
            case 2:
                return Direction.LEFT;
            case 3:
                return Direction.RIGHT;

        }

        return null;
    }

    //坦克移动的方法
    public void move() {
        //当坦克与墙发生碰撞,则再次按同方向键,不应该移动.
        if (direction == this.baddir) {
            //如果不能移动,那就移动最小间距
            this.direction  = getRandom();
            return;
        }

        //根据方向不同进行不同的移动.
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
        //坦克的移动不能超出边界.图片的左上角为坐标原点,向右为X正方向,向下为Y正方向
        if (x > Config.WIDTH - 64) {
            x = Config.WIDTH - 64;
            this.direction  = getRandom();
        }
        if (x < 0) {
            x = 0;
            this.direction  = getRandom();
        }
        if (y > Config.HEIGHT - 64) {
            y = Config.HEIGHT - 64;
            this.direction  = getRandom();
        }
        if (y < 0) {
            y = 0;
            this.direction  = getRandom();
        }

    }

    //绘制坦克的方法
    @Override
    public void draw() {
        //根据不同的方向,绘制不同的坦克

        //定义变量接收四个坦克的图片,根据不同的方向获取不同的图片
        String srcPath = "";
        switch (direction) {
            case UP:
                srcPath = path1;
                break;  //break不能省略,因为case具有穿透性.
            case DOWN:
                srcPath = path2;
                break;
            case LEFT:
                srcPath = path3;
                break;
            case RIGHT:
                srcPath = path4;
                break;
        }
        //捕获异常
        try {
            DrawUtils.draw(srcPath, x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //坦克发射子弹方法,将子弹类作为返回值
    public Bullet shoot() {
        //定义子弹的时间间隔,当时间间隔小于300,不创建子弹
        nowTime = System.currentTimeMillis();
        if (nowTime - lastTime < 300) {
            return null;
        } else {
            lastTime = nowTime; //将前时间赋值给最后时间
            //返回子弹类,this代表当前的坦克
            return new Bullet(this);
        }
    }

    //坦克与墙之间的阻挡功能
    public boolean cheakHit(Blockable block) {
        Element e = (Element)block;
        //假设坦克是第一个矩形,墙是第二个矩形
        int x1 = this.x;
        int y1 = this.y;
        int w1 = this.width;
        int h1 = this.height;

        switch (direction) {
            case UP:
                y1 -= speed;
                break;  //break不能省略,因为case具有穿透性.
            case DOWN:
                y1 += speed;
                break;
            case LEFT:
                x1 -= speed;
                break;
            case RIGHT:
                x1 += speed;
                break;
        }
        int x2 = e.x;
        int y2 = e.y;
        int w2 = e.width;
        int h2 = e.height;
        /*
        当speed不是64的倍数时,那么会产生移动不到位的情况.则还需要再次计算每次移动后产生的最小间距
        在碰撞前只需要移动最小间距即可
         */
        switch (direction) {
            case UP:
                minSpeed = this.y - (y2 + h2);
                break;  //break不能省略,因为case具有穿透性.
            case DOWN:
                minSpeed = y2 - (this.y + this.height);
                break;
            case LEFT:
                minSpeed = this.x - (x2 + w2);
                break;
            case RIGHT:
                minSpeed = x2 - (this.x + this.width);
                break;
        }
        /*
        调用CollsionUtils类的isCollsionWithRect()方法进行判断是否碰撞.
        此方法里包含等号,则会出现当墙与坦克产生交集时,才发生碰撞,但是我们应当认为只要两个边界重合就碰撞,
        则需要再做一个提前的预判断,判断下一步是否会碰撞.,需要给坦克的坐标按照方向进行移位.
         */
        boolean res = CollsionUtils.isCollsionWithRect(x1, y1, w1, h1, x2, y2, w2, h2);
        if (res) {
            //如果res为true,则记录当前方向为不能移动方向
            baddir = this.direction;
        } else {
            baddir = null;  //如果没有将baddir设为null,则会记录之前的方向,不论什么情况,那个方向都会失效.
        }
        return res;
    }


}



