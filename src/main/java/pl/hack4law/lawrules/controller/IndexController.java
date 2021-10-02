package pl.hack4law.lawrules.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.hack4law.lawrules.service.AccountService;

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
}
