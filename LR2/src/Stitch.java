public enum Stitch {
    KNIT("лицевая"),
    PURL("изнаночная"),
    YARN_OVER("накид"),
    SLIP("снятая");

    private final String title;

    Stitch(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
