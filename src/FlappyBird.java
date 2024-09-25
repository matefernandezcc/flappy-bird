import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// Extends es herencia
public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeigth = 640;

    //////////////////////// Imágenes ////////////////////////
    Image backgroundImage;
    Image birdImg;
    Image topPipeImage;
    Image bottomPipeImage;

    //////////////////////// Bird ////////////////////////
    int birdX = boardWidth/8;
    int birdY = boardWidth/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int heigth = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

    //////////////////////// Pipes ////////////////////////
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64; // Scaled by 1/6
    int pipeHeigth = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int heigth = pipeHeigth;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }

    //////////////////////// Lógica del juego ////////////////////////
    Bird bird;
    int velocityX = -4; // Velocidad con la que los pipes se mueven a la izquierda
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;

    //////////////////////////////////// Constructor ////////////////////////////////////
    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth, boardHeigth));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //////////////////////// Carga de imágenes ////////////////////////
        backgroundImage = new ImageIcon(getClass().getResource("/images/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("/images/flappybird.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("/images/toppipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("/images/bottompipe.png")).getImage();

        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //////////////////////// Pipes Timer ////////////////////////
        placePipesTimer = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();

        //////////////////////// Timer ////////////////////////
        gameLoop = new Timer(1000/60, this);  // 60 veces por segundo, delay[ms] (1000ms = 1s)
        gameLoop.start();
    }

    //////////////////////////////////// Utils ////////////////////////////////////
    public void placePipes(){
        int randomPipeY = (int)(pipeY - pipeHeigth/4 - Math.random()*(pipeHeigth/2));
        int openingSpace = boardHeigth/4;

        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + pipeHeigth + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //////////////////////// Fondo ////////////////////////
        // Los draw siempre empiezan desde la esquina superior izquierda con coordenadas (x:0, y:0)
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeigth, null);

        //////////////////////// Bird ////////////////////////
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.width, null);

        //////////////////////// Pipes ////////////////////////
        for(int i = 0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.heigth, null);
        }

        //////////////////////// Puntaje ////////////////////////
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over: "+String.valueOf((int) score), 10, 35);
        }
        else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move(){
        //////////////////////// Bird ////////////////////////
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); // Cappeamos la posicion y = 0 por si llega a pasar a un num negativo (se va más arriba de la pantalla)

        //////////////////////// Pipes ////////////////////////
        for(int i = 0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5; // Como hay dos pipes es un 0,5 por cada una siendo 1 punto por cada par de pipes pasadas
            }

            if(collision(bird, pipe)){
                gameOver = true;
            }
        }

        //////////////////////// Game Over ////////////////////////
        if(bird.y > boardHeigth){
            gameOver = true;
        }
    }

    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.heigth &&
                a.y + a.heigth > b.y;
    }

    //////////////////////// Implemented Methods ////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W){
            velocityY = -9;
            if(gameOver){
                //////////////////////// Reiniciar el juego ////////////////////////
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

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
