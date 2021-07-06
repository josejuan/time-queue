package org.timequeue.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;

@Getter
@Setter
@AllArgsConstructor
public class Msg {
    private Level level;
    private String text;
}
