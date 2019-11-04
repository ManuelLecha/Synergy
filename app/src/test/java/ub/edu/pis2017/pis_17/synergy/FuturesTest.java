package ub.edu.pis2017.pis_17.synergy;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kanales on 07/05/2018.
 */


class Buildable {
    int a;
    int b;

    public Buildable(int a, int b) {
        this.a = a;
        this.b = b;
    }
}

class Builder {
    int a;
    int b;

    public void setA(int a) {
        this.a = a;
    }

    public void setB(int b) {
        this.b = b;
    }

    public Buildable build() {
        return new Buildable(a,b);
    }
}

public class FuturesTest {
    FutureBox<Integer> fi, fi2;
    FutureBox<String> fs;
    int placeholderi;
    String placeholders;
    boolean finalized;
    Buildable x;

    private FutureBox<Integer> askForInteger() {
        FutureBox<Integer> f = FutureBox.empty();
        this.fi = f;
        return f;
    }

    private FutureBox<Integer> askForInteger2() {
        FutureBox<Integer> f = FutureBox.empty();
        this.fi2 = f;
        return f;
    }

    private FutureBox<String> askForString() {
        FutureBox<String> f = FutureBox.empty();
        this.fs = f;
        return f;
    }


    private void receiveInteger(int i) {
        this.fi.give(i);
    }

    private void receiveInteger2(int i) {
        this.fi2.give(i);
    }

    private void receiveString(String s) {
        this.fs.give(s);
    }

    @Before
    public void init() {
        placeholderi = 0;
        placeholders = null;
        finalized = false;
        x = null;
    }

    @Test
    public void futuresWork() {
        FutureBox<Integer> fi = askForInteger();
        assertFalse(fi.isDone());
        fi.whenFull(x -> this.placeholderi = x);
        receiveInteger(7);
        assertEquals(placeholderi, 7);
        assertTrue(fi.isDone());
    }

    @Test
    public void multipleFuturesAtTheSameTime() {
        FutureBox<Integer> fi = askForInteger();
        FutureBox<String> fs = askForString();
        fi.whenFull(x -> placeholderi = x);
        fs.whenFull(s -> placeholders = s);
        fi.also(fs).resolve(() -> finalized = true);
        assertFalse(finalized);
        receiveInteger(7);
        assertFalse(finalized);
        receiveString("S");
        assertTrue(finalized);
    }

    @Test
    public void builderTest() {
        Builder b = new Builder();

        FutureBox<Integer> fi1 = askForInteger();
        FutureBox<Integer> fi2 = askForInteger2();

        fi1.whenFull(i -> b.setA(i));
        fi2.whenFull(i -> b.setB(i));
        fi1.also(fi2).resolve(() -> x=b.build());
        assertNull(x);
        receiveInteger(5);
        assertNull(x);
        receiveInteger2(7);
        assertNotNull(x);
        assertEquals(5,x.a);
        assertEquals(7,x.b);
    }

    @Test
    public void cancellationTest() {
        Builder b = new Builder();

        placeholderi = 0; // not used
        FutureBox<Integer> fi1 = askForInteger();

        fi1.whenFull(i -> {
            placeholderi = i;
        }).onCancellation((err) -> {
            placeholders = err.toString();
        });

        fi1.error("error");
        assertEquals("error", placeholders);
        assertEquals(0,placeholderi);
    }

    @Test
    public void pipelineCancellationTest() {
        Builder b = new Builder();

        placeholderi = 0; // not used
        FutureBox<Integer> fi1 = askForInteger();
        FutureBox<Integer> fi2 = askForInteger2();

        fi1.whenFull(i -> {
            placeholderi = i;
        });
        fi2.whenFull(i -> {
            placeholderi += i*10;
        }).onCancellation((err) -> {
            placeholders = (String) err;
        });

        fi1.give(4);
        fi2.error("error");
        fi2.give(1);
        assertEquals(4,placeholderi);
        assertEquals("error",placeholders);
    }


    private FutureBox<Integer> delayedAllocation(int i) {
        placeholderi = i;
        fi2 = FutureBox.empty();
        return fi2;
    }

    private void allocate() {
        fi2.give(placeholderi);
    }

    @Test
    public void flatMapTest() {
        FutureBox<Integer> f = FutureBox.empty();
        FutureBox<Integer> mappedf = f.flatMap(this::delayedAllocation);

        mappedf.whenFull((Integer x) -> {
            assertEquals(7, x.intValue());
        });

        assertFalse(mappedf.isDone());
        f.give(7);
        assertFalse(mappedf.isDone());
        assertTrue(f.isDone());

        allocate();
        assertTrue(mappedf.isDone());
    }

    @Test
    public void monadicLaws() {
        //
        askForInteger().flatMap(FutureBox::of).whenFull(x -> {
            assertEquals(7,x.intValue());
        });
        receiveInteger(7);

        FutureBox.of(3).flatMap(x -> delayedAllocation(x)).whenFull(x -> {
            assertEquals(3,x.intValue());
        });
        allocate();

        (FutureBox.of(3).flatMap(x -> FutureBox.of(x+1)))
                       .flatMap(x -> FutureBox.of(x*2))
                       .whenFull(x -> placeholderi = x);
        FutureBox.of(3).flatMap(x -> FutureBox.of(x+1).flatMap(y -> FutureBox.of(y*2))
                .whenFull(z -> assertEquals(placeholderi, z.intValue())));
    }
}
