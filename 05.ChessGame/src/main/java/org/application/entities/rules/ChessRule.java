package org.application.entities.rules;

import java.util.ArrayList;
import java.util.List;

import org.application.entities.ChessBoard;
import org.application.entities.Direction;
import org.application.entities.Position;
import org.application.entities.pieces.ChessPiece;

public class ChessRule {

    public static List<Position> getPathTo(ChessPiece piece, Position target, ChessBoard board, int limit, Direction... directions) {
        List<Position> pos = new ArrayList<>();

        if (!board.positionExists(target.getRow(), target.getColumn())) return pos;
        if (limit <= 0) return pos;

        for (Direction dir : directions) {
            if (dir == null) {
                continue;
            }

            int currentRow = piece.getPosition().getRow();
            int currentColumn = piece.getPosition().getColumn();

            for (int i = 0; i < limit; i++) {
                currentRow += dir.getRowOffset();
                currentColumn += dir.getColumnOffset();

                if(!board.positionExists(currentRow, currentColumn)) break;;

                Position p = new Position(currentRow, currentColumn);

                if (p.equals(target)) {
                    pos.add(p);
                    return pos;
                }

                if (board.getPiece(currentRow, currentColumn) != null) {
                    pos.clear();
                    break;
                }

                pos.add(p);
            }

            pos.clear();
        }

        return pos;
    };

    public static List<Position> calculatePossibleMoves(ChessPiece piece, ChessBoard board, int limit, Direction... directions) {
        List<Position> positions = new ArrayList<>();
        if (piece == null || board == null) {
            throw new IllegalArgumentException("Error: Piece or board are null!");
        }

        Position currentPosition = piece.getPosition();

        for (Direction dir : directions) {
            int rowStep = dir.getRowOffset();
            int columnStep = dir.getColumnOffset();

            int currentRow = currentPosition.getRow();
            int currentColumn = currentPosition.getColumn();

            for (int i = 0; i < limit; i++) {
                currentRow += rowStep;
                currentColumn += columnStep;

                if (!board.positionExists(currentRow, currentColumn))
                    break;

                ChessPiece p = board.getPiece(currentRow, currentColumn);
                if (p == null) {
                    positions.add(new Position(currentRow, currentColumn));
                    continue;
                }

                if (p.getColor() != piece.getColor()) {
                    positions.add(new Position(currentRow, currentColumn));
                }

                break;
            }
        }

        return positions;
    }
}
