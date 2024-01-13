package app.page;

import java.util.Stack;

public class PageHistory {
    private Stack<PageSnapshot> history = new Stack<>();
    private Stack<PageSnapshot> future = new Stack<>();

    /**
     * Add a snapshot to the history and reset the future.
     *
     * @param snapshot the snapshot to be added
     */
    public void addSnapshot(final PageSnapshot snapshot) {
        history.push(snapshot);
        future.clear();
    }

    /**
     * Add a snapshot to the future.
     *
     * @param snapshot the snapshot to be added
     */
    public void addFutureSnapshot(final PageSnapshot snapshot) {
        future.push(snapshot);
    }

    /**
     * Add a snapshot to the history.
     *
     * @param snapshot the snapshot to be added
     */
    public void addHistorySnapshot(final PageSnapshot snapshot) {
        history.push(snapshot);
    }

    /**
     * Get the last snapshot from the history.
     *
     * @return the last snapshot from the history
     */
    public PageSnapshot backward() {
        if (history.isEmpty()) {
            return null;
        }
        PageSnapshot snapshot = history.pop();
        return snapshot;
    }

    /**
     * Get the last snapshot from the future.
     *
     * @return the last snapshot from the future
     */
    public PageSnapshot forward() {
        if (future.isEmpty()) {
            return null;
        }
        PageSnapshot snapshot = future.pop();
        return snapshot;
    }
}
