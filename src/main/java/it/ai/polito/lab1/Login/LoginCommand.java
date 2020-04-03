package it.ai.polito.lab1.Login;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class LoginCommand {
    @NotEmpty(message = "{command.email.message}")
    @Email(message = "{command.email.message}")
    private String email;
    @NotEmpty(message = "{loginCommand.psw.notEmpty.message}")
    private String psw;
}
