package htk.example.typeclasses;

import htk.example.helpers.Kind1;

import java.util.function.BiFunction;

public interface Foldable<WITNESS> {
    <A, B, In extends Kind1<WITNESS, A>> B fold(BiFunction<B, ? super A, B> bif, B init, In in);
}
