package pl.hack4law.lawrules.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.hack4law.lawrules.model.Account;
import pl.hack4law.lawrules.service.AccountService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(path = "/")
@AllArgsConstructor
public class IndexController {
    private final AccountService accountService;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }


    @GetMapping("/register")
    public String registrationForm(Model model, Account account) {
        model.addAttribute("newAccount", account);

        return "registration-form";
    }

    @PostMapping("/register")
    public String register(@Valid Account account,
                           BindingResult result,
                           @RequestParam(name = "password-confirm") String passwordConfirm,
                           Model model) {

        if(result.hasErrors()){
            return registrationError(model, account, result.getFieldError().getDefaultMessage());
        }

        if (!account.getPassword().equals(passwordConfirm)) {
            return registrationError(model, account, "Passwords do not match.");
        }

        if(!accountService.register(account)){
            return registrationError(model, account, "User with given username already exists.");
        }

        return "redirect:/login";
    }

    private String registrationError(Model model, Account account, String message) {
        model.addAttribute("newAccount", account);
        model.addAttribute("errorMessage", message);

        return "registration-form";
    }
}
