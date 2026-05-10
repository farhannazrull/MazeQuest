# MazeQuest — BFS vs DFS Visualizer

> **Course:** EF234405 Design & Analysis of Algorithms — 2025/2026 (Semester 6)
> **Assignment:** Quiz 2 — Group Project
> **Stack:** Java 21 + Swing (no external dependencies)

MazeQuest is an interactive maze-solving visualizer that demonstrates two
fundamental graph traversal algorithms from the course — **Breadth-First
Search (BFS)** and **Depth-First Search (DFS)**. Generate a fresh perfect
maze, watch each algorithm explore it cell-by-cell, and compare them
side-by-side on the same grid.

[gambar: BFS solving a 25x25 maze]

## Why this project?

The maze is the cleanest possible illustration of an unweighted graph:
cells are vertices, "no wall between" is an edge. Running BFS and DFS on
the same maze makes their differences immediately visible — and the
visual mapping is intuitive enough that anyone watching gets it without
needing the theory first.

In a *perfect maze* (exactly one path between any two cells) BFS and DFS
must return paths of equal length. Where they differ is **how much of
the maze each one has to look at** before it gets there — and that's the
story this project tells.

## Algorithms implemented

| Algorithm | Data structure | Path optimality | Complexity |
|---|---|---|---|
| BFS | FIFO queue | Shortest path on unweighted graphs | O(V + E) time, O(V) space |
| DFS | LIFO stack | Any path (not necessarily shortest) | O(V + E) time, O(V) space |

A third algorithm is hiding in the maze *generator* itself — the
recursive backtracker is randomized DFS over cells, removing walls as it
goes.

## Project structure

```

```

## Build and run

Requires **JDK 17 or newer** (we tested on JDK 21).

```bash

```

## Using the app


## Team

| Member | Student ID | Contribution |
|---|---|---|
| Mohammad Farhan Nazrul Ilhami | 5025231053 | 33.33% — Project setup, GitHub repo, ... |
| Rafael Jonathan | 5025 | 33.33% — ... |
| Ulil Amry Ghovary | 5025 | 33.33% — ... |
