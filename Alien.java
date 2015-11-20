import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Rectangle;

public class Alien
{
    private String alien = "alien.png";
    private int x, y, lives;
    private double dy=0;
    private Image image;
    private Image[] images = new Image[12];
    private int width, height;
    private boolean visible, dying;
    private boolean redraw;
    private Bomb bomb;
    private boolean boss = false;
    public Alien(int x, int y, int toughness){
        ImageIcon ii = new ImageIcon(this.getClass().getResource("alien.png"));
        images[1] = ii.getImage();
        ii = new ImageIcon(this.getClass().getResource("explosion.png"));
        images[0] = ii.getImage();
        ii = new ImageIcon(this.getClass().getResource("bogey.png"));
        images[2]=images[3]=ii.getImage();
        ii = new ImageIcon(this.getClass().getResource("fiver.png"));
        images[4]=images[5]=ii.getImage();
        ii = new ImageIcon(this.getClass().getResource("niner.png"));
        images[6]=images[7]=images[8]=images[9]=ii.getImage();
        ii = new ImageIcon(this.getClass().getResource("ghost.png"));
        images[10] = ii.getImage();
        ii = new ImageIcon(getClass().getResource("boss.png"));
        images[11]=ii.getImage();
        this.x = x;
        this.y = y;
        
        
        lives = toughness;
        if (lives == 30) {
            image = images[11];
            boss = true;
        }
        else
            image = images[lives];
        if (lives >= 9)
            redraw = false;
        else 
            redraw = true;
        visible = true;
        dying = false;
        width = image.getWidth(null);
        height = image.getHeight(null);
        
        if(boss) {
            bomb = new Bomb (this.x+10, this.y + getHeight()/2);
            bomb.setSpeedy();
        }
        else
            bomb = new Bomb (x,y);
    }
    
    public boolean isBoss() {
        return boss;
    }
    
    public Bomb getBomb() {
        return bomb;
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
    
    public int getBombX(){
        if(boss)
            return x+10;
        return x;
    }
    
    public int getBombY(){
        if(boss)
            return y+height/2;
        return y;
    }
    
    public void setX(int x){
        this.x=x;
    }
    
    public void setY(int y){
        this.y=y;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void act(double direction) {
        dy+=direction;
        if (Math.abs(dy)>2) {
            y+=direction;
            if(boss)
            y+=direction;
            dy = 0;
        }
    }
    
    public Image getImage(){
        if (lives == 0)             
            return images[0];
        else if(redraw)
            return images[lives];
        else {
            return image;
        }
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
    
    public void isShot() {
        lives--;
        if (lives < 0)
            lives = 0;
    }
    
    public int getLives() {
        return lives;
    }
    
    public Rectangle getBounds(){
        if (boss)
        return new Rectangle(x+33,y,15, 25);
        else
        return new Rectangle(x,y,width,height);
    }
}
