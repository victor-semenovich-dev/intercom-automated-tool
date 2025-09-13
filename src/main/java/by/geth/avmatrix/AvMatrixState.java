package by.geth.avmatrix;

public class AvMatrixState {
    private int pgm; // 0-based
    private int pvw; // 0-based

    public AvMatrixState(int pgm, int pvw) {
        this.pgm = pgm;
        this.pvw = pvw;
    }

    public int getPgm() {
        return pgm;
    }

    public int getPvw() {
        return pvw;
    }

    @Override
    public String toString() {
        return "AvMatrixState{" +
                "pgm=" + pgm +
                ", pvw=" + pvw +
                '}';
    }
}
