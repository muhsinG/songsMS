package SBApp;

import SBApp.Controller.UserController;
import SBApp.Model.User;
import SBApp.Repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserRepository userRepository;

    User USER = new User("testuserid", "testpassword", "testfirstname", "testlastname", "testtoken");

    @Test
    public void authTestSuccess() throws Exception {
        List<User> users = new ArrayList<>(Arrays.asList(USER));
        Mockito.when(userRepository.findUserByUserId(USER.getUserId())).thenReturn(users);

        Map<String, Object> user = new HashMap<>();
        user.put("userId", "testuserid");
        user.put("password", "testpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void authTestWrongPassword() throws Exception {
        List<User> users = new ArrayList<>(Arrays.asList(USER));
        Mockito.when(userRepository.findUserByUserId(USER.getUserId())).thenReturn(users);

        Map<String, Object> user = new HashMap<>();
        user.put("userId", "testuserid");
        user.put("password", "wrong");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void authTestNoUser() throws Exception {
        List<User> users = new ArrayList<>(Arrays.asList(USER));
        Mockito.when(userRepository.findUserByUserId(USER.getUserId())).thenReturn(users);

        Map<String, Object> user = new HashMap<>();
        user.put("userId", "wrong");
        user.put("password", "testpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getUserIdFromTokenSuccess() throws Exception{
        List<User> users = new ArrayList<>(Arrays.asList(USER));
        Mockito.when(userRepository.findUserByToken(USER.getToken())).thenReturn(users);



        mockMvc.perform(MockMvcRequestBuilders.get("/getUserId").header("Authorization", "testtoken").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getUserIdFromTokenFailure() throws Exception{
        List<User> users = new ArrayList<>(Arrays.asList());
        Mockito.when(userRepository.findUserByToken(USER.getToken())).thenReturn(users);



        mockMvc.perform(MockMvcRequestBuilders.get("/getUserId").header("Authorization", "testtoken").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$").doesNotExist());
    }
    @Test
    public void getCheckUserIdSuccess() throws Exception{
        List<User> users = new ArrayList<>(Arrays.asList(USER));
        Mockito.when(userRepository.findUserByUserId(USER.getUserId())).thenReturn(users);

        String uriTemplate = "/checkUserId/" + USER.getUserId();


        mockMvc.perform(MockMvcRequestBuilders.get(uriTemplate).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void getCheckUserIdFailure() throws Exception{
        List<User> users = new ArrayList<>(Arrays.asList());
        Mockito.when(userRepository.findUserByUserId(USER.getUserId())).thenReturn(users);

        String uriTemplate = "/checkUserId/" + USER.getUserId();


        mockMvc.perform(MockMvcRequestBuilders.get(uriTemplate).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$").doesNotExist());
    }
}
