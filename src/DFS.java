import java.util.*;

public class DFS {
    private Maze maze;
    private List<int[]> visitedOrder;  
    private List<int[]> finalPath;     

    public DFS(Maze maze) {
        this.maze = maze;
        this.visitedOrder = new ArrayList<>();
        this.finalPath = new ArrayList<>();
    }

    public void solve() {
        visitedOrder.clear();
        finalPath.clear();

        int[] start = maze.getStart();
        int[] end = maze.getEnd();

        int rows = maze.getRows();
        int cols = maze.getCols();

        boolean[][] visited = new boolean[rows][cols];
        List<int[]> currentPath = new ArrayList<>();

        dfsRecursive(start[0], start[1], end[0], end[1], visited, currentPath);
    }

    private boolean dfsRecursive(int r, int c, int endR, int endC,
                                  boolean[][] visited, List<int[]> currentPath) {
        if (!maze.inBounds(r, c) || visited[r][c] || maze.isWall(r, c))
            return false;

        visited[r][c] = true;
        visitedOrder.add(new int[]{r, c});
        currentPath.add(new int[]{r, c});

        if (r == endR && c == endC) {
            finalPath = new ArrayList<>(currentPath);
            return true;
        }

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            if (dfsRecursive(r + dir[0], c + dir[1], endR, endC, visited, currentPath))
                return true;
        }

        currentPath.remove(currentPath.size() - 1);
        return false;
    }

    public List<int[]> getVisitedOrder() { return visitedOrder; }
    public List<int[]> getFinalPath() { return finalPath; }

    public String getStats() {
        return String.format("DFS — Visited: %d cells | Path Length: %d steps",
                visitedOrder.size(), finalPath.size());
    }
}
