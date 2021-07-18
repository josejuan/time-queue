package org.timequeue.data.model;

public enum UpdateMode {
    // When System.now > Event.nextEvent:

    // Do nothing.
    NEVER,

    // Event.nextEvent.Day += 1
    DAILY,

    // Event.nextEvent.Day += 7
    WEEK,

    // Event.nextEvent.Month += 1   (truncated to the max month day if required)
    MONTH,

    // Event.nextEvent.Year += 1   (truncated to the max month day if required)
    ANNUALLY
}
