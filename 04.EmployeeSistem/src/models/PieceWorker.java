package models;

public class PieceWorker extends Employee{
    private double wage;
    private int pieces;

    public PieceWorker(String firstName, String lastName, String socialSecurityNumber, double wage, int pieces) {
        super(firstName, lastName, socialSecurityNumber);
        if (wage < 0.0)
            throw new IllegalArgumentException("Wage must be >= 0.0");
        if (pieces < 0)
            throw new IllegalArgumentException("Pieces must be >= 0");
        this.wage = wage;
        this.pieces = pieces;
    }

    public void setWage(double wage) {
        if (wage < 0.0)
            throw new IllegalArgumentException("Wage must be >= 0.0");
        this.wage = wage;
    }

    public double getWage() {
        return wage;
    }

    public void setPieces(int pieces) {
        if (pieces < 0)
            throw new IllegalArgumentException("Pieces must be >= 0");
        this.pieces = pieces;
    }

    public int getPieces() {
        return pieces;
    }

    @Override
    public double getPaymentAmount() {
        return getWage() * getPieces();
    }

    @Override
    public String toString() {
        return String.format("""
                        piece worker: %s
                        wage: %.2f
                        pieces: %d""",
                super.toString(), getWage(), getPieces());
    }
}
