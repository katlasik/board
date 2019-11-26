package katlasik.board.services;

import katlasik.board.dtos.NewAnswer;
import katlasik.board.dtos.NewQuestion;
import katlasik.board.dtos.QuestionView;
import katlasik.board.model.Answer;
import katlasik.board.model.Question;
import katlasik.board.repositories.AnswerRepository;
import katlasik.board.repositories.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SecurityService securityService;

    public QuestionService(
            QuestionRepository questionRepository,
            AnswerRepository answerRepository, SecurityService securityService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.securityService = securityService;
    }

    public Optional<Question> findWithAnswers(Long questionId) {
        return questionRepository.findWithAnswers(questionId);
    }

    public List<QuestionView> findQuestionViews() {
        return questionRepository.findQuestionViews();
    }

    public List<QuestionView> findQuestionViewsByUserId(Long userId) {
        return questionRepository.findQuestionViewsById(userId);
    }

    public Question createQuestion(NewQuestion newQuestion) {
        var question = new Question();
        question.setTitle(newQuestion.getTitle());
        question.setContent(newQuestion.getContent());
        question.setUser(securityService.getLoggedInUser());
        return questionRepository.save(question);
    }

    public Answer createAnswer(NewAnswer newAnswer) {
        var answer = new Answer();
        answer.setQuestion(questionRepository.getOne(newAnswer.getQuestionId()));
        answer.setUser(securityService.getLoggedInUser());
        answer.setContent(newAnswer.getContent());
        return answerRepository.save(answer);
    }
}

