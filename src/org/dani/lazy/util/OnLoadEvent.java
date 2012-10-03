package org.dani.lazy.util;

import java.util.EventObject;

@SuppressWarnings("serial")
public class OnLoadEvent extends EventObject {
    private final IndexInterval interval;

    public OnLoadEvent(Object source, IndexInterval interval) {
        super(source);
        this.interval = interval;
    }

    public IndexInterval getIndexInterval() {
        return interval;
    }
}
