package htk.example.typeclasses;

import java.util.function.BiFunction;

public interface Foldable<WITNESS> {
    <A, B, In extends Kind1<WITNESS, A>> B fold(BiFunction<? super A, B, B> bif, B init, In in);
}
