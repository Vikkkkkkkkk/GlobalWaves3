package app.page;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PageSnapshot {
    private Page page;

    public PageSnapshot(Page page) {
        this.page = page;
    }
}
