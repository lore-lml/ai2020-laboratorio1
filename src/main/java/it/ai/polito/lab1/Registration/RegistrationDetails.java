package it.ai.polito.lab1.Registration;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

@Value
@Builder
public class RegistrationDetails {
    String name, surname, email, psw;
    boolean privacy;
    Date registrationDate;

    public String dateToString(){
        LocalDateTime ldt = registrationDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return String.format(Locale.getDefault(), "%02d / %02d / %d", ldt.getDayOfMonth(), ldt.getMonth().getValue(), ldt.getYear());
    }
}
