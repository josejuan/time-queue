package org.timequeue.pojo;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @org.junit.jupiter.api.Test
    void getMinutes() {
        IntStream.range(0, 100).map(i -> ThreadLocalRandom.current().nextInt(1000000)).forEach(i -> assertEquals(i, Notification.from(i).getMinutes()));
    }
}