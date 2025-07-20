package org.application.entities.pieces;

import java.util.ArrayList;
import java.util.List;

import org.application.entities.ChessBoard;
import org.application.entities.Color;
import org.application.entities.Position;

public class Pawn extends ChessPiece {
    private boolean isFirstMove;
    private int moves;

    public Pawn(Color color, Position position) {
        super(color, position);
        this.isFirstMove = true;
        this.moves = 0;
    }

    @Override
    public List<Position> pathToPosition(ChessBoard board, Position target) {
        List<Position> pos = possibleMoves(board);

        if (!possibleMoves(board).contains(target)) {
            pos.clear();
        }

        return pos;
    }

    @Override
    public List<Position> possibleMoves(ChessBoard board) {
        List<Position> positions = new ArrayList<>(3);

        int aggregator = super.getColor() == Color.WHITE ? 1 : -1;

        int row = super.getPosition().getRow();
        int column = super.getPosition().getColumn();

        int limit = isFirstMove ? 2 : 1;

        for (int i = 1; i <= limit ; i++) {
            if (!board.positionExists(row + (i * aggregator), column)) break;
            if (board.getPiece(row + (i * aggregator), column) != null) break;
            positions.add(new Position(row + (i * aggregator), column));
        }

        for (int i = column - 1; i <= column + 1; i += 2) {
            if (!board.positionExists(row + 1, i))
                continue;

            ChessPiece piece = board.getPiece(row + aggregator, i);
            if (piece == null)
                continue;

            if (piece.getColor() == super.getColor())
                continue;

            positions.add(new Position(row + aggregator, i));
        }

        return positions;
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
        this.isFirstMove = false;
        moves++;
    }

    @Override
    public String toString() {
        return "P";
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

}
