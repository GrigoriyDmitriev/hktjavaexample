package htk.example.typeclasses;

public class Monoids {
    private Monoids() {}

    public static Monoid<String> String = new MonoidString();

    public static <T> Monoid<T> Dual(Monoid<T> monoid) {
        return new Monoid<T>() {
            @Override
            public T id() {
                return monoid.id();
            }

            @Override
            public T plus(T fst, T snd) {
                return monoid.plus(snd, fst);
            }
        };
    }

    // (Foldable f, Monoid m) => f m -> m
    public static <A, FoldableWtns, In extends Kind1<FoldableWtns, A>> A foldMap(Foldable<FoldableWtns> foldable, Monoid<A> monoid, In in) {
        return foldable.fold(monoid::plus, monoid.id(), in);
    }

    private static class MonoidString implements Monoid<String> {
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
