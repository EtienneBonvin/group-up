package ch.epfl.sweng.groupup.lib;

public interface Watchee {

    void notifyAllWatchers();

    void addWatcher(Watcher newWatcher);

    void removeWatcher(Watcher watcher);
}
