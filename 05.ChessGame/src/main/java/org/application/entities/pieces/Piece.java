package org.application.entities.pieces;

import java.util.List;

import org.application.entities.ChessBoard;
import org.application.entities.Color;
import org.application.entities.Position;

public interface Piece {
    public Position getPosition();
    public Color getColor();
    public List<Position> pathToPosition(ChessBoard board, Position target);
    public List<Position> possibleMoves(ChessBoard board);
    public boolean canMoveToPosition(ChessBoard board, Position target);
    public boolean isThereAnyPossibleMove(ChessBoard board);
}
