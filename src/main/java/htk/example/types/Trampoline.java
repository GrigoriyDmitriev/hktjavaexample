package htk.example.types;

import htk.example.typeclasses.Kind1;

import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Trampoline<A> implements Kind1<Trampoline.Mu, A> {
    public static final class Mu {}

    public abstract <B> Trampoline<B> map(Function<? super A, ? extends B> f);
    public <B> Trampoline<B> flatMap(Function<? super A, Trampoline<? extends B>> f) {
        return new FlatMapped<A, B>(this, f);
    }
    public abstract A get();
    public abstract boolean isDone();

    @SuppressWarnings("unchecked")
    public static <A> A run(Trampoline<A> trampoline) {
        LinkedList<Function> functionsStack = new LinkedList<>();
        Trampoline unsafeTrampoline = trampoline;
        while (true) {
            if (unsafeTrampoline.isDone()) {
                Object value = unsafeTrampoline.get();
                if (!functionsStack.isEmpty()) {
                    unsafeTrampoline = (Trampoline)functionsStack.pop().apply(value);
                } else {
                    return (A)value;
                }
            } else {
                FlatMapped mapped = (FlatMapped)unsafeTrampoline;
                unsafeTrampoline = mapped.start;
                functionsStack.push(mapped.fn);
            }
        }
    }

    public static <A> Trampoline<A> strict(A value) {
        return new Done<>(value);
    }

    public static <A> Trampoline<A> lazy(Supplier<A> value) {
        return new Done<>(null).flatMap(a -> new Done<>(value.get()));
    }

    private static class Done<A> extends Trampoline<A> {
        private final A value;

        @Override
        public <B> Trampoline<B> map(Function<? super A, ? extends B> f) {
            return new Done<>(f.apply(value));
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public A get() {
            return value;
        }

        public Done(A value) {
            this.value = value;
        }
    }

    private static class FlatMapped<C, A> extends Trampoline<A> {
        private Trampoline<C> start;
        private Function<? super C, Trampoline<? extends A>> fn;

        public FlatMapped(Trampoline<C> start, Function<? super C, Trampoline<? extends A>> fn) {
            this.start = start;
            this.fn = fn;
        }

        @Override
        public <B> Trampoline<B> map(Function<? super A, ? extends B> f) {
            return new FlatMapped<>(this, a -> new Done<>(f.apply(a)));
        }

        @Override
        public A get() {
            return run(this);
        }

        @Override
        public boolean isDone() {
            return false;
        }
    }
}
