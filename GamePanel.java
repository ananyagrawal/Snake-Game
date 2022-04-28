import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int width = 600;
    static final int height = 600;
    static final int unit_size = 25;
    static final int units = (width * height) / unit_size;
    static final int delay = 75;
    final int x[] = new int[units];
    final int y[] = new int[units];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        start();
    }

    public void start() {
        Apple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            for (int i = 0; i < height / unit_size; i++) {
                g.drawLine(i * unit_size, 0, i * unit_size, height);
                g.drawLine(0, i * unit_size, width, i * unit_size);
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unit_size, unit_size);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                }
            }

            g.setColor(Color.yellow);
            g.setFont(new Font("Monospace", Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (width-metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }

    public void Apple() {
        appleX = random.nextInt((int) (width / unit_size)) * unit_size;
        appleY = random.nextInt((int) (height / unit_size)) * unit_size;

    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - unit_size;
                break;
            case 'D':
                y[0] = y[0] + unit_size;
                break;
            case 'L':
                x[0] = x[0] - unit_size;
                break;
            case 'R':
                x[0] = x[0] + unit_size;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            Apple();
        }
    }

    public void checkCollisions() {

        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if (x[0] < 0) {
            running = false;
        }

        if (x[0] > width) {
            running = false;
        }

        if (y[0] < 0) {
            running = false;
        }

        if (y[0] > height) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {

        g.setColor(Color.yellow);
        g.setFont(new Font("Monospace", Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (width-metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Monospace", Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (width-metrics2.stringWidth("Game Over"))/2, height/2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
