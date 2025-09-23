package by.geth.avmatrix;

public class AvMatrixState {
    private int pgm; // 0-based
    private int pvw; // 0-based
    private int auto;
    private int tbar;

    public AvMatrixState(int pgm, int pvw, int auto, int tbar) {
        this.pgm = pgm;
        this.pvw = pvw;
        this.auto = auto;
        this.tbar = tbar;
    }

    public int getPgm() {
        return pgm;
    }

    public int getPvw() {
        return pvw;
    }

    public int getAuto() {
        return auto;
    }

    public int getTbar() {
        return tbar;
    }

    @Override
    public String toString() {
        return "AvMatrixState{" +
                "pgm=" + pgm +
                ", pvw=" + pvw +
                ", auto=" + auto +
                ", tbar=" + tbar +
                '}';
    }
}
