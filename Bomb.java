import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;
public class Bomb {

    private int dx, dy, x, y;
    private Image image,bomb,pic;
    private int width, height;
    private boolean destroyed, dying,speedy;
       
    private final int BOARD_WIDTH = 500;
    private  int missileSpeed = 1;
    
    public Bomb(int x, int y){
        ImageIcon ii = new ImageIcon(this.getClass().getResource("bomb.png"));
        bomb = ii.getImage();
        ii = new ImageIcon(getClass().getResource("explosion.png"));
        pic = ii.getImage();
        image = bomb;
        destroyed = true;
        dying = false;
        width = image.getWidth(null);
        height = image.getHeight(null);
        this.x = x;
        this.y = y;
    }       
    
    public void move() {
        if(x<0)
            destroyed = true;
        x-=missileSpeed;
    }
    public void setSpeedy() {
        speedy=true;
        missileSpeed = 4;
    }
    public void die() {
        destroyed = true;
    }
    
    public void toExpl() {
        image = pic;
    }
    
    public void toBomb() {
        image = bomb;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void setX(int x){
        this.x=x;
    }
    
    public void setY(int y){
        this.y=y;
    }
    
    public Image getImage(){
        return image;
    }
    
    public void setImage(Image ima) {
        image = ima;
    }
    
    public boolean isDying() {
        return dying;
    }
    
    public void setDying(boolean death) {
        dying = death;
    }
    
    public void setDestroyed(boolean dest) {
        destroyed = dest;
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    public Rectangle getBounds(){
        if (image.equals(bomb))
            return new Rectangle(x,y,width,height);
        else
            return new Rectangle(0,0,0,0);
    }
}
