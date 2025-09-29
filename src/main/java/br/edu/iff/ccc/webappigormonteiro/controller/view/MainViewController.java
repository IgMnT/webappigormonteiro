package br.edu.iff.ccc.webappigormonteiro.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainViewController {

    @GetMapping({"/principal", "/home"})
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/")
    public String redirectRaiz() {
        return "redirect:/principal";
    }
}