import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

enum Stone {

    WHITE(Color.WHITE), BLACK(Color.BLACK), NONE(null);

    private final static Random rand = new Random();
    final Color color;

    private Stone(Color c) {
        color = c;
    }

    public void paint(Graphics g, int dimension) {
        if (this == NONE) return;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        int x = 5;
        g2d.fillOval(rand.nextInt(x), rand.nextInt(x), dimension - x, dimension - x);

    }

}

public class GoBoard {
    public static void main(String[] args) {
        // create a new frame to hold the panel
        JFrame application = new JFrame();
        // set the layout of the frame
        application.setLayout(new BorderLayout());
        // add the panel to the frame
        application.add(new GoPanel(19), BorderLayout.CENTER);

        application.setSize(600, 625);
        // set the frame to exit when it is closed
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // make sure the size of the frame does not change
        application.setResizable(false);
        // make the frame visible
        application.setVisible(true);
    }
}

class GoPanel extends JPanel {

    Square[][] board;
    boolean whiteToMove;

    //	GoPanel Constructor
    GoPanel(int dimension) {
        board = new Square[dimension][dimension];
        whiteToMove = true;
        initBoard(dimension);
    }

    // a Method that initialises the Board of the game
    private void initBoard(int dimension) {
        super.setLayout(new GridLayout(dimension, dimension));
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                board[row][col] = new Square(row, col);
                super.add(board[row][col]);
            }
        }
        repaint();
    }

    // a method to place the stones in the intersections using a mouse click and paints/draw the lines on the board
    private class Square extends JPanel {

        final int row;
        final int column;
        Stone stone;

        Square(int r, int c) {
            stone = Stone.NONE;
            row = r;
            column = c;
            super.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent me) {
                    if (stone != Stone.NONE) return;
                    stone = whiteToMove ? Stone.WHITE : Stone.BLACK;
                    whiteToMove = !whiteToMove;
                    repaint();
                }
            });
        }

        // a method that paints/draws the lines of the board and sets its color.
        protected void paintComponent(Graphics g) {

            // call paintComponent to ensure the panel displays correctly
            super.paintComponent(g);

            int width = super.getWidth(); // total width
            int height = super.getHeight(); // total height

            g.setColor(new Color(0xB78600));// set the background color

            g.fillRect(0, 0, width, height);// the background color will fill the whole board

            g.setColor(Color.BLACK);// set color for the vertical and horizontal lines of the board

            if (row == 0 || row == board.length - 1 || column == 0 || column == board.length - 1) { // draw the lines of the board
                if (column == 0) {
                    g.drawLine(width / 2, height / 2, width, height / 2);
                    if (row == 0) g.drawLine(width / 2, height / 2, width / 2, height);
                    else if (row == 18) g.drawLine(width / 2, height / 2, width / 2, 0);
                    else g.drawLine(width / 2, 0, width / 2, height);
                } else if (column == 18) {
                    g.drawLine(0, height / 2, width / 2, height / 2);
                    if (row == 0) g.drawLine(width / 2, height / 2, width / 2, height);
                    else if (row == 18) g.drawLine(width / 2, height / 2, width / 2, 0);
                    else g.drawLine(width / 2, 0, width / 2, height);
                } else if (row == 0) {
                    g.drawLine(0, height / 2, width, height / 2);
                    g.drawLine(width / 2, height / 2, width / 2, height);
                } else {
                    g.drawLine(0, height / 2, width, height / 2);
                    g.drawLine(width / 2, height / 2, width / 2, 0);
                }
            } else {
                g.drawLine(0, height / 2, width, height / 2);
                g.drawLine(width / 2, 0, width / 2, height);
            }
            stone.paint(g, width);
        }
    }
}
