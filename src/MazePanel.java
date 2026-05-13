import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MazePanel extends JPanel {
    private Maze maze;
    private List<int[]> visitedOrder = new ArrayList<>();
    private List<int[]> finalPath = new ArrayList<>();
    private int visitedStep = 0;
    private int pathStep = 0;
    private Timer animationTimer;
    private String overlayText = "Press Generate, then BFS or DFS";
    private Color glowColor = new Color(122, 92, 255);

    public MazePanel(Maze maze) {
        this.maze = maze;
        setOpaque(true);
        setBackground(new Color(8, 11, 24));
        setDoubleBuffered(true);
    }

    public void setMaze(Maze maze) {
        stopAnimation();
        this.maze = maze;
        visitedOrder = new ArrayList<>();
        finalPath = new ArrayList<>();
        visitedStep = 0;
        pathStep = 0;
        overlayText = "New maze generated";
        repaint();
    }

    public void animateSolution(List<int[]> visited, List<int[]> path, String label, Color tone, Runnable onComplete) {
        stopAnimation();
        this.visitedOrder = (visited == null) ? new ArrayList<>() : visited;
        this.finalPath = (path == null) ? new ArrayList<>() : path;
        this.visitedStep = 0;
        this.pathStep = 0;
        this.glowColor = tone;
        this.overlayText = label;

        animationTimer = new Timer(17, e -> {
            if (visitedStep < visitedOrder.size()) {
                visitedStep += 2;
            } else if (pathStep < finalPath.size()) {
                pathStep += 1;
            } else {
                ((Timer) e.getSource()).stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
            repaint();
        });
        animationTimer.start();
    }

    public void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (maze == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int rows = maze.getRows();
        int cols = maze.getCols();
        int margin = 24;
        int availableW = getWidth() - margin * 2;
        int availableH = getHeight() - margin * 2;
        int cell = Math.max(6, Math.min(availableW / cols, availableH / rows));
        int mazeW = cell * cols;
        int mazeH = cell * rows;
        int x0 = (getWidth() - mazeW) / 2;
        int y0 = (getHeight() - mazeH) / 2;

        drawPanelFrame(g2, x0 - 12, y0 - 12, mazeW + 24, mazeH + 24);
        drawGrid(g2, x0, y0, cell, rows, cols);
        drawVisited(g2, x0, y0, cell);
        drawPath(g2, x0, y0, cell);
        drawStartEnd(g2, x0, y0, cell);
        drawOverlay(g2);

        g2.dispose();
    }

    private void drawPanelFrame(Graphics2D g2, int x, int y, int w, int h) {
        GradientPaint gp = new GradientPaint(x, y, new Color(24, 30, 57), x + w, y + h, new Color(16, 20, 40));
        g2.setPaint(gp);
        g2.fillRoundRect(x, y, w, h, 22, 22);
        g2.setColor(new Color(90, 102, 154, 150));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, w, h, 22, 22);
    }

    private void drawGrid(Graphics2D g2, int x0, int y0, int cell, int rows, int cols) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int val = maze.getCell(r, c);
                if (val == Maze.WALL) {
                    g2.setColor(new Color(24, 30, 56));
                } else {
                    g2.setColor(new Color(225, 232, 246));
                }
                g2.fillRect(x0 + c * cell, y0 + r * cell, cell, cell);
            }
        }
    }

    private void drawVisited(Graphics2D g2, int x0, int y0, int cell) {
        int max = Math.min(visitedStep, visitedOrder.size());
        g2.setColor(new Color(91, 140, 255, 185));
        for (int i = 0; i < max; i++) {
            int[] p = visitedOrder.get(i);
            g2.fillRect(x0 + p[1] * cell, y0 + p[0] * cell, cell, cell);
        }
    }

    private void drawPath(Graphics2D g2, int x0, int y0, int cell) {
        int max = Math.min(pathStep, finalPath.size());
        g2.setColor(new Color(20, 231, 196, 215));
        for (int i = 0; i < max; i++) {
            int[] p = finalPath.get(i);
            g2.fillRect(x0 + p[1] * cell, y0 + p[0] * cell, cell, cell);
        }
    }

    private void drawStartEnd(Graphics2D g2, int x0, int y0, int cell) {
        int[] start = maze.getStart();
        int[] end = maze.getEnd();

        g2.setColor(new Color(66, 245, 147));
        g2.fillRoundRect(x0 + start[1] * cell, y0 + start[0] * cell, cell, cell, 8, 8);

        g2.setColor(new Color(255, 92, 122));
        g2.fillRoundRect(x0 + end[1] * cell, y0 + end[0] * cell, cell, cell, 8, 8);
    }

    private void drawOverlay(Graphics2D g2) {
        int pad = 14;
        Font font = new Font("Segoe UI", Font.BOLD, 16);
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(overlayText);
        int boxW = textW + pad * 2;
        int boxH = fm.getHeight() + 10;
        int x = (getWidth() - boxW) / 2;
        int y = 12;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(x, y, boxW, boxH, 14, 14);
        g2.setColor(glowColor);
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawRoundRect(x, y, boxW, boxH, 14, 14);
        g2.setColor(new Color(20, 26, 48));
        g2.drawString(overlayText, x + pad + 1, y + fm.getAscent() + 4);
        g2.setColor(Color.WHITE);
        g2.drawString(overlayText, x + pad, y + fm.getAscent() + 3);
    }
}
