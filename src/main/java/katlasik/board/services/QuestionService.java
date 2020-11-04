package katlasik.board.services;

import katlasik.board.dtos.NewAnswer;
import katlasik.board.dtos.NewQuestion;
import katlasik.board.dtos.QuestionView;
import katlasik.board.model.Answer;
import katlasik.board.model.Image;
import katlasik.board.model.Question;
import katlasik.board.repositories.AnswerRepository;
import katlasik.board.repositories.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Page<QuestionView> findQuestionViews(Pageable page) {
        return questionRepository.findQuestionViews(page);
    }

    public Page<QuestionView> findQuestionViewsByUserId(Long userId, Pageable page) {
        return questionRepository.findQuestionViewsById(userId, page);
    }

    private List<Image> processImages(MultipartFile[] files) {
        return Arrays.stream(files)
                .flatMap(file -> {
                    try {
                        var bytes = file.getBytes();

                        if (contentService.isImage(bytes)) {
                            return Stream.of(new Image(
                                    file.getOriginalFilename(),
                                    bytes,
                                    file.getContentType(),
                                    file.getSize()
                            ));
                        } else if (bytes.length > 0) {
                            throw new AccessDeniedException("Illegal mime type for image.");
                        } else {
                            return Stream.empty();
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

