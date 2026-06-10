package com.dio.models;

public class SudokuTableItem {
  
    private int value;
    private boolean isEditable;

    public SudokuTableItem(int value, boolean isEditable) {
        this.value = value;
        this.isEditable = isEditable;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}
