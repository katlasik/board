package katlasik.board.controllers;

import katlasik.board.services.SecurityService;
import katlasik.board.services.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final QuestionService questionService;
    private final SecurityService securityService;

    public HomeController(QuestionService questionService, SecurityService securityService) {
        this.questionService = questionService;
        this.securityService = securityService;
    }

    @GetMapping("/")
    public String getWelcome(Model model) {
        model.addAttribute("questions", questionService.findQuestionViews());
        return "welcome";
    }

    @GetMapping("/my-questions")
    public String getMyQuestions(Model model) {
        var userId = securityService.getLoggedInUser().getId();
        model.addAttribute("questions", questionService.findQuestionViewsByUserId(userId));
        return "welcome";
    }

    @GetMapping("/login")
    public String getLogin(Model model, @RequestParam(defaultValue = "false") boolean error) {
        model.addAttribute("error", error);
        return "login";
    }

}
