package app.page;

import lombok.Getter;

@Getter
public class PageSnapshot {
    private Page page;

    public PageSnapshot(final Page page) {
        this.page = page;
    }
}
