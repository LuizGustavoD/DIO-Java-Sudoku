package com.dio.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.dio.models.SudokuFullTable;
import com.dio.models.SudokuTableItem;
import com.dio.models.size.EmptyNumbersRange;
import com.dio.models.size.GameSize;

public class SudokuTableGenerator {
  
  private GameSize gameSize;
  private EmptyNumbersRange emptyNumbersRange;
  private final Random random = new Random();

  public SudokuTableGenerator(GameSize gameSize, EmptyNumbersRange emptyNumbersRange) {
    this.gameSize = gameSize;
    this.emptyNumbersRange = emptyNumbersRange;
  }

  public SudokuFullTable getTable() {
    return generateTable();
  }

  private SudokuFullTable generateTable() {
    try{
      SudokuFullTable initTable = generateSolvedTable(gameSize.getSize());
      SudokuFullTable sudokuFinalTableGame = createPlayableTable(initTable);
      return sudokuFinalTableGame;
    }
    catch (Exception e){
      System.out.println("Error generating table: " + e.getMessage());
      return null;
    }
  }

  private SudokuFullTable generateSolvedTable(int size){
    int subgridSize = (int) Math.sqrt(size);
    if (subgridSize * subgridSize != size) {
      throw new IllegalArgumentException("Tamanho inválido para Sudoku: " + size);
    }

    int[][] solved = new int[size][size];

    List<Integer> numbers = new ArrayList<>();
    for (int value = 1; value <= size; value++) {
      numbers.add(value);
    }
    Collections.shuffle(numbers, random);

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        int base = (row * subgridSize + row / subgridSize + col) % size;
        solved[row][col] = numbers.get(base);
      }
    }

    List<Integer> rowOrder = shuffledIndexesBySubgrid(size, subgridSize);
    List<Integer> colOrder = shuffledIndexesBySubgrid(size, subgridSize);

    SudokuTableItem[][] result = new SudokuTableItem[size][size];
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        int value = solved[rowOrder.get(row)][colOrder.get(col)];
        result[row][col] = new SudokuTableItem(value, false);
      }
    }

    return new SudokuFullTable(result);
  }

  private List<Integer> shuffledIndexesBySubgrid(int size, int subgridSize) {
    List<Integer> blockIndexes = new ArrayList<>();
    for (int block = 0; block < size / subgridSize; block++) {
      blockIndexes.add(block);
    }
    Collections.shuffle(blockIndexes, random);

    List<Integer> ordered = new ArrayList<>(size);
    for (int block : blockIndexes) {
      List<Integer> inner = new ArrayList<>();
      int start = block * subgridSize;
      for (int i = 0; i < subgridSize; i++) {
        inner.add(start + i);
      }
      Collections.shuffle(inner, random);
      ordered.addAll(inner);
    }

    return ordered;
  }

  private SudokuFullTable createPlayableTable(SudokuFullTable table){
    int emptyCount = calculateEmptyCount(table.size());

    int attempts = 0;
    while (emptyCount > 0 && attempts < table.size() * table.size() * 4) {
      int randomRow = random.nextInt(gameSize.getSize());
      int randomCol = random.nextInt(gameSize.getSize());
      SudokuTableItem item = table.getItem(randomRow, randomCol);
      if (item.getValue() != 0) {
        table.setItem(randomRow, randomCol, new SudokuTableItem(0, true));
        emptyCount--;
      }
      attempts++;
    }

    return table;
  }

  private int calculateEmptyCount(int size) {
    int start = Math.max(0, emptyNumbersRange.start());
    int end = Math.max(start, emptyNumbersRange.end());

    int maxAllowed = size * size - 1;
    if (start > maxAllowed) {
      start = maxAllowed;
    }
    if (end > maxAllowed) {
      end = maxAllowed;
    }

    return start + random.nextInt(end - start + 1);
  }

}
