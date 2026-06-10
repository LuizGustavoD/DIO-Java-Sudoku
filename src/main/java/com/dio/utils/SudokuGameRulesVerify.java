package com.dio.utils;

import com.dio.models.SudokuError;
import com.dio.models.SudokuFullTable;

public class SudokuGameRulesVerify {
  
  private SudokuFullTable table;

  public SudokuGameRulesVerify(SudokuFullTable table) {
    this.table = table;
  }

  public boolean verifyUserInput(int row, int col, int value) {
    table.clearErrors();
    boolean canInsertValue = verifyRow(row, col, value)
      && verifyColumn(row, col, value)
      && verifySubgrid(row, col, value)
      && !userInputConflitsWithInitialValue(row, col);
    return canInsertValue;
  }

  public boolean verifyRow(int targetRow, int targetCol, int value) {
    for (int col = 0; col < table.getTable().length; col++) {
      if (col == targetCol) {
        continue;
      }
      if (table.getItem(targetRow, col).getValue() == value) {
        table.getErrors().add(new SudokuError("Valor já existe na linha", targetRow, col));
        return false;
      }
    }
    return true;
  }

  public boolean verifyColumn(int targetRow, int targetCol, int value) {
    for (int row = 0; row < table.getTable().length; row++) {
      if (row == targetRow) {
        continue;
      }
      if (table.getItem(row, targetCol).getValue() == value) {
        table.getErrors().add(new SudokuError("Valor já existe na coluna", row, targetCol));
        return false;
      }
    }
    return true;
  }

  public boolean verifySubgrid(int row, int col, int value) {
    int subgridSize = (int) Math.sqrt(table.getTable().length);
    int subgridRowStart = (row / subgridSize) * subgridSize;
    int subgridColStart = (col / subgridSize) * subgridSize;

    for (int r = subgridRowStart; r < subgridRowStart + subgridSize; r++) {
      for (int c = subgridColStart; c < subgridColStart + subgridSize; c++) {
        if (r == row && c == col) {
          continue;
        }
        if (table.getItem(r, c).getValue() == value) {
          table.getErrors().add(new SudokuError("Valor já existe na subgrade", r, c));
          return false;
        }
      }
    }
    return true;
  }

  public boolean userInputConflitsWithInitialValue(int row, int col) {
    if (table.isEditable(row, col)) {
      return false;
    }
    else{
      table.getErrors().add(new SudokuError("Valor conflita com valor inicial", row, col));
      return true;
    }
  }

}
