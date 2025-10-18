import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.File;

/**
 * Reproductor musical con interfaz renovada (modo oscuro elegante)
 * Mantiene toda la funcionalidad original.
 */
public class App {
    private JFrame frame;
    private JTree tree;
    private JLabel imagenLabel;
    private JPanel controlsPanel;
    private JButton playButton, pauseButton, stopButton;
    private JLabel timeLabel;
    private Timer timer;
    private ReproductorMP3 reproductor;
    private ArtistaManager manager;
    private Cancion cancionSeleccionada;
    private int duracionTotal;
    private int tiempoActual;
    private boolean isPaused = false;

    public App() {
        try {
            manager = new ArtistaManager("datos/artistas.json");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error cargando artistas: " + e.getMessage());
            return;
        }
        reproductor = new ReproductorMP3();
        initUI();
    }

    private void initUI() {
        // 🎨 Tema oscuro elegante
        Color fondo = new Color(30, 30, 35);
        Color panelOscuro = new Color(45, 45, 55);
        Color azul = new Color(0, 122, 204);
        Color rojo = new Color(220, 53, 69);
        Color verde = new Color(46, 204, 113);
        Color texto = Color.WHITE;

        frame = new JFrame("🎧 Reproductor de Artistas y Canciones");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 480);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(fondo);
        frame.setLocationRelativeTo(null);

        // 🌳 Panel izquierdo: árbol de artistas y canciones
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Artistas");
        for (Artista artista : manager.getArtistas()) {
            DefaultMutableTreeNode artistaNode = new DefaultMutableTreeNode(artista);
            for (Cancion cancion : artista.getCanciones()) {
                artistaNode.add(new DefaultMutableTreeNode(cancion));
            }
            root.add(artistaNode);
        }

        tree = new JTree(root);
        tree.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tree.setBackground(panelOscuro);
        tree.setForeground(texto);
        tree.setRootVisible(true);
        tree.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setPreferredSize(new Dimension(240, 0));
        treeScroll.getViewport().setBackground(panelOscuro);
        frame.add(treeScroll, BorderLayout.WEST);

        // 🖼️ Panel derecho
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(fondo);

        imagenLabel = new JLabel();
        imagenLabel.setHorizontalAlignment(JLabel.CENTER);
        imagenLabel.setPreferredSize(new Dimension(340, 320));
        imagenLabel.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 80), 2));
        rightPanel.add(imagenLabel, BorderLayout.CENTER);

        // 🎚️ Panel de controles
        controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 10));
        controlsPanel.setBackground(panelOscuro);

        playButton = crearBoton("▶ Reproducir", verde, "imagenes/play.png");
        pauseButton = crearBoton("⏸ Pausar", azul, "imagenes/pause.png");
        stopButton = crearBoton("⏹ Detener", rojo, "imagenes/stop.png");

        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);

        controlsPanel.add(playButton);
        controlsPanel.add(pauseButton);
        controlsPanel.add(stopButton);

        // ⏱ Etiqueta del tiempo
        timeLabel = new JLabel("00:00 / 00:00");
        timeLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        timeLabel.setForeground(new Color(200, 200, 210));
        controlsPanel.add(timeLabel);

        rightPanel.add(controlsPanel, BorderLayout.SOUTH);
        frame.add(rightPanel, BorderLayout.CENTER);

        // 🎧 Listeners
        tree.addTreeSelectionListener(e -> onTreeSelection());
        playButton.addActionListener(e -> onPlay());
        pauseButton.addActionListener(e -> onPause());
        stopButton.addActionListener(e -> onStop());

        frame.setVisible(true);
    }

    private JButton crearBoton(String texto, Color color, String iconPath) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Ícono (si existe)
        if (iconPath != null && new File(iconPath).exists()) {
            ImageIcon icon = new ImageIcon(iconPath);
            btn.setIcon(new ImageIcon(icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        }

        // Efectos hover y clic
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(color.brighter());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    private void onTreeSelection() {
        TreePath path = tree.getSelectionPath();
        if (path == null) return;
        Object nodeObj = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();

        if (nodeObj instanceof Artista artista) {
            mostrarImagen(artista.getImagen());
            playButton.setEnabled(false);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
            cancionSeleccionada = null;
            timeLabel.setText("00:00 / 00:00");
        } else if (nodeObj instanceof Cancion cancion) {
            cancionSeleccionada = cancion;
            DefaultMutableTreeNode parentNode =
                (DefaultMutableTreeNode) ((DefaultMutableTreeNode) path.getLastPathComponent()).getParent();
            Artista artista = (Artista) parentNode.getUserObject();
            mostrarImagen(artista.getImagen());
            playButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
            duracionTotal = obtenerDuracionMP3(cancionSeleccionada.getArchivo());
            timeLabel.setText("00:00 / " + formatoTiempo(duracionTotal));
        }
    }

    private void mostrarImagen(String rutaImagen) {
        try {
            ImageIcon icon = new ImageIcon(rutaImagen);
            Image img = icon.getImage().getScaledInstance(340, 320, Image.SCALE_SMOOTH);
            imagenLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imagenLabel.setIcon(null);
        }
    }

    private void onPlay() {
        if (cancionSeleccionada != null) {
            reproductor.stop();
            reproductor.play(cancionSeleccionada.getArchivo());
            tiempoActual = 0;
            isPaused = false;
            playButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            iniciarTimer();
        }
    }

    private void onPause() {
        if (isPaused) {
            reproductor.resume();
            isPaused = false;
            pauseButton.setText("⏸ Pausar");
        } else {
            reproductor.pause();
            isPaused = true;
            pauseButton.setText("▶ Reanudar");
        }
    }

    private void onStop() {
        reproductor.stop();
        detenerTimer();
        tiempoActual = 0;
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        pauseButton.setText("⏸ Pausar");
        timeLabel.setText("00:00 / " + formatoTiempo(duracionTotal));
    }

    private void iniciarTimer() {
        detenerTimer();
        timer = new Timer(1000, e -> {
            if (!isPaused) {
                tiempoActual++;
                if (tiempoActual > duracionTotal) {
                    onStop();
                } else {
                    timeLabel.setText(formatoTiempo(tiempoActual) + " / " + formatoTiempo(duracionTotal));
                }
            }
        });
        timer.start();
    }

    private void detenerTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private int obtenerDuracionMP3(String rutaArchivo) {
        try {
            File file = new File(rutaArchivo);
            long bytes = file.length();
            int bitrate = 128 * 1024 / 8;
            return (int) (bytes / bitrate);
        } catch (Exception e) {
            return 0;
        }
    }

    private String formatoTiempo(int segundos) {
        int min = segundos / 60;
        int seg = segundos % 60;
        return String.format("%02d:%02d", min, seg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}
