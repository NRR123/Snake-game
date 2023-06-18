import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Time;

public class Board extends JPanel implements ActionListener {
    int Max_Dots=400*400;
    int X_pos[]=new int[Max_Dots];
    int Y_pos[]=new int[Max_Dots];
    int dot_size=10;
    int apple_x;
    int apple_y;
    int B_Height=400;
    int B_Width=400;
    int Dots;//starting size of snake
    //Image
    Image body,head,apple;
    Timer timer;
    int Delay=150;
    boolean left=true;
    boolean right=false;
    boolean up=false;
    boolean down=false;
    private boolean ingame=true;


    Board(){
        setPreferredSize(new Dimension(B_Width,B_Height));
        //setSize(B_Width,B_Height);
        setBackground(Color.BLACK);
        initgame();
        Tadapeter tadapeter=new Tadapeter();
        setFocusable(true);
        addKeyListener(tadapeter);
        loadimage();
    }
    public void initgame(){
        Dots=3;
        X_pos[0]=250;
        Y_pos[0]=250;

        for(int i=0;i<Dots;i++){
            X_pos[i]=X_pos[0]+dot_size*i;
            Y_pos[i]=Y_pos[0];
        }
        //Initialize the position of the apple
//        apple_x=150;
//        apple_y=150;
        locateapple();
        timer=new Timer(Delay,this);
        timer.start();
    }
    public void loadimage(){
        ImageIcon bodyicon=new ImageIcon("src/resources/dot.png");
        body=bodyicon.getImage();
        ImageIcon headicon=new ImageIcon("src/resources/head.png");
        head=headicon.getImage();
        ImageIcon appleicon=new ImageIcon("src/resources/apple.png");
        apple=appleicon.getImage();

    }
    @Override
    //loading the graphics in frame
   public void paintComponent(Graphics g){
        super.paintComponent(g);//what is this?
        dodrawing(g);
    }

    public void dodrawing(Graphics g){
        //now drawing the image at particular position
        if(ingame){
            g.drawImage(apple,apple_x,apple_y,this);
            for(int i=0;i<Dots;i++){
                if(i==0){
                    //its a head
                    g.drawImage(head,X_pos[0],Y_pos[0],this);
                }else{
                    //snake body
                    g.drawImage(body,X_pos[i],Y_pos[i],this);
                }

            }
        }else{
            gameOver(g);
            timer.stop();
        }

    }
    //randomize the position of apple
    public void locateapple(){
        apple_x=((int)(Math.random()*39))*10;
        apple_y=((int)(Math.random()*39))*10;

    }
    public void gameOver(Graphics g){
        String msg = "Game Over";
        int score = (Dots-3)*100;
        String scoremsg = "Score:"+Integer.toString(score);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg,(B_Width-fontMetrics.stringWidth(msg))/2 , B_Height/4);
        g.drawString(scoremsg,(B_Width-fontMetrics.stringWidth(scoremsg))/2 , 3*(B_Height/4));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //now whenever the timer will be ++ we have to make our snake move
        if(ingame) {
            checkCollison();
            eatfood();
            move();
            repaint();
        }
    }
    public void move(){
        //you have to change the position of x -coordinates and y-coordinates
        for(int i=Dots-1;i>0;i--){
            X_pos[i]=X_pos[i-1];
            Y_pos[i]=Y_pos[i-1];
        }
        //now its time to move head
        if(left==true){
            X_pos[0]=X_pos[0]-dot_size;
        }
        if(right==true){
            X_pos[0]=X_pos[0]+dot_size;
        }
        if(up==true){
            Y_pos[0]=Y_pos[0]-dot_size;
        }
        if(down==true){
            Y_pos[0]=Y_pos[0]+dot_size;
        }

    }
    //reading the key pressed on the keyboard and taking action accordingly
    public class Tadapeter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key=keyEvent.getKeyCode();
            if(key==keyEvent.VK_LEFT && !right){
                left=true;
                up=false;
                down=false;
            }
            if(key==keyEvent.VK_RIGHT && !left){
                right=true;
                up=false;
                down=false;
            }
            if(key==keyEvent.VK_UP && !down){
                right=false;
                up=true;
                left=false;
            }
            if(key==keyEvent.VK_DOWN && !up){
                right=false;
                down=true;
                left=false;
            }
      }

    }
    public void eatfood(){
        if(apple_x==X_pos[0] && apple_y==Y_pos[0]){
            Dots++;
            locateapple();
        }
    }
    //check collision
    public void checkCollison(){
        for(int i=1;i<Dots;i++){
            if(i>=4 && X_pos[0]==X_pos[i] && Y_pos[0]==Y_pos[i]){
                ingame=false;
            }
        }
        if(X_pos[0]<0 || Y_pos[0]<0 || X_pos[0]>B_Width || X_pos[0]>B_Height){
            ingame=false;
            timer.stop();
        }

    }

}
