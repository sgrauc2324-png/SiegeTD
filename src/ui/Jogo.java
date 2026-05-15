package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import database.DatabaseManager;
import entities.*;

public class Jogo extends JFrame {
    private JTextField txtUser = new JTextField(15);
    private JLabel lblStatus = new JLabel("Introduce usuario", SwingConstants.CENTER);
    private DatabaseManager db;
    private WaveManager wm;
    private Timer gameTimer;

    private int money;
    private List<Tower> towers = new ArrayList<>();
    private JLabel lblMoney = new JLabel();

    public Jogo() {
        try {
            db = new DatabaseManager();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        setTitle("Login");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        JPanel pnlLogin = new JPanel(new GridLayout(5, 1, 10, 10));
        JLabel lblUser = new JLabel("Usuario:", SwingConstants.CENTER);
        JButton btnLogin = new JButton("Login");
        JButton btnCreate = new JButton("Crear");

        pnlLogin.add(lblUser);
        pnlLogin.add(txtUser);
        pnlLogin.add(btnLogin);
        pnlLogin.add(btnCreate);
        pnlLogin.add(lblStatus);
        add(pnlLogin);

        btnLogin.addActionListener(e -> {
            try {
                var u = db.loadUser(txtUser.getText().trim());
                if (u != null) {
                    levelSelector();
                } else {
                    lblStatus.setText("No existe");
                }
            } catch (SQLException ex) {
                lblStatus.setText("Error BBDD");
            }
        });

        btnCreate.addActionListener(e -> {
            try {
                db.createUser(txtUser.getText().trim());
                lblStatus.setText("Usuario creado");
            } catch (SQLException ex) {
                lblStatus.setText("Error BBDD");
            }
        });

        setVisible(true);
    }

    private void levelSelector() {
        getContentPane().removeAll();
        setTitle("Seleccionar Nivel");
        setLayout(new GridBagLayout());

        JPanel pnlLevels = new JPanel(new GridLayout(3, 1, 20, 20));
        JButton btnLvl1 = new JButton("Nivel 1");
        JButton btnLvl2 = new JButton("Nivel 2");
        JButton btnLvl3 = new JButton("Nivel 3");

        btnLvl1.addActionListener(e -> level1());
        btnLvl2.addActionListener(e -> level2());
        btnLvl3.addActionListener(e -> level3());

        pnlLevels.add(btnLvl1);
        pnlLevels.add(btnLvl2);
        pnlLevels.add(btnLvl3);
        add(pnlLevels);

        revalidate();
        repaint();
    }

    private void level1() {
        getContentPane().removeAll();
        setTitle("Jugando - Nivel 1");
        setLayout(new BorderLayout());

        money = 200;
        towers.clear();
        lblMoney.setText("Dinero: $" + money + " | Nueva: $50 | Mejorar: $100");
        lblMoney.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel topPanel = new JPanel();
        topPanel.add(lblMoney);
        add(topPanel, BorderLayout.NORTH);

        wm = new WaveManager();
        wm.startNextWave(5);

        int separation = 0;
        for (Enemy e : wm.getEnemies()) {
            e.setX(separation);
            e.setY(285);
            separation -= 60;
        }

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(Color.LIGHT_GRAY);
                for (int i = 0; i < getWidth(); i += 40) {
                    g.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight(); i += 40) {
                    g.drawLine(0, i, getWidth(), i);
                }

                g.setColor(Color.GRAY);
                g.fillRect(0, 280, 440, 40);
                g.fillRect(400, 280, 40, 320);

                for (Tower t : towers) {
                    g.setColor(Color.BLUE);
                    g.fillRect(t.getX(), t.getY(), 40, 40);
                    g.setColor(Color.WHITE);
                    g.drawString("L" + t.getLevel(), t.getX() + 12, t.getY() + 24);
                }

                g.setColor(Color.RED);
                for (Enemy e : wm.getEnemies()) {
                    if (e.getX() >= 0) {
                        g.fillOval(e.getX(), e.getY(), 30, 30);
                    }
                }
            }
        };

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int gridX = (e.getX() / 40) * 40;
                int gridY = (e.getY() / 40) * 40;

                Tower existingTower = null;
                for (Tower t : towers) {
                    if (t.getX() == gridX && t.getY() == gridY) {
                        existingTower = t;
                        break;
                    }
                }

                if (existingTower == null) {
                    if (money >= 50) {
                        towers.add(new Tower(gridX, gridY));
                        money -= 50;
                    }
                } else {
                    if (money >= 100 && existingTower.getLevel() < 3) {
                        existingTower.upgrade();
                        money -= 100;
                    }
                }

                lblMoney.setText("Dinero: $" + money + " | Nueva: $50 | Mejorar: $100");
                gamePanel.repaint();
            }
        });

        gameTimer = new Timer(20, e -> {
            for (Enemy en : wm.getEnemies()) {
                if (en.getX() < 405) {
                    en.setX(en.getX() + 2);
                } else {
                    en.setY(en.getY() + 2);
                }

                if (en.getY() > 600) {
                    en.setHealth(0);
                }
            }
            wm.update();
            gamePanel.repaint();

            if (wm.isWaveFinished()) {
                gameTimer.stop();
                JOptionPane.showMessageDialog(this, "¡Oleada Completada!");
                levelSelector();
            }
        });

        add(gamePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        gameTimer.start();
    }

    private void level2() {
        getContentPane().removeAll();
        setTitle("Jugando - Nivel 2");
        setLayout(new BorderLayout());
        add(new JLabel("Pantalla del Nivel 2: Dificultad Media", SwingConstants.CENTER), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void level3() {
        getContentPane().removeAll();
        setTitle("Jugando - Nivel 3");
        setLayout(new BorderLayout());
        add(new JLabel("Pantalla del Nivel 3: Oleada Final", SwingConstants.CENTER), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}