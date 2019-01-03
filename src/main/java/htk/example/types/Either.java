package htk.example.types;

import htk.example.helpers.Kind1;
import htk.example.typeclasses.Monad;

import java.util.function.Function;

public abstract class Either<A, B> implements Kind1<Either.Mu<A>, B> {
    public static final class Mu<A> {}

    @SuppressWarnings("unchecked")
    public static <A, B, F extends Kind1<Mu<A>, B>> Either<A, B> narrowK(F f) {
        return (Either<A, B>)f;
    }

    @SuppressWarnings("unchecked")
    public static <A, B, F extends Kind1<Mu<A>, B>> F extendK(Either<A, B> f) {
        return (F)f;
    }

    public static <A, B> Either<A, B> left(A value) {
        return new Left<>(value);
    }

    public static <A, B> Either<A, B> right(B value) {
        return new Right<>(value);
    }


    //Monad is right-biased
    public static <Z> Monad<Mu<Z>> Monad() {
        return new Monad<Mu<Z>>() {
            @Override
            @SuppressWarnings("unchecked")
            public <A, B, F extends Kind1<Mu<Z>, A>, G extends Kind1<Mu<Z>, B>> G flatMap(Function<? super A, ? extends G> fn, F f) {
                Either<Z, A> either = narrowK(f);
                if (either.isRight()) {
                    return fn.apply(either.getRight());
                }
                return extendK((Either<Z, B>)either);
            }

            @Override
            public <A, F extends Kind1<Mu<Z>, A>> F pure(A value) {
                return extendK(right(value));
            }
        };
    }


    public abstract boolean isRight();
    public abstract B getRight();
    public final boolean isLeft() { return !isRight(); }
    public abstract A getLeft();

    public final <T> T either(Function<A, T> onLeft, Function<B, T> onRight) {
        if (isRight()) {
            return onRight.apply(getRight());
        }
        return onLeft.apply(getLeft());
    }

    public static class Left<A, B> extends Either<A, B> {
        private A value;

        private Left(A value) {
            this.value = value;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public B getRight() {
            throw new IllegalStateException();
        }

        @Override
        public A getLeft() {
            return value;
        }
    }
    public static class Right<A, B> extends Either<A, B> {
        private B value;

        public Right(B value) {
            this.value = value;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public B getRight() {
            return value;
        }

        @Override
        public A getLeft() {
            return null;
        }
    }
}
