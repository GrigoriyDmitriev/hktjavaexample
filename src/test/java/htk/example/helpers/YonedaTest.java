package htk.example.helpers;

import htk.example.typeclasses.Functor;
import htk.example.types.ListK;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class YonedaTest {
    @Test
    public void testYoneda() {
        Integer[] lazinessCheck = new Integer[]{ 0 };
        Function<Integer, Integer> mapping = a -> { lazinessCheck[0]++; return a + 1; };
        ListK<Integer> ints = ListK.of(asList(1, 2, 3));
        Yoneda<Integer, ListK.Mu> yoneda = Yoneda.liftYoneda(ListK.Monad, ints);
        Functor<Yoneda.Mu<ListK.Mu>> yonedaFunctor = Yoneda.Functor();

        yoneda = yonedaFunctor.map(mapping, yoneda);
        assertEquals(0, lazinessCheck[0]);

        yoneda = yonedaFunctor.map(mapping, yoneda);
        assertEquals(0, lazinessCheck[0]);

        yoneda = yonedaFunctor.map(mapping, yoneda);
        assertEquals(0, lazinessCheck[0]);

        ints = yoneda.lower();
        assertEquals(asList(4, 5, 6), ints);
        assertEquals(9, lazinessCheck[0]);
    }
}