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
    public String registrationPage(Model m, @ModelAttribute("command") RegistrationCommand rc){
        log.info("Register: " + rc);
        return "register";
    }

    @PostMapping("/login")
    public String postLogin(){
        return "";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("command") @Valid RegistrationCommand rc, BindingResult br){
        log.info("POST: registrazione");
        log.info("Register: " + rc);

        String psw1, psw2;
        psw1 = rc.getPsw();
        psw2 = rc.getPswv();

        if(!psw1.equals(psw2))
            br.addError(new FieldError("command","pswv", "Le due password non corrispondono"));
        if(!rc.isPrivacy())
            br.addError(new FieldError("command", "privacy", "Non è stata accettata la norma sulla privacy"));
        if(br.hasErrors())
            return "/register";

        RegistrationDetails rd = RegistrationDetails.builder().name(rc.getName())
                .surname(rc.getSurname()).email(rc.getEmail()).psw(rc.getPsw())
                .privacy(rc.isPrivacy()).registrationDate(new Date()).build();

        if(rm.putIfAbsent(rc.getEmail(), rd) != null) {
            br.addError(new FieldError("command", "email", "Email già registrata"));
            return "/register";
        }

        return "redirect:/";
    }
}
