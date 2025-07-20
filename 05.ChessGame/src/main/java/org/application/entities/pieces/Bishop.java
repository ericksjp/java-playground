package org.application.entities.pieces;

import java.util.List;

import org.application.entities.ChessBoard;
import org.application.entities.Color;
import org.application.entities.Direction;
import org.application.entities.Position;
import org.application.entities.rules.ChessRule;

public class Bishop extends ChessPiece {
    public Bishop(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> pathToPosition(ChessBoard board, Position target) {
        return ChessRule.getPathTo(
            this, target, board, board.getColumns() - 1,
            Direction.NORTHEAST,
            Direction.NORTHWEST, 
            Direction.SOUTHEAST, 
            Direction.SOUTHWEST
        );
    }

    @Override
    public List<Position> possibleMoves(ChessBoard board) {
        return ChessRule.calculatePossibleMoves(
            this, board, board.getColumns() - 1,
            Direction.NORTHEAST,
            Direction.NORTHWEST, 
            Direction.SOUTHEAST, 
            Direction.SOUTHWEST
        );
    }

    @Override
    public String toString() {
        return "B";
    }   
}
