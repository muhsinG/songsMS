package SBApp;

import SBApp.Controller.SongController;
import SBApp.Model.Song;
import SBApp.Repo.SongRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SongController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SongControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Autowired
    RestTemplate restTemplate;

    String authHeader = "wNerOJneBJBUgEjxcKHq";

    @MockBean
    SongRepository songRepository;

    Song SONG_1 = new Song(1, "SongTitle1", "SongArtist1", "SongLabel1", 0001);
    Song SONG_2 = new Song(3, "SongTitle2", "SongArtist2", "SongLabel2", 0002);
    Song SONG_3 = new Song(2, "SongTitle3", "SongArtist3", "SongLabel3", 0003);

//    @BeforeAll
    public void getAuthHeader() throws JSONException {
        JSONObject login = new JSONObject();
        login.put("userId", "test");
        login.put("password", "test");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(login.toString(), headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("http://auth", HttpMethod.POST, entity, String.class);
        authHeader=responseEntity.getBody();
    }

    @Test
    public void getAllSongsTest() throws Exception {
        List<Song> songs = new ArrayList<>(Arrays.asList(SONG_1, SONG_2, SONG_3));

        Mockito.when(songRepository.findAll()).thenReturn(songs);

        mockMvc.perform(MockMvcRequestBuilders.
                get("/songs/").header("Authorization", authHeader).
                contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[2].title", is("SongTitle3")));
    }

}
