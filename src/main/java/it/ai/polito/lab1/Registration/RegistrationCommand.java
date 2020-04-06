package it.ai.polito.lab1.Registration;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegistrationCommand {
    public static final int MIN_PSW = 6, MAX_PSW = 30;
    public static final int MIN_TEXT = 2, MAX_TEXT = 50;

    @Size(min = MIN_TEXT, max = MAX_TEXT, message = "{command.namesurname.message}")
    private String name;
    @Size(min = MIN_TEXT, max = MAX_TEXT, message = "{command.namesurname.message}")
    private String surname;
    @NotEmpty(message = "{command.email.message}")
    @Email(message = "{command.email.message}")
    private String email;
    @Size(min = MIN_PSW, max = MAX_PSW, message = "{command.psw.message}")
    private String psw;
    @Size(min = MIN_PSW, max = MAX_PSW, message = "{command.psw.message}")
    private String pswv;
    @AssertTrue(message = "{command.privacy.message}")
    private boolean privacy;
}
