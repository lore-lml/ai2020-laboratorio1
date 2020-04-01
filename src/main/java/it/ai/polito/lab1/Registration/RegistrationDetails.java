package it.ai.polito.lab1.Registration;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class RegistrationDetails {
    String name, surname, email, psw;
    boolean privacy;
    Date registrationDate;
}
