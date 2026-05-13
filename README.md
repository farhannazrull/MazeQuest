# MazeQuest — BFS vs DFS Visualizer

> **Course:** EF234405 Design & Analysis of Algorithms — 2025/2026 (Semester 6)
> **Assignment:** Quiz 2 — Group Project
> **Stack:** Java 21 + Swing (no external dependencies)

MazeQuest is an interactive maze-solving visualizer that demonstrates two
fundamental graph traversal algorithms from the course, **Breadth-First
Search (BFS)** and **Depth-First Search (DFS)**. Generate a fresh perfect
maze, watch each algorithm explore it cell-by-cell, and compare them
side-by-side on the same grid.

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/7fcf718f-5a91-4f3b-be28-95ffdbd56b6c" />

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

## 🖥️ Features

-  Dark-themed animated GUI (Java Swing)
-  Adjustable maze sizes: 11×11, 15×15, 21×21, 31×31, 41×41, 75×75, 90×90
-  Adjustable tracking speed
-  Step-by-step animation with progress bar
-  Stats display: cells visited & path length
-  Color-coded visualization:
  -  Green = Start
  -  Red = End
  -  Blue = Visited cells
  -  Yellow = Solution path
  -  Orange = Current cell

---

## Project structure

```
MazeQuest/
├── src/
│   ├── Main.java
│   ├── Maze.java          → Generator maze
│   ├── BFS.java           → Algoritma BFS
│   ├── DFS.java           → Algoritma DFS
│   └── MazePanel.java     → Animation, GUI 
├── README.md
└── .gitignore

```

## Build and run

Requires **JDK 17 or newer** (we tested on JDK 21).

```bash

```

## Using the app


## Team

| Member | Student ID | Contribution |
|---|---|---|
| Mohammad Farhan Nazrul Ilhami | 5025231053 | 33.33% — Project setup, GitHub repo, BFS Algorithm |
| Rafael Jonathan | 5025231252 | 33.33% — Project Structure , DFS Algorithm, Maze Generator |
| Ulil Amry Ghovary | 5025231125 | 33.33% — UI Animation, Report  |
