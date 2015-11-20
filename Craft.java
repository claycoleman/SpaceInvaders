  


import java.awt.Image;
import java.awt.geom.Area;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Craft
{
    private String craft = "xwing.png";
    private int dy, y;
    private int missileNum = 3;
    private final int X = 20;
    private Image image,superImage,superrImage,death;
    private int width, height;
    private boolean visible, moreMissiles, justDied;
    private ArrayList<Missile> missiles;
    private int fireCount = 0;
    private boolean shouldPowerShot;
    public Craft() {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(craft));
        image = ii.getImage();
        ii = new ImageIcon(this.getClass().getResource("glow.png"));
        superImage = ii.getImage();
        ii = new ImageIcon(this.getClass().getResource("glow2.png"));
        superrImage = ii.getImage();
        ii = new ImageIcon(this.getClass().getResource("flicker.gif"));
        death = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
        missiles = new ArrayList<Missile>();
        visible = true;
        y = 60;
    }
    public void isShot(boolean shot) {
        justDied = shot;
        if(justDied)
        y = 60;
    }
    public boolean wasShot() {
        return justDied;
    }
    public void shouldPowerShoot() {
        shouldPowerShot = true;
    }
    public void dontPowerShoot() {
        shouldPowerShot = false;
    }
    public void setNumMiss(int num){
        missileNum = num;
        if(missileNum ==3)
            moreMissiles=false;
        else {
        fireCount = 0;
        moreMissiles = true;
    }
    }
        
        
    public void move(){
        y += dy;
        
        if(y < 1)
            y = 1;
        if (y > 370-height)
            y=370-height;
    }
    public ArrayList<Missile> getMissiles(){
        return missiles;
    }
    
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_SPACE)
            this.fire();
        if(key == KeyEvent.VK_Z)
            this.fire(y+3);
        if(key == KeyEvent.VK_X)
            this.fire(y+height-3);
        if(key == KeyEvent.VK_UP)
            dy = -3;
        if(key == KeyEvent.VK_DOWN)
            dy = 3;
    }
    public boolean isPowerShooting() {
        return shouldPowerShot;
    }
    public boolean isMultipleShooter() {
        return moreMissiles;
    }
    public void fire(){
        if (missiles.size() < missileNum) {
            fireCount++;
            if(fireCount >= 15) {
                missileNum = 3;
                moreMissiles = false;
            }
            
            Missile shot;
            if(fireCount%2==0)
                shot = new Missile(X + width/2+5, y + 3, moreMissiles);
            else
                shot = new Missile(X + width/2+5, y -3 + height, moreMissiles);
            if(shouldPowerShot) {
                shot.setPowerShot(true);
                shouldPowerShot = false;
            }
            missiles.add(shot);
            
            Board.initMusic("blaster1.wav", 0);
        }
    }
    
    public void fire(int loc){
        if (missiles.size() < missileNum) {
            fireCount++;
            if(fireCount >= 15) {
                missileNum = 3;
                moreMissiles = false;
            }
            
            Missile shot;
                shot = new Missile(X + width/2+5, loc, moreMissiles);
            if(shouldPowerShot) {
                shot.setPowerShot(true);
                shouldPowerShot = false;
            }
            missiles.add(shot);
            
            Board.initMusic("blaster1.wav", 0);
        }
    }
    
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_UP)
            dy = 0;
        if(key == KeyEvent.VK_DOWN)
            dy = 0;
    }
    
    public int getX(){
        return X;
    }
    
    public int getY(){
        return y;
    }
    
    public Image getImage(){
        if(shouldPowerShot)
        return superImage;
        else if (moreMissiles)
            return superrImage;
        else if(justDied)
            return death;
        else
        
        return image;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public boolean isVisible(){
        return visible;
    }
    
    public void setVisible(boolean visible){
        this.visible = visible;
    }
    
    public Area getBounds(){
        Area area = new Area();
        area.add(new Area(new Rectangle(X+5,y+16,35,8)));
        area.add(new Area(new Rectangle(X+6,y+2,8,height-4)));
        area.add(new Area(new Rectangle(X+6,y+2,26,2)));
        area.add(new Area(new Rectangle(X+6,y-4+height,26,2)));
        return area;
    }
}

