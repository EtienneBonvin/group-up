package ch.epfl.sweng.groupup.lib;

import static org.junit.Assert.*;
//import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({"EqualsBetweenInconvertibleTypes", "EqualsWithItself"})
public class NewOptionalsShould {
    private Optional<String> empty;
    private Optional<String> nonEmpty;

    @Before
    public void init(){
        empty = Optional.empty();
        nonEmpty = Optional.from("Hello");
    }

    @Test
    public void beEmptyWhenCreatedEmpty() {
        assertTrue(empty.isEmpty());
    }

    @Test
    public void beNonEmptyWhenCreatedNonEmpty() {
        assertFalse(nonEmpty.isEmpty());
    }

    @Test
    public void beCreatableFromNull() {
        Optional<String> opt = Optional.from(null);
        assertTrue(opt.isEmpty());
    }

    @Test
    public void beEquatable() {
        //Function<String, String> f = x -> x + " Xavier";
        assertFalse(empty.equals(nonEmpty));
        assertFalse(nonEmpty.equals(empty));
        assertTrue(empty.equals(empty));
        assertTrue(nonEmpty.equals(nonEmpty));
        assertFalse(empty.equals("Hello"));
        assertFalse(nonEmpty.equals("Hello"));
        //assertTrue(empty.map(f).equals(empty));
        //assertFalse(nonEmpty.map(f).equals(nonEmpty));
    }
}
