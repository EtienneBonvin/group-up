package ch.epfl.sweng.groupup.object.account;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;

public final class Account extends User {

    private static final String NO_FIRST_NAME = "NO_FIRST_NAME";
    private static final String NO_LAST_NAME = "NO_LAST_NAME";
    private static final String NO_EMAIL = "default@defaul.default";

    public static Account shared = new Account(NO_FIRST_NAME, NO_LAST_NAME, NO_EMAIL,
            Optional.<Event>empty(), new ArrayList<Event>(), new ArrayList<Event>());

    private final Optional<Event> currentEvent;
    private final List<Event> pastEvents;
    private final List<Event> futureEvents;

    private Account(String firstName, String lastName, String email,
                    Optional<Event> currentEvent, List<Event> past, List<Event> future) {
        super(firstName, lastName, email);
        this.currentEvent = currentEvent;
        this.pastEvents = past;
        this.futureEvents = future;
    }

    /**
     * Getter for the first name of the account
     * @return String first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for the last name of the account
     * @return String last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for the email of the account
     * @return String email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for the current event of the account
     * @return Event current
     */
    public Optional<Event> getCurrentEvent() {
        return currentEvent;
    }

    /**
     * Getter for the list of past events
     * @return List<Event> last events
     */
    public List<Event> getPastEvents() {
        return pastEvents;
    }

    /**
     * Getter for the list of future events
     * @return List<Event> future events
     */
    public List<Event> getFutureEvents() {
        return futureEvents;
    }

    /**
     * Change the first name of the shared account
     * @param firstName
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withFirstName(String firstName) {
        shared = new Account(firstName, shared.getLastName(), shared.getEmail(),
                shared.currentEvent, shared.pastEvents, shared.futureEvents);
        return shared;
    }

    /**
     * Change the last name of the shared account
     * @param lastName
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withLastName(String lastName) {
        shared = new Account(shared.getFirstName(), lastName, shared.getEmail(),
                shared.currentEvent, shared.pastEvents, shared.futureEvents);
        return shared;
    }

    /**
     * Change the email of the shared account
     * @param email
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withEmail(String email) {
        if (emailCheck(email)){
            shared = new Account(shared.getFirstName(), shared.getLastName(), email,
                shared.currentEvent, shared.pastEvents, shared.futureEvents);
            return shared;
        }
        else throw new IllegalArgumentException("The email is not properly formed");

    }

    /**
     * Change the current event of the shared account
     * @param current event
     * @return the modified shared account, so that it is easier to call in chain
     */

    public Account withCurrentEvent(Optional<Event> current) {
        if (current.isEmpty()){
            shared = new Account(shared.getFirstName(), shared.getLastName(), shared.getEmail(),
                    current, shared.getPastEvents(), shared.getFutureEvents());
        }
        else {
            if (current.get().getEventStatus().equals(EventStatus.CURRENT)) {
                shared = new Account(shared.getFirstName(), shared.getLastName(), shared.getEmail(),
                        current, shared.getPastEvents(), shared.getFutureEvents());
            } else throw new IllegalArgumentException("Event is not "+ EventStatus.CURRENT.toString());
        }
        return shared;
    }

    /**
     * Change the past event list of the shared account
     * @param past event list
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withPastEvents(List<Event> past) {
        shared = new Account(shared.getFirstName(), shared.getLastName(), shared.getEmail(),
                shared.getCurrentEvent(), past, shared.getFutureEvents());
        return shared;
    }

    /**
     * Change the future event list of the shared account
     * @param future event list
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withFutureEvents(List<Event> future) {
        shared = new Account(shared.getFirstName(), shared.getLastName(), shared.getEmail(),
                shared.getCurrentEvent(), shared.getPastEvents(), future);
        return shared;
    }
    /**
     * Add a past event list of the shared account
     * @param past event to add
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account addPastEvent(Event past) {
        if (past.getEventStatus().equals(EventStatus.PAST)) {
            List<Event> newPast = new ArrayList<>(shared.getPastEvents());
            newPast.add(past);
            shared.withPastEvents(newPast);
            return shared;
        } else throw new IllegalArgumentException("Event is not "+ EventStatus.PAST.toString());
    }

    /**
     * Add a future event list of the shared account
     * @param future event to add
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account addFutureEvent(Event future) {
        if (future.getEventStatus().equals(EventStatus.FUTURE)) {
            List<Event>newFuture = new ArrayList<>(shared.getFutureEvents());
            newFuture.add(future);
            shared.withFutureEvents(newFuture);
            return shared;
        } else throw new IllegalArgumentException("Event is not "+ EventStatus.FUTURE.toString());
    }
    /**
     * Clear the shared account
     * @return a cleared shared account
     */
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
                ", currentEvent=" + currentEvent.get().toString() +
                ", pastEvents=" + pastEvents +
                ", futureEvents=" + futureEvents +
                '}';
    }

    /**
     * Print a shorter version of the account with just the names the email and the current event
     * @return string containing basic informations about an account
     */
    public String toStringShort() {
        return "Account{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", currentEvent=" + currentEvent.get().toString() +
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

