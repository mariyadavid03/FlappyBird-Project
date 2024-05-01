import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        //Setting Size
        int SCREEN_WIDTH = 360;
        int SCREEN_HEIGHT = 600;

        //Seeting game panel
        JFrame gameFrame = new JFrame("Flappy Bird");
        GamePanel gamePanel = new GamePanel();

        gameFrame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameFrame.add(gamePanel);
        gameFrame.pack();
        gamePanel.requestFocus();
        gameFrame.setVisible(true);


    }
}
