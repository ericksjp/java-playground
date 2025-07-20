package org.application.entities.pieces;

import java.util.List;

import org.application.entities.ChessBoard;
import org.application.entities.Color;
import org.application.entities.Direction;
import org.application.entities.Position;
import org.application.entities.rules.ChessRule;

public class Rook extends ChessPiece {
    private boolean isFirstMove;

    public Rook(Color color, Position position) {
        super(color, position);
        this.isFirstMove = true;
    }

    @Override
    public List<Position> pathToPosition(ChessBoard board, Position target) {
        return ChessRule.getPathTo(
            this, target, board, board.getColumns() - 1,
            Direction.NORTH,
            Direction.SOUTH, 
            Direction.EAST, 
            Direction.WEST
        );
    }

    @Override
    public List<Position> possibleMoves(ChessBoard board) {
        return ChessRule.calculatePossibleMoves(
            this, board, board.getColumns() - 1,
            Direction.NORTH,
            Direction.SOUTH, 
            Direction.EAST, 
            Direction.WEST
        );
    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
        this.isFirstMove = false;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }
}
