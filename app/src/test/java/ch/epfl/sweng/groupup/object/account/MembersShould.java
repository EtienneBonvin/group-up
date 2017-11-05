package ch.epfl.sweng.groupup.object.account;

import org.junit.Test;

import static org.junit.Assert.*;

public class MembersShould {
    @Test
    public void beCreated(){
        Member m = new Member("UUID", "XavierP", "Xavier", "Pantet", "xavier@pantet.ch", null);
    }

    @Test
    public void allowAccessToItsAttributes(){
        Member m = new Member("UUID", "XavierP", "Xavier", "Pantet", "xavier@pantet.ch", null);
        assertEquals(m.getUUID().get(), "UUID");
        assertEquals(m.getDisplayName().get(), "XavierP");
        assertEquals(m.getGivenName().get(), "Xavier");
        assertEquals(m.getFamilyName().get(), "Pantet");
        assertEquals(m.getEmail().get(), "xavier@pantet.ch");
    }

    @Test
    public void allowToBeModified(){
        Member m = new Member("UUID", "XavierP", "Xavier", "Pantet", "xavier@pantet.ch", null);
        m = m.withUUID("UUID2").withDisplayName("CedricM").withFirstName("Cedric").withLastName("Maire").withEmail("cedric@maire.de");
        assertEquals(m.getUUID().get(), "UUID2");
        assertEquals(m.getDisplayName().get(), "CedricM");
        assertEquals(m.getGivenName().get(), "Cedric");
        assertEquals(m.getFamilyName().get(), "Maire");
        assertEquals(m.getEmail().get(), "cedric@maire.de");
    }

    @Test
    public void beImmutable(){
        String UUID = "UUID";
        String displayName = "XavierP";
        String firstName = "Xavier";
        String lastName = "Pantet";
        String email = "xavier@pantet.ch";
        Member m0 = new Member(UUID, displayName, firstName, lastName, email, null);
        assertFalse(m0.withUUID("UUID2") == m0);
        assertFalse(m0.withDisplayName("CedricM") == m0);
        assertFalse(m0.withFirstName("Cedric") == m0);
        assertFalse(m0.withLastName("Maire") == m0);
        assertFalse(m0.withEmail("cedric@maire.de") == m0);
    }

    @SuppressWarnings({"EqualsBetweenInconvertibleTypes", "EqualsWithItself"})
    @Test
    public void beEquatable(){
        Member m1 = new Member("UUID", "XavierP", "Xavier", "Pantet", "xavier@pantet.ch", null);
        Member m2 = new Member("UUID2", "XavierP", "Xavier", "Pantet", "xavier@pantet.ch", null);
        Member m3 = new Member("UUID", "Xavier", "Xavier", "Pantet", "xavier@pantet.ch", null);
        Member m4 = new Member("UUID", "XavierP", "Xavie", "Pantet", "xavier@pantet.ch", null);
        Member m5 = new Member("UUID", "XavierP", "Xavier", "Pante", "xavier@pantet.ch", null);
        Member m6 = new Member("UUID", "XavierP", "Xavier", "Pantet", "xavier@pantet.com", null);
        String m7 = "Hello";

        assertTrue(m1.equals(m1));
        assertFalse(m1.equals(m2));
        assertFalse(m1.equals(m3));
        assertFalse(m1.equals(m4));
        assertFalse(m1.equals(m5));
        assertFalse(m1.equals(m6));
        assertFalse(m1.equals(m7));
    }
}
