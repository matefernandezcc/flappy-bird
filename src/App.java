import javax.swing.*;

public class App {
    public static void main(String []args) throws Exception{
        int boardWidth = 300;
        int boardHeigth = 640;


        /* Ventana de la aplicacion */
        JFrame frame = new JFrame("Flappy Bird");
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeigth);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        frame.setVisible(true);

    }

};