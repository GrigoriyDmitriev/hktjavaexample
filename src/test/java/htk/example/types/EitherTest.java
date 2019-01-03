package htk.example.types;

import htk.example.typeclasses.Monad;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class EitherTest {

    @Test
    public void testMonad() {
        Monad<Either.Mu<Exception>> monad = Either.<Exception>Monad();
        Either<Exception, String> either = monad.pure("hello");
        Either<Exception, Integer> tryParse = monad.flatMap(EitherTest::f, either);

        assertTrue(tryParse.isLeft());
        assertTrue(tryParse.getLeft() instanceof NumberFormatException);

        either = monad.pure("15");
        tryParse = monad.flatMap(EitherTest::f, either);

        assertTrue(tryParse.isRight());
        assertEquals(15, tryParse.getRight());
    }

    private static Either<Exception, Integer> f(String num) {
        try {
            return Either.right(Integer.parseInt(num));
        } catch (Exception e) {
            return Either.left(e);
        }
    }

}