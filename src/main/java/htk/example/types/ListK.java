package htk.example.types;

import htk.example.typeclasses.Foldable;
import htk.example.typeclasses.Kind1;
import htk.example.typeclasses.Monad;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public class ListK<T> implements Kind1<ListK.Mu, T>, List<T> {
    public static final class Mu {}

    public static final Monad<Mu> monad = new Monad<Mu>() {
        @Override
        public <A, B, F extends Kind1<Mu, A>, G extends Kind1<Mu, B>> G flatMap(Function<? super A, ? extends G> fn, F f) {
            ListK<A> list = narrowK(f);
            ListK<B> result = ListK.empty();
            for (A elem : list) {
                result.addAll(narrowK(fn.apply(elem)));
            }
            return extendK(result);
        }

        @Override
        public <A, F extends Kind1<Mu, A>> F pure(A value) {
            List<A> singleElemList = new ArrayList<>();
            singleElemList.add(value);
            return extendK(ListK.of(singleElemList));
        }
    };

    public static final Foldable<Mu> foldable = new Foldable<Mu>() {
        @Override
        public <A, B, In extends Kind1<Mu, A>> B fold(BiFunction<B, ? super A, B> bif, B init, In in) {
            ListK<A> list = narrowK(in);
            B result = init;
            for (A elem : list) {
                result = bif.apply(result, elem);
            }
            return result;
        }
    };

    @SuppressWarnings("unchecked")
    public static <T, F extends Kind1<Mu, T>> ListK<T> narrowK(F f) {
        return (ListK<T>)f;
    }

    @SuppressWarnings("unchecked")
    public static <T, F extends Kind1<Mu, T>> F extendK(ListK<T> f) {
        return (F)f;
    }

    public static <T> ListK<T> of(List<T> delegate) {
        return new ListK<>(delegate);
    }

    public static <T> ListK<T> empty() {
        return new ListK<>(new ArrayList<>());
    }

    private final List<T> delegate;

    public ListK(List<T> delegate) {
        this.delegate = delegate;
    }

    public List<T> unpack() {
        return delegate;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return delegate.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        return delegate.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return delegate.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return delegate.addAll(collection);
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        return delegate.addAll(i, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return delegate.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return delegate.retainAll(collection);
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        delegate.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        delegate.sort(c);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public T get(int i) {
        return delegate.get(i);
    }

    @Override
    public T set(int i, T t) {
        return delegate.set(i, t);
    }

    @Override
    public void add(int i, T t) {
        delegate.add(i, t);
    }

    @Override
    public T remove(int i) {
        return delegate.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return delegate.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return delegate.listIterator(i);
    }

    @Override
    public List<T> subList(int i, int i1) {
        return delegate.subList(i, i1);
    }

    @Override
    public Spliterator<T> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return delegate.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return delegate.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return delegate.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        delegate.forEach(action);
    }
}
