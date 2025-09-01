package com.example.a2048game;

import java.util.Random;

public class Game2048 {
    private int[][] grid = new int[4][4];
    private Random random = new Random();

    public int[][] getGrid() {
        return grid;
    }

    public void startGame(){
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                grid[i][j] = 0;
            }
        }
        addNewTile();
        addNewTile();
    }
    public void addNewTile() {
        int row, col;
        do {
            row = random.nextInt(4);
            col = random.nextInt(4);
        } while (grid[row][col] != 0);

        grid[row][col] = random.nextInt(10) < 9 ? 2 : 4;

        System.out.println("💡 Pridaná nová dlaždica na (" + row + "," + col + ") = " + grid[row][col]);
    }


    private int[] processRow(int[] row) {
        int[] newRow = new int[4];
        int pos = 0;

        // 1. stlačiť doľava
        for (int j = 0; j < 4; j++) {
            if (row[j] != 0) {
                newRow[pos++] = row[j];
            }
        }

        // 2. merge
        for (int j = 0; j < 3; j++) {
            if (newRow[j] != 0 && newRow[j] == newRow[j + 1]) {
                newRow[j] *= 2;
                newRow[j + 1] = 0;
            }
        }

        // 3. znova stlačiť
        int[] finalRow = new int[4];
        pos = 0;
        for (int j = 0; j < 4; j++) {
            if (newRow[j] != 0) {
                finalRow[pos++] = newRow[j];
            }
        }

        return finalRow;
    }
    public boolean moveLeft() {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            int[] finalRow = processRow(grid[i]);
            if (!java.util.Arrays.equals(grid[i], finalRow)) {
                grid[i] = finalRow;
                moved = true;
            }
        }
        if (moved) addNewTile();
        return moved;
    }

    private int[] reverseRow(int[]row){
        int[] newRow = new int[4];
        for (int j=0;j<4;j++){
            newRow[j] = row[3-j];
        }
        return newRow;
    }

    private void transposeGrid(){
        int[][] newGrid = new int[4][4];
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                newGrid[i][j] = grid[j][i];
            }
        }
        grid = newGrid;
    }

    public boolean moveRight(){
        boolean moved = false;
        for (int i=0;i<4;i++){
            int[] reversedRow = reverseRow(grid[i]);
            int[] finalRow = processRow(reversedRow);
            finalRow = reverseRow(finalRow);
            if (!java.util.Arrays.equals(grid[i], finalRow)){
                grid[i] = finalRow;
                moved = true;
            }
        }
        if (moved) addNewTile();
        return moved;
    }

    public boolean moveUp(){
        transposeGrid();
        boolean moved = moveLeft();
        transposeGrid();
        return moved;
    }

    public boolean moveDown(){
        transposeGrid();
        boolean moved = moveRight();
        transposeGrid();
        return moved;
    }





}
