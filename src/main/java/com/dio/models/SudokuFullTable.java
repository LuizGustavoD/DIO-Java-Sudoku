package com.dio.models;

import java.util.ArrayList;
import java.util.List;

public class SudokuFullTable {

    private SudokuTableItem[][] table;
    private List<SudokuError> errors;

    public List<SudokuError> getErrors() {
      return errors;
    }

    public void setErrors(List<SudokuError> errors) {
      this.errors = errors;
    }

    public SudokuFullTable(SudokuTableItem[][] table) {
        this.table = table;
        this.errors = new ArrayList<>();
    }

    public SudokuTableItem[][] getTable() {
        return table;
    }

    public void setTable(SudokuTableItem[][] table) {
        this.table = table;
    }

    public SudokuTableItem getItem(int row, int col) {
        return table[row][col];
    }

    public void setItem(int row, int col, SudokuTableItem item) {
        table[row][col] = item;
    }

    public boolean isEditable(int row, int col) {
        return table[row][col].isEditable();
    }

    public int size() {
        return table.length;
    }

    public void clearErrors() {
        errors.clear();
    }
    
}
