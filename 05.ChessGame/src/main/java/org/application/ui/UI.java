package org.application.ui;

import java.util.List;
import java.util.Scanner;

import org.application.entities.ChessPlayer;
import org.application.entities.ChessPosition;
import org.application.entities.Color;
import org.application.entities.Position;
import org.application.entities.pieces.ChessPiece;

public class UI {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void drawChess(ChessPiece[][] pieces) {
        System.out.println();
        for (int i = pieces.length - 1; i >= 0; i--) {
            System.out.print("   " + (i + 1) + "  ");
            for (int j = 0; j < pieces[i].length; j++) {
                drawPiece(pieces[i][j], (i + j) % 2 == 0 ? Color.BLACK : Color.WHITE);
            }
            System.out.println();
        }
        System.out.println("      a b c d e f g h");
        System.out.println();
    }

    public static void drawChessWithSelectedPositions(ChessPiece[][] pieces, List<Position> positions) {
        System.out.println();
        for (int i = pieces.length - 1; i >= 0; i--) {
            System.out.print("   " + (i + 1) + "  ");
            for (int j = 0; j < pieces[i].length; j++) {
                boolean highligth = positions.contains(new Position(i, j));
                drawPiece(pieces[i][j], (i + j) % 2 == 0 ? Color.BLACK : Color.WHITE, highligth);
            }
            System.out.println();
        }
        System.out.println("      a b c d e f g h");
        System.out.println();
    }

    private static void drawPiece(ChessPiece piece, Color fallback, boolean highlight) {
        String background = highlight ? ANSI_CYAN_BACKGROUND : "";

        if (piece == null) {
            System.out.print(background + drawSquare(fallback) + ANSI_RESET);
        } else {
            String color = piece.getColor() == Color.BLACK ? ANSI_RED : ANSI_YELLOW;
            System.out.print(background + color + piece + ANSI_RESET);
        }

        System.out.print(" ");
    }

    private static void drawPiece(ChessPiece piece, Color fallback) {
        drawPiece(piece, fallback, false);
    }

    public static ChessPosition selectSource(Scanner sc) {
        System.out.print("Src: ");
        String args = sc.nextLine();

        char column = args.charAt(0);
        int row = Integer.parseInt(args.charAt(1) + "");

        return new ChessPosition(column, row);
    }

    public static ChessPosition selectDest(Scanner sc, ChessPosition src) {
        System.out.println("Src: " + src);
        System.out.print("Dest: ");
        String args = sc.nextLine();

        char column = args.charAt(0);
        int row = Integer.parseInt(args.charAt(1) + "");

        return new ChessPosition(column, row);
    }

    private static char drawSquare(Color color) {
        if (color == Color.BLACK)
            return ' ';
        else {
            return '-';
        }
    }

    public static void drawCapturedPieces(ChessPlayer... players) {
        for (ChessPlayer player : players) {
            System.out.print(player.getName() + ": ");
            player.getCapturedPieces().forEach(System.out::print);
            System.out.println();
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
