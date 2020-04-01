package it.ai.polito.lab1.Registration;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RegistrationManager extends ConcurrentHashMap<String, RegistrationDetails> {}
