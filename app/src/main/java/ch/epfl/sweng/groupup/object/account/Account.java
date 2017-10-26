package ch.epfl.sweng.groupup.object.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;

public final class Account extends User {

    public static Account shared = new Account(Optional.<String>empty(), Optional.<String>empty(), Optional.<String>empty(),
            Optional.<String>empty(), Optional.<String>empty(), Optional.<Event>empty(),
            new ArrayList<Event>(), new ArrayList<Event>());

    private final Optional<Event> currentEvent;
    private final List<Event> pastEvents;
    private final List<Event> futureEvents;

    private Account(Optional<String> UUID, Optional<String> displayName, Optional<String> givenName,
                    Optional<String> familyName, Optional<String> email,
                    Optional<Event> currentEvent, List<Event> past, List<Event> future) {
        super(displayName, givenName, familyName, email, UUID);
        this.currentEvent = currentEvent;
        this.pastEvents = past;
        this.futureEvents = future;
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
     * Change the UUID of the shared account
     * @param UUID the new UUID
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withUUID(String UUID) {
        shared = new Account(Optional.from(UUID), displayName, givenName, familyName, email, currentEvent, pastEvents, futureEvents);
        return shared;
    }

    /**
     * Change the display name of the shared account
     * @param displayName the new display name
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withDisplayName(String displayName) {
        shared = new Account(UUID, Optional.from(displayName), givenName, familyName, email,
                currentEvent, pastEvents, futureEvents);
        return shared;
    }

    /**
     * Change the first name of the shared account
     * @param givenName the new given name
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withGivenName(String givenName) {
        shared = new Account(UUID, displayName, Optional.from(givenName), familyName, email,
                currentEvent, pastEvents, futureEvents);
        return shared;
    }

    /**
     * Change the last name of the shared account
     * @param familyName the new family name
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withFamilyName(String familyName) {
        shared = new Account(UUID, displayName, givenName, Optional.from(familyName), email,
                currentEvent, pastEvents, futureEvents);
        return shared;
    }

    /**
     * Change the email of the shared account
     * @param email the new email
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withEmail(String email) {
        if (emailCheck(email)) {
            shared = new Account(UUID, displayName, givenName, familyName, Optional.from(email),
                    currentEvent, pastEvents, futureEvents);
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
            shared = new Account(UUID, displayName, givenName, familyName, email,
                    current, pastEvents, futureEvents);
        }
        else {
            if (current.get().getEventStatus().equals(EventStatus.CURRENT)) {
                shared = new Account(UUID, displayName, givenName, familyName, email,
                        current, pastEvents, futureEvents);
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
        shared = new Account(UUID, displayName, givenName, familyName, email,
                currentEvent, past, futureEvents);
        return shared;
    }

    /**
     * Change the future event list of the shared account
     * @param future event list
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withFutureEvents(List<Event> future) {
        shared = new Account(UUID, displayName, givenName, familyName, email,
                currentEvent, pastEvents, future);
        return shared;
    }

    /**
     * Add an event to the right event list depending on its EventStatus property
     * @param event the event to add
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account addOrUpdateEvent(Event event) {
        switch (event.getEventStatus()){
            case FUTURE:
                return addOrUpdateFutureEvent(event);
            case PAST:
                return addOrUpdatePastEvent(event);
            default:
                return withCurrentEvent(Optional.<Event>from(event));
        }
    }

    /**
     * Add a past event list of the shared account or updates it if it already exists
     * @param past event to add
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account addOrUpdatePastEvent(Event past) {
        if (past.getEventStatus().equals(EventStatus.PAST)) {
            List<Event> newPast = new ArrayList<>(pastEvents);
            Iterator<Event> eventIterator = pastEvents.iterator();
            int i = 0;
            boolean found = false;
            while(eventIterator.hasNext() && !found){
                Event e = eventIterator.next();
                if(e.getUUID().equals(past.getUUID())){
                    newPast.set(i, past);
                    found = true;
                }
                ++i;
            }
            if(!found){
                newPast.add(past);
            }
            return withPastEvents(newPast);
        } else throw new IllegalArgumentException("Event is not "+ EventStatus.PAST.toString());
    }

    /**
     * Add a future event list of the shared account or updates it if is already exists
     * @param future event to add
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account addOrUpdateFutureEvent(Event future) {
        if (future.getEventStatus().equals(EventStatus.FUTURE)) {

            List<Event> newFuture = new ArrayList<>(futureEvents);
            Iterator<Event> eventIterator = futureEvents.iterator();
            int i = 0;
            boolean found = false;
            while(eventIterator.hasNext() && !found){
                Event e = eventIterator.next();
                if(e.getUUID().equals(future.getUUID())){
                    newFuture.set(i, future);
                    found = true;
                }
                ++i;
            }
            if(!found){
                newFuture.add(future);
                Collections.sort(newFuture, new Comparator<Event>() {
                  @Override
                  public int compare(Event o1, Event o2) {
                      return o2.getStartTime().compareTo(o1.getStartTime());
                  }
              });
            }
            return withFutureEvents(newFuture);
        } else throw new IllegalArgumentException("Event is not "+ EventStatus.FUTURE.toString());
    }
    /**
     * Clear the shared account
     * @return a cleared shared account
     */
    public Account clear() {
        shared = new Account(Optional.<String>empty(), Optional.<String>empty(), Optional.<String>empty(), Optional.<String>empty(), Optional.<String>empty(), Optional.<Event>empty(), new ArrayList<Event>(), new ArrayList<Event>());
        return shared;
    }

    @Override
    public String toString() {
        return "Account{" +
                "UUID='" + UUID + '\'' +
                "displayName='" + displayName + '\'' +
                "givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
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
                "givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", email='" + email + '\'' +
                ", currentEvent=" + currentEvent.get().toString() +
                '}';
    }

    /**
     * Check that the passed email is an "acceptable" form (not the icann official definition)
     * @param email the email to check
     * @return true of email ok else false
     */
    private boolean emailCheck(String email){
        Pattern p = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,13}\\b",Pattern.CASE_INSENSITIVE);
        Matcher m=p.matcher(email);
        return m.matches();
    }

    /**
     * Converts an Account to a Member (essentially for comparison purpose)
     * @return a members representing the account
     */
    public Member toMember(){
        return new Member(UUID, displayName, givenName, familyName, email);
    }
}