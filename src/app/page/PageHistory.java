package app.page;

import java.util.Stack;

public class PageHistory {
    private Stack<PageSnapshot> history = new Stack<>();
    private Stack<PageSnapshot> future = new Stack<>();

    public void addSnapshot(PageSnapshot snapshot) {
        history.push(snapshot);
        future.clear();
    }

    public PageSnapshot backward() {
        if (history.isEmpty()) {
            return null;
        }
        PageSnapshot snapshot = history.pop();
        future.push(snapshot);
        return snapshot;
    }

    public PageSnapshot forward() {
        if (future.isEmpty()) {
            return null;
        }
        PageSnapshot snapshot = future.pop();
        history.push(snapshot);
        return snapshot;
    }
}
