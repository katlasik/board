package katlasik.board.controllers;

import katlasik.board.model.Role;
import katlasik.board.model.User;
import katlasik.board.repositories.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersRepository usersRepository;

    @Test
    @DisplayName("User should be able to register")
    void postRegistration() throws Exception {

        var user = new User();
        user.setName("name");
        user.setActive(true);
        user.setEmail("myemail@gmail.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        when(usersRepository.save(user)).thenReturn(user);
        when(usersRepository.checkIfMailExists("myemail@gmail.com")).thenReturn(false);
        when(usersRepository.checkIfNameExists("name")).thenReturn(false);


        mockMvc.perform(post("/registration")
                .param("name", "name")
                .param("password", "password")
                .param("passwordRepeat", "password")
                .param("email", "myemail@gmail.com")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound());


        verify(usersRepository, times(1)).checkIfMailExists("myemail@gmail.com");
        verify(usersRepository, times(1)).checkIfNameExists("name");
        verify(usersRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("User should be able to visit registration page")
    void getRegistration() throws Exception {
        mockMvc.perform(get("/registration")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Zarejestruj się")));
    }

    @Test
    @DisplayName("User should be able to visit thank you page")
    void getThankYou() throws Exception {
        mockMvc.perform(get("/thank-you")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Dziękujęmy za rejestrację!")));
    }
}
