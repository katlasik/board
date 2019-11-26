package katlasik.board.controllers;

import katlasik.board.dtos.NewAnswer;
import katlasik.board.dtos.NewQuestion;
import katlasik.board.services.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class QuestionController {

    private final QuestionService questionService;


    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/new-question")
    public String getNewQuestion(Model model) {

        var question = new NewQuestion();
        model.addAttribute("question", question);

        return "new-question";
    }

    @GetMapping("/question/{questionId}")
    public String getQuestion(Model model, @PathVariable long questionId) {

        var maybeQuestion = questionService.findWithAnswers(questionId);

        return maybeQuestion.map(
            question -> {
                model.addAttribute("question", question);
                model.addAttribute("answer", new NewAnswer(questionId));
                return "question";
            }
        ).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Couldn't find question with id=" + questionId + "."));
    }

    @PostMapping("/new-question")
    public String postQuestion(@ModelAttribute("question") @Valid NewQuestion question, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new-question";
        } else {
            var persisted = questionService.createQuestion(question);
            return "redirect:/question/" + persisted.getId();
        }
    }

    @PostMapping("/new-answer")
    public String postAnswer(@ModelAttribute("answer") @Valid NewAnswer answer, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            questionService.createAnswer(answer);
        }
        return "redirect:/question/" + answer.getQuestionId();
    }

}
