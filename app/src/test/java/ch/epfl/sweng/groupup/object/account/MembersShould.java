package ch.epfl.sweng.groupup.object.account;

import org.junit.Test;

import static org.junit.Assert.*;

public class MembersShould {
    @Test
    public void beCreated(){
        Member m = new Member("Xavier", "Pantet", "xavier@pantet.ch");
    }

    @Test
    public void allowAccessToItsAttributes(){
        Member m = new Member("Xavier", "Pantet", "xavier@pantet.ch");
        assertEquals(m.getFirstName(), "Xavier");
        assertEquals(m.getLastName(), "Pantet");
        assertEquals(m.getEmail(), "xavier@pantet.ch");
    }

    @Test
    public void allowToBeModified(){
        Member m = new Member("Xavier", "Pantet", "xavier@pantet.ch");
        m = m.withFirstName("Cedric").withLastName("Maire").withEmail("cedric@maire.de");
        assertEquals(m.getFirstName(), "Cedric");
        assertEquals(m.getLastName(), "Maire");
        assertEquals(m.getEmail(), "cedric@maire.de");
    }

    @Test
    public void beImmutable(){
        String firstName = "Xavier";
        String lastName = "Pantet";
        String email = "xavier@pantet.ch";
        Member m0 = new Member(firstName, lastName, email);
        assertFalse(m0.withFirstName("Cedric") == m0);
        assertFalse(m0.withLastName("Maire") == m0);
        assertFalse(m0.withEmail("cedric@maire.de") == m0);
    }
}
