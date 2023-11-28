import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame {
    private JLabel butterflyLabel;
    private JLabel netLabel;
    private volatile boolean isButterflyAnimationRunning = false;
    private volatile boolean isNetAnimationRunning = false;
    private int butterflyX;
    private int butterflyY;
    private int netX;
    private int netY;
    private int butterflyCaughtCount;
    private int netCaughtCount;

    public App() {
        setTitle("Приложение бабочка и сачок");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);

        // Установка изображения фона
        ImageIcon backgroundImage = new ImageIcon("C:/LABA6/src/fon.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        getContentPane().add(backgroundLabel);

        // Добавление изображения бабочки
        ImageIcon butterflyImage = new ImageIcon("C:/LABA6/src/butterfly.png");
        int butterflyWidth = butterflyImage.getIconWidth() / 2;
        int butterflyHeight = butterflyImage.getIconHeight() / 2;
        Image resizedButterflyImage = butterflyImage.getImage().getScaledInstance(butterflyWidth, butterflyHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedButterflyIcon = new ImageIcon(resizedButterflyImage);
        butterflyX = 100;
        butterflyY = 100;
        butterflyLabel = new JLabel(resizedButterflyIcon);
        butterflyLabel.setBounds(butterflyX, butterflyY, butterflyWidth, butterflyHeight);
        backgroundLabel.add(butterflyLabel);

        // Добавление изображения сачка
        ImageIcon netImage = new ImageIcon("C:/LABA6/src/net.png");
        int netWidth = netImage.getIconWidth() / 2;
        int netHeight = netImage.getIconHeight() / 2;
        Image resizedNetImage = netImage.getImage().getScaledInstance(netWidth, netHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedNetIcon = new ImageIcon(resizedNetImage);
        netX = 500;
        netY = 200;
        netLabel = new JLabel(resizedNetIcon);
        netLabel.setBounds(netX, netY, netWidth, netHeight);
        backgroundLabel.add(netLabel);

        // Добавление кнопок
        JButton startButterflyButton = new JButton("Старт бабочки");
        startButterflyButton.setBounds(10, 10, 120, 30);
        startButterflyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButterflyAnimation();
            }
        });
        backgroundLabel.add(startButterflyButton);

        JButton startNetButton = new JButton("Старт сачка");
        startNetButton.setBounds(140, 10, 100, 30);
        startNetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startNetAnimation();
            }
        });
        backgroundLabel.add(startNetButton);

        JButton stopButton = new JButton("Стоп");
        stopButton.setBounds(270, 10, 100, 30);
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopAnimations();
                showResult();
            }
        });
        backgroundLabel.add(stopButton);

        setVisible(true);
    }

    private void startButterflyAnimation() {
        if (!isButterflyAnimationRunning) {
            isButterflyAnimationRunning = true;

            Thread butterflyThread = new Thread(new Runnable() {
                public void run() {
                    while (isButterflyAnimationRunning) {
                        moveButterfly();
                        checkButterflyCaught();
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            butterflyThread.start();
        }
    }

    private void stopButterflyAnimation() {
        isButterflyAnimationRunning = false;
    }

    private void moveButterfly() {
        int x = butterflyLabel.getX();
        int y = butterflyLabel.getY();

        if (x <= 0 || x >= getWidth() - butterflyLabel.getWidth()) {
            butterflyX *= -1;
        }

        if (y <= 0 || y >= getHeight() - butterflyLabel.getHeight()) {
            butterflyY *= -1;
        }

        x += butterflyX;
        y += butterflyY;

        butterflyLabel.setLocation(x, y);
    }

    private void checkButterflyCaught() {
        Rectangle butterflyBounds = butterflyLabel.getBounds();
        Rectangle netBounds = netLabel.getBounds();

        if (butterflyBounds.intersects(netBounds)) {
            netCaughtCount++;
        }
        if (butterflyBounds.intersects(netBounds)) {
            butterflyCaughtCount++;
        }
    }

    private void startNetAnimation() {
        if (!isNetAnimationRunning) {
            isNetAnimationRunning = true;

            Thread netThread = new Thread(new Runnable() {
                public void run() {
                    while (isNetAnimationRunning) {
                        moveNet();
                        checkNetCaught();
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            netThread.start();
        }
    }

    private void stopNetAnimation() {
        isNetAnimationRunning = false;
    }

    private void moveNet() {
        int x = netLabel.getX();
        int y = netLabel.getY();

        if (x <= 0 || x >= getWidth() - netLabel.getWidth()) {
            netX *= -1;
        }

        if (y <= 0 || y >= getHeight() - netLabel.getHeight()) {
            netY *= -1;
        }

        x += netX;
        y += netY;

        netLabel.setLocation(x, y);
    }

    private void checkNetCaught() {
        Rectangle netBounds = netLabel.getBounds();
        Rectangle butterflyBounds = butterflyLabel.getBounds();

        if (netBounds.intersects(butterflyBounds)) {
            netCaughtCount++;
        }
    }

    private void stopAnimations() {
        stopButterflyAnimation();
        stopNetAnimation();
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this, "Бабочки сама угодила в сачок: " + butterflyCaughtCount + "\n" +
                "Бабочки пойманы сачком: " + netCaughtCount, "Результаты", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App();
            }
        });
    }
}