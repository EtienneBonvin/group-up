package ch.epfl.sweng.groupup.object.account;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;

public final class Account {

    private static final String NO_FIRST_NAME = "NO_FIRST_NAME";
    private static final String NO_LAST_NAME = "NO_LAST_NAME";
    private static final String NO_EMAIL = "NO_EMAIL";

    public static Account shared = new Account(NO_FIRST_NAME, NO_LAST_NAME, NO_EMAIL,
            null, new ArrayList<Event>(), new ArrayList<Event>());

    private final String firstName;
    private final String lastName;
    private final String email;
    private final Event currentEvent;
    private final List<Event> pastEvents;
    private final List<Event> futureEvents;

    private Account(String firstName, String lastName, String email,
                    Event currentEvent, List<Event> past, List<Event> future) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.currentEvent = currentEvent;
        this.pastEvents = past;
        this.futureEvents = future;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public List<Event> getPastEvents() {
        return pastEvents;
    }

    public List<Event> getFutureEvents() {
        return futureEvents;
    }

    public Account withFirstName(String firstName) {
        shared = new Account(firstName, shared.getLastName(), shared.getEmail(),
                shared.currentEvent, shared.pastEvents, shared.futureEvents);
        return shared;
    }

    public Account withLastName(String lastName) {
        shared = new Account(shared.getFirstName(), lastName, shared.getEmail(),
                shared.currentEvent, shared.pastEvents, shared.futureEvents);
        return shared;
    }

    public Account withEmail(String email) {
        if (emailCheck(email)){
            shared = new Account(shared.getFirstName(), shared.getLastName(), email,
                shared.currentEvent, shared.pastEvents, shared.futureEvents);
            return shared;
        }
        else throw new IllegalArgumentException("The email is not properly formed");

    }

    public Account withCurrentEvent(Event current) {
        if (current.getEventStatus().equals(EventStatus.CURRENT)) {
            shared = new Account(shared.getFirstName(), shared.getLastName(), shared.getEmail(),
                    current, shared.getPastEvents(), shared.getFutureEvents());
            return shared;
        } else throw new IllegalArgumentException("Event is not "+ EventStatus.CURRENT.toString());
    }

    public Account withPastEvents(List<Event> past) {
        shared = new Account(shared.getFirstName(), shared.getLastName(), shared.getEmail(),
                shared.getCurrentEvent(), past, shared.getFutureEvents());
        return shared;
    }

    public Account withFutureEvents(List<Event> future) {
        shared = new Account(shared.getFirstName(), shared.getLastName(), shared.getEmail(),
                shared.getCurrentEvent(), shared.getPastEvents(), future);
        return shared;
    }

    public Account addPastEvent(Event past) {
        if (past.getEventStatus().equals(EventStatus.PAST)) {
            List<Event> newPast = new ArrayList<>(shared.getPastEvents());
            newPast.add(past);
            shared.withPastEvents(newPast);
            return shared;
        } else throw new IllegalArgumentException("Event is not "+ EventStatus.PAST.toString());
    }

    public Account addFutureEvent(Event future) {
        if (future.getEventStatus().equals(EventStatus.FUTURE)) {
            List<Event>newFuture = new ArrayList<>(shared.getFutureEvents());
            newFuture.add(future);
            shared.withFutureEvents(newFuture);
            return shared;
        } else throw new IllegalArgumentException("Event is not "+ EventStatus.FUTURE.toString());
    }

    public Account clear() {
        shared = new Account(NO_FIRST_NAME, NO_LAST_NAME, NO_EMAIL, null,
                new ArrayList<Event>(), new ArrayList<Event>());
        return shared;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!firstName.equals(account.firstName)) return false;
        if (!lastName.equals(account.lastName)) return false;
        if (!email.equals(account.email)) return false;
        if (currentEvent != null ? !currentEvent.equals(account.currentEvent)
                : account.currentEvent != null) {
            return false;
        }
        if (!pastEvents.equals(account.pastEvents)) return false;
        return futureEvents.equals(account.futureEvents);
    }

    @Override
    public String toString() {
        return "Account{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", currentEvent=" + currentEvent +
                ", pastEvents=" + pastEvents +
                ", futureEvents=" + futureEvents +
                '}';
    }

    public String toStringShort() {
        return "Account{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", currentEvent=" + currentEvent +
                '}';
    }

    /**
     * Check that the passed email is an "acceptable" form (not the icann official definition)
     * @param email
     * @return true of email ok else false
     */
    private boolean emailCheck(String email){
        Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,13}\\b",Pattern.CASE_INSENSITIVE);
        Matcher m=p.matcher(email);
        return m.matches();
        }
    }

