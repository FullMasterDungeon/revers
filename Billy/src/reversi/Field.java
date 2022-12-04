package reversi;

import static reversi.Ansi.*;

public class Field {
    private static final int ARR_SIZE = 8;
    public static int[][] table = new int[ARR_SIZE][ARR_SIZE];
    public static void showField() {
        int count = 1;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++) {
                switch (table[i][j]) {
                    case 0:
                        System.out.print("X  ");
                        System.out.print(ANSI_RESET);
                        break;
                    case 1:
                        System.out.print(ANSI_BLUE + "●  " + ANSI_RESET);
                        break;
                    case 2:
                        System.out.print(ANSI_RED + "●  " + ANSI_RESET);
                        break;
                    case 3: {
                        if (count > 9) {
                            System.out.print(count++ + " ");
                        } else {
                            System.out.print(count++ + "  ");
                        }
                        break;
                    }
                }
            }
            System.out.print("\n");
        }
    }
    public static void changeTable(int row, int col, int y, int x) {
        while (true) {
            row += y;
            col += x;
            if (Field.table[row][col] == Reversi.getPlayer())
                return;
            Field.table[row][col] = Reversi.getPlayer();
        }
    }
    public static void copy(int[][] arr1, int[][] arr2) {
        for (int i = 0; i < ARR_SIZE; i++) {
            System.arraycopy(arr2[i], 0, arr1[i], 0, 8);
        }
    }
    public static void createNewTable(){
        table = new int[ARR_SIZE][ARR_SIZE];
    }
}
