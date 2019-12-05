package katlasik.board.services;

import katlasik.board.dtos.NewAnswer;
import katlasik.board.dtos.NewQuestion;
import katlasik.board.dtos.QuestionView;
import katlasik.board.model.Answer;
import katlasik.board.model.Image;
import katlasik.board.model.Question;
import katlasik.board.repositories.AnswerRepository;
import katlasik.board.repositories.QuestionRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SecurityService securityService;
    private final ContentService contentService;


    public QuestionService(
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            SecurityService securityService,
            ContentService contentService
    ) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.securityService = securityService;
        this.contentService = contentService;
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

    private List<Image> processImages(MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> {
                    try {
                        var bytes = file.getBytes();
                        if (contentService.isImage(bytes)) {
                            return new Image(
                                    file.getOriginalFilename(),
                                    bytes,
                                    file.getContentType(),
                                    file.getSize()
                            );
                        } else {
                            throw new AccessDeniedException("Illegal mime type for image.");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public Question createQuestion(NewQuestion newQuestion) {
        var question = new Question();
        question.setTitle(newQuestion.getTitle());
        question.setContent(newQuestion.getContent());
        question.setUser(securityService.getLoggedInUser());
        processImages(newQuestion.getFiles()).forEach(question::addImage);
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

