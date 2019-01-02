package htk.example.typeclasses;

public class Monoids {
    private Monoids() {}

    // (Foldable f, Monoid m) => f m -> m
    public static <A, FoldableWtns, In extends Kind1<FoldableWtns, A>> A foldMap(Foldable<FoldableWtns> foldable, Monoid<A> monoid, In in) {
        return foldable.fold(monoid::plus, monoid.id(), in);
    }

    public static class MonoidString implements Monoid<String> {
        public static final MonoidString _i = new MonoidString();
        private MonoidString() {}

        @Override
        public String id() {
            return "";
        }

        @Override
        public String plus(String fst, String snd) {
            return fst + snd;
        }
    }
}
