package katlasik.board.controllers;

import katlasik.board.dtos.RegistrationCheck;
import katlasik.board.exceptions.IllegalFieldException;
import katlasik.board.services.UserService;
import katlasik.board.dtos.UserRegistration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/registration/check", produces = "application/json")
    public @ResponseBody RegistrationCheck checkField(@RequestParam("field") String field, @RequestParam("value") String value) {
        switch (field) {
            case "email": return new RegistrationCheck(field, userService.checkIfMailIsTaken(value));
            case "name": return new RegistrationCheck(field, userService.checkIfNameIsTaken(value));
            default: throw new IllegalFieldException();
        }
    }

    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute("user") @Valid UserRegistration user, BindingResult result) {

        if (!user.getPassword().equals(user.getPasswordRepeat())) {
            result.rejectValue("password", "registration.unmatchedPasswords");
        } else if (userService.checkIfMailIsTaken(user.getEmail())) {
            result.rejectValue("email", "registration.emailExists");
        } else if (userService.checkIfNameIsTaken(user.getName())) {
            result.rejectValue("name", "registration.nameExists");
        } else if (!result.hasErrors()) {
            userService.createUser(user);
            return "redirect:/thank-you";
        }
        return "registration";

    }

    @GetMapping("/registration")
    public String getRegistration(Model model) {

        var user = new UserRegistration();
        model.addAttribute("user", user);

        return "registration";
    }

    @GetMapping("/thank-you")
    public String getThankYou() {
        return "thank-you";
    }
}
