package htk.example.typeclasses;

import htk.example.types.Unit;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Monads {
    private Monads() {}

    // Monad m => m (m a) -> m a
    public static <A, WITNESS,
            In extends Kind1<WITNESS, Out>,
            Out extends Kind1<WITNESS, A>> Out join(Monad<WITNESS> monad, In in) {
        return monad.flatMap(Function.identity(), in);
    }

    // (Foldable t, Monad m) => t a -> (a -> m b) -> m ()
    public static <A, B, FoldableWtns, MonadWtns,
            In extends Kind1<FoldableWtns, A>,
            FOut extends Kind1<MonadWtns, B>,
            Out extends Kind1<MonadWtns, Unit>> Out mapM_(Foldable<FoldableWtns> foldable, Monad<MonadWtns> monad, Function<? super A, FOut> f, In in) {
        BiFunction<Out, A, Out> foldFunc = (mu, a) -> monad.flatMap(c -> monad.<Unit, Out>pure(Unit.get()),f.apply(a));
        return foldable.fold(foldFunc, monad.pure(Unit.get()), in);
    }
}
