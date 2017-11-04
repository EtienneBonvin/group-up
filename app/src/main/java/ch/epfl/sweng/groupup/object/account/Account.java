package ch.epfl.sweng.groupup.object.account;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;

public final class Account extends User {

    public static Account shared = new Account(Optional.<String>empty(),
                                               Optional.<String>empty(),
                                               Optional.<String>empty(),
                                               Optional.<String>empty(),
                                               Optional.<String>empty(),
                                               Optional.<Event>empty(),
                                               new ArrayList<Event>(),
                                               new ArrayList<Event>(),
                                               Optional.<Location>empty());

    private final Optional<Event> currentEvent;
    private final List<Event> pastEvents;
    private final List<Event> futureEvents;

    private Account(Optional<String> UUID,
                    Optional<String> displayName,
                    Optional<String> givenName,
                    Optional<String> familyName,
                    Optional<String> email,
                    Optional<Event> currentEvent,
                    List<Event> past,
                    List<Event> future,
                    Optional<Location> location) {
        super(displayName, givenName, familyName, email, UUID, location);
        this.currentEvent = currentEvent.isEmpty() ?
                            Optional.<Event>empty() :
                            Optional.from(currentEvent.get());
        this.pastEvents = new ArrayList<>(Collections.unmodifiableList(past));
        this.futureEvents =
                new ArrayList<>(Collections.unmodifiableList(future));
    }

    /**
     * Getter for the current event of the account
     *
     * @return Event current
     */
    public Optional<Event> getCurrentEvent() {
        updateEventList();
        return currentEvent.isEmpty() ?
               Optional.<Event>empty() :
               Optional.from(currentEvent.get());
    }

    /**
     * Getter for the list of past events
     *
     * @return List<Event> last events
     */
    public List<Event> getPastEvents() {
        updateEventList();
        return new ArrayList<>(Collections.unmodifiableList(pastEvents));
    }

    /**
     * Getter for the list of future events
     *
     * @return List<Event> future events
     */
    public List<Event> getFutureEvents() {
        updateEventList();
        return new ArrayList<>(Collections.unmodifiableList(futureEvents));
    }

    /**
     * Getter for all events, return first the future then current then past
     */
    public List<Event> getEvents() {
        updateEventList();
        List<Event> allEvents = new ArrayList<>(futureEvents);
        if (!currentEvent.isEmpty()) {
            allEvents.add(currentEvent.get());
        }
        allEvents.addAll(pastEvents);

        return Collections.unmodifiableList(allEvents);
    }

    /**
     * updated futureEvent and currentEvent based on eventStatus
     *
     * @return Account with updated and sorted event lists
     */
    private void updateEventList() {
        List<Event> newFuture = new ArrayList<>(futureEvents);
        List<Event> newPast = new ArrayList<>(pastEvents);

        // check if currentEvent still current, else add to newPast and set currentEvent to empty
        if (!currentEvent.isEmpty() &&
            currentEvent.get().getEventStatus().equals(EventStatus.PAST)) {
            newPast.add(currentEvent.get());
            Account.shared.withCurrentEvent(Optional.<Event>empty());
        }

        // check if future event still future
        for (Event e : futureEvents) {
            switch (e.getEventStatus()) {
                case PAST:
                    newPast.add(e);
                    newFuture.remove(e);
                    break;
                case CURRENT:
                    Account.shared.withCurrentEvent(Optional.from(e));
                    newFuture.remove(e);
                    break;
                default:
            }
        }

        Collections.sort(newFuture, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o2.getStartTime().compareTo(o1.getStartTime());
            }
        });
        Collections.sort(newPast, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
        Account.shared.withFutureEvents(newFuture);
        Account.shared.withPastEvents(newPast);
    }

    /**
     * Change the UUID of the shared account
     *
     * @param UUID the new UUID
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withUUID(String UUID) {
        shared = new Account(Optional.from(UUID),
                             displayName,
                             givenName,
                             familyName,
                             email,
                             currentEvent,
                             pastEvents,
                             futureEvents,
                             location);
        return shared;
    }

    /**
     * Change the display name of the shared account
     *
     * @param displayName the new display name
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withDisplayName(String displayName) {
        shared = new Account(UUID,
                             Optional.from(displayName),
                             givenName,
                             familyName,
                             email,
                             currentEvent,
                             pastEvents,
                             futureEvents,
                             location);
        return shared;
    }

    /**
     * Change the first name of the shared account
     *
     * @param givenName the new given name
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withGivenName(String givenName) {
        shared = new Account(UUID,
                             displayName,
                             Optional.from(givenName),
                             familyName,
                             email,
                             currentEvent,
                             pastEvents,
                             futureEvents,
                             location);
        return shared;
    }

    /**
     * Change the last name of the shared account
     *
     * @param familyName the new family name
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withFamilyName(String familyName) {
        shared = new Account(UUID,
                             displayName,
                             givenName,
                             Optional.from(familyName),
                             email,
                             currentEvent,
                             pastEvents,
                             futureEvents,
                             location);
        return shared;
    }

    /**
     * Change the email of the shared account
     *
     * @param email the new email
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withEmail(String email) {
        if (emailCheck(email)) {
            shared = new Account(UUID,
                                 displayName,
                                 givenName,
                                 familyName,
                                 Optional.from(email),
                                 currentEvent,
                                 pastEvents,
                                 futureEvents,
                                 location);
            return shared;
        } else {
            throw new IllegalArgumentException(
                    "The email is not properly formed");
        }
    }

    /**
     * Change the current event of the shared account
     *
     * @param current event
     * @return the modified shared account, so that it is easier to call in chain
     */

    public Account withCurrentEvent(Optional<Event> current) {
        if (current.isEmpty()) {
            shared = new Account(UUID,
                                 displayName,
                                 givenName,
                                 familyName,
                                 email,
                                 current,
                                 pastEvents,
                                 futureEvents,
                                 location);
        } else {
            Event e = current.get().withEventName(current.get().getEventName());
            if (current.get().getEventStatus().equals(EventStatus.CURRENT)) {
                shared =
                        new Account(UUID,
                                    displayName,
                                    givenName,
                                    familyName,
                                    email,
                                    Optional.from(e),
                                    pastEvents,
                                    futureEvents,
                                    location);
            } else {
                throw new IllegalArgumentException("Event is " +
                                                   current.get()
                                                           .getEventStatus() +
                                                   " and not " +
                                                   EventStatus.CURRENT.toString());
            }
        }
        return shared;
    }

    /**
     * Change the past event list of the shared account
     *
     * @param past event list
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withPastEvents(List<Event> past) {
        shared = new Account(UUID, displayName, givenName, familyName, email,
                             currentEvent, past, futureEvents, location);
        return shared;
    }

    /**
     * Change the future event list of the shared account
     *
     * @param future event list
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withFutureEvents(List<Event> future) {
        shared = new Account(UUID, displayName, givenName, familyName, email,
                             currentEvent, pastEvents, future, location);
        return shared;
    }

    /**
     * Changes the location of the shared account.
     *
     * @param location - the new location
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account withLocation(Location location) {
        shared = new Account(UUID,
                             displayName,
                             givenName,
                             familyName,
                             email,
                             currentEvent,
                             pastEvents,
                             futureEvents,
                             Optional.from(location));
        return shared;
    }

    /**
     * Add an event to the right event list depending on its EventStatus property
     *
     * @param event the event to add
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account addOrUpdateEvent(Event event) {
        switch (event.getEventStatus()) {
            case FUTURE:
                return addOrUpdateFutureEvent(event);
            case PAST:
                return addOrUpdatePastEvent(event);
            default:
                return withCurrentEvent(Optional.from(event));

        }
    }

    /**
     * Add a past event list of the shared account or updates it if it already exists
     *
     * @param past event to add
     * @return the modified shared account, so that it is easier to call in chain
     * @throws IllegalArgumentException
     */
    Account addOrUpdatePastEvent(Event past) {
        if (past.getEventStatus().equals(EventStatus.PAST)) {
            List<Event> newPast = new ArrayList<>(pastEvents);
            for (Event e : pastEvents) {
                if (e.getUUID().equals(past.getUUID())) {
                    newPast.remove(e);
                }
            }
            newPast.add(past);
            Collections.sort(newPast, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    return o1.getStartTime().compareTo(o2.getStartTime());
                }
            });
            return Account.shared.withPastEvents(newPast);
        }
        throw new IllegalArgumentException("This is not a past event");
    }

    /**
     * Add a future event list of the shared account or updates it if is already exists
     * This guarentees that the event are sorted
     *
     * @param future event to add
     * @return the modified shared account, so that it is easier to call in chain
     * @throws IllegalArgumentException
     */
    Account addOrUpdateFutureEvent(Event future) {
        if (future.getEventStatus().equals(EventStatus.FUTURE)) {

            List<Event> newFuture = new ArrayList<>(futureEvents);
            for (Event e : futureEvents) {
                if (e.getUUID().equals(future.getUUID())) {
                    newFuture.remove(e);
                }
            }
            newFuture.add(future);
            Collections.sort(newFuture, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    return o2.getStartTime().compareTo(o1.getStartTime());
                }
            });
            return Account.shared.withFutureEvents(newFuture);
        }
        throw new IllegalArgumentException("This is not a future event");
    }

    /**
     * Clear the shared account
     *
     * @return a cleared shared account
     */
    public Account clear() {
        shared = new Account(Optional.<String>empty(),
                             Optional.<String>empty(),
                             Optional.<String>empty(),
                             Optional.<String>empty(),
                             Optional.<String>empty(),
                             Optional.<Event>empty(),
                             new ArrayList<Event>(),
                             new ArrayList<Event>(),
                             Optional.<Location>empty());
        return shared;
    }

    @Override
    public String toString() {
        String currEvent;
        if (!currentEvent.isEmpty()) {
            currEvent = currentEvent.get().toString();
        } else {
            currEvent = "No current event";
        }
        return "Account{" +
               "UUID='" + UUID + '\'' +
               "displayName='" + displayName + '\'' +
               "givenName='" + givenName + '\'' +
               ", familyName='" + familyName + '\'' +
               ", email='" + email + '\'' +
               ", currentEvent=" + currEvent +
               ", pastEvents=" + pastEvents +
               ", futureEvents=" + futureEvents +
               '}';
    }

    /**
     * Print a shorter version of the account with just the names the email and the current event
     *
     * @return string containing basic informations about an account
     */
    public String toStringShort() {
        String currEvent;
        if (!currentEvent.isEmpty()) {
            currEvent = currentEvent.get().toString();
        } else {
            currEvent = "No current event";
        }
        return "Account{" +
               "givenName='" + givenName + '\'' +
               ", familyName='" + familyName + '\'' +
               ", email='" + email + '\'' +
               ", currentEvent=" + currEvent +
               '}';
    }

    /**
     * Check that the passed email is an "acceptable" form (not the icann official definition)
     *
     * @param email the email to check
     * @return true of email ok else false
     */
    private boolean emailCheck(String email) {
        Pattern p =
                Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,13}\\b",
                                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Converts an Account to a Member (essentially for comparison purpose)
     *
     * @return a members representing the account
     */
    public Member toMember() {
        return new Member(UUID, displayName, givenName, familyName, email,
                          location
        );
    }
}
