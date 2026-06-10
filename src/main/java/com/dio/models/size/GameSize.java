package com.dio.models.size;

public enum GameSize {
    SIZE_4X4(4),
    SIZE_9X9(9),
    SIZE_16X16(16);

    private final int size;

    GameSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
  
}
