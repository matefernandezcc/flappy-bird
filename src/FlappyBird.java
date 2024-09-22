import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// Extends es herencia
public class FlappyBird extends JPanel {
    int boardWidth = 360;
    int boardHeigth = 640;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth, boardHeigth));
        setBackground(Color.blue);
    }

}
