package org.application.entities;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import org.application.entities.pieces.Bishop;
import org.application.entities.pieces.ChessPiece;
import org.application.entities.pieces.King;
import org.application.entities.pieces.Knight;
import org.application.entities.pieces.Pawn;
import org.application.entities.pieces.Queen;
import org.application.entities.pieces.Rook;

public class ChessBoard {
    private final int SIZE;

    private final ChessPiece[][] board;
    private final boolean[][] colors;

    public ChessBoard() {
        this.SIZE = 8;
        this.board = new ChessPiece[SIZE][SIZE];
        this.colors = new boolean[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            board[1][i] = new Pawn(Color.WHITE, new Position(1, i));
            board[6][i] = new Pawn(Color.BLACK, new Position(6, i));

            for (int j = 0; j < SIZE; j++) {
                colors[i][j] = (i + j) % 2 != 0;
            }
        }

        // kings
        board[0][4] = new King(Color.WHITE, new Position(0, 4));
        board[7][4] = new King(Color.BLACK, new Position(7, 4));

        // queens
        board[0][3] = new Queen(Color.WHITE, new Position(0, 3));
        board[7][3] = new Queen(Color.BLACK, new Position(7, 3));

        // bishops
        board[0][2] = new Bishop(Color.WHITE, new Position(0, 2));
        board[0][5] = new Bishop(Color.WHITE, new Position(0, 5));
        board[7][2] = new Bishop(Color.BLACK, new Position(7, 2));
        board[7][5] = new Bishop(Color.BLACK, new Position(7, 5));

        // knights
        board[0][1] = new Knight(Color.WHITE, new Position(0, 1));
        board[0][6] = new Knight(Color.WHITE, new Position(0, 6));
        board[7][1] = new Knight(Color.BLACK, new Position(7, 1));
        board[7][6] = new Knight(Color.BLACK, new Position(7, 6));

        // rooks
        board[0][0] = new Rook(Color.WHITE, new Position(0, 0));
        board[0][7] = new Rook(Color.WHITE, new Position(0, 7));
        board[7][0] = new Rook(Color.BLACK, new Position(7, 0));
        board[7][7] = new Rook(Color.BLACK, new Position(7, 7));
    }

    public int getRows() {
        return this.SIZE;
    }

    public int getColumns() {
        return this.SIZE;
    }

    public ChessPiece getPiece(int row, int column) {
        validatePosition(row, column);
        return board[row][column];
    }

    public ChessPiece getPiece(Position position) {
        return this.getPiece(position.getRow(), position.getColumn());
    }

    public ChessPiece removePiece(int row, int column) {
        validatePosition(row, column);
        ChessPiece piece = getPiece(row, column);

        if (piece == null) {
            throw new InputMismatchException("Error: No piece at specified position!");
        }

        board[row][column] = null;

        return piece;
    }

    public ChessPiece capturePiece(int row, int column) {
        try {
            return removePiece(row, column);
        } catch (InputMismatchException e) {
            return  null;
        }
    }

    public ChessPiece capturePiece(Position pos) {
        try {
            return  removePiece(pos.getRow(), pos.getColumn());
        } catch (RuntimeException e) {
            return  null;
        }
    }

    public ChessPiece removePiece(Position position) {
        return this.removePiece(position.getRow(), position.getColumn());
    }

    public void placePiece(int row, int column, ChessPiece piece) {
        validatePosition(row, column);
        if (getPiece(row, column) != null) {
            throw new InputMismatchException("Error: Space occupied by another piece!");
        }

        board[row][column] = piece;
        piece.setPosition(new Position(row, column));
    }

    public void placePiece(Position position, ChessPiece piece) {
        this.placePiece(position.getRow(), position.getColumn(), piece);
    }

    public Color getColor(int row, int column) {
        validatePosition(row, column);
        return colors[row][column] ? Color.WHITE : Color.BLACK;
    }

    public Color getColor(Position position) {
        return this.getColor(position.getRow(), position.getColumn());
    }

    public List<ChessPiece> getChessPiecesByColor(Color color) {
        List<ChessPiece> pieces = new ArrayList<>(16);

        for (ChessPiece[] line : board) {
            for (ChessPiece piece : line) {
                if (piece != null && piece.getColor() == color) {
                    pieces.add(piece);
                }
            }
        }

        return pieces;
    }

    public ChessPiece[][] getPieces() {
        return this.board;
    }

    public boolean[][] getColors() {
        return this.colors;
    }

    public boolean positionExists(int row, int column) {
        return row >= 0 && row < SIZE && column >= 0 && column < SIZE;
    }

    public boolean positionExists(Position position) {
        return this.positionExists(position.getRow(), position.getColumn());
    }

    private void validatePosition(int row, int column) {
        if (positionExists(row, column)) return;
        throw new IllegalArgumentException("Error: Position out of bounds!");
    }

    public void validatePosition(Position position) {
        this.validatePosition(position.getRow(), position.getColumn());
    }

}
