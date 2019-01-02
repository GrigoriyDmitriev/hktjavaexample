package htk.example.typeclasses;

// a + (b + c) = (a + b) + c
public interface Semigroup<T> {
    T plus(T fst, T snd);
}
