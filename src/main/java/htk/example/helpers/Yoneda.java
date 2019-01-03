package htk.example.helpers;

import htk.example.typeclasses.Functor;

import java.util.function.Function;

//Abstracts over any functor making map operation effectively lazy
public final class Yoneda<A, FunctorWitness> implements Kind1<Yoneda.Mu<FunctorWitness>, A>{
    public static final class Mu<F> {}

    @SuppressWarnings("unchecked")
    public static <A, FunctorWitness, G extends Kind1<Mu<FunctorWitness>, A>> Yoneda<A, FunctorWitness> narrowK(G g) {
        return (Yoneda<A, FunctorWitness>)g;
    }

    @SuppressWarnings("unchecked")
    public static <A, FunctorWitness, G extends Kind1<Mu<FunctorWitness>, A>> G extendK(Yoneda<A, FunctorWitness> g) {
        return (G)g;
    }

    private static final Functor Functor = new FunctorInstance();
    @SuppressWarnings("unchecked")
    public static <FunctorWitness> Functor<Mu<FunctorWitness>> Functor() {
        return (Functor<Mu<FunctorWitness>>)Functor;
    }

    public static <A, FunctorWitness, F extends Kind1<FunctorWitness, A>> Yoneda<A, FunctorWitness> liftYoneda(Functor<FunctorWitness> functorInst, F functor) {
        return new Yoneda<>(functorInst, functor, Function.identity());
    }

    private final Functor<FunctorWitness> functorInst;
    private final Kind1<FunctorWitness, A> functor;
    private final Function fn;

    private <F extends Kind1<FunctorWitness, A>> Yoneda(Functor<FunctorWitness> functorInst, F functor, Function fn) {
        this.functor = functor;
        this.functorInst = functorInst;
        this.fn = fn;
    }

    @SuppressWarnings("unchecked")
    public <B> Yoneda<B, FunctorWitness> map(Function<? super A, ? extends B> fn) {
        return (Yoneda<B, FunctorWitness>)new Yoneda(functorInst, functor, this.fn.andThen(fn));
    }

    @SuppressWarnings("unchecked")
    public <F extends Kind1<FunctorWitness, A>> F lower() {
        return (F)functorInst.map(fn, functor);
    }

    private static class FunctorInstance<FunctorWitness> implements Functor<Mu<FunctorWitness>> {
        @Override
        public <A, B, F extends Kind1<Mu<FunctorWitness>, A>, G extends Kind1<Mu<FunctorWitness>, B>> G map(Function<? super A, ? extends B> g, F f) {
            Yoneda<A, FunctorWitness> yoneda = narrowK(f);
            return extendK(yoneda.<B>map(g));
        }
    }
}
