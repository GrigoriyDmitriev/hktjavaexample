package htk.example.typeclasses;

// a + id = a
// id + a = a
public interface Monoid<T> extends Semigroup<T> {
    T id();
}
