package org.application.entities.pieces;

import java.util.ArrayList;
import java.util.List;

import org.application.entities.ChessBoard;
import org.application.entities.Color;
import org.application.entities.Position;

public class Knight extends ChessPiece {
    public Knight(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> pathToPosition(ChessBoard board, Position target) {
        List<Position> pos = new ArrayList<>(1);
        if (possibleMoves(board).contains(target)) {
            pos.add(new Position(target.getRow(), target.getColumn()));
        }
        return pos;
    }

    @Override
    public List<Position> possibleMoves(ChessBoard board) {
        if (!super.getCanMove()) return new ArrayList<>(0);
        List<Position> positions = new ArrayList<>(8);

        int row = super.getPosition().getRow();
        int column = super.getPosition().getColumn();

        int[][] directions = {
                { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 },
                { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }
        };


        for (int[] dir : directions) {
            if (!board.positionExists(row + dir[0], column + dir[1])) continue;

            ChessPiece piece = board.getPiece(row + dir[0], column + dir[1]);
            if (piece != null && piece.getColor() == super.getColor()) continue;

            positions.add(new Position(row + dir[0], column + dir[1]));
        }

        return positions;
    }

    
    @Override
    public String toString() {
        return "N";
    }
}
