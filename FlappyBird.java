import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;  // stores all pipes
import java.util.Random;    //pipes at random positions
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener  { //inherits JPanel = new class with all similarities
    int boardWidth = 360;
    int boardHeight = 640;

    //Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg; // stores Image Objects

    //Bird
    int birdX  = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird { // hold the values
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    //Pipes

    int pipeX = boardWidth; // top screen
    int pipeY = 0;  //right side
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false; //flappy bird has passed pipe yet

        Pipe(Image img) {
            this.img = img;
        }
    }

    //game logic
    Bird bird;
    int velocityX = -4; //move pipes to the left speed
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;

    double score = 0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("bottompipe.png")).getImage();

        //bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();


        //game timer
        gameLoop = new Timer(1000/60, this); // -> in millisecond = 1 second , this refers to flappybird class
        gameLoop.start();
    }

    public void placePipes() {
        //(0-1) * pipeHeight/2 -> (0 -256)
        // 128
        // 0 - 128 - (0-256) --> pipeHeight/4 -> 3/4 pipeHeight

        int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y =  randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {  //needs to loop
        super.paintComponent(g); // super = parentclass
        draw(g);
    }

    public void draw (Graphics g) {
 
        //background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        //bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //pipes
        for(int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score),10,35);

        } else {
            g.drawString(String.valueOf((int)score), 10, 35);
        }
    }

    public void move() {
        //bird
        velocityY += gravity; 
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); // top of screen

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5; // because 2 pipes
            }

            if(collision(bird, pipe)){
                gameOver = true;
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }

    }

    public boolean collision(Bird a, Pipe b) { //corners
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //action performed every sec
        move(); // update position of bird
        repaint(); //call paint 
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }

    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if(gameOver) {
                //restart game by resetting conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }

    }

    
}
F
