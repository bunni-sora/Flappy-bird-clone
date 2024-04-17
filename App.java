import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;  // Dimensions of background image
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird Clone");
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);  // Window at center of screen
        frame.setResizable(false);  // User cant resize frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Jpanel = canvas

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    } 
}
