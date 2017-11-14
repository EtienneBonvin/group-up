package ch.epfl.sweng.groupup.lib;

/**
 * Class Watcher.
 * Describes the behavior of an object able to be notified of some notifications regarding a
 * Watchee object.
 */
public interface Watcher {

    /**
     * Describes the action to execute when a watchee notifies a modification.
     */
    void notifyWatcher();
}
