package processor;

public class Multiplexer {
    private int select;
    private String x, y, z; // x if multiplexer chose 0, y if multiplexer chose 1

    public Multiplexer() {
        select = 0;
        x = y = "";
    }
    public Multiplexer(String x, String y) {
        select = 0;
        this.x = x;
        this.y = y;
    }
    public Multiplexer(String x, String y, String z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public String select(int s) {
        if (s == 0)
            return x;
        else if (s == 1)
            return y;
        else
            return getLast();
    }
    public String select() {
        return select(select);
    }
    public String getLast() {
        return z;
    }
}
