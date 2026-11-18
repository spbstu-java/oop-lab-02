public class Knitter {
    private final String name;
    private int finishedRows;
    private int needleChanges;
    private int wovenEnds;
    private int rescuedStitches;

    public Knitter(String name) {
        this.name = name;
    }

    public String castOn(int stitchCount, Needle needle) {
        return name + ": набрано " + stitchCount + " петель на " + needle;
    }

    public String chooseYarn(Yarn yarn, String project) {
        return name + ": для «" + project + "» взята пряжа " + yarn;
    }

    @Repeat(times = 5)
    public String showFinished(String item, int hours) {
        return name + ": «" + item + "» связано за " + hours + " ч";
    }

    @Repeat(times = 2)
    protected String knitRow(Stitch stitch, int stitches, Yarn yarn) {
        finishedRows++;
        return name + ": ряд " + finishedRows + ", " + stitches
                + " петель «" + stitch + "», пряжа " + yarn;
    }

    @Repeat(times = 3)
    protected String changeNeedle(Needle current, double newSizeMm) {
        needleChanges++;
        Needle next = new Needle(newSizeMm, current.kind());
        return name + ": смена спиц " + needleChanges + ", " + current + " заменены на " + next;
    }

    protected int countRows(int stitchesPerRow, int targetStitches) {
        if (stitchesPerRow <= 0) {
            throw new IllegalArgumentException("В ряду нужна хотя бы одна петля");
        }
        return targetStitches / stitchesPerRow;
    }

    @Repeat(times = 2)
    private String weaveEnds(Yarn yarn, int endCount) {
        wovenEnds++;
        int minutes = endCount * (yarn.meters() / 100 + 2);
        return name + ": заправка " + wovenEnds + ", концов " + endCount
                + ", пряжа " + yarn + ", " + minutes + " мин";
    }

    @Repeat(times = 1)
    private String rescueStitch(int row, int column, Stitch stitch) {
        rescuedStitches++;
        return name + ": поднята петля " + rescuedStitches
                + " «" + stitch + "», ряд " + row + ", место " + column;
    }

    private String writeNote(String note, int page) {
        return name + ": стр. " + page + ". " + note;
    }
}
