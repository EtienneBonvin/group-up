package ch.epfl.sweng.groupup.object.account;

import org.junit.Test;

import static org.junit.Assert.*;

public class MembersShould {
    @Test
    public void beCreated(){
        Member m = new Member("XavierP", "Xavier", "Pantet", "xavier@pantet.ch");
    }

    @Test
    public void allowAccessToItsAttributes(){
        Member m = new Member("XavierP", "Xavier", "Pantet", "xavier@pantet.ch");
        assertEquals(m.getDisplayName().get(), "XavierP");
        assertEquals(m.getGivenName().get(), "Xavier");
        assertEquals(m.getFamilyName().get(), "Pantet");
        assertEquals(m.getEmail().get(), "xavier@pantet.ch");
    }

    @Test
    public void allowToBeModified(){
        Member m = new Member("XavierP", "Xavier", "Pantet", "xavier@pantet.ch");
        m = m.withDisplayName("CedricM").withFirstName("Cedric").withLastName("Maire").withEmail("cedric@maire.de");
        assertEquals(m.getDisplayName().get(), "CedricM");
        assertEquals(m.getGivenName().get(), "Cedric");
        assertEquals(m.getFamilyName().get(), "Maire");
        assertEquals(m.getEmail().get(), "cedric@maire.de");
    }

    @Test
    public void beImmutable(){
        String displayName = "XavierP";
        String firstName = "Xavier";
        String lastName = "Pantet";
        String email = "xavier@pantet.ch";
        Member m0 = new Member(displayName, firstName, lastName, email);
        assertFalse(m0.withDisplayName("CedricM") == m0);
        assertFalse(m0.withFirstName("Cedric") == m0);
        assertFalse(m0.withLastName("Maire") == m0);
        assertFalse(m0.withEmail("cedric@maire.de") == m0);
    }
}
