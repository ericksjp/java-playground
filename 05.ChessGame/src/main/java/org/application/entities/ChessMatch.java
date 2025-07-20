package org.application.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.application.entities.pieces.Bishop;
import org.application.entities.pieces.ChessPiece;
import org.application.entities.pieces.King;
import org.application.entities.pieces.Knight;
import org.application.entities.pieces.Pawn;
import org.application.entities.pieces.Queen;
import org.application.entities.pieces.Rook;
import org.application.entities.rules.ChessRule;

public class ChessMatch {
    private final ChessBoard board = new ChessBoard();
    private final ChessPlayer whitePlayer;
    private final ChessPlayer blackPlayer;

    private Color turn;
    private boolean isCheck;
    private boolean isOver;
    private Color winner;

    private Position promotion;
    private boolean inPromotion;
    private ChessPiece selectedChessPiece;
    private Set<Position> checkPath = new HashSet<>();

    private final List<Move> moves;

    public ChessMatch(ChessPlayer whitePlayer, ChessPlayer blackPlayer, Color starter) {
        validatePlayers(whitePlayer, blackPlayer);
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.turn = starter;
        this.promotion = null;
        this.inPromotion = false;
        this.moves = new ArrayList<>();
    }

    public List<Position> selectPiece(int row, int column, Color color) {
        validateTurn(color);
        validatePosition(row, column);

        ChessPiece piece = board.getPiece(row, column);
        if (piece == null || piece.getColor() != color) {
            throw new IllegalArgumentException("Error: Invalid piece selection!");
        }

        selectedChessPiece = piece;
        return getPossibleMovesForChessPiece(piece);
    }

    public List<Position> selectPiece(Position pos, Color color) {
        return selectPiece(pos.getRow(), pos.getColumn(), color);
    }

    public void moveChessPiece(int targetRow, int targetColumn, Color color) {
        validateTurn(color);
        validatePosition(targetRow, targetColumn);

        if (selectedChessPiece == null) {
            throw new IllegalStateException("Error: No source piece selected!");
        }

        Position targetPosition = new Position(targetRow, targetColumn);
        if (!getPossibleMovesForChessPiece(selectedChessPiece).contains(targetPosition)) {
            throw new IllegalArgumentException("Error: Invalid target position!");
        }

        moves.add(new Move(selectedChessPiece.getPosition(), targetPosition, turn));
        executeMove(targetPosition);
    }

    public void moveChessPiece(Position pos, Color color) {
        moveChessPiece(pos.getRow(), pos.getColumn(), color);
    }

    public void resolvePromotion(char piece, Color color) {
        if (promotion == null) {
            throw  new IllegalStateException("Error: No promotion in place!");
        }

        if (color != turn) {
            throw  new IllegalArgumentException("Error: Not your turn!");
        }

        ChessPiece p = switch (piece) {
            case 'Q' -> new Queen(color, promotion);
            case 'N' -> new Knight(color, promotion);
            case 'B' -> new Bishop(color, promotion);
            case 'R' -> new Rook(color, promotion);
            default -> throw new IllegalArgumentException("Error: Unrecognizable promotion!");
        };

        board.removePiece(promotion);
        board.placePiece(promotion, p);
        checkForCheck(p);
        switchTurn();
        checkForMatchEnd(turn);
        this.inPromotion = false;
    }

    
    private void executeMove(Position targetPosition) {
        promotion = null;

        if (passaintMove(selectedChessPiece, targetPosition)) return;
        if (nullCapturedMove(selectedChessPiece, targetPosition)) return;
        if (castleMove(selectedChessPiece, targetPosition)) return;

        pieceCapturedMove(selectedChessPiece, targetPosition);
    }

    // ----------------------------------

    private boolean passaintMove(ChessPiece selectedPiece, Position targetPosition) {
        if (!(selectedPiece instanceof Pawn)) return false;

        if (passaintPosition((Pawn)selectedPiece) == null) return false;

        ChessPiece captured = performEnPassant((Pawn)(selectedPiece), targetPosition);
        getPlayerByColor(selectedPiece.getColor()).addCapturedPiece(captured);

        updateMatchState();

        return true;
    }

    private boolean castleMove(ChessPiece selectedPiece, Position targetPosition) {
        if (!(selectedPiece instanceof King)) return false;

        ChessPiece captured = board.getPiece(targetPosition);
        if (captured == null) return false;
        if (captured.getColor() != selectedPiece.getColor()) return false;

        performCastle(selectedPiece, captured);

        updateMatchState();

        return true;
    }

    private boolean nullCapturedMove(ChessPiece selectedPiece, Position targetPosition) {
        ChessPiece captured = board.getPiece(targetPosition);
        if (captured != null) return false;

        board.removePiece(selectedPiece.getPosition());
        board.placePiece(targetPosition, selectedPiece);

        checkPromotion(selectedPiece);

        updateMatchState();

        return true;
    }

    private boolean pieceCapturedMove(ChessPiece selectedPiece, Position targetPosition) {
        ChessPiece captured = board.capturePiece(targetPosition);
        if (captured == null) return false;

        getPlayerByColor(selectedPiece.getColor()).addCapturedPiece(captured);

        board.removePiece(selectedPiece.getPosition());
        board.placePiece(targetPosition, selectedPiece);


        checkPromotion(selectedPiece);

        moves.add(new Move(selectedPiece.getPosition(), targetPosition, turn));
        updateMatchState();

        return true;
    }

    // ----------------------------------

    private void performCastle(ChessPiece king, ChessPiece rook) {
        int kingRow = king.getPosition().getRow();

        int rookColumn = rook.getPosition().getColumn() == 0 ? 3 : 5;
        int kingColumn = rookColumn == 3 ? 2 : 6;

        board.removePiece(rook.getPosition());
        board.removePiece(king.getPosition());

        board.placePiece(kingRow, rookColumn, rook);
        board.placePiece(kingRow, kingColumn, king);
    }

    private ChessPiece performEnPassant(Pawn pawnPiece, Position targetPosition) {
        int limit = pawnPiece.getColor() == Color.WHITE ? 5 : 4;

        ChessPiece removed = board.removePiece(limit, targetPosition.getColumn());

        board.removePiece(pawnPiece.getPosition());
        board.placePiece(targetPosition, pawnPiece);

        return removed;
    }

    // -------------------------------------------
    
    private void updateMatchState() {
        if (promotion != null) return;
        checkForCheck(selectedChessPiece);
        switchTurn();
        checkForMatchEnd(turn);
    }

    // -------------------------------------------
    
    private  void checkPromotion(ChessPiece piece) {
        if (!(piece instanceof Pawn)) return;;

        int limit = piece.getColor() == Color.WHITE ? 7 : 0;

        if (piece.getPosition().getRow() == limit) {
            this.promotion = piece.getPosition();
            this.inPromotion = true;
        }
    }

    private void checkForCheck(ChessPiece selectedPiece) {
        this.checkPath = getCheckPath(selectedPiece);
        this.isCheck = !checkPath.isEmpty();
    }

    private void checkForMatchEnd(Color color) {
        List<ChessPiece> myPieces = board.getChessPiecesByColor(color);

        // lonely kings
        if (myPieces.size() == 1 && board.getChessPiecesByColor(invert(color)).size() == 1) {
            this.isOver = true;
            this.winner = null;
            return;
        }

        // check if the player has any valid move, ifs not, just return the function
        for (ChessPiece piece : myPieces) {
            if (!getPossibleMovesForChessPiece(piece).isEmpty()) {
                return;
            }
        }

        this.isOver = true;
        this.winner = isCheck ? invert(color) : null;
    }

    // -----------------------------------------

    private Set<Position> getCheckPath(ChessPiece attacker) {
        ChessPiece king = getKing(invert(attacker.getColor()));
        Set<Position> path = new HashSet<>(attacker.pathToPosition(board, king.getPosition()));

        if (!path.isEmpty()) {
            path.add(attacker.getPosition());
            path.remove(king.getPosition());
        }

        return path;
    }

    private List<Position> getPossibleMovesForChessPiece(ChessPiece piece) {
        if (piece instanceof King) {
            return kingPositions(turn);
        }

        List<Position> positions = getValidMoves(piece);

        if (piece instanceof Pawn) {
            positions.add(passaintPosition((Pawn)piece));
        }

        return positions;
    }

   private List<Position> getValidMoves(ChessPiece piece) {
        if (isCheck) {
            Set<Position> legal = new HashSet<>(piece.possibleMoves(board));
            legal.retainAll(checkPath);
            return new ArrayList<>(legal);
        }

        Position current = piece.getPosition();
        board.getPieces()[current.getRow()][current.getColumn()] = null;

        Set<Position> threatPath = new HashSet<>();
        for (ChessPiece enemy : board.getChessPiecesByColor(invert(piece.getColor()))) {
            threatPath = getCheckPath(enemy);
            if (!threatPath.isEmpty()) break;
        }

        board.getPieces()[current.getRow()][current.getColumn()] = piece;
        List<Position> moves = piece.possibleMoves(board);

        if (threatPath.isEmpty()) return moves;

        threatPath.retainAll(moves);
        return new ArrayList<>(threatPath);
    }

    // --------------------------------------------

    private List<Position> kingPositions(Color color) {
        ChessPiece king = getKing(color);

        List<Position> moves = king.possibleMoves(board);
        List<ChessPiece> enemies = board.getChessPiecesByColor(invert(color));

        int kingRow = king.getPosition().getRow();
        int kingColumn = king.getPosition().getColumn();

        Set<Position> prohibited = new HashSet<>();

        for (Position move : moves) {
            // remove the piece at the possible position
            ChessPiece enemy = board.capturePiece(move);

            // set the current king position as null
            board.getPieces()[kingRow][kingColumn] = null;
            // put the king at the possible position
            board.getPieces()[move.getRow()][move.getColumn()] = king;

            // remove the enemie if he can move to the position
            for (ChessPiece enemyPiece : enemies) {
                if (enemyPiece.canMoveToPosition(board, move)) {
                    prohibited.add(move);
                    break;
                }
            }

            // sets the king pos as null
            board.getPieces()[move.getRow()][move.getColumn()] = null;
            board.getPieces()[move.getRow()][move.getColumn()] = enemy;
            board.getPieces()[kingRow][kingColumn] = king;
        }


        moves.removeIf(prohibited::contains);

        moves.addAll(castlePositions((King) getKing(color)));

        return moves;
    }

    private Position passaintPosition(Pawn pawnPiece) {
        int limit = pawnPiece.getColor() == Color.WHITE ? 4 : 5;
        if (pawnPiece.getPosition().getRow() != limit) return null;

        Move lastMove = moves.getLast();

        ChessPiece movedPiece = board.getPiece(lastMove.getTo());
        if (!(movedPiece instanceof Pawn )) return null;
        if (Math.abs(lastMove.getFrom().getRow() - lastMove.getTo().getRow()) != 2) return null;

        int column = pawnPiece.getPosition().getColumn();
        int row = pawnPiece.getPosition().getRow();

        for (int i = column - 1; i <= column + 1; i+=2) {
            Position pos = new Position(row, i);
            if (!movedPiece.getPosition().equals(pos)) continue;
            pos.setRow(row + (pawnPiece.getColor() == Color.WHITE ? 1 : -1));
            return pos;
        }

        return null;
    }

    private List<Position> castlePositions(King piece) {
        if (!piece.isFirstMove() || isCheck) return new ArrayList<>(0);
        List<Position> castleMoves = new ArrayList<>(2);

        int kingColumn = piece.getPosition().getColumn();
        int kingRow = piece.getPosition().getRow();

        Arrays.asList(0, 7).forEach(col -> {
            ChessPiece rooĸ = board.getPiece(kingRow, col);
            if (!(rooĸ instanceof Rook && ((Rook) rooĸ).isFirstMove())) return;

            Direction dir = col == 7 ? Direction.EAST : Direction.WEST;

            int limit = Math.abs(kingColumn - col);
            for (int i = 1; i < limit; i++) {
                int currentCol = kingColumn + (i * dir.getColumnOffset());
                if (board.getPiece(kingRow, currentCol) != null) {
                    return;
                }
            }

            List<Position> positions = ChessRule.getPathTo(piece, rooĸ.getPosition(), board, 4, dir);
            positions.add(piece.getPosition());
            for (ChessPiece enemyPiece : board.getChessPiecesByColor(invert(piece.getColor()))) {
                if (positions.stream().anyMatch(p -> enemyPiece.canMoveToPosition(board, p))) {
                    return;
                }
            }

            castleMoves.add(rooĸ.getPosition());
        });

        return castleMoves;
    }

    // --------------------------------------------

    private void switchTurn() {
        turn = invert(turn);
        selectedChessPiece = null;
    }

    private ChessPlayer getPlayerByColor(Color color) {
        return (color == Color.WHITE) ? whitePlayer : blackPlayer;
    }

    private ChessPiece getKing(Color color) {
        return board.getChessPiecesByColor(color).stream()
                .filter(p -> p instanceof King)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Error: King not found for " + color + " color!"));
    }

    private Color invert(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void validateTurn(Color color) {
        if (isOver) throw new IllegalStateException("Error: The match is over!");
        if (color != turn) throw new IllegalArgumentException("Error: Not player turn!");
    }

    private void validatePosition(int row, int column) {
        if (!board.positionExists(row, column)) {
            throw new IllegalArgumentException("Error: Invalid position! Valid positions are from a1 to h8.");
        }
    }

    private void validatePlayers(ChessPlayer white, ChessPlayer black) {
        if (white == null || black == null) throw new IllegalArgumentException("Error: Players cannot be null!");
        if (white.getColor() == black.getColor()) throw new IllegalArgumentException("Error: Players must have different colors!");
        if (!white.getCapturedPieces().isEmpty() || !black.getCapturedPieces().isEmpty()) {
            throw new IllegalArgumentException("Error: Players cannot have captured pieces at the start!");
        }
    }

    public ChessPiece[][] getPieces() {
        return board.getPieces();
    }

    public Color getTurn() {
        return turn;
    }

    public boolean isOver() {
        return isOver;
    }

    public ChessPlayer getWhitePlayer() {
        return whitePlayer;
    }

    public ChessPlayer getBlackPlayer() {
        return blackPlayer;
    }

    public boolean inPromotion() {
        return this.inPromotion;
    }

    public Position getPromotion() {
        return this.promotion;
    }

    public Color getWinner() {
        if (!isOver) {
            throw new IllegalStateException("Error: Cannot declare a winner because the game is not over yet!");
        }

        return winner;
    }
}
