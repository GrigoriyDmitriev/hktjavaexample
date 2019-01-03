package htk.example.helpers;

import htk.example.helpers.Trampoline;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrampolineTest {
    @Test
    public void testTrampoline() {
        Trampoline<Integer> one = Trampoline.lazy(() -> 100);
        assertEquals("10000", one.map(a -> a * 100).map(Object::toString).get());
    }

}