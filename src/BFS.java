import java.util.*;

public class BFS {
    private Maze maze;
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

        int[] start = maze.getStart();
        int[] end = maze.getEnd();

        int rows = maze.getRows();
        int cols = maze.getCols();

        boolean[][] visited = new boolean[rows][cols];
        int[][][] parent = new int[rows][cols][2];
        for (int[][] row : parent) {
            for (int[] p : row) {
                Arrays.fill(p, -1);
            }
        }

        Queue<int[]> queue = new LinkedList<>();

        // Inisialisasi titik start
        queue.offer(start);
        visited[start[0]][start[1]] = true;
        visitedOrder.add(new int[]{start[0], start[1]});

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        boolean found = false;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];

            if (r == end[0] && c == end[1]) {
                found = true;
                break;
            }

            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                if (maze.inBounds(nr, nc) && !visited[nr][nc] && !maze.isWall(nr, nc)) {
                    visited[nr][nc] = true;
                    int[] nextNode = new int[]{nr, nc};

                    queue.offer(nextNode);
                    visitedOrder.add(nextNode);

                    parent[nr][nc] = new int[]{r, c};
                }
            }
        }

        if (found) {
            List<int[]> path = new ArrayList<>();
            int currR = end[0];
            int currC = end[1];

            while (currR != -1 && currC != -1) {
                path.add(new int[]{currR, currC});
                if (currR == start[0] && currC == start[1]) {
                    break;
                }
                int[] p = parent[currR][currC];
                currR = p[0];
                currC = p[1];
            }

            Collections.reverse(path);
            finalPath.addAll(path);
        }
    }

    public List<int[]> getVisitedOrder() {
        return visitedOrder;
    }

    public List<int[]> getFinalPath() {
        return finalPath;
    }

    public String getStats() {
        return String.format("BFS — Visited: %d cells | Path Length: %d steps",
                visitedOrder.size(), finalPath.size());
    }
}
