import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MazeFrame extends JFrame {
    private static final Font UI_FONT_BOLD = new Font("Segoe UI", Font.BOLD, 16);
    private static final Color BTN_BASE_BG = new Color(128, 180, 255);
    private static final Color BTN_HOVER_BG = new Color(156, 201, 255);
    private static final Color BTN_TEXT = new Color(10, 27, 58);
    private static final Color BTN_DISABLED_BG = new Color(64, 74, 102);
    private static final Color BTN_DISABLED_TEXT = new Color(187, 197, 224);
    private Maze maze;
    private MazePanel mazePanel;
    private JLabel titleLabel;
    private JLabel statsLabel;
    private JLabel sizeLabel;
    private JComboBox<String> sizeSelector;
    /** Internal 1 (lambat) … 100 (cepat); UI hanya Slow–Fast. */
    private JSlider speedSlider;
    private JLabel slowSpeedLabel;
    private JLabel fastSpeedLabel;
    private JPanel controlsPanel;

    private static final int SPEED_MIN = 1;
    private static final int SPEED_MAX = 100;
    private static final int SPEED_DEFAULT = 50;
    /** Delay minimum (ms) saat slider = cepat. */
    private static final int ANIM_DELAY_MIN_MS = 2;
    /** Delay maksimum (ms) saat slider = lambat. */
    private static final int ANIM_DELAY_MAX_MS = 120;
    private StyledButton generateBtn;
    private StyledButton bfsBtn;
    private StyledButton dfsBtn;
    private boolean algorithmRunning = false;

    public MazeFrame() {
        super("MazeQuest - BFS vs DFS");
        this.maze = new Maze(25, 25);
        initLookAndFeel();
        initUi();
        setVisible(true);
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException ignored) {
            // Use default look if system look cannot be applied.
        }
    }

    private void initUi() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 760);
        setMinimumSize(new Dimension(920, 640));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(9, 13, 28));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateResponsiveControls();
            }
        });
        updateResponsiveControls();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 0, 16));
        panel.setOpaque(false);

        titleLabel = new JLabel("MazeQuest");
        titleLabel.setForeground(new Color(232, 240, 255));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        panel.add(titleLabel, BorderLayout.WEST);

        JLabel subtitle = new JLabel("BFS DFS Comparison ", SwingConstants.RIGHT);
        subtitle.setForeground(new Color(195, 210, 245));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(subtitle, BorderLayout.EAST);

        return panel;
    }

    private JPanel buildCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        mazePanel = new MazePanel(maze);
        panel.add(mazePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildBottomBar() {
        JPanel wrapper = new JPanel(new GridLayout(2, 1, 0, 8));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));

        controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        controlsPanel.setBackground(new Color(16, 21, 40));
        controlsPanel.setBorder(BorderFactory.createLineBorder(new Color(78, 93, 140), 1, true));

        String[] sizes = {"11x11", "15x15", "21x21", "25x25", "31x31", "41x41", "75x75", "90x90"};
        sizeSelector = new JComboBox<>(sizes);
        sizeSelector.setSelectedItem("25x25");
        sizeSelector.setFont(UI_FONT_BOLD);
        sizeSelector.setForeground(new Color(19, 28, 54));
        sizeSelector.setBackground(new Color(236, 242, 255));

        generateBtn = createStyledButton("Generate", false);
        bfsBtn = createStyledButton("BFS", true);
        dfsBtn = createStyledButton("DFS", true);

        generateBtn.addActionListener(e -> generateMaze());
        bfsBtn.addActionListener(e -> runBfs());
        dfsBtn.addActionListener(e -> runDfs());

        sizeLabel = new JLabel("Maze Size:");
        sizeLabel.setForeground(new Color(236, 242, 255));
        sizeLabel.setFont(UI_FONT_BOLD);
        controlsPanel.add(sizeLabel);
        controlsPanel.add(sizeSelector);
        controlsPanel.add(generateBtn);
        controlsPanel.add(bfsBtn);
        controlsPanel.add(dfsBtn);

        JPanel speedStrip = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        speedStrip.setOpaque(false);
        slowSpeedLabel = new JLabel("Slow");
        slowSpeedLabel.setForeground(new Color(195, 210, 245));
        fastSpeedLabel = new JLabel("Fast");
        fastSpeedLabel.setForeground(new Color(195, 210, 245));

        speedSlider = new JSlider(SwingConstants.HORIZONTAL, SPEED_MIN, SPEED_MAX, SPEED_DEFAULT);
        speedSlider.setOpaque(false);
        speedSlider.setPaintTicks(false);
        speedSlider.setPaintLabels(false);
        speedSlider.setFocusable(false);

        speedSlider.addChangeListener(e ->
                mazePanel.setAnimationSpeed(delayMsFromSpeedScale(speedSlider.getValue())));

        speedStrip.add(slowSpeedLabel);
        speedStrip.add(speedSlider);
        speedStrip.add(fastSpeedLabel);
        controlsPanel.add(speedStrip);

        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(new Color(16, 21, 40));
        info.setBorder(BorderFactory.createLineBorder(new Color(78, 93, 140), 1, true));
        statsLabel = new JLabel("Ready. Generate maze and run an algorithm.", SwingConstants.CENTER);
        statsLabel.setForeground(new Color(244, 248, 255));
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        info.add(statsLabel, BorderLayout.CENTER);

        wrapper.add(controlsPanel);
        wrapper.add(info);
        return wrapper;
    }

    private void updateResponsiveControls() {
        if (controlsPanel == null) {
            return;
        }
        int w = getWidth();
        int controlFontSize = clamp(w / 65, 14, 19);
        int smallBtnW = clamp(w / 14, 64, 96);
        int smallBtnH = clamp(w / 32, 26, 36);
        int genBtnW = clamp(w / 11, 78, 115);
        int selectorWidth = clamp(w / 10, 88, 126);
        int rowHeight = Math.max(smallBtnH, 30);
        int hGap = clamp(w / 90, 6, 14);
        int sliderW = clamp(w / 7, 100, 200);

        Font controlFont = new Font("Segoe UI", Font.BOLD, controlFontSize);
        Font speedHintFont = new Font("Segoe UI", Font.PLAIN, Math.max(11, controlFontSize - 3));
        sizeLabel.setFont(controlFont);
        slowSpeedLabel.setFont(speedHintFont);
        fastSpeedLabel.setFont(speedHintFont);
        sizeSelector.setFont(controlFont);
        sizeSelector.setPreferredSize(new Dimension(selectorWidth, rowHeight));

        speedSlider.setPreferredSize(new Dimension(sliderW, 22));

        Font smallBtnFont = new Font("Segoe UI", Font.BOLD, Math.max(11, controlFontSize - 2));
        generateBtn.applyResponsiveSizing(controlFont, genBtnW, rowHeight);
        bfsBtn.applyResponsiveSizing(smallBtnFont, smallBtnW, smallBtnH);
        dfsBtn.applyResponsiveSizing(smallBtnFont, smallBtnW, smallBtnH);

        FlowLayout layout = (FlowLayout) controlsPanel.getLayout();
        layout.setHgap(hGap);
        controlsPanel.revalidate();
        controlsPanel.repaint();
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private StyledButton createStyledButton(String text, boolean compact) {
        return new StyledButton(text, compact);
    }

    private void generateMaze() {
        if (algorithmRunning) {
            return;
        }
        String selected = String.valueOf(sizeSelector.getSelectedItem());
        int size = Integer.parseInt(selected.split("x")[0]);
        maze = new Maze(size, size);
        mazePanel.setMaze(maze);
        statsLabel.setText("Maze generated (" + size + "x" + size + "). Choose BFS or DFS.");
    }

    private void runBfs() {
        if (algorithmRunning) {
            return;
        }
        setAlgorithmRunning(true);
        BFS bfs = new BFS(maze);
        bfs.solve();
        mazePanel.animateSolution(
                bfs.getVisitedOrder(),
                bfs.getFinalPath(),
                "BFS animation",
                new Color(91, 140, 255),
                delayMsFromSpeedScale(speedSlider.getValue()),
                () -> setAlgorithmRunning(false)
        );
        statsLabel.setText(bfs.getStats());
    }

    private void runDfs() {
        if (algorithmRunning) {
            return;
        }
        setAlgorithmRunning(true);
        DFS dfs = new DFS(maze);
        dfs.solve();
        mazePanel.animateSolution(
                dfs.getVisitedOrder(),
                dfs.getFinalPath(),
                "DFS animation",
                new Color(122, 92, 255),
                delayMsFromSpeedScale(speedSlider.getValue()),
                () -> setAlgorithmRunning(false)
        );
        statsLabel.setText(dfs.getStats());
    }

    private void setAlgorithmRunning(boolean running) {
        this.algorithmRunning = running;
        sizeSelector.setEnabled(!running);
        generateBtn.setEnabled(!running);
        bfsBtn.setEnabled(!running);
        dfsBtn.setEnabled(!running);
        if (running) {
            statsLabel.setText(statsLabel.getText() + "  |  Running animation...");
        }
    }

    /** Internal: 1 = lambat, 100 = cepat (dipetakan ke delay ms). */
    private int delayMsFromSpeedScale(int scale) {
        int v = clamp(scale, SPEED_MIN, SPEED_MAX);
        if (SPEED_MAX <= SPEED_MIN) {
            return ANIM_DELAY_MIN_MS;
        }
        int span = ANIM_DELAY_MAX_MS - ANIM_DELAY_MIN_MS;
        return ANIM_DELAY_MIN_MS + span * (SPEED_MAX - v) / (SPEED_MAX - SPEED_MIN);
    }

    private class StyledButton extends JButton {
        StyledButton(String text, boolean compact) {
            super(text);
            setFocusPainted(false);
            setFont(UI_FONT_BOLD);
            setMargin(compact ? new Insets(4, 8, 4, 8) : new Insets(6, 10, 6, 10));
            setBorder(BorderFactory.createLineBorder(new Color(43, 96, 191), 2));
            setOpaque(true);
            setForeground(BTN_TEXT);
            setBackground(BTN_BASE_BG);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (isEnabled()) {
                        setBackground(BTN_HOVER_BG);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (isEnabled()) {
                        setBackground(BTN_BASE_BG);
                    }
                }
            });
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            if (enabled) {
                setBackground(BTN_BASE_BG);
                setForeground(BTN_TEXT);
                setBorder(BorderFactory.createLineBorder(new Color(43, 96, 191), 2));
            } else {
                setBackground(BTN_DISABLED_BG);
                setForeground(BTN_DISABLED_TEXT);
                setBorder(BorderFactory.createLineBorder(new Color(52, 61, 86), 2));
            }
        }

        void applyResponsiveSizing(Font font, int width, int height) {
            setFont(font);
            setPreferredSize(new Dimension(width, height));
        }
    }
}
