//Minesweeper.java
//Author:  Matthew

import java.util.*;
import java.lang.Math;

public class Minesweeper {

    static Scanner scan = new Scanner(System.in);
    static int[][] mapShown;
    static int[][] mapHidden;
    static int bomb;
    static int size;

    public static void main(final String[] args) {
        start();
        // print(mapShown);
        //print(mapHidden);
    }

    public static void makeMap(int sizeN, int bombN) {
        size = sizeN;
        mapShown = new int[size][size];
        mapHidden = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mapShown[i][j] = 10;
                mapHidden[i][j] = 0;
            }
        }
        bomb = bombN;
        int x;
        int y;

        for (int i = 0; i < bomb; i++) {
            x = (int) (Math.random() * size);
            y = (int) (Math.random() * size);
            if (mapHidden[x][y] != 0) {
                i -= 1;
            } else {
                mapHidden[x][y] = 9;
            }
        }

        markNeighbors();

    }

    public static void print(int[][] map) {
        // Need to adapt the lettering to allow for different grids than just size 9
        String str = "\n    a b c d e f g h i\n\n";
        for (int i = 0; i < size; i++) {
            str += i + 1 + "   ";
            for (int j = 0; j < size; j++) {
                if (map[i][j] == 10) {
                    str += '*' + " ";
                } else if (map[i][j] == 9) {
                    str += 'B' + " ";
                } else {
                    str += map[i][j] + " ";
                }
            }
            str += "\n";
        }
        System.out.println(str);
    }

    public static void markNeighbors() {
        int temp;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (mapHidden[i][j] == 9) {

                    if (i > 0 && j > 0) {
                        temp = mapHidden[i - 1][j - 1];
                        if (temp != 9) {
                            mapHidden[i - 1][j - 1] = temp + 1;
                        }
                    }

                    if (i > 0) {
                        temp = mapHidden[i - 1][j];
                        if (temp != 9) {
                            mapHidden[i - 1][j] = temp + 1;
                        }
                    }

                    if (j > 0) {
                        temp = mapHidden[i][j - 1];
                        if (temp != 9) {
                            mapHidden[i][j - 1] = temp + 1;
                        }
                    }

                    if (i < (size - 1) && j > 0) {
                        temp = mapHidden[i + 1][j - 1];
                        if (temp != 9) {
                            mapHidden[i + 1][j - 1] = temp + 1;
                        }
                    }

                    if (i > 0 && j < (size - 1)) {
                        temp = mapHidden[i - 1][j + 1];
                        if (temp != 9) {
                            mapHidden[i - 1][j + 1] = temp + 1;
                        }
                    }

                    if (i < (size - 1) && j < (size - 1)) {
                        temp = mapHidden[i + 1][j + 1];
                        if (temp != 9) {
                            mapHidden[i + 1][j + 1] = temp + 1;
                        }
                    }

                    if (i < (size - 1)) {
                        temp = mapHidden[i + 1][j];
                        if (temp != 9) {
                            mapHidden[i + 1][j] = temp + 1;
                        }
                    }

                    if (j < (size - 1)) {
                        temp = mapHidden[i][j + 1];
                        if (temp != 9) {
                            mapHidden[i][j + 1] = temp + 1;
                        }
                    }

                }
            }
        }
    }

    public static void start() {
        int in1;
        int in2;
        System.out.println("Hello and welcome to MINESWEEPER!\nYour goal is to locate all of the mines.\n"+
                "Type the size for the board (the recommendation is 9): ");
        in1 = scan.nextInt();
        System.out.println("Now type the number of bombs on the board (the recommendation is the same as size): ");
        in2 = scan.nextInt();
        if (in2 < (in1 * in1)){
            makeMap(in1, in2);
            nextMove();
        } else {
            System.out.println("You failed, try again");
        }
    }

    public static void nextMove() {
        print(mapShown);
        System.out.println("Type a coordinate in the form \"letter,number\"");
                //"This will reveal the tile at this location\nIf you select a mine, you lose.");
        String str = scan.next();
        //alterMap(number, letter)
        //System.out.println(((int)str.charAt(2)-49)+" "+((int)str.charAt(0)-97));
        alterMap((int)str.charAt(2)-49,(int)str.charAt(0)-97);

        
    }

    public static void alterMap(int iN, int jN) {
        if (iN < 0 || iN > size || jN < 0 || jN > size) {
            System.out.println("You failed, bad parameters, try again");
        } else if (mapHidden[iN][jN] == 9) {
            System.out.println("\nYou selected a bomb and died.\nGAME OVER");
            print(mapHidden);
        } else if (mapHidden[iN][jN] == 0) {
            emptyReveal(iN,jN);
            nextMove();
        } else {
            mapShown[iN][jN] = mapHidden[iN][jN];
            nextMove();
        }
    }

    public static void emptyReveal(int i, int j) {
        int temp;
        mapShown[i][j] = 0;

        if (i > 0 && j > 0) {
            temp = mapHidden[i - 1][j - 1];
            if (temp == 0) {
                if(mapShown[i - 1][j - 1] != 0){
                    emptyReveal(i - 1, j - 1);
                }
            } else {
                mapShown[i - 1][j - 1] = temp;
            }
        }

        if (i > 0) {
            temp = mapHidden[i - 1][j];
            if (temp == 0) {
                if(mapShown[i - 1][j] != 0){
                    emptyReveal(i - 1, j);
                }
            } else {
                mapShown[i - 1][j] = temp;
            }
        }

        if (j > 0) {
            temp = mapHidden[i][j - 1];
            if (temp == 0) {
                if(mapShown[i][j - 1] != 0){
                    emptyReveal(i, j - 1);
                }
            } else {
                mapShown[i][j - 1] = temp;
            }
        }

        if (i < (size - 1) && j > 0) {
            temp = mapHidden[i + 1][j - 1];
            if (temp == 0) {
                if(mapShown[i + 1][j - 1] != 0){
                    emptyReveal(i + 1, j - 1);
                }
            } else {
                mapShown[i + 1][j - 1] = temp;
            }
        }

        if (i > 0 && j < (size - 1)) {
            temp = mapHidden[i - 1][j + 1];
            if (temp == 0) {
                if(mapShown[i - 1][j + 1] != 0){
                    emptyReveal(i - 1, j + 1);
                }
            } else {
                mapShown[i - 1][j + 1] = temp;
            }
        }

        if (i < (size - 1) && j < (size - 1)) {
            temp = mapHidden[i + 1][j + 1];
            if (temp == 0) {
                if(mapShown[i + 1][j + 1] != 0){
                    emptyReveal(i + 1, j + 1);
                }
            } else {
                mapShown[i + 1][j + 1] = temp;
            }
        }

        if (i < (size - 1)) {
            temp = mapHidden[i + 1][j];
            if (temp == 0) {
                if(mapShown[i + 1][j] != 0){
                    emptyReveal(i + 1, j);
                }
            } else {
                mapShown[i + 1][j] = temp;
            }
        }

        if (j < (size - 1)) {
            temp = mapHidden[i][j + 1];
            if (temp == 0) {
                if(mapShown[i][j + 1] != 0){
                    emptyReveal(i, j + 1);
                }
            } else {
                mapShown[i][j + 1] = temp;
            }
        }

    }
    //TODO:
        //ask where to check   //optional: add flags with '!'
        //add win if you flag all bombs (and no safe spaces)
}
