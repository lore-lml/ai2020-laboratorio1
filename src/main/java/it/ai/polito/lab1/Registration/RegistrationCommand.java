package it.ai.polito.lab1.Registration;

import lombok.Data;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegistrationCommand {
    @Size(min = 2, max = 50, message = "{command.namesurname.message}")
    private String name;
    @Size(min = 2, max = 50, message = "{command.namesurname.message}")
    private String surname;
    @NotEmpty(message = "{command.email.message}")
    @Email(message = "{command.email.message}")
    private String email;
    @Size(min = 8, max = 16, message = "{command.psw.message}")
    private String psw;
    @Size(min = 8, max = 16, message = "{command.psw.message}")
    private String pswv;
    private boolean privacy;
}
