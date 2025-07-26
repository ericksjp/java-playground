package org.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.application.entities.ChessMatch;
import org.application.entities.ChessPlayer;
import org.application.entities.ChessPosition;
import org.application.entities.Color;
import org.application.entities.Position;
import org.application.ui.UI;

public class Main {
    public static List <ChessPosition> loadPlays(File file) throws FileNotFoundException, IOException {
        List<ChessPosition> moves = new ArrayList<>();

        var reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            String[] args = line.split(",");
            if (args.length == 2) {
                for (String arg : args) {
                    moves.add(new ChessPosition(arg.charAt(0), Integer.parseInt(arg.charAt(1) + "")));
                }
            }
            line = reader.readLine();
        }

        reader.close();
        return moves;
    }

    public static List <Character> loadPromotions(File file) throws FileNotFoundException, IOException {
        List<Character> promotions = new ArrayList<>();

        var reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            String[] args = line.split(",");
            if (args.length == 1) {
                promotions.add(args[0].charAt(0));
            }
            line = reader.readLine();
        }

        reader.close();
        return promotions;
    }

    public static void writePlay(FileWriter writer, ChessPosition from, ChessPosition to) throws FileNotFoundException, IOException {
        writer.append(from + "," + to + System.lineSeparator());
    }

    public static void writePromotion(FileWriter writer, char piece) throws FileNotFoundException, IOException {
        writer.append(piece + System.lineSeparator());
    }

    public static void putPlays(List<ChessPosition> plays, List<Character>promotions, ChessMatch match) {
        for (int i = 0; i < plays.size(); i++) {
            if (match.inPromotion()) {
                match.resolvePromotion(promotions.removeFirst(), match.getTurn());
            }

            if (i % 2 == 0) {
                match.selectPiece(plays.get(i).toPosition(), match.getTurn());
            } else {
                match.moveChessPiece(plays.get(i).toPosition(), match.getTurn());
            }
        }
    }

    public static void loadChessFile(String filepath, ChessMatch match) throws FileNotFoundException, IOException {
        File f = new File(filepath);
        if (!f.exists() && !f.isDirectory()) {
            throw new FileNotFoundException("Could not open file for reading :" + filepath);
        }

        List<ChessPosition> plays = loadPlays(f);
        List<Character> promotions = loadPromotions(f);
        putPlays(plays, promotions, match);
    }

    public static FileWriter loadFileWriter(String filepath) throws IOException {
        File file = new File(filepath);
        if (!file.exists() && !file.isDirectory()) {
            throw new FileNotFoundException("Could not open file for writing: " + filepath);
        }

        return new FileWriter(file, true);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner sc = new Scanner(System.in);
        ChessPlayer p1 = new ChessPlayer("WHITE", Color.WHITE);
        ChessPlayer p2 = new ChessPlayer("BLACK", Color.BLACK);
        ChessMatch match = new ChessMatch(p1, p2, Color.WHITE);

        if (args.length > 0) {
            loadChessFile(args[0], match);
        }

        FileWriter writer = null;
        if (args.length > 1) {
            writer = loadFileWriter(args[1]);
        }

        while (!match.isOver()) {
            try {
                if (match.inPromotion()) {
                    char promotionThing = promotionScreen(match, sc);
                    match.resolvePromotion(promotionThing, match.getTurn());
                    if (writer != null) {
                        writePromotion(writer, promotionThing);
                    }
                    
                    if (match.isOver()) break;;
                }
                ChessPosition src = selectScreen(match, sc);
                List<Position> moveable = match.selectPiece(src.toPosition(), match.getTurn());
                ChessPosition dest = destScreen(match, sc, src, moveable);
                match.moveChessPiece(dest.toPosition(), match.getTurn());
                if (writer != null) {
                    writePlay(writer, src, dest);
                }
            } catch (RuntimeException e) {
                System.out.print(e.getMessage());
                sc.nextLine();
            }
        }

        winnerScreen(match);
        if (writer != null) {
            writer.flush();
            writer.close();
        }
        sc.close();
    }

    public static ChessPosition selectScreen(ChessMatch match, Scanner sc) {
            UI.clearScreen();
            System.out.println("------- CAPTURED --------");
            UI.drawCapturedPieces(match.getWhitePlayer(), match.getBlackPlayer());
            System.out.println("--------- BOARD ---------");
            UI.drawChess(match.getPieces());
            System.out.println("------ " + match.getTurn() + " TURN -------");
            return UI.selectSource(sc);
    }

    public static ChessPosition destScreen(ChessMatch match, Scanner sc, ChessPosition src, List<Position> possiblePositions) {
            UI.clearScreen();
            System.out.println("------- CAPTURED --------");
            UI.drawCapturedPieces(match.getWhitePlayer(), match.getBlackPlayer());
            System.out.println("--------- BOARD ---------");
            UI.drawChessWithSelectedPositions(match.getPieces(), possiblePositions);
            System.out.println("------ " + match.getTurn() + " TURN -------");
            return UI.selectDest(sc, src);
    }

    public static char promotionScreen(ChessMatch match, Scanner sc) {
        UI.clearScreen();
        System.out.println("------- CAPTURED --------");
        UI.drawCapturedPieces(match.getWhitePlayer(), match.getBlackPlayer());
        System.out.println("--------- BOARD ---------");
        UI.drawChessWithSelectedPositions(match.getPieces(), List.of(match.getPromotion()));
        System.out.println("------ " + match.getTurn() + " TURN -------");
        System.out.print("Promote Pawn at '" + ChessPosition.toChessPosition(match.getPromotion()) + "' to (Q,B,N,R): ");
        char character = sc.nextLine().toUpperCase().charAt(0);
        return character;
    }

    public static void winnerScreen(ChessMatch match) {
        UI.clearScreen();
        System.out.println("------- CAPTURED --------");
        UI.drawCapturedPieces(match.getWhitePlayer(), match.getBlackPlayer());
        System.out.println("--------- BOARD ---------");
        UI.drawChessWithSelectedPositions(match.getPieces(), List.of(match.getPromotion()));
        if (match.getWinner() != null) {
            System.out.println("--- !!! " + match.getWinner() + " WINS !!! ----");
        } else {
            System.out.println("--- --- Stalemate DRAW --- ---");
        }
    }

}
