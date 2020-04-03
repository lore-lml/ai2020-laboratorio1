package it.ai.polito.lab1.Controllers;

import it.ai.polito.lab1.Registration.RegistrationCommand;
import it.ai.polito.lab1.Registration.RegistrationDetails;
import it.ai.polito.lab1.Registration.RegistrationManager;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

@Controller
@Log(topic = "HomeController")
public class HomeController {
    @Autowired
    private RegistrationManager rm;

    @GetMapping("/")
    public String home(){
        return "login";
    }

    @GetMapping("/register")
    public String registrationPage(@ModelAttribute("command") RegistrationCommand registrationCommand){
        log.info("Register: " + registrationCommand);
        return "register";
    }

    @PostMapping("/login")
    public String postLogin(){
        return "";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("command") @Valid RegistrationCommand registrationCommand, BindingResult br, RedirectAttributes redirectAttributes){
        log.info("POST: registrazione");
        log.info("Register: " + registrationCommand);

        String psw1, psw2;
        psw1 = registrationCommand.getPsw();
        psw2 = registrationCommand.getPswv();

        if(!psw1.equals(psw2))
            br.addError(new FieldError("command","pswv", "Le due password non corrispondono"));
        if(!registrationCommand.isPrivacy())
            br.addError(new FieldError("command", "privacy", "Non è stata accettata la norma sulla privacy"));
        if(br.hasErrors()) {
            return "/register";
        }

        RegistrationDetails rd = RegistrationDetails.builder().name(registrationCommand.getName())
                .surname(registrationCommand.getSurname()).email(registrationCommand.getEmail()).psw(registrationCommand.getPsw())
                .privacy(registrationCommand.isPrivacy()).registrationDate(new Date()).build();

        if(rm.putIfAbsent(registrationCommand.getEmail(), rd) != null) {
            br.addError(new FieldError("command", "email", "Email già registrata"));
            return "/register";
        }

        return "redirect:/";
    }
}
