package htk.example.typeclasses;

import htk.example.helpers.Kind1;
import htk.example.helpers.Tuple2;

public interface ApplicativeFunctor<WITNESS> extends Functor<WITNESS> {
    <A, F extends Kind1<WITNESS, A>> F pure(A value);
    <A, B, F extends Kind1<WITNESS, A>, G extends Kind1<WITNESS, B>, Res extends Kind1<WITNESS, Tuple2<A, B>>> Res
        product(F f, G g);
}
