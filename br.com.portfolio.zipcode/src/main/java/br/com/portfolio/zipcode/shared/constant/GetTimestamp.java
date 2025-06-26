package br.com.portfolio.zipcode.shared.constant;

import java.sql.Timestamp;
import java.time.Instant;

public class GetTimestamp {

    public static Timestamp getTimestamp() {

        Instant instant = Instant.now();
        Timestamp timestamp = Timestamp.from(instant);
        return timestamp;

    }

}
