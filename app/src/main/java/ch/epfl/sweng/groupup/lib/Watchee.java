package ch.epfl.sweng.groupup.lib;

/**
 * Class Watchee
 * Represents an object which state can be watched by a Watcher object.
 */
public interface Watchee {

    /**
     * Registers a watcher who will be notified when a change happens.
     *
     * @param newWatcher the watcher to be added.
     */
    void addWatcher(Watcher newWatcher);

    /**
     * Notify all the watchers that a modification happened
     */
    void notifyAllWatchers();

    /**
     * Removes a watcher, this watcher should not be notified anymore of further changes.
     *
     * @param watcher the watcher to unregister.
     */
    void removeWatcher(Watcher watcher);
}
