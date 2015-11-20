

import javax.swing.JFrame;
import java.awt.Color;

public class SpaceInvaders extends JFrame
{
    public SpaceInvaders(){
        add(new Board() );
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(530, 490);
        setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("SpaceInvaders");
        this.setBackground(Color.BLACK);
        this.setVisible(true);
    }
    
    public static void main(String[] args){
        new SpaceInvaders();
    }
}