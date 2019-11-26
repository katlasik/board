package katlasik.board.controllers;

import katlasik.board.model.Answer;
import katlasik.board.model.Question;
import katlasik.board.model.Role;
import katlasik.board.model.User;
import katlasik.board.repositories.AnswerRepository;
import katlasik.board.repositories.QuestionRepository;
import katlasik.board.services.SecurityService;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SecurityService securityService;

    @MockBean
    QuestionRepository questionRepository;

    @MockBean
    AnswerRepository answerRepository;

    @Test
    @WithMockUser(username = "user@gmail.com")
    void postQuestion() throws Exception {

        var title = "title title";
        var content = "content content content conent";

        var user = mock(User.class);
        when(user.getEmail()).thenReturn("user@gmail.com");

        var question = new Question();
        question.setUser(user);
        question.setContent(content);
        question.setTitle(title);

        var persisted = mock(Question.class);
        when(persisted.getId()).thenReturn(1L);

        when(securityService.getLoggedInUser()).thenReturn(user);
        when(questionRepository.save(question)).thenReturn(persisted);

        mockMvc.perform(post("/new-question")
                .param("title", title)
                .param("content", content)
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound());

        verify(securityService, times(1)).getLoggedInUser();
        verify(questionRepository, times(1)).save(question);

    }

    @Test
    @WithMockUser
    void getNewQuestion() throws Exception {
        mockMvc.perform(get("/new-question")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Tytuł")))
                .andExpect(content().string(containsString("Treść")))
                .andExpect(content().string(containsString("Zapytaj")));
    }

    private void setupQuestion() {
        var askingUser = mock(User.class);
        when(askingUser.getName()).thenReturn("User1");

        var answeringUser1 = mock(User.class);
        when(answeringUser1.getName()).thenReturn("User2");

        var answeringUser2 = mock(User.class);
        when(answeringUser2.getName()).thenReturn("User3");

        var question = new Question();

        question.setTitle("title title title");

        question.setContent("content content content content content content");

        question.setUser(askingUser);

        var answer1 = new Answer();
        answer1.setUser(answeringUser1);
        answer1.setContent("answer1");

        question.addAnswers(answer1);

        var answer2 = new Answer();
        answer2.setUser(answeringUser2);
        answer2.setContent("answer2");

        question.addAnswers(answer2);

        when(questionRepository.findWithAnswers(1L)).thenReturn(Optional.of(question));
    }

    @Test
    @WithMockUser
    void getQuestionAuthenticated() throws Exception {

        setupQuestion();

        mockMvc.perform(get("/question/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("title title title")))
                .andExpect(content().string(containsString("content content content content content content")))
                .andExpect(content().string(containsString("User1")))
                .andExpect(content().string(containsString("User2")))
                .andExpect(content().string(containsString("User3")))
                .andExpect(content().string(containsString("answer1")))
                .andExpect(content().string(containsString("answer2")))
                .andExpect(content().string(containsString("Odpowiedz")));
    }

    @Test
    void getQuestionAnonymous() throws Exception {

        setupQuestion();

        mockMvc.perform(get("/question/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("title title title")))
                .andExpect(content().string(containsString("content content content content content content")))
                .andExpect(content().string(containsString("User1")))
                .andExpect(content().string(containsString("User2")))
                .andExpect(content().string(containsString("User3")))
                .andExpect(content().string(containsString("answer1")))
                .andExpect(content().string(containsString("answer2")))
                .andExpect(content().string(not(containsString("Odpowiedz"))));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void postAnswer() throws Exception {
        var content = "content content content content";

        var user = mock(User.class);
        when(user.getEmail()).thenReturn("user@gmail.com");

        var answer = new Answer();
        answer.setUser(user);
        answer.setContent(content);

        var persisted = mock(Answer.class);
        when(persisted.getId()).thenReturn(1L);

        var question = mock(Question.class);

        when(securityService.getLoggedInUser()).thenReturn(user);
        when(questionRepository.getOne(1L)).thenReturn(question);
        when(answerRepository.save(answer)).thenReturn(persisted);

        mockMvc.perform(post("/new-answer")
                .param("content", content)
                .param("questionId", "1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound());

        verify(securityService, times(1)).getLoggedInUser();
        verify(questionRepository, times(1)).getOne(1L);
        verify(answerRepository, times(1)).save(answer);
    }
}
