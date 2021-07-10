package org.timequeue.data.model;

public enum UpdateMode {
    // When System.now > Event.nextEvent:

    // Do nothing.
    NEVER,

    // Event.nextEvent.Year += 1
    ANNUALLY
}
