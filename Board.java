import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.util.Random;
import java.awt.Container;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.AudioInputStream;

public class Board extends JPanel implements ActionListener 
{
    Timer timer =new Timer(10, this);  
    private Craft craft;
    private int consec;
    private final int CHANCE = 7;
    private int missedShots, aliensKilled ;
    private ArrayList<Alien> aliens;
    private boolean ingame, beginGame = false;
    private boolean powerShot, powerShotWriter, freeze, unlimShot, life;
    private boolean won = false;
    private boolean isPaused = false, isControlled = false;
    private int B_WIDTH, B_HEIGHT;
    private int level = 1, mult = 0;
    private int[][] pos;
    private Rectangle earth;
    private int score;
    private String message;
    private String song, msg = " ";
    private boolean shouldMove = false;
    private double direction = 1;
    private PowerUp power;
    private MyBar lives;
    private MyBar consecShots;
    private TAdapter listen = new TAdapter();
    private int powerCount = 0;
    private Scanner scoreReader;
    private int highscore;
    public Board(){
        setLayout(null);
        listen = new TAdapter();
        this.addFocusListener(listen);
        this.addKeyListener(listen);
        this.setFocusable(true);        
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        startGame();
        try {
            scoreReader = new Scanner(getClass().getResource("highscores.txt").openStream());
        }
        catch (Exception e) {
            System.out.println("no pos file");
            return;
        }
        highscore = scoreReader.nextInt();
    }
    
    public void startGame() {
        
        ingame = beginGame = false;
        direction =1;
        earth = new Rectangle (0,0,20,400);
        this.initAliens(level);
        craft = new Craft();
        lives = new MyBar(54, 409, 3, 0, craft.getImage(), false, false);
        consecShots = new MyBar(268, 414, 0, 4, new ImageIcon(getClass().getResource("shot2.png")).getImage(), false, true);
        
        timer.start();
    }
    
    public void init() {
        ingame = beginGame = true;
        
                    initMusic("counter.wav", 0);
        try {
            Thread.sleep(3500);
            timer.stop();
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
        direction =1;
        earth = new Rectangle (0,0,20,400);
        this.initAliens(level);
        craft = new Craft();
        lives = new MyBar(54, 409, 3, 0, craft.getImage(), false, false);
        consecShots = new MyBar(268, 414, 0, 4, new ImageIcon(getClass().getResource("shot2.png")).getImage(), false, true);
        timer.start();
    }
    
    public static synchronized void initMusic(final String url, final int count) {
    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
      public void run() {
        try {
          Clip clip = AudioSystem.getClip();
          AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(url));
          clip.open(inputStream);
          clip.loop(count);
        } catch (Exception e) {
          System.err.println(e.getMessage());
        }
      } 
    }).start();
   }
    
    public void initAliens(int level){
        
        powerShot= powerShotWriter= freeze= unlimShot = false;
        aliens = new ArrayList<Alien>();
        
        Scanner blueprint;
        try {
            blueprint = new Scanner(getClass().getResource("poslevel" + level + ".txt").openStream());
        }
        catch (Exception e) {
            System.out.println("no pos file");
            return;
        }
        int numAliens = blueprint.nextInt();
        pos = new int [numAliens][2];
        for (int als = 0; als < numAliens; als++) {
            pos[als][0] = blueprint.nextInt();
            pos[als][1] = blueprint.nextInt();
        }
        
        try {
            blueprint = new Scanner(getClass().getResource("alienslevel" + level + ".txt").openStream());
        }
        catch (Exception e) {
            System.out.println("no alien file");
            return;
        }
        song = blueprint.next();
        int numKinds = blueprint.nextInt();
        
        for (int first=0; first<numKinds; first++) {
            int numAliensOfKind = blueprint.nextInt();
            int toughness = blueprint.nextInt();
            
            for (int second=0; second<numAliensOfKind; second++) {
                numAliens--;
                aliens.add(new Alien(pos[numAliens][0],pos[numAliens][1],toughness));
            }
        }
    }
     
     
    public void addNotify(){
        super.addNotify();
        B_WIDTH = this.getWidth();
        B_HEIGHT = this.getHeight();
    }
    
    public void paint(Graphics g){
        super.paint(g);
        if(!beginGame) {
            g.setColor(Color.black);
        g.fillRect(0, 0, 530, 475);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, 475/2 - 30, 530-100, 50);
        g.setColor(Color.white);
        g.drawRect(50, 475/2 - 30, 530-100, 50);

        Font small = new Font("Futura", Font.PLAIN, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        String message = "Space Invaders! Press 'C' to learn the controls or 'R' to begin.";
        g.drawString(message, (530 - metr.stringWidth(message))/2, 
            475/2);
        }
        else 
        if(ingame) {
            Graphics2D g2d = (Graphics2D) g;  
            paintWorld(g2d);
            paintCraft(g2d);
            paintAliens(g2d);
            paintMissiles(g2d);
            paintBombs(g2d);
            if(power!=null && power.isVisible()) {
                paintPower(g2d);
            }
            
            g2d.setColor(Color.BLACK);
            drawStatus(g2d);
        }
        else {
            if (!won && !msg.equals("Invasion! Game Over"))
                msg = "Game Over.";
            Font small = new Font("Futura", Font.PLAIN, 14);
            FontMetrics metr = this.getFontMetrics(small);
            g.setColor(Color.WHITE);
            g.setFont(small);
            g.drawString(msg, 50,50);
            
            if (won&&level<=11) {
                win(g);
            }
            else
                endGame(g);
        }
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void drawStatus(Graphics2D g2d) {
            message="Score: " + score + "                 " +
                           "Level " + level+ "               Multiplier: " + (mult/2 +1);
                    Font small = new Font("Futura", Font.PLAIN, 14);
                    g2d.setFont(small);
            String bottomMessage = "Press C for Controls and P to Pause";
            if(level>11)
                bottomMessage= "Final Alien            BOSS";
               if(freeze) {
                   message+= "             Freeze!";
                }
                else if(powerShotWriter) {
                    message+="             Power Shot!";
                    for(Missile m: craft.getMissiles()) {
                        if(m.isPowerShot())
                            powerShotWriter = m.isVisible();
                    }
                }
                else if(unlimShot) {
                    message+="             Rapid Fire!";
                    unlimShot = craft.isMultipleShooter();
                }
                else if(life)  {
                    message+="             Extra Life!";
                }
            if(isPaused)
                message = "PAUSED. Press P to unpause.";
            else if(isControlled) {
                message = "  Space Bar ===> Blast        Up/Down Arrow Keys ===> Move        P ===> Pause";
                        
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0,380,600,200);
                
                g2d.setColor(Color.BLACK);
                g2d.drawString("   Z ===> Top Blaster           X ===> Bottom Blaster         R ===> Restart" , 5, 423);
            }
                else{
                g2d.drawString("Lives:                                        PowerUp: " , 5, 423);
                lives.paintBar(g2d, 22,20);
                consecShots.paintBar(g2d,18,10);
            }
                g2d.drawString(message, 5, 395);
            g2d.drawString(bottomMessage, 5, 455);
    }
    
    public void paintPower(Graphics2D g2) {
        g2.drawImage(power.getImage(),power.getX(),power.getY(), this);
    }
    
    public void paintWorld (Graphics2D g2d) {
        g2d.setColor(Color.green.darker().darker());
        g2d.fillRect(0,0,20,400);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0,380,600,200);
        if (freeze) 
            
        this.setBackground(new Color(116, 187, 251));
        else
            
        this.setBackground(Color.BLACK);
          
        g2d.setColor(Color.blue.darker().darker());
        g2d.fillOval(5,20,13,150);
        
        g2d.setColor(Color.gray.darker().darker());
        g2d.fillRect(3,320,50,30);
        g2d.setColor(Color.gray.darker());
        g2d.fillRect(3,310,30,20);
        if(freeze||life||craft.wasShot())
        powerCount++;
        if(powerCount>300&&life) {
            life = false;
            powerCount = 0;
        }
        if(powerCount>600&&freeze) {
            freeze = false;
            powerCount = 0;
        }
        if(powerCount>400&&craft.wasShot()) {
            craft.isShot(false);
            powerCount = 0;
        }
    }
    public void paintCraft (Graphics2D g2d) {
        if(craft.isVisible())
                g2d.drawImage(craft.getImage(),craft.getX(),craft.getY(),44,40, this);
    }
    
    public void paintMissiles (Graphics2D g2d) {
        ArrayList<Missile> ms = craft.getMissiles();
        for(int i = 0; i < ms.size();i++){
            Missile m = (Missile) ms.get(i);
            g2d.drawImage(m.getImage(), m.getX(), m.getY(), this);
        }
    }
    
    public void paintAliens (Graphics2D g2d) {        
        for(int i = 0;i < aliens.size();i++){
            Alien a = (Alien)aliens.get(i);
            g2d.drawImage(a.getImage(),a.getX(),a.getY(), this);
        }
    }
    
    public void paintBombs (Graphics2D g2d) {
        for(int i = 0;i < aliens.size();i++){
            Alien a = (Alien)aliens.get(i);
            Bomb b = a.getBomb();
            g2d.drawImage(b.getImage(),b.getX(),b.getY(), this);
        }
    }
    
    public void win(Graphics g) {
        ingame = true;
        direction = 1;
        craft.getMissiles().clear();
        level++;
        timer.stop();
        if (level <= 11) {
            initMusic("win.wav", 0);
        try {
            Thread.sleep(1000);
            timer.stop();
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
            timer.start();
            initAliens(level);
            if(level%3-1==0 && lives.getSize()<3)
                lives.setSize(lives.getSize()+1);
            freeze = powerShot = unlimShot = false;
            craft.setNumMiss(3);
            powerCount = 0;
            power = null;
        }
        else {
            Font big = new Font("Futura", Font.PLAIN, 68);
            FontMetrics metri = this.getFontMetrics(big);
            g.setColor(Color.WHITE);
            g.setFont(big);
            g.drawString("You won!", 100,200);
        }
    }
    
    public void endGame(Graphics g) {
        timer.stop();
        String mes = "You ";
        if (won) {
            mes += "beat the game! ";
        }
        else {
            if(level==11)
            mes+="got to the boss!";
            else
            mes += "got to level " + level + ". ";
            timer.stop();
        }
        craft.getMissiles().clear();
        initMusic("fail.wav", 0);
        mes += "You missed " + missedShots + " shots and killed " + aliensKilled + " aliens.";
        Font small = new Font("Futura", Font.PLAIN, 14);
        FontMetrics metr = this.getFontMetrics(small);
        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(mes, 50,350);
    }
    
    public void actionPerformed(ActionEvent e){
            
        if(aliens.size() == 0){
            ingame = false;
            won = true;
        }
        if(power!=null) {
            if(power.isVisible())
                power.move();
            else
                power = null;
        }
            
        ArrayList<Missile> ms = craft.getMissiles();
      
        for(int i = 0; i < ms.size();i++){
            Missile m = (Missile) ms.get(i);
            if(m.isVisible()) {
                m.move();
                
            if (m.isPowerShot()) {
                m.setSpeedShot(true);
            }
            }
            else {
                ms.remove(i);
                if(!m.isPowerShot())
                mult = 0;
                missedShots++;
                consec = 0;
                if(power==null)
                consecShots.setSize(0);
            }
        }
        Random generator = new Random();
        for(int i = 0; i < aliens.size();i++){
            Alien a = (Alien) aliens.get(i);            
            int rand = generator.nextInt(25);
            if (a.isDying()) {
                a.die();
                a.setDying(false);
            }
            else if(a.isVisible()) {
                 int y = a.getY();
    
                 if (y  >= 378 - a.getHeight() && direction != -1) {
                     direction = -1;
                     shouldMove = true;
                 }
    
                 if (y <= 5 && direction != 1) {
                     direction = 1;
                     shouldMove = true;
                 } 
            }
            else {
                aliens.remove(i);
                aliensKilled++;
                
            }
            Bomb b = a.getBomb();
            if (rand == CHANCE && b.isDestroyed()) {
                b.setDestroyed(false);
                b.toBomb();
                b.setX(a.getBombX());
                b.setY(a.getBombY());
            }
            if(!b.isDestroyed()) {
                b.move();
            }
            if(freeze) 
                b.setX(-100);
        }
        for(int j = 0; j < aliens.size();j++){
            if(!freeze) {Alien a2 = (Alien) aliens.get(j);
             a2.act(direction); }
         }
        if (shouldMove&&!freeze)
            aliensMove();
        craft.move();
        this.checkCollisions();
        repaint();
    }
    
    public void aliensMove() {
        for(int j = 0; j < aliens.size();j++){
            Alien a2 = (Alien) aliens.get(j);
            a2.setX(a2.getX() - 38);
         }
        shouldMove=false;
    }
    
    public void checkCollisions() {
        ArrayList<Missile> ms = craft.getMissiles();
        Area rc = craft.getBounds();
        for(int i = 0; i < aliens.size();i++){
            Alien a = (Alien) aliens.get(i);
            Rectangle ra = a.getBounds();
            if(rc.intersects(ra)) {
                craft.setVisible(false);
                    ingame=false;
                    won = false;
            }
            if(power!=null && rc.intersects(power.getBounds())) {
                 startPowerUp((int)(Math.random()*12345)%10);
//                 startPowerUp(0);
//                 startPowerUp(1);
//                  startPowerUp(2);
                if(!life)
                 initMusic("powerup.wav", 0);
                else
                    initMusic("1up.wav", 0);
                 consecShots.setSize(0);
                 power=null;
            }
            if (ra.intersects(earth)) {
                craft.setVisible(false);
                ingame = false;
                won = false;
                msg = "Invasion! Game Over";
            }
            Bomb b = a.getBomb();
            Rectangle rb = b.getBounds();
            if(rc.intersects(rb)) {
                if(!craft.isPowerShooting()){
                    if(!craft.wasShot()) {
                    lives.setSize(lives.getSize()-1);
                    initMusic("bomb.wav", 0);
                    b.die();
                    b.toExpl();
                    craft.isShot(true);
                    if(lives.maxed()){
                        craft.setVisible(false);
                        ingame=false;
                        won = false;
                    } 
                }
                }
                else {
                    b.die();
                    b.toExpl();
                    craft.dontPowerShoot();
                    powerShotWriter = powerShot =  false;
                }
            }
            if(rb.intersects(earth)) {
                b.die();
                b.toExpl();
            }
            if(power!=null&&power.getBounds().intersects(earth)) {
                power=null;
                consecShots.setSize(0);
            }
        
            for(int j = 0; j < ms.size();j++){
                Missile m = (Missile) ms.get(j);
                if(m.getBounds().intersects(rb)){
                    b.die();
                    b.toExpl();if(!m.isPowerShot())
                    ms.remove(j);
                    Board.initMusic("blaster2.wav" , 0);
                }
                else
                if(m.getBounds().intersects(ra)) {
                    if(!m.isPowerShot()) {
                        ms.remove(j);
                        if(!(freeze||unlimShot||m.isMultShot())) {
                            consec++;
                            consecShots.setSize(consecShots.getSize() + 1);
                        }
                    }
                    else
                        m.setSpeedShot(false);
                    a.isShot();
                    if(a.getLives()==0) {
                        
                        score+=100*(mult/2 +1);
                    }
                    else
                        score+=50*(mult/2 +1);
                    
                    if(mult<10) {
                        mult++;
                    }
                        Board.initMusic("blaster2.wav" , 0);
                    if(a.getLives() == 0) {
                        a.setDying(true);
                    }
                    if(consec >= 4&&power==null&&!(freeze||powerShot||craft.isPowerShooting())) {
                        power = new PowerUp(a.getX(), a.getY());
                        
                        consec = 0;
                    }
                }
            }
        }
    }

    private void pause()
    {
        if(isControlled){
            control(); return;}
        isPaused = !isPaused;
        if (isPaused) {
            drawStatus((Graphics2D)getGraphics());
            timer.stop();
        } else {
            timer.start();
        }
        repaint();
    }
    
    public void control() {
        if(isPaused){
            pause(); return;}
        isControlled = !isControlled;
        if(isControlled) {
            initMusic("listen.wav", 0);
            drawStatus((Graphics2D)getGraphics());
            timer.stop();
        } else {
            timer.start();
        }
    }

    
    public void startPowerUp(int power) {
        craft.dontPowerShoot();
        if(power == 0||power==3||power==5||power==6) {
            powerShot = powerShotWriter = true;
            craft.shouldPowerShoot();
        }
        else if (power ==1||power==4||power==7) 
            freeze = true;
        else if(power==2||power==8){
            unlimShot=true;
            craft.setNumMiss(15);
        }
        else {
            life=true;
            if(lives.getSize()<5)
                lives.setSize(lives.getSize()+1);
        }
    }
            
    private class PowerUp {
        private String source;
        private int dx, dy, x, y;
        private Image image;
        private int width, height;
        private boolean visible, dying;
       
        private final int BOARD_WIDTH = 525;
        private final int MISSILE_SPEED = 3;
    
        public PowerUp(int x, int y){
            this.source = "power.png";
            ImageIcon ii = new ImageIcon(this.getClass().getResource(source));
            image = ii.getImage();
            visible = true;
            dying = false;
            width = image.getWidth(null);
            height = image.getHeight(null);
            this.x = x;
            this.y = y;
        }  
        
        public void move()  {
            x-=3;
        }
        
        public boolean isVisible() {
            return visible;
        }
        
        public void setVisible(boolean vis) {
            visible = vis;
        }
        
        public int getX(){
            return x;
        }
        
        public int getY(){
            return y;
        }
        
        public Image getImage(){
            return image;
        }
        
        public Rectangle getBounds(){
            return new Rectangle(x,y,width,height);
        }
    }
    
    private class TAdapter extends KeyAdapter implements FocusListener {
        
    
        public void focusLost(FocusEvent e) {
            pause();
        }
    
        public void focusGained(FocusEvent e) {}
        
        public void keyReleased(KeyEvent e) {
            craft.keyReleased(e);
        }
        
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if(key==KeyEvent.VK_P)
                pause();
            else
            if(key==KeyEvent.VK_C)
                control();  
            else if(key==KeyEvent.VK_R) {
               
                    level = 1;
                    timer.stop();
                    beginGame = true;
                    init();
                    
            }
             
                    
            if(powerShot) {
                craft.shouldPowerShoot();
                
                    
                        powerShot = false;
                        consec=0;
                  
            }
            
            
            craft.keyPressed(e);
        }
    }
}