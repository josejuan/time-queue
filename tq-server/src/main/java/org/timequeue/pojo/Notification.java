package org.timequeue.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private String key;
    private String text;

    public static Notification from(int minutes) {

        if (minutes < 0)
            throw new RuntimeException("minutes can not be negative");

        String key = "";
        String text = "";

        if (minutes == 0) {
            key = "0m";
            text = "0 minutes";
        } else {
            int weeks = minutes / (7 * 24 * 60);
            int weeks_r = minutes % (7 * 24 * 60);
            int days = weeks_r / (24 * 60);
            int days_r = weeks_r % (24 * 60);
            int hours = days_r / 60;
            int hours_r = days_r % 60;
            if (weeks > 0) {
                key += weeks + "w";
                text += weeks + " weeks ";
            }
            if (days > 0) {
                key += days + "d";
                text += days + " days ";
            }
            if (hours > 0) {
                key += hours + "h";
                text += hours + " hours ";
            }
            if (hours_r > 0) {
                key += hours_r + "m";
                text += hours_r + " minutes ";
            }
        }
        return new Notification(key, text + "before");
    }

    public int getMinutes() {
        int m = 0;
        int k = 0;
        for (char c : key.toCharArray())
            if (Character.isDigit(c))
                k = 10 * k + c - 48;
            else {
                switch (c) {
                    case 'm':
                        m += k;
                        break;
                    case 'h':
                        m += 60 * k;
                        break;
                    case 'd':
                        m += 24 * 60 * k;
                        break;
                    case 'w':
                        m += 7 * 24 * 60 * k;
                        break;
                    default:
                        // wrong measurement unit
                        break;
                }
                k = 0;
            }
        return m;
    }
}
