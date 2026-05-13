import java.util.*;

public class Maze {
    public static final int WALL = 0;
    public static final int PATH = 1;
    public static final int START = 2;
    public static final int END = 3;

    private int rows;
    private int cols;
    private int[][] grid;
    private Random random = new Random();

    public Maze(int rows, int cols) {
        this.rows = (rows % 2 == 0) ? rows + 1 : rows;
        this.cols = (cols % 2 == 0) ? cols + 1 : cols;
        this.grid = new int[this.rows][this.cols];
        generate();
    }

    private void generate() {
        for (int[] row : grid)
            Arrays.fill(row, WALL);

        carve(1, 1);
        grid[1][1] = START;
        grid[rows - 2][cols - 2] = END;
    }

    private void carve(int r, int c) {
        grid[r][c] = PATH;

        int[][] directions = {{0, 2}, {0, -2}, {2, 0}, {-2, 0}};
        shuffleArray(directions);

        for (int[] dir : directions) {
            int nr = r + dir[0];
            int nc = c + dir[1];

            if (nr > 0 && nr < rows - 1 && nc > 0 && nc < cols - 1 && grid[nr][nc] == WALL) {
                // Remove
                grid[r + dir[0] / 2][c + dir[1] / 2] = PATH;
                carve(nr, nc);
            }
        }
    }

    private void shuffleArray(int[][] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getCell(int r, int c) { return grid[r][c]; }
    public int[][] getGrid() { return grid; }

    public int[] getStart() { return new int[]{1, 1}; }
    public int[] getEnd() { return new int[]{rows - 2, cols - 2}; }

    public boolean isWall(int r, int c) {
        return grid[r][c] == WALL;
    }

    public boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }
}
