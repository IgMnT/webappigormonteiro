package br.edu.iff.ccc.webappigormonteiro.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/principal")
public class MainViewController {
    @GetMapping()
    public String getHomePage() {
        // Render template src/main/resources/templates/home.html => view name "home"
        return "home";
    }

}