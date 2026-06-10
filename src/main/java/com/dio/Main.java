package com.dio;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.dio.models.SudokuError;
import com.dio.models.SudokuFullTable;
import com.dio.models.SudokuTableItem;
import com.dio.models.size.EmptyNumbersRange;
import com.dio.models.size.GameSize;
import com.dio.utils.SudokuGameRulesVerify;
import com.dio.utils.SudokuTableGenerator;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Sudoku Console ===");

        GameSize gameSize = chooseGameSize();
        EmptyNumbersRange emptyNumbersRange = chooseDifficulty(gameSize);

        SudokuTableGenerator generator = new SudokuTableGenerator(gameSize, emptyNumbersRange);
        SudokuFullTable table = generator.getTable();
        if (table == null) {
            System.out.println("Nao foi possivel gerar o tabuleiro.");
            return;
        }

        SudokuGameRulesVerify rulesVerify = new SudokuGameRulesVerify(table);

        while (true) {
            printBoard(table);

            if (isSolved(table)) {
                System.out.println("Parabens! Voce concluiu o Sudoku.");
                break;
            }

            System.out.println("Comando: linha coluna valor (1-base), 'clear linha coluna' ou 'sair'");
            System.out.print("> ");
            String command = SCANNER.nextLine().trim();

            if (command.equalsIgnoreCase("sair")) {
                System.out.println("Jogo encerrado.");
                break;
            }

            if (command.toLowerCase().startsWith("clear ")) {
                handleClearCommand(command, table);
                continue;
            }

            String[] parts = command.split("\\s+");
            if (parts.length != 3) {
                System.out.println("Entrada invalida. Exemplo: 1 3 9");
                continue;
            }

            try {
                int row = Integer.parseInt(parts[0]) - 1;
                int col = Integer.parseInt(parts[1]) - 1;
                int value = Integer.parseInt(parts[2]);

                if (!isInsideBoard(table, row, col)) {
                    System.out.println("Linha/coluna fora do limite.");
                    continue;
                }

                if (!table.isEditable(row, col)) {
                    System.out.println("Essa posicao eh fixa e nao pode ser alterada.");
                    continue;
                }

                if (value < 1 || value > table.size()) {
                    System.out.println("Valor invalido. Use um numero entre 1 e " + table.size() + ".");
                    continue;
                }

                if (rulesVerify.verifyUserInput(row, col, value)) {
                    table.getItem(row, col).setValue(value);
                    System.out.println("Jogada aplicada.");
                } else {
                    printErrors(table);
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Utilize apenas numeros.");
            }
        }

        SCANNER.close();
    }

    private static GameSize chooseGameSize() {
        while (true) {
            System.out.println("Escolha o tamanho: 1) 4x4  2) 9x9  3) 16x16");
            System.out.print("> ");
            String option = SCANNER.nextLine().trim();
            switch (option) {
                case "1":
                    return GameSize.SIZE_4X4;
                case "2":
                    return GameSize.SIZE_9X9;
                case "3":
                    return GameSize.SIZE_16X16;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private static EmptyNumbersRange chooseDifficulty(GameSize gameSize) {
        int size = gameSize.getSize();
        int cells = size * size;

        if (size == 4) {
            return new EmptyNumbersRange(4, 8);
        }
        if (size == 9) {
            return new EmptyNumbersRange(30, 45);
        }

        int start = Math.max(40, cells / 3);
        int end = Math.max(start + 10, cells / 2);
        return new EmptyNumbersRange(start, end);
    }

    private static void handleClearCommand(String command, SudokuFullTable table) {
        String[] parts = command.split("\\s+");
        if (parts.length != 3) {
            System.out.println("Use: clear linha coluna");
            return;
        }

        try {
            int row = Integer.parseInt(parts[1]) - 1;
            int col = Integer.parseInt(parts[2]) - 1;

            if (!isInsideBoard(table, row, col)) {
                System.out.println("Linha/coluna fora do limite.");
                return;
            }

            if (!table.isEditable(row, col)) {
                System.out.println("Nao eh possivel limpar uma posicao fixa.");
                return;
            }

            table.getItem(row, col).setValue(0);
            System.out.println("Posicao limpa.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida. Utilize numeros para linha e coluna.");
        }
    }

    private static boolean isInsideBoard(SudokuFullTable table, int row, int col) {
        return row >= 0 && row < table.size() && col >= 0 && col < table.size();
    }

    private static void printBoard(SudokuFullTable table) {
        int size = table.size();
        int subgridSize = (int) Math.sqrt(size);

        String horizontal = buildHorizontalSeparator(size, subgridSize);
        System.out.println(horizontal);

        for (int row = 0; row < size; row++) {
            StringBuilder line = new StringBuilder();
            for (int col = 0; col < size; col++) {
                if (col % subgridSize == 0) {
                    line.append("| ");
                }
                SudokuTableItem item = table.getItem(row, col);
                line.append(formatCellValue(item.getValue(), size)).append(' ');
            }
            line.append("|");
            System.out.println(line);

            if ((row + 1) % subgridSize == 0) {
                System.out.println(horizontal);
            }
        }
    }

    private static String buildHorizontalSeparator(int size, int subgridSize) {
        StringBuilder separator = new StringBuilder("+");
        for (int col = 0; col < size; col++) {
            separator.append("---");
            if ((col + 1) % subgridSize == 0) {
                separator.append("+");
            }
        }
        return separator.toString();
    }

    private static String formatCellValue(int value, int size) {
        if (value == 0) {
            return ".";
        }
        if (size > 9 && value > 9) {
            return String.valueOf((char) ('A' + (value - 10)));
        }
        return String.valueOf(value);
    }

    private static void printErrors(SudokuFullTable table) {
        if (table.getErrors().isEmpty()) {
            System.out.println("Jogada invalida.");
            return;
        }

        System.out.println("Jogada invalida:");
        for (SudokuError error : table.getErrors()) {
            System.out.println("- " + error.getErrorMessage() + " (linha " + (error.getRow() + 1) + ", coluna " + (error.getCol() + 1) + ")");
        }
    }

    private static boolean isSolved(SudokuFullTable table) {
        int size = table.size();
        int subgridSize = (int) Math.sqrt(size);

        for (int row = 0; row < size; row++) {
            Set<Integer> seen = new HashSet<>();
            for (int col = 0; col < size; col++) {
                int value = table.getItem(row, col).getValue();
                if (value < 1 || value > size || !seen.add(value)) {
                    return false;
                }
            }
        }

        for (int col = 0; col < size; col++) {
            Set<Integer> seen = new HashSet<>();
            for (int row = 0; row < size; row++) {
                int value = table.getItem(row, col).getValue();
                if (!seen.add(value)) {
                    return false;
                }
            }
        }

        for (int boxRow = 0; boxRow < size; boxRow += subgridSize) {
            for (int boxCol = 0; boxCol < size; boxCol += subgridSize) {
                Set<Integer> seen = new HashSet<>();
                for (int row = boxRow; row < boxRow + subgridSize; row++) {
                    for (int col = boxCol; col < boxCol + subgridSize; col++) {
                        int value = table.getItem(row, col).getValue();
                        if (!seen.add(value)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}