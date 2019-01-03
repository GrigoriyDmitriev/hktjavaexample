package htk.example.typeclasses;

import htk.example.helpers.Kind1;

import java.util.function.Function;

public class Functors {
    private Functors() {}

    //Functor f => (a -> b) -> (f a -> f b)
    public static <A, B, WITNESS,
            In extends Kind1<WITNESS, A>,
            Out extends Kind1<WITNESS, B>> Function<In, Out> liftF(Functor<WITNESS> functor, Function<? super A, ? extends B> f) {
        return a -> functor.<A, B, In, Out>map(f, a);
    }
}
