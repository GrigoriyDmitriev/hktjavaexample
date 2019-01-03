package htk.example.types;

import htk.example.typeclasses.Monads;
import htk.example.typeclasses.Monoids;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class ListKTest {
    @Test
    public void testFoldStrings() {
        ListK<String> numbers = ListK.of(asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"));
        assertEquals("1234567890", Monoids.foldMap(ListK.Foldable, Monoids.String, numbers));
    }

    @Test
    public void testMonad() {
        ListK<List<Integer>> listofLists = ListK.of(asList(asList(1, 2), asList(3, 4), asList(5, 6)));
        ListK<ListK<Integer>> listKListK = ListK.Monad.map(ListK::of, listofLists);
        assertEquals(asList(1, 2, 3, 4, 5, 6), Monads.join(ListK.Monad, listKListK));
    }

}