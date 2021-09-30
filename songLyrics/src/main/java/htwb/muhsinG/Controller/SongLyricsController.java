package htwb.muhsinG.Controller;

import htwb.muhsinG.Model.SongLyrics;
import htwb.muhsinG.Repo.SongLyricsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/lyrics")
public class SongLyricsController {

    private SongLyricsRepository lyricsRepository;

    public SongLyricsController(SongLyricsRepository lyricsRepository){
        this.lyricsRepository=lyricsRepository;
    }

    @GetMapping(value ="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLyrics(@PathVariable(value = "id") Integer id) throws IOException {
        if (lyricsRepository.findbyId(id).isEmpty()) {
            return new ResponseEntity<>("Song Lyrics not found", HttpStatus.NOT_FOUND);
        }
        List<SongLyrics> lyricsList = lyricsRepository.findbyId(id);
        String lyrics = lyricsList.get(0).getTitle() + " by " + lyricsList.get(0).getArtist() + ": " + System.lineSeparator() + lyricsList.get(0).getLyrics();
        return new ResponseEntity<>(lyrics, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> postLyrics(@RequestBody SongLyrics newlyrics){
        if (newlyrics.getTitle() != null || newlyrics.getArtist() != null){
            SongLyrics l = lyricsRepository.save(newlyrics);
            Integer id = l.getId();
            return new ResponseEntity<>("Location: " + "/lyrics/" + id, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Request Body wrong", HttpStatus.BAD_REQUEST);
    }
}
