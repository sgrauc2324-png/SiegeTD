package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import database.DatabaseManager;
import database.User;
import database.Game;
import entities.*;

public class Jogo extends JFrame {
    private JTextField txtUser = new JTextField(15);
    private JLabel lblStatus = new JLabel("Introduce usuario", SwingConstants.CENTER);
    private DatabaseManager db;
    private WaveManager wm;
    private Timer gameTimer;
    private int money;
    private int lives;
    private List<Tower> towers = new ArrayList<>();
    private JLabel lblMoney = new JLabel();
    private User currentUser;
    private long startTime;
    private int currentWave;
    private int currentLevel;

    public Jogo() {
        try {
            db = new DatabaseManager();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        setTitle("SiegeTD");
        setSize(800, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        JPanel pnlLogin = new JPanel(new GridLayout(5, 1, 10, 10));
        JLabel lblUser = new JLabel("Usuario:");
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
                currentUser = db.loadUser(txtUser.getText().trim());
                if (currentUser != null) levelSelector();
                else lblStatus.setText("No existe");
            } catch (SQLException ex) {
                lblStatus.setText("Error BBDD");
            }
        });

        btnCreate.addActionListener(e -> {
            try {
                db.createUser(txtUser.getText().trim());
                lblStatus.setText("Creado");
            } catch (SQLException ex) {
                lblStatus.setText("Error BBDD");
            }
        });

        setVisible(true);
    }

    private void levelSelector() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel pnlCenter = new JPanel(new GridLayout(1, 3, 20, 20));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        for (int i = 1; i <= 3; i++) {
            final int levelNum = i;
            JPanel pnlLvl = new JPanel(new BorderLayout(10, 10));
            JButton btnLvl = new JButton("Nivel " + i);
            btnLvl.setEnabled(currentUser.getLevel() >= i);
            btnLvl.addActionListener(e -> {
                if (levelNum == 1) level1();
                else if (levelNum == 2) level2();
                else level3();
            });

            JTextArea txtRanking = new JTextArea();
            txtRanking.setEditable(false);
            txtRanking.setBackground(getBackground());
            txtRanking.setFont(new Font("Monospaced", Font.PLAIN, 12));

            try {
                List<String> top = db.getTopTimes(i);
                StringBuilder sb = new StringBuilder("TOP TIEMPOS:\n");
                if (top.isEmpty()) sb.append("Sin datos");
                for (String s : top) sb.append(s).append("\n");
                txtRanking.setText(sb.toString());
            } catch (SQLException ex) {
                txtRanking.setText("Error ranking");
            }

            pnlLvl.add(btnLvl, BorderLayout.NORTH);
            pnlLvl.add(new JScrollPane(txtRanking), BorderLayout.CENTER);
            pnlCenter.add(pnlLvl);
        }

        add(new JLabel("Bienvenido, " + currentUser.getUsername(), SwingConstants.CENTER), BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void level1() {
        currentLevel = 1;
        setupLevel(1, 200, 3);
        wm = new WaveManager();
        wm.startNextWave(5);
        resetEnemyPositions(285);

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
                g.setColor(Color.GRAY);
                g.fillRect(0, 280, 440, 40);
                g.fillRect(400, 280, 40, 320);
                drawEntities(g);
            }
        };

        setupMouseListener(gamePanel);
        gameTimer = new Timer(20, e -> {
            for (Enemy en : wm.getEnemies()) {
                if (en.getX() < 405) en.setX(en.getX() + 2);
                else en.setY(en.getY() + 2);
                checkEnemyBounds(en);
            }
            updateGameLogic(gamePanel, 1);
        });
        finishSetup(gamePanel);
    }

    private void level2() {
        currentLevel = 2;
        setupLevel(2, 300, 3);
        currentWave = 1;
        wm = new WaveManager();
        wm.startNextWave(10);
        resetEnemyPositions(85);

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
                g.setColor(Color.GRAY);
                g.fillRect(0, 80, 440, 40);
                g.fillRect(400, 80, 40, 160);
                g.fillRect(120, 200, 320, 40);
                g.fillRect(120, 200, 40, 160);
                g.fillRect(120, 320, 520, 40);
                g.fillRect(600, 320, 40, 280);
                drawEntities(g);
            }
        };

        setupMouseListener(gamePanel);
        gameTimer = new Timer(20, e -> {
            for (Enemy en : wm.getEnemies()) {
                if (en.getY() == 85 && en.getX() < 405) en.setX(en.getX() + 2);
                else if (en.getX() >= 405 && en.getY() < 205) en.setY(en.getY() + 2);
                else if (en.getY() >= 205 && en.getY() < 210 && en.getX() > 125) en.setX(en.getX() - 2);
                else if (en.getX() <= 125 && en.getY() < 325) en.setY(en.getY() + 2);
                else if (en.getY() >= 325 && en.getY() < 330 && en.getX() < 605) en.setX(en.getX() + 2);
                else en.setY(en.getY() + 2);
                checkEnemyBounds(en);
            }
            if (wm.isWaveFinished() && currentWave < 3) {
                currentWave++;
                wm.startNextWave(10 + (currentWave * 2));
                resetEnemyPositions(85);
            }
            updateGameLogic(gamePanel, 2);
        });
        finishSetup(gamePanel);
    }

    private void level3() {
        currentLevel = 3;
        setupLevel(3, 400, 5);
        currentWave = 1;
        wm = new WaveManager();
        wm.startNextWave(10);
        resetEnemyPositions(45);

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
                g.setColor(Color.GRAY);
                g.fillRect(0, 40, 700, 40);
                g.fillRect(660, 40, 40, 200);
                g.fillRect(200, 200, 500, 40);
                g.fillRect(200, 200, 40, 200);
                g.fillRect(200, 360, 600, 40);
                drawEntities(g);
            }
        };

        setupMouseListener(gamePanel);
        gameTimer = new Timer(20, e -> {
            for (Enemy en : wm.getEnemies()) {
                if (en.getY() == 45 && en.getX() < 665) en.setX(en.getX() + 2);
                else if (en.getX() >= 665 && en.getY() < 205) en.setY(en.getY() + 2);
                else if (en.getY() >= 205 && en.getY() < 210 && en.getX() > 205) en.setX(en.getX() - 2);
                else if (en.getX() <= 205 && en.getY() < 365) en.setY(en.getY() + 2);
                else en.setX(en.getX() + 2);
                checkEnemyBounds(en);
            }
            if (wm.isWaveFinished() && currentWave < 5) {
                currentWave++;
                wm.startNextWave(10 + (currentWave * 3));
                resetEnemyPositions(45);
            }
            updateGameLogic(gamePanel, 3);
        });
        finishSetup(gamePanel);
    }

    private boolean isPath(int x, int y) {
        if (currentLevel == 1) {
            if (y == 280 && x <= 400) return true;
            if (x == 400 && y >= 280) return true;
        } else if (currentLevel == 2) {
            if (y == 80 && x <= 400) return true;
            if (x == 400 && y >= 80 && y <= 200) return true;
            if (y == 200 && x >= 120 && x <= 400) return true;
            if (x == 120 && y >= 200 && y <= 320) return true;
            if (y == 320 && x >= 120 && x <= 600) return true;
            if (x == 600 && y >= 320) return true;
        } else if (currentLevel == 3) {
            if (y == 40 && x <= 680) return true;
            if (x == 640 && y >= 40 && y <= 200) return true;
            if (y == 200 && x >= 200 && x <= 680) return true;
            if (x == 200 && y >= 200 && y <= 360) return true;
            if (y == 360 && x >= 200) return true;
        }
        return false;
    }

    private void setupLevel(int n, int m, int l) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        money = m; lives = l; towers.clear();
        startTime = System.currentTimeMillis();
        currentWave = 0;
        updateUI();
        JPanel top = new JPanel(); top.add(lblMoney);
        add(top, BorderLayout.NORTH);
    }

    private void resetEnemyPositions(int yBase) {
        int sep = 0;
        for (Enemy e : wm.getEnemies()) {
            e.setX(sep); e.setY(yBase);
            sep -= 60;
        }
    }

    private void checkEnemyBounds(Enemy en) {
        if (en.getY() > 600 || en.getX() > 800) { en.setHealth(0); lives--; updateUI(); }
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 800; i += 40) g.drawLine(i, 0, i, 600);
        for (int i = 0; i < 600; i += 40) g.drawLine(0, i, 800, i);
    }

    private void drawEntities(Graphics g) {
        for (Tower t : towers) {
            g.setColor(Color.BLUE); g.fillRect(t.getX(), t.getY(), 40, 40);
            g.setColor(Color.WHITE); g.drawString("L" + t.getLevel(), t.getX() + 12, t.getY() + 24);
        }
        g.setColor(Color.RED);
        for (Enemy e : wm.getEnemies()) {
            if (e.getHealth() > 0 && e.getX() >= 0) {
                g.fillOval(e.getX(), e.getY(), 30, 30);
                g.setColor(Color.GREEN);
                g.fillRect(e.getX(), e.getY() - 10, (int)(30 * (e.getHealth()/100.0)), 5);
                g.setColor(Color.RED);
            }
        }
    }

    private void setupMouseListener(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int gx = (e.getX() / 40) * 40, gy = (e.getY() / 40) * 40;
                if (isPath(gx, gy)) return;
                Tower f = null;
                for (Tower t : towers) if (t.getX() == gx && t.getY() == gy) f = t;
                if (f == null && money >= 50) { towers.add(new Tower(gx, gy)); money -= 50; }
                else if (f != null && money >= 100 && f.getLevel() < 3) { f.upgrade(); money -= 100; }
                updateUI(); panel.repaint();
            }
        });
    }

    private void updateGameLogic(JPanel panel, int level) {
        for (Tower t : towers) {
            if (t.cooldown > 0) t.cooldown--;
            else {
                for (Enemy en : wm.getEnemies()) {
                    double d = Math.sqrt(Math.pow(t.x - en.getX(), 2) + Math.pow(t.y - en.getY(), 2));
                    if (d < 150 && en.getHealth() > 0 && en.getX() >= 0) {
                        en.setHealth(en.getHealth() - t.damage);
                        if (en.getHealth() <= 0) { money += 25; updateUI(); }
                        t.cooldown = t.attackSpeed; break;
                    }
                }
            }
        }
        wm.update(); panel.repaint();
        if (lives <= 0) finishGame(level, "Derrota");
        else if (wm.isWaveFinished()) {
            if (level == 1 || (level == 2 && currentWave == 3) || (level == 3 && currentWave == 5)) {
                finishGame(level, "Victoria");
            }
        }
    }

    private void finishGame(int level, String msg) {
        gameTimer.stop();
        long s = (System.currentTimeMillis() - startTime) / 1000;
        String ts = String.format("00:%02d:%02d", s / 60, s % 60);
        try {
            new Game(currentUser.getId(), level, ts).saveGame(db);
            if (msg.equals("Victoria") && currentUser.getLevel() == level) {
                db.updateUserLevel(currentUser.getId(), level + 1);
                currentUser = db.loadUser(currentUser.getUsername());
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        JOptionPane.showMessageDialog(this, msg + " - Tiempo: " + ts);
        levelSelector();
    }

    private void finishSetup(JPanel p) { add(p, BorderLayout.CENTER); revalidate(); repaint(); gameTimer.start(); }
    private void updateUI() { lblMoney.setText("Dinero: $" + money + " | Vidas: " + lives + (currentWave > 0 ? " | Oleada: " + currentWave : "")); }
}