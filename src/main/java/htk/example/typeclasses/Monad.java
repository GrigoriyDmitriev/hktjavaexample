package htk.example.typeclasses;

import htk.example.types.Tuple2;

import java.util.function.Function;

public interface Monad<WITNESS> extends ApplicativeFunctor<WITNESS> {
    <A, B, F extends Kind1<WITNESS, A>, G extends Kind1<WITNESS, B>> G flatMap(Function<? super A, ? extends G> fn, F f);

    // do { a <- f; pure a }
    @Override
    default <A, B, F extends Kind1<WITNESS, A>, G extends Kind1<WITNESS, B>> G map(Function<? super A, ? extends B> g, F f) {
        return flatMap(a -> this.<B, G>pure(g.apply(a)), f);
    }

    // do { a <- f; b <- g; pure (a, b) }
    @Override
    default <A, B, F extends Kind1<WITNESS, A>, G extends Kind1<WITNESS, B>, Res extends Kind1<WITNESS, Tuple2<A, B>>> Res product(F f, G g) {
        return flatMap(a -> flatMap(b -> this.<Tuple2<A, B>, Res>pure(Tuple2.t(a, b)), g) , f);
    }
}
