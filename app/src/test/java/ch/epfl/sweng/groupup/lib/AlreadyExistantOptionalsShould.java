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

    @Test
    public void bePrettyPrintable() {
        assertEquals(empty.toString(), "Optional.empty");
        assertEquals(nonEmpty.toString(), "Optional[Hello]");
    }
}