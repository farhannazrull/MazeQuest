import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BFS {
    private final Maze maze;
    private List<int[]> visitedOrder;
    private List<int[]> finalPath;

    public BFS(Maze maze) {
        this.maze = maze;
        this.visitedOrder = new ArrayList<>();
        this.finalPath = new ArrayList<>();
    }

    public void solve() {
        visitedOrder.clear();
        finalPath.clear();

        int rows = maze.getRows();
        int cols = maze.getCols();
        int[] start = maze.getStart();
        int[] end = maze.getEnd();

        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new ArrayDeque<>();
        Map<String, String> parent = new HashMap<>();

        queue.offer(new int[]{start[0], start[1]});
        visited[start[0]][start[1]] = true;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        boolean found = false;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0];
            int c = cell[1];
            visitedOrder.add(new int[]{r, c});

            if (r == end[0] && c == end[1]) {
                found = true;
                break;
            }

            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];
                if (maze.inBounds(nr, nc) && !visited[nr][nc] && !maze.isWall(nr, nc)) {
                    visited[nr][nc] = true;
                    queue.offer(new int[]{nr, nc});
                    parent.put(key(nr, nc), key(r, c));
                }
            }
        }

        if (found) {
            buildPath(parent, end, start);
        }
    }

    private void buildPath(Map<String, String> parent, int[] end, int[] start) {
        Deque<int[]> stack = new ArrayDeque<>();
        String current = key(end[0], end[1]);
        String startKey = key(start[0], start[1]);

        while (current != null) {
            String[] parts = current.split(",");
            stack.push(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
            if (current.equals(startKey)) {
                break;
            }
            current = parent.get(current);
        }

        finalPath = new ArrayList<>(stack);
        if (finalPath.isEmpty() || finalPath.get(0)[0] != start[0] || finalPath.get(0)[1] != start[1]) {
            finalPath = Collections.emptyList();
        }
    }

    private String key(int r, int c) {
        return r + "," + c;
    }

    public List<int[]> getVisitedOrder() {
        return visitedOrder;
    }

    public List<int[]> getFinalPath() {
        return finalPath;
    }

    public String getStats() {
        return String.format("BFS - Visited: %d cells | Path Length: %d steps",
                visitedOrder.size(), finalPath.size());
    }
}
