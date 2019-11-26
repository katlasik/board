package katlasik.board.controllers;

import katlasik.board.dtos.QuestionView;
import katlasik.board.model.Role;
import katlasik.board.model.User;
import katlasik.board.repositories.QuestionRepository;
import katlasik.board.repositories.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
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
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    QuestionRepository questionRepository;

    @MockBean
    UsersRepository usersRepository;


    @Test
    @DisplayName("Anonymous user should see questions and be able to use buttons for login and registration")
    void getWelcomeAnonymous() throws Exception {

        when(questionRepository.findQuestionViews()).thenReturn(
                List.of(
                        new QuestionView(1L, "pyt1", 1),
                        new QuestionView(2L, "pyt2", 2)
                )
        );

        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Zarejestruj się")))
                .andExpect(content().string(containsString("Zarejestruj się")))
                .andExpect(content().string(containsString("pyt1")))
                .andExpect(content().string(containsString("pyt2")))
                .andExpect(content().string(not(containsString("Wyloguj się"))));

        verify(questionRepository, times(1)).findQuestionViews();

    }

    @WithMockUser
    @Test
    @DisplayName("Authenticated user should see questions and see navigation bar")
    void getWelcomeAuthenticated() throws Exception {

        when(questionRepository.findQuestionViews()).thenReturn(
                List.of(
                        new QuestionView(1L, "pyt1", 1),
                        new QuestionView(2L, "pyt2", 2)
                )
        );

        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Zarejestruj się"))))
                .andExpect(content().string(not(containsString("Zarejestruj się"))))
                .andExpect(content().string(containsString("pyt1")))
                .andExpect(content().string(containsString("pyt2")))
                .andExpect(content().string(containsString("Wyloguj się")));

        verify(questionRepository, times(1)).findQuestionViews();

    }

    @DisplayName("Authenticated users should see all their questions")
    @WithMockUser("myemail@gmail.com")
    @Test
    void getMyQuestions() throws Exception {

        var user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        when(questionRepository.findQuestionViewsById(1L)).thenReturn(
                List.of(
                        new QuestionView(1L, "pyt1", 1),
                        new QuestionView(2L, "pyt2", 2)
                )
        );

        when(usersRepository.findByEmail("myemail@gmail.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/my-questions")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Zarejestruj się"))))
                .andExpect(content().string(not(containsString("Zarejestruj się"))))
                .andExpect(content().string(containsString("pyt1")))
                .andExpect(content().string(containsString("pyt2")))
                .andExpect(content().string(containsString("Wyloguj się")));

        verify(questionRepository, times(1)).findQuestionViewsById(1L);
        verify(usersRepository, times(1)).findByEmail("myemail@gmail.com");

    }

    @Test
    @DisplayName("Anonymous users should be able to see login screen")
    void getLogin() throws Exception {
        mockMvc.perform(get("/login")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Email")))
                .andExpect(content().string(containsString("Hasło")))
                .andExpect(content().string(containsString("Zaloguj się")));

    }

    @Test
    @DisplayName("Should allow users to log in")
    @WithMockUser(username = "name@gmail.com", password = "password")
    void postLogin() throws Exception {

        mockMvc.perform(post("/login")
                .param("username", "name@gmail.com")
                .param("password", "password")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound());
    }
}
