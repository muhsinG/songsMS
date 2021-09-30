package htwb.muhsinG;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.muhsinG.Controller.SongLyricsController;
import htwb.muhsinG.Model.SongLyrics;
import htwb.muhsinG.Repo.SongLyricsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.*;

@WebMvcTest(SongLyricsController.class)
public class SongLyricsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    SongLyricsRepository songLyricsRepository;

    SongLyrics LYRICS = new SongLyrics(1, "testtitle", "testartist", "testlyrics abcdefghi");

    @Test
    public void getLyricsTest() throws Exception{
        List<SongLyrics> songLyrics = new ArrayList<>(Arrays.asList(LYRICS));

        Mockito.when(songLyricsRepository.findbyId(LYRICS.getId())).thenReturn(songLyrics);

        String uri = "/lyrics/" + songLyrics.get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isOk());
    }

//    @Test
//    public void postLyricsTest() throws Exception{
//        List<SongLyrics> songLyrics = new ArrayList<>(Arrays.asList(LYRICS));
//
//        Mockito.when(songLyricsRepository.save(LYRICS)).thenReturn(LYRICS);
//        Map<String, Object> lyric = new HashMap<>();
//        lyric.put("id", 1);
//        lyric.put("title", "testtitle");
//        lyric.put("artist", "testartist");
//        lyric.put("lyrics", "testlyrics abcdefghi");
//
//        String uri = "/lyrics/";
//
//        mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(lyric)))
//                .andExpect(status().isCreated());
//    }
}
