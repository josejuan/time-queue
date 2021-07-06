package org.timequeue.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassword {
    private String old;
    private String new1;
    private String new2;
}
