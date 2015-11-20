 

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class MyBar {
    private int size, x, y, extr;
    private Image pic;
    private boolean direction, isMax;
    public MyBar(int x1, int y1, int iSize, int iExtr, boolean dir, boolean max){
        x = x1;
        y = y1;
        size = iSize;
        extr = iExtr;
        pic = new ImageIcon(this.getClass().getResource("Images/default.png")).getImage();
        direction = dir;
        isMax = max;
    }
    public MyBar(int x1, int y1, int iSize, int iExtr, Image incremental, boolean dir, boolean max){
        x = x1;
        y = y1;
        size = iSize;
        extr = iExtr;
        pic = incremental;
        direction = dir;
        isMax = max;
    }
    public boolean maxed(){
        return size==extr;
    }
    public int getSize(){
        return size;
    }
    public int getExtr(){
        return extr;
    }
    public void setSize(int newSize){
        if(isMax) {
         if(newSize <= extr)
            size = newSize;
        }
        else {
            if(newSize>=extr)
                size=newSize;
        }
    }
    public void paintBar(Graphics2D g){
        if(direction){
            int iY = y;
            for(int count = 1; count<=size; count++){
                g.drawImage(pic, x, iY, null);
                iY-=15;
            }
        }
        else{
            int iX = x;
            for(int count = 1; count<=size; count++){
                g.drawImage(pic, iX, y, null);
                iX+=15;
            }
        }
    }
    public void paintBar(Graphics2D g, int width, int height){
        if(direction){
            int iY = y;
            for(int count = 1; count<=size; count++){
                g.drawImage(pic, x, iY, width, height, null);
                iY-=20;
            }
        }
        else{
            int iX = x;
            for(int count = 1; count<=size; count++){
                g.drawImage(pic, iX, y, width, height, null);
                iX+=25;
            }
        }
    }
    
}
