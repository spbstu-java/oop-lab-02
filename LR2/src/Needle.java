public class Needle {
    private final double sizeMm;
    private final String kind;

    public Needle() {
        this(4.0, "прямые спицы");
    }

    public Needle(double sizeMm, String kind) {
        this.sizeMm = sizeMm;
        this.kind = kind;
    }

    public double sizeMm() {
        return sizeMm;
    }

    public String kind() {
        return kind;
    }

    @Override
    public String toString() {
        return kind + " " + sizeMm + " мм";
    }
}
