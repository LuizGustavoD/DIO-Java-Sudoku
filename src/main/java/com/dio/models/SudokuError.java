package com.dio.models;

public class SudokuError {
  
  private String errorMessage;
  private int row;
  private int col;

  public SudokuError(String errorMessage, int row, int col) {
    this.errorMessage = errorMessage;
    this.row = row;
    this.col = col;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  

}
