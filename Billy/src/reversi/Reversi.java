package reversi;

import java.util.*;

import static reversi.Ansi.*;
import static reversi.Field.changeTable;


public class Reversi {
    private static int player;
    private static boolean gameMode = true;
    private static ArrayList<Integer> possible;
    private static int bestScore = 0;
    private static int[][] stepBackField;

    private static void findWhereToMove() {
        for (int i = 0; i < Field.table.length; i++) {
            for (int j = 0; j < Field.table.length; j++) {
                if (Field.table[i][j] == 0 || Field.table[i][j] == 3) {
                    Field.table[i][j] = 0; // Чтобы все тройки убрать
                    if (j < 7 && Field.table[i][j + 1] == 3 - player && isValidToMove(i, j, 0, 1))
                        Field.table[i][j] = 3;
                    else if (j < 7 && i < 7 && Field.table[i + 1][j + 1] == 3 - player && isValidToMove(i, j, 1, 1))
                        Field.table[i][j] = 3;
                    else if (i < 7 && Field.table[i + 1][j] == 3 - player && isValidToMove(i, j, 1, 0))
                        Field.table[i][j] = 3;
                    else if (i < 7 && j > 0 && Field.table[i + 1][j - 1] == 3 - player && isValidToMove(i, j, 1, -1))
                        Field.table[i][j] = 3;
                    else if (j > 0 && Field.table[i][j - 1] == 3 - player && isValidToMove(i, j, 0, -1))
                        Field.table[i][j] = 3;
                    else if (i > 0 && j > 0 && Field.table[i - 1][j - 1] == 3 - player && isValidToMove(i, j, -1, -1))
                        Field.table[i][j] = 3;
                    else if (i > 0 && Field.table[i - 1][j] == 3 - player && isValidToMove(i, j, -1, 0))
                        Field.table[i][j] = 3;
                    else if (i > 0 && j < 7 && Field.table[i - 1][j + 1] == 3 - player && isValidToMove(i, j, -1, 1))
                        Field.table[i][j] = 3;
                }
            }
        }
        possible = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (Field.table[i][j] == 3)
                    possible.add(i * 10 + j);
            }
        }
    }

    public static int getPlayer() {
        return player;
    }

    private static boolean isValidToMove(int row, int col, int y, int x) {
        row += y;
        col += x;
        while (true) {
            row += y;
            col += x;
            if (row == -1 || row == 8 || col == -1 || col == 8)
                return false;
            if (Field.table[row][col] == player)
                return true;
            if (Field.table[row][col] == 0 || Field.table[row][col] == 3)
                return false;
        }
    }

    private static void finishGame() {
        int scoreBlue = 0;
        int scoreRed = 0;
        for (int[] row : Field.table) {
            for (int el : row) {
                if (el == 1) {
                    ++scoreBlue;
                } else if (el == 2) {
                    ++scoreRed;
                }
            }
        }
        System.out.println();

        if (scoreBlue > bestScore) {
            bestScore = scoreBlue;
        }
        if (scoreRed > bestScore && !gameMode) {
            bestScore = scoreRed;
        }

        if (scoreBlue > scoreRed) {
            System.out.println(ANSI_BLUE + "● player wins\n" +
                    "● score: " + scoreBlue + ANSI_RED + " ● score: " + scoreRed + ANSI_RESET);

        } else if (scoreBlue == scoreRed) {
            System.out.println("Draw\n" +
                    ANSI_BLUE + "● score: " + scoreBlue + ANSI_RED + " ● score: " + scoreRed + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "● player wins\n" +
                    "● score: " + scoreRed + ANSI_BLUE + " ● score: " + scoreBlue + ANSI_RESET);
        }
        System.out.println();
        showMenu();
    }

    private static int moveFromAI() {
        Random random = new Random();
        int x, y, bestOption;
        int maxBest = -1;
        ArrayList<Integer> max = new ArrayList<>();
        for (int cord : possible) {
            x = cord / 10;
            y = cord % 10;
            bestOption = 0;
            if (y < 7 && Field.table[x][y + 1] == 3 - player && isValidToMove(x, y, 0, 1))
                bestOption += countPos(x, y, 0, 1);
            if (x < 7 && y < 7 && Field.table[x + 1][y + 1] == 3 - player && isValidToMove(x, y, 1, 1))
                bestOption += countPos(x, y, 1, 1);
            if (x < 7 && Field.table[x + 1][y] == 3 - player && isValidToMove(x, y, 1, 0))
                bestOption += countPos(x, y, 1, 0);
            if (x < 7 && y > 0 && Field.table[x + 1][y - 1] == 3 - player && isValidToMove(x, y, 1, -1))
                bestOption += countPos(x, y, 1, -1);
            if (y > 0 && Field.table[x][y - 1] == 3 - player && isValidToMove(x, y, 0, -1))
                bestOption += countPos(x, y, 0, -1);
            if (x > 0 && y > 0 && Field.table[x - 1][y - 1] == 3 - player && isValidToMove(x, y, -1, -1))
                bestOption += countPos(x, y, -1, -1);
            if (x > 0 && Field.table[x - 1][y] == 3 - player && isValidToMove(x, y, -1, 0))
                bestOption += countPos(x, y, -1, 0);
            if (x > 0 && y < 7 && Field.table[x - 1][y + 1] == 3 - player && isValidToMove(x, y, -1, 1))
                bestOption += countPos(x, y, -1, 1);

            if (bestOption > maxBest) {
                maxBest = bestOption;
                max = new ArrayList<>();
                max.add(cord);
            } else if (bestOption == maxBest) {
                max.add(cord);
            }
        }
        return max.get(random.nextInt(max.size()));
    }

    private static void move() {
        int x, y;

        if (player == 1)
            System.out.println(ANSI_BLUE + "● turn." + ANSI_RESET);
        else
            System.out.println(ANSI_RED + "● turn." + ANSI_RESET);

        if (possible.size() == 0) {
            player = 3 - player;
            findWhereToMove();
            if (possible.size() == 0) {
                System.out.println("\nGAME OVER.");
                finishGame();
                return;
            }
            System.out.println("there are no available turns. Player have to skip step.");
            Field.showField();
            move();
        }

        if (gameMode && player == 2) {
            int result = moveFromAI();
            x = result / 10;
            y = result % 10;
        } else {
            int[][] prev = new int[8][8];
            Field.copy(prev, Field.table);

            int place;
            Scanner in = new Scanner(System.in);
            while (true) {
                String input = in.nextLine();
                try {
                    place = Integer.parseInt(input.trim());
                    if (place == -1) {
                        backToPrevious();
                    } else if (place < 1 || place > possible.size()) {
                        System.out.println("Enter the number.");
                    } else {
                        break;
                    }
                } catch (Exception ex) {
                    System.out.println("Enter the number.");
                }
            }
            place--;
            x = possible.get(place) / 10;
            y = possible.get(place) % 10;
            stepBackField = prev.clone();
        }

        Field.table[x][y] = player;
        if (y < 7 && Field.table[x][y + 1] == 3 - player && isValidToMove(x, y, 0, 1))
            changeTable(x, y, 0, 1);
        if (x < 7 && y < 7 && Field.table[x + 1][y + 1] == 3 - player && isValidToMove(x, y, 1, 1))
            changeTable(x, y, 1, 1);
        if (x < 7 && Field.table[x + 1][y] == 3 - player && isValidToMove(x, y, 1, 0))
            changeTable(x, y, 1, 0);
        if (x < 7 && y > 0 && Field.table[x + 1][y - 1] == 3 - player && isValidToMove(x, y, 1, -1))
            changeTable(x, y, 1, -1);
        if (y > 0 && Field.table[x][y - 1] == 3 - player && isValidToMove(x, y, 0, -1))
            changeTable(x, y, 0, -1);
        if (x > 0 && y > 0 && Field.table[x - 1][y - 1] == 3 - player && isValidToMove(x, y, -1, -1))
            changeTable(x, y, -1, -1);
        if (x > 0 && Field.table[x - 1][y] == 3 - player && isValidToMove(x, y, -1, 0))
            changeTable(x, y, -1, 0);
        if (x > 0 && y < 7 && Field.table[x - 1][y + 1] == 3 - player && isValidToMove(x, y, -1, 1))
            changeTable(x, y, -1, 1);

        player = 3 - player;
        findWhereToMove();
        Field.showField();
        move();
    }

    private static int countPos(int row, int col, int vert, int x) {
        int chips = 0;
        while (true) {
            row += vert;
            col += x;
            if (Field.table[row][col] == player)
                return chips;
            ++chips;
        }
    }

    private static void backToPrevious() {
        if (stepBackField.length == 0) {
            System.out.println("Impossible to cancel a step.");
        } else {
            System.out.println("Step canceled.");
            Field.table = stepBackField.clone();
            stepBackField = new int[0][0];
            Field.showField();
            move();
        }
    }

    public static void showMenu() {
        player = 1;
        Field.createNewTable();
        stepBackField = new int[0][0];
        Field.table[3][3] = 2;
        Field.table[4][4] = 2;
        Field.table[3][4] = 1;
        Field.table[4][3] = 1;
        System.out.println(
                """
                        Choose game mode:
                        1. Player versus player
                        2. Player versus computer
                        3. Get the best score
                        4. Close app""");
        Scanner in = new Scanner(System.in);
        while (true) {
            int input = in.nextInt();
            if (input == 1) {
                gameMode = false;
                break;
            } else if (input == 2) {
                gameMode = true;
                break;
            } else if (input == 3) {
                System.out.println("The best score is " + bestScore);
            } else if (input == 4) {
                System.exit(1);
            } else {
                System.out.println("incorrect input");
            }
        }
        findWhereToMove();
        Field.showField();
        move();
    }
}