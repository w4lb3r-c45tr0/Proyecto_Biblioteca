package ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
 
public class PantallaBienvenida extends JFrame {
 
    private static final long serialVersionUID = 1L;
 
    // ─── Paleta de colores ────────────────────────────────────────────────────
    private static final Color C_BG_DARK   = new Color(10, 14, 26);
    private static final Color C_BG_CARD   = new Color(18, 24, 42);
    private static final Color C_GOLD      = new Color(212, 175, 95);
    private static final Color C_GOLD_SOFT = new Color(212, 175, 95, 60);
    private static final Color C_WHITE     = new Color(240, 238, 230);
    private static final Color C_GREY      = new Color(130, 130, 150);
    private static final Color C_BTN_HOVER = new Color(232, 195, 115);
 
    // ─── Animación ────────────────────────────────────────────────────────────
    private float alpha = 0f;           // opacidad del contenido (fade-in)
    private float dotPhase = 0f;        // fase para los puntos decorativos
    private Timer fadeTimer;
    private Timer dotTimer;
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new PantallaBienvenida().setVisible(true);
        });
    }
 
    public PantallaBienvenida() {
        setTitle("Biblioteca UMG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);                       // sin barra del sistema
        setSize(750, 520);
        setLocationRelativeTo(null);
        setBackground(C_BG_DARK);
 
        // Panel principal con pintado personalizado
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintBackground(g);
            }
        };
        root.setOpaque(true);
        root.setBackground(C_BG_DARK);
        root.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(root);
 
        // ── Panel central (tarjeta de cristal) ─────────────────────────────
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Tarjeta con borde dorado sutil
                g2.setColor(C_BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(C_GOLD_SOFT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(480, 360));
        AlphaCompositePanel cardWrapper = new AlphaCompositePanel(card);
 
        // ── Icono de libro (SVG-like dibujado en Canvas) ───────────────────
        BookIconCanvas bookIcon = new BookIconCanvas(80, 80);
 
        // ── Textos ─────────────────────────────────────────────────────────
        JLabel lblUMG = new JLabel("UNIVERSIDAD MARIANO GÁLVEZ");
        lblUMG.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblUMG.setForeground(C_GOLD);
        lblUMG.setHorizontalAlignment(SwingConstants.CENTER);
 
        JLabel lblTitle = new JLabel("BIBLIOTECA UMG");
        lblTitle.setFont(new Font("Garamond", Font.BOLD, 38));
        lblTitle.setForeground(C_WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
 
        JLabel lblSeparator = new JLabel("────────────────────");
        lblSeparator.setForeground(C_GOLD_SOFT);
        lblSeparator.setHorizontalAlignment(SwingConstants.CENTER);
 
        JLabel lblSub = new JLabel("Sistema de Gestión y Control de Materiales");
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblSub.setForeground(C_GREY);
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);
 
        // ── Botón de entrada ───────────────────────────────────────────────
        JButton btnEntrar = buildEnterButton();
 
        // ── Pie de versión ─────────────────────────────────────────────────
        JLabel lblVersion = new JLabel("v2.0  •  2025");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblVersion.setForeground(new Color(80, 80, 100));
        lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
 
        // ── Layout dentro de la tarjeta ────────────────────────────────────
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(0, 40, 0, 40);
        gbc.gridy = 0; gbc.insets = new Insets(30, 40, 8, 40);  card.add(bookIcon, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(4, 40, 2, 40);   card.add(lblUMG, gbc);
        gbc.gridy = 2; gbc.insets = new Insets(2, 40, 4, 40);   card.add(lblTitle, gbc);
        gbc.gridy = 3; gbc.insets = new Insets(2, 40, 4, 40);   card.add(lblSeparator, gbc);
        gbc.gridy = 4; gbc.insets = new Insets(2, 40, 20, 40);  card.add(lblSub, gbc);
        gbc.gridy = 5; gbc.insets = new Insets(4, 60, 8, 60);   card.add(btnEntrar, gbc);
        gbc.gridy = 6; gbc.insets = new Insets(4, 40, 20, 40);  card.add(lblVersion, gbc);
 
        // ── Botón cerrar (X) en esquina ────────────────────────────────────
        JButton btnCerrar = buildCloseButton();
 
        // Layout del root
        root.add(cardWrapper, BorderLayout.CENTER);
        root.add(buildBottomBar(), BorderLayout.SOUTH);
 
        // Botón cerrar flotante
        JLayeredPane layered = getLayeredPane();
        btnCerrar.setBounds(getWidth() - 45, 10, 30, 30);
        layered.add(btnCerrar, JLayeredPane.POPUP_LAYER);
 
        // ── Acción: abrir sistema principal ───────────────────────────────
        btnEntrar.addActionListener(e -> {
            fadeTimer.stop();
            dotTimer.stop();
            dispose();
            SwingUtilities.invokeLater(() -> {
                VentanaBiblioteca ventana = new VentanaBiblioteca();
                ventana.setVisible(true);
            });
        });
 
        btnCerrar.addActionListener(e -> System.exit(0));
 
        // ── Animación fade-in ──────────────────────────────────────────────
        alpha = 0f;
        fadeTimer = new Timer(16, ev -> {
            alpha = Math.min(1f, alpha + 0.025f);
            cardWrapper.setAlpha(alpha);
            repaint();
            if (alpha >= 1f) ((Timer) ev.getSource()).stop();
        });
 
        // ── Animación de fondo (partículas) ───────────────────────────────
        dotTimer = new Timer(40, ev -> {
            dotPhase += 0.03f;
            root.repaint();
        });
 
        // Hacer arrastrable (sin barra de título)
        addDragSupport(root);
 
        fadeTimer.start();
        dotTimer.start();
    }
 
    /** Pinta el fondo oscuro con partículas de libros/puntos flotantes */
    private void paintBackground(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
 
        // Gradiente radial de fondo
        g2.setColor(C_BG_DARK);
        g2.fillRect(0, 0, w, h);
 
        // Glow central sutil
        RadialGradientPaint glow = new RadialGradientPaint(
            new Point2D.Float(w / 2f, h / 2f),
            Math.min(w, h) * 0.65f,
            new float[]{0f, 1f},
            new Color[]{new Color(30, 45, 80, 60), new Color(0, 0, 0, 0)}
        );
        g2.setPaint(glow);
        g2.fillRect(0, 0, w, h);
 
        // Partículas flotantes (puntos dorados)
        g2.setColor(new Color(212, 175, 95, 30));
        int[][] pts = {{80,60},{680,80},{40,400},{700,300},{150,470},{620,450},{300,30},{460,490}};
        for (int i = 0; i < pts.length; i++) {
            float rx = (float) (pts[i][0] + 12 * Math.sin(dotPhase + i));
            float ry = (float) (pts[i][1] + 10 * Math.cos(dotPhase * 0.7f + i));
            float r = 2.5f + (float) Math.sin(dotPhase * 1.3f + i) * 1.5f;
            g2.fillOval((int) rx, (int) ry, (int)(r*2), (int)(r*2));
        }
 
        // Líneas decorativas horizontales tenues
        g2.setColor(new Color(212, 175, 95, 10));
        g2.setStroke(new BasicStroke(1f));
        for (int y = 40; y < h; y += 60) {
            g2.drawLine(0, y, w, y);
        }
    }
 
    private JButton buildEnterButton() {
        JButton btn = new JButton("  INGRESAR AL SISTEMA  ▶") {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hovered ? C_BTN_HOVER : C_GOLD;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(hovered ? C_BG_DARK : C_BG_DARK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int tx = (getWidth() - fm.stringWidth(text)) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, tx, ty);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(300, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
 
    private JButton buildCloseButton() {
        JButton btn = new JButton("✕");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(C_GREY);
        btn.setBackground(new Color(30, 30, 50));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e)  { btn.setForeground(C_GREY); }
        });
        return btn;
    }
 
    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bar.setBackground(C_BG_DARK);
        bar.setBorder(new EmptyBorder(6, 0, 10, 0));
        JLabel lbl = new JLabel("© 2025 Universidad Mariano Gálvez de Guatemala  •  Todos los derechos reservados");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lbl.setForeground(new Color(60, 60, 80));
        bar.add(lbl);
        return bar;
    }
 
    /** Permite arrastrar la ventana sin barra de título */
    private void addDragSupport(JComponent c) {
        final Point[] origin = {null};
        c.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { origin[0] = e.getPoint(); }
        });
        c.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (origin[0] != null) {
                    Point loc = getLocation();
                    setLocation(loc.x + e.getX() - origin[0].x, loc.y + e.getY() - origin[0].y);
                }
            }
        });
    }
 
    // ──────────────────────────────────────────────────────────────────────────
    //  Componentes internos de apoyo
    // ──────────────────────────────────────────────────────────────────────────
 
    /** Panel wrapper que aplica composición de alpha para el fade-in */
    @SuppressWarnings("serial")
    static class AlphaCompositePanel extends JPanel {
        private float alpha = 1f;
        AlphaCompositePanel(JComponent child) {
            setOpaque(false);
            setLayout(new GridBagLayout());
            add(child, new GridBagConstraints());
        }
        void setAlpha(float a) { this.alpha = a; }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paintComponent(g2);
            g2.dispose();
        }
        @Override
        protected void paintChildren(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paintChildren(g2);
            g2.dispose();
        }
    }
 
    /** Canvas que dibuja un ícono de libro estilizado con color dorado */
    @SuppressWarnings("serial")
    static class BookIconCanvas extends JPanel {
        BookIconCanvas(int w, int h) {
            setPreferredSize(new Dimension(w, h));
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int W = getWidth(), H = getHeight();
            // Glow detrás del libro
            RadialGradientPaint glow = new RadialGradientPaint(
                new Point2D.Float(W/2f, H/2f), W*0.5f,
                new float[]{0f, 1f},
                new Color[]{new Color(212,175,95,50), new Color(0,0,0,0)}
            );
            g2.setPaint(glow);
            g2.fillOval(0, 0, W, H);
            // Libro: tapa trasera
            g2.setColor(new Color(160, 130, 60));
            g2.fillRoundRect(W/2-18, H/2-24, 36, 42, 5, 5);
            // Lomo
            g2.setColor(new Color(140, 110, 45));
            g2.fillRect(W/2-18, H/2-24, 7, 42);
            // Tapa delantera
            g2.setColor(new Color(212, 175, 95));
            g2.fillRoundRect(W/2-10, H/2-26, 32, 44, 5, 5);
            // Líneas de páginas
            g2.setColor(new Color(255,255,255,90));
            g2.setStroke(new BasicStroke(1f));
            for (int i = 0; i < 5; i++) {
                int ly = H/2 - 18 + i * 7;
                g2.drawLine(W/2 - 4, ly, W/2 + 16, ly);
            }
            // Destello en esquina
            g2.setColor(new Color(255,255,200,120));
            g2.fillOval(W/2+12, H/2-22, 8, 8);
            g2.dispose();
        }
    }
}