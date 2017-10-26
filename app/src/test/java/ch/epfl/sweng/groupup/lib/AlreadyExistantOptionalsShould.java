package ch.epfl.sweng.groupup.lib;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;
//import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

public class AlreadyExistantOptionalsShould {
    private Optional<String> empty;
    private Optional<String> nonEmpty;

    @Before
    public void init(){
        empty = Optional.empty();
        nonEmpty = Optional.from("Hello");
    }

    @Test(expected=NoSuchElementException.class)
    public void throwExceptionWhenGetIsCalledOnEmpty() {
        empty.get();
    }

    @Test
    public void returnValueWhenGetIsCalledOnNonEmpty() {
        assertEquals(nonEmpty.get(), "Hello");
    }

    @Test
    public void returnProvidedValueForGetOrElseWhenEmpty() {
        assertEquals(empty.getOrElse("Alternate"), "Alternate");
    }

    @Test
    public void returnInternalValueForGetOrElseWhenNonEmpty() {
        assertEquals(nonEmpty.getOrElse("Alternate"), "Hello");
    }

    /*@Test
    public void beMappableToEmptyOptionalOnEmpty() {
        assertTrue(empty.map(x -> 42).isEmpty());
    }*/

    /*@Test
    public void beMappableToOtherObjectOnNonEmpty() {
        Function<String, String> f = x -> x + " Xavier";
        Function<String, Integer> g = x -> x.length();
        assertEquals(nonEmpty.map(f).get(), "Hello Xavier");
        assertEquals(nonEmpty.map(g).get(), new Integer(5));
        assertEquals(nonEmpty.map(f).map(g).get(), new Integer(12));
    }*/

    /*@Test
    public void beFlatMappableToEmptyOnEmpty() {
        Function<String, Optional<Integer>> f = x -> Optional.<Integer>from(x.length());
        assertTrue(empty.flatMap(f).isEmpty());
    }*/

    /*@Test
    public void beFlatMappableToOtherObjectOnNonEmpty() {
        Function<String, Optional<Integer>> f = x -> Optional.<Integer>from(x.length());
        assertEquals(nonEmpty.flatMap(f).get(), new Integer(5));
    }*/

    /*@Test
    public void beMatchableAgainstAPredicate() {
        Function<String, String> f = x -> x + " Xavier";
        Function<String, Boolean> p = x -> x.length() == 5;
        assertFalse(empty.match(p));
        assertTrue(nonEmpty.match(p));
        assertFalse(nonEmpty.map(f).match(p));
    }*/

    /*@Test
    public void beMonads() {
        Function<Integer, Optional<Integer>> f = x -> Optional.<Integer>from(2*x);
        Function<Integer, Optional<Integer>> g = x -> Optional.<Integer>from(x);
        Function<Integer, Optional<Integer>> h = x -> Optional.<Integer>from(3*x);
        Function<Integer, Optional<Integer>> i = x -> f.apply(x).flatMap(y -> h.apply(y));

        // Left identity
        assertEquals(Optional.<Integer>from(10).flatMap(f), f.apply(10));

        // Right identity
        assertEquals(Optional.<Integer>from(10).flatMap(g), Optional.<Integer>from(10));

        // Associativity
        assertEquals(Optional.<Integer>from(10).flatMap(f).flatMap(h), Optional.<Integer>from(10).flatMap(i));
    }*/

    @Test
    public void bePrettyPrintable() {
        assertEquals(empty.toString(), "Optional.empty");
        assertEquals(nonEmpty.toString(), "Optional[Hello]");
    }
}