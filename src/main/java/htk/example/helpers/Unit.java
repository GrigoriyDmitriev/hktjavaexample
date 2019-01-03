package htk.example.helpers;

public class Unit {
    private static Unit instance = new Unit();

    private Unit() {}

    public static Unit get() {
        return instance;
    }
}
