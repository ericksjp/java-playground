package org.application.entities.rules;

import java.util.List;

import org.application.entities.ChessBoard;
import org.application.entities.Direction;
import org.application.entities.Position;
import org.application.entities.pieces.ChessPiece;

public interface Rule {
    List<Position> getPathTo(ChessPiece piece, Position target, ChessBoard board, int limit, Direction ...directions);
    List<Position> calculatePossibleMoves(ChessPiece piece, ChessBoard board, int limit, Direction... directions);
}
