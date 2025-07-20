package org.application.entities;

import java.util.ArrayList;
import java.util.List;

import org.application.entities.pieces.ChessPiece;

public class ChessPlayer {
    private String name;
    private Color color;
    private List<ChessPiece> capturedPieces;

    public ChessPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
        this.capturedPieces = new ArrayList<>(16);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<ChessPiece> getCapturedPieces() {
        return capturedPieces;
    }

    public void addCapturedPiece(ChessPiece piece) {
        if (piece != null) {
            capturedPieces.add(piece);
        }
    }

    public void removeCapturedPiece(ChessPiece piece) {
        capturedPieces.remove(piece);
    }
}
