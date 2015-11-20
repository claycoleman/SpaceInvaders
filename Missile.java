
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Rectangle;

public class Missile {
    
    private String source;
    private int dx, dy, x, y;
    private Image image,superImage;
    private int width, height;
    private boolean visible, dying;
    private boolean powerShot, multShot, speedy;
    private final int BOARD_WIDTH = 525;
    private  int missileSpeed = 3;
    
    public Missile(int x, int y, boolean mult){
        this.source = "shot2.png";
        ImageIcon ii = new ImageIcon(this.getClass().getResource(source));
        image = ii.getImage();
        ii = new ImageIcon(this.getClass().getResource("supershot.png"));
        superImage = ii.getImage();
        visible = true;
        dying = false;
        width = image.getWidth(null);
        height = image.getHeight(null);
        this.x = x;
        this.y = y;
        multShot = mult;
    }       
    
    public boolean isPowerShot() {
        return powerShot;
    }
    
    public boolean isMultShot() {
        return multShot;
    }
    
    public void setSpeedShot(boolean shot) {
        speedy=shot;
    }
    
    public void setPowerShot(boolean shot) {
        powerShot=speedy=shot;
    }
    
    public void move(){
        if(speedy)
            x+=9;
        else
            x+=3;
        if(x > BOARD_WIDTH)
            visible = false;
        
    }
    
    public void die() {
        visible = false;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public Image getImage(){
        if(powerShot) 
        return superImage;
        else
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
    
    public boolean isVisible(){
        return visible;
    }
    
    public void setVisible(boolean vis) {
        visible = vis;
    }
    
    public Rectangle getBounds(){
        return new Rectangle(x,y,width,height);
    }
}