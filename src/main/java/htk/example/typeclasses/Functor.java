package htk.example.typeclasses;

import htk.example.helpers.Kind1;

import java.util.function.Function;

public interface Functor<WITNESS> {
    <A, B, F extends Kind1<WITNESS, A>, G extends Kind1<WITNESS, B>> G map(Function<? super A, ? extends B> g, F f);
}
