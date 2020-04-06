package it.ai.polito.lab1.Controllers;

import it.ai.polito.lab1.Login.LoginCommand;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
@Log(topic = "HomeController")
public class HomeController {
    @Autowired
    private RegistrationManager rm;

    @GetMapping("/")
    public String home(Model model, @ModelAttribute("command") LoginCommand lc, HttpSession session){
        log.info("HOME: " + lc);
        //If session exists, redirect to private page avoiding another login
        String email = (String)session.getAttribute("username");
        if(email != null)
            return "redirect:/private";
        //Else show login form
        model.addAttribute("success1", model.getAttribute("success1")); //Just to make intelliJ recognize redirect attribute.
        model.addAttribute("success2", model.getAttribute("success2")); //Just to make intelliJ recognize redirect attribute.
        return "login";
    }

   @GetMapping("/private")
   public String privatePage(Model model, @ModelAttribute("command") LoginCommand loginCommand, HttpSession session){
       //If session doesn't exists, redirect to home page avoiding to get an unavailable page
       String email = (String)session.getAttribute("username");
       if(email == null || rm.get(email) == null)
            return "redirect:/";
        //Else store user information to be shown and return private page
        model.addAttribute("command", rm.get(email));
        return "private";
    }

    @GetMapping("/login")
    public String login(){
        //Just to avoid error of missing page if the user use the get method for this url after a login try
        return "redirect:/";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute("command") @Valid LoginCommand loginCommand, BindingResult bindingResult, HttpSession session){
        log.info("LOGIN: " + loginCommand);
        //If there errors cause by automatic validation, show them
        if(bindingResult.hasErrors())
            return "login";

        //Else if the account doesn't exist or the password doesn't match, show incorrect combination error
        ResourceBundle rb = ResourceBundle.getBundle("ValidationMessages");
        RegistrationDetails rd = rm.get(loginCommand.getEmail());
        if(rd == null || !rd.getPsw().equals(loginCommand.getPsw())) {
            bindingResult.addError(new FieldError("command", "email", rb.getString("loginCommand.email.pswDontMatch")));
            bindingResult.addError(new FieldError("command", "psw", "")); //Just to enable to red border on psw field
            return "login";
        }

        //Else add user info to the current session and show his private page
        session.setAttribute("username", loginCommand.getEmail());
        return "redirect:/private";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session){
        //Remove user info from the current session
        log.info("LOGOUT: "+session);
        session.removeAttribute("username");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registrationPage(@ModelAttribute("command") RegistrationCommand registrationCommand){
        log.info("Register: " + registrationCommand);
        //Simply shows register form page
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("command") @Valid RegistrationCommand registrationCommand, BindingResult br, RedirectAttributes redirectAttributes){
        log.info("POST: registrazione ");
        log.info(registrationCommand.toString());

        ResourceBundle rb = ResourceBundle.getBundle("ValidationMessages");
        String psw1, psw2;
        psw1 = registrationCommand.getPsw();
        psw2 = registrationCommand.getPswv();

        //Check if passwords matches
        if(!psw1.equals(psw2))
            br.addError(new FieldError("command","pswv", rb.getString("command.register.pswDontMatch")));

        //Show the errors in case something went wrong
        if(br.hasErrors())
            return "/register";

        //Else if everything is fine, try to register the user if the email isn't already present in the map
        RegistrationDetails rd = RegistrationDetails.builder().name(registrationCommand.getName())
                .surname(registrationCommand.getSurname()).email(registrationCommand.getEmail()).psw(registrationCommand.getPsw())
                .privacy(registrationCommand.isPrivacy()).registrationDate(new Date()).build();

        if(rm.putIfAbsent(registrationCommand.getEmail(), rd) != null) {
            br.addError(new FieldError("command", "email", rb.getString("command.register.duplicatedEmail")));
            return "/register";
        }

        redirectAttributes.addFlashAttribute("success1", rb.getString("command.login.success1"));
        redirectAttributes.addFlashAttribute("success2", rb.getString("command.login.success2"));
        return "redirect:/";
    }
}
