package htk.example.types;

import htk.example.typeclasses.Monads;
import htk.example.typeclasses.Monoids;
import org.junit.jupiter.api.Test;

import static htk.example.types.Option.MonadInstance.instance;
import static org.junit.jupiter.api.Assertions.*;

class OptionTest {
    @Test
    public void testMap() {
        Option<Integer> num = Option.opt(5);
        Option<String> str = instance.map(Object::toString, num);
        assertTrue(str.isDefined());
        assertEquals("5", str.get());
    }

    @Test
    public void testApplicative() {
        Option<Integer> one = instance.pure(1);
        Option<String> hello = instance.pure("hello");
        Option<Tuple2<Integer, String>> one_hello = instance.product(one, hello);
        assertTrue(one_hello.isDefined());
        assertEquals(1, one_hello.get().getA());
        assertEquals("hello", one_hello.get().getB());
    }

    @Test
    public void testMonad() {
        Option<String> one = instance.pure("1");
        Option<Integer> oneParsed = instance.flatMap(OptionTest::tryParse, one);
        Option<String> hello = instance.pure("hello");
        Option<Integer> helloFailed = instance.flatMap(OptionTest::tryParse, hello);

        assertTrue(oneParsed.isDefined());
        assertEquals(1, oneParsed.get());
        assertFalse(helloFailed.isDefined());
    }

    @Test
    public void testFoldable() {
        Option<String> none = Option.none();
        assertEquals("hello", Option.FoldableInstance.instance.fold((a, b) -> null, "hello", none));

        Option<Integer> one = instance.pure(1);
        assertEquals(2, Option.FoldableInstance.instance.fold((a, b) -> a + b, 1, one));
    }

    @Test
    public void testJoin() {
        Option<String> opt = instance.pure("hello");
        Option<Option<String>> optopt = instance.pure(opt);
        Option<String> flatten = Monads.join(instance, optopt);
        assertTrue(flatten.isDefined());
        assertEquals("hello", flatten.get());
    }

    @Test
    public void testFoldMap() {
        Option<String> opt = instance.pure("hello");
        assertEquals("hello", Monoids.foldMap(Option.FoldableInstance.instance, Monoids.String, opt));
        assertEquals("", Monoids.foldMap(Option.FoldableInstance.instance, Monoids.String, Option.none()));
    }

    private static Option<Integer> tryParse(String str) {
        try {
            return Option.opt(Integer.parseInt(str));
        } catch (Exception e) {
            return Option.none();
        }
    }
}