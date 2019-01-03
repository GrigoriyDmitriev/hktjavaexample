package htk.example.types;

import htk.example.typeclasses.Foldable;
import htk.example.typeclasses.Kind1;
import htk.example.typeclasses.Monad;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Option<T> implements Kind1<Option.Mu, T> {
    public final static class Mu {}

    public abstract T get();
    public abstract boolean isDefined();

    private final static class Some<T> extends Option<T> {
        private T value;

        private Some(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public boolean isDefined() {
            return true;
        }
    }

    private final static class None<T> extends Option<T> {
        private static None instance = new None();

        @Override
        public T get() {
            throw new NullPointerException();
        }

        @Override
        public boolean isDefined() {
            return false;
        }
    }

    public static <T> Option<T> opt(T value) {
        if (value == null) {
            return none();
        }
        return new Some<>(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> Option<T> none() {
        return (Option<T>)None.instance;
    }

    @SuppressWarnings("unchecked")
    public static <T, G extends Kind1<Mu, T>> Option<T> narrowK(G option) {
        return (Option<T>)option;
    }

    @SuppressWarnings("unchecked")
    public static <T, G extends Kind1<Mu, T>> G extendK(Option<T> option) {
        return (G)option;
    }

    public enum MonadInstance implements Monad<Mu> {
        instance;

        @Override
        public <A, B, F extends Kind1<Mu, A>, G extends Kind1<Mu, B>> G map(Function<? super A, ? extends B> g, F f) {
            Option<A> option = narrowK(f);
            if (option.isDefined()) {
                return extendK(Option.<B>opt(g.apply(option.get())));
            }
            return extendK(Option.<B>none());
        }


        @Override
        public <A, B, F extends Kind1<Mu, A>, G extends Kind1<Mu, B>> G flatMap(Function<? super A, ? extends G> fn, F f) {
            Option<A> option = narrowK(f);
            if (option.isDefined()) {
                return fn.apply(option.get());
            }
            return extendK(Option.<B>none());
        }

        @Override
        public <A, F extends Kind1<Mu, A>> F pure(A value) {
            return extendK(opt(value));
        }

        @Override
        public <A, B, F extends Kind1<Mu, A>, G extends Kind1<Mu, B>, Res extends Kind1<Mu, Tuple2<A, B>>> Res product(F f, G g) {
            Option<A> opt1 = narrowK(f);
            Option<B> opt2 = narrowK(g);
            if (opt1.isDefined() && opt2.isDefined()) {
                return extendK(opt(Tuple2.t(opt1.get(), opt2.get())));
            }
            return extendK(Option.<Tuple2<A, B>>none());
        }
    }

    public enum FoldableInstance implements Foldable<Mu> {
        instance;

        @Override
        public <A, B, In extends Kind1<Mu, A>> B fold(BiFunction<B, ? super A, B> bif, B init, In in) {
            Option<A> option = narrowK(in);
            if (option.isDefined()) {
                return bif.apply(init, option.get());
            }
            return init;
        }
    }
}
