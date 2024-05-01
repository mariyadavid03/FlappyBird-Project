import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.*;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
    //Initializing Panel Size 
    int SCREEN_WIDTH = 360;
    int SCREEN_HEIGHT = 600;
    int UNIT_SIZE = 25;

    //Variables for Images
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //Initializing Bird Diemenions
    int birdX = SCREEN_WIDTH/8;
    int birdY = SCREEN_HEIGHT/2;
    int birdWidth = 34;
    int birdHeight = 24;

    //Bird Class
    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

    //Pipe Class
    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }

    //Pipes Dimensions
    int pipeX = SCREEN_WIDTH;
    int pipeY = 0;
    int pipeWidth = 45;
    int pipeHeight = 512;

    //Loops for pipe generation and game
    Timer gameLoop;
    Timer pipeTimer;

    
    //Bird Moving Velocity
    int velocityY = 0;
    int gravity = 1;

    //Pipe Velcoity
    int velocityX = -4;

    Bird bird;
    ArrayList<Pipe> pipes;
    Random random = new Random();
    boolean gameOver = false;
    double score = 0;

    GamePanel(){
        //Panel Setting Up
        setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        setBackground(new Color(33, 154, 194));
        setFocusable(true);
        addKeyListener(this);

        //Image Loading
        birdImg = new ImageIcon(getClass().getResource("./whitebird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./topp.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottomp.png")).getImage();
        
        //Obejct Creation
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //Timer for pipe generation
        pipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        } );

        pipeTimer .start();
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

    }

    // To place pipes randomly
    public void placePipes(){
        int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int opening = SCREEN_HEIGHT/4;

        //Top pipe
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        //Bottom Pipe
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + opening;
        pipes.add(bottomPipe);
    }

    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g){
        //Drawing Bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //Drawing Pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        g.setColor(Color.yellow);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over: "+ String.valueOf((int)score), 10, 35);
        } else{
            g.drawString(String.valueOf((int)score), 10, 35);
        }

    }

    public void move(){
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //Moving the Pipes
        for (int i =0 ;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            //Score Adding
            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;
            }
            //Checking for bird and pipe collison
            if(collison(bird, pipe)){
                gameOver = true;
            }
        }
        //Checking for bird and screen collison
        if(bird.y > SCREEN_HEIGHT){
           gameOver = true; 
        }
        
    }

    //Checking for collisons
    public boolean collison(Bird a, Pipe b){
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            pipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP){
            velocityY = -9;
            if(gameOver){
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                pipeTimer.start();
            }
        }
    
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
