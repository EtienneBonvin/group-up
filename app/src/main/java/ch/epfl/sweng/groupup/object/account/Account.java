package ch.epfl.sweng.groupup.object.account;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.groupup.lib.Optional;
import ch.epfl.sweng.groupup.lib.Watchee;
import ch.epfl.sweng.groupup.lib.Watcher;
import ch.epfl.sweng.groupup.object.event.Event;
import ch.epfl.sweng.groupup.object.event.EventStatus;

public final class Account extends User implements Watchee {

    public static Account shared = new Account(Optional.<String>empty(),
            Optional.<String>empty(),
            Optional.<String>empty(),
            Optional.<String>empty(),
            Optional.<String>empty(),
            new ArrayList<Event>(),
            new ArrayList<Event>(),
            new ArrayList<Event>(),
            Optional.<Location>empty());

    private static List<Watcher> watchers = new ArrayList<>();

    private final List<Event> currentEvents;
    private final List<Event> pastEvents;
    private final List<Event> futureEvents;

    private Account(Optional<String> UUID,
                    Optional<String> displayName,
                    Optional<String> givenName,
                    Optional<String> familyName,
                    Optional<String> email,
                    List<Event> current,
                    List<Event> past,
                    List<Event> future,
                    Optional<Location> location) {
        super(displayName, givenName, familyName, email, UUID, location);
        this.currentEvents = new ArrayList<>(Collections.unmodifiableList(current));
        this.pastEvents = new ArrayList<>(Collections.unmodifiableList(past));
        this.futureEvents = new ArrayList<>(Collections.unmodifiableList(future));
    }

    /**
     * Getter for the currentEvents event of the account
     *
     * @return Event currentEvents
     */
    public List<Event> getCurrentEvents() {
        updateEventList();
        return currentEvents;
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
     * Getter for all events, return first the future then currentEvents then past
     */
    public List<Event> getEvents() {
        updateEventList();
        List<Event> allEvents = new ArrayList<>(futureEvents);
        allEvents.addAll(currentEvents);
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
        List<Event> newCurrent = new ArrayList<>(currentEvents);
        // check if currentEvents events are still currentEvents, else add to newPast and remove them from currentEvents
        for (Event e : currentEvents) {
            if (e.getEventStatus().equals(EventStatus.PAST)) {
                newCurrent.remove(e);
                newPast.add(e);
            }
        }
        // check if future event still future
        for (Event e : futureEvents) {
            switch (e.getEventStatus()) {
                case PAST:
                    newPast.add(e);
                    newFuture.remove(e);
                    break;
                case CURRENT:
                    newCurrent.add(e);
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
        Collections.sort(newCurrent, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o2.getStartTime().compareTo(o1.getStartTime());
            }
        });
        Collections.sort(newPast, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o2.getStartTime().compareTo(o1.getStartTime());
            }
        });
        Account.shared.withFutureEvents(newFuture).withCurrentEvent(newCurrent).withPastEvents(newPast);
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
                currentEvents,
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
                currentEvents,
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
                currentEvents,
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
                currentEvents,
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
                    currentEvents,
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
     * Change the currentEvents event list of the shared account
     *
     * @param current event list
     * @return the modified shared account, so that it is easier to call in chain
     */

    public Account withCurrentEvent(List<Event> current) {
        shared = new Account(UUID,
                displayName,
                givenName,
                familyName,
                email,
                current,
                pastEvents,
                futureEvents,
                location);
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
                currentEvents, past, futureEvents, location);
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
                currentEvents, pastEvents, future, location);
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
                currentEvents,
                pastEvents,
                futureEvents,
                Optional.from(location));
        if (User.observer != null) {
            User.observer.requestLocation();
        }
        return shared;
    }

    /**
     * Add an event to the right event list depending on its EventStatus property
     *
     * @param event the event to add
     * @return the modified shared account, so that it is easier to call in chain
     */
    public Account addOrUpdateEvent(Event event) {
        if (observer != null) {
            observer.updateEventIfNeeded(event);
        }

        switch (event.getEventStatus()) {
            case FUTURE:
                addOrUpdateFutureEvent(event);
                break;
            case PAST:
                addOrUpdatePastEvent(event);
                break;
            default:
                addOrUpdateCurrentEvent(event);
        }
        return shared;
    }

    /**
     * Add a past event list of the shared account or updates it if it already exists
     *
     * @param past event to add
     * @return the modified shared account, so that it is easier to call in chain
     * @throws IllegalArgumentException
     */
    private Account addOrUpdatePastEvent(Event past) {
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


    /**
     * Add a future event list of the shared account or updates it if is already exists
     * This guarantees that the event are sorted
     *
     * @param future event to add
     * @return the modified shared account, so that it is easier to call in chain
     * @throws IllegalArgumentException
     */
    private Account addOrUpdateFutureEvent(Event future) {
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

    /**
     * Add a currnet event list of the shared account or updates it if is already exists
     * This guarantees that the event are sorted
     *
     * @param current event to add
     * @return the modified shared account, so that it is easier to call in chain
     * @throws IllegalArgumentException
     */
    private Account addOrUpdateCurrentEvent(Event current) {
        List<Event> newCurrent = new ArrayList<>(currentEvents);
        for (Event e : futureEvents) {
            if (e.getUUID().equals(current.getUUID())) {
                newCurrent.remove(e);
            }
        }
        newCurrent.add(current);
        Collections.sort(newCurrent, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o2.getStartTime().compareTo(o1.getStartTime());
            }
        });
        return Account.shared.withFutureEvents(newCurrent);
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
                new ArrayList<Event>(),
                new ArrayList<Event>(),
                new ArrayList<Event>(),
                Optional.<Location>empty());
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
                ", currentEvent=" + currentEvents +
                ", pastEvents=" + pastEvents +
                ", futureEvents=" + futureEvents +
                '}';
    }

    /**
     * Print a shorter version of the account with just the names the email and the currentEvents event
     *
     * @return string containing basic informations about an account
     */
    public String toStringShort() {
        return "Account{" +
                "givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", email='" + email + '\'' +
                ", currentEvent=" + currentEvents +
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
                location);
    }

    @Override
    public void notifyAllWatchers() {
        for (Watcher w : Account.watchers) {
            w.notifyWatcher();
        }
    }

    @Override
    public void addWatcher(Watcher newWatcher) {
        Account.watchers.add(newWatcher);
    }

    @Override
    public void removeWatcher(Watcher watcher) {
        Account.watchers.remove(watcher);
    }
}
