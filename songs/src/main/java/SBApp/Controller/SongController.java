package SBApp.Controller;

import SBApp.Exception.ResourceNotFoundException;
import SBApp.Model.Song;
import SBApp.Repo.SongRepository;
import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping()
public class SongController {

    @Autowired
    private RestTemplate restTemplate;


    private final SongRepository songRepository;

    public SongController(SongRepository sRepository){
        this.songRepository = sRepository;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<String>("/songs/test called", HttpStatus.OK);
    }

    @GetMapping("/callauth")
    public String callauthTest(){
        return restTemplate.getForObject("http://auth/test", String.class);
    }

    /** Helper method to get userID from token */
    private String getUserID(String token){
        HttpHeaders header = new HttpHeaders();

        header.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<String> responseEntity = restTemplate.exchange("http://auth/getUserId", HttpMethod.GET, entity, String.class);

        return responseEntity.getBody();
    }

    /** Helper Method: return unauthorized */
    private void checkAuth(String authToken){if (getUserID(authToken)==null) {Unauthorized();}}
    private ResponseEntity<String> Unauthorized(){
        return new ResponseEntity<String>("Unauthorized: Token not valid", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/songs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSongs(@RequestHeader("Authorization") String authToken) throws IOException {

        if (getUserID(authToken)==null) {return Unauthorized();}
        return new ResponseEntity<>(songRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSong(@PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) throws IOException {
        if (getUserID(authToken)==null) {return Unauthorized();}
        // return songRepository.findById(id)
        //        .orElseThrow(() -> new ResourceNotFoundException("Song","id", id));
        return new ResponseEntity<>(songRepository.findById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
                produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addSong(@RequestBody Song newSong, @RequestHeader("Authorization") String authToken){
        if (getUserID(authToken)==null) {return Unauthorized();}

        if (newSong.getTitle() != null) {
            Song s = songRepository.save(newSong);
            return new ResponseEntity<>("Location : " + "http://localhost:8080/songs/" + s.getId(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Song format wrong- Example song: body : {\"id\":2,\"title\":\"Covid\",\"artist\":\"Someone\",\"label\":\"RCA\",\"released\":2020} ", HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateSong(@RequestBody Song newSong,
                                        @PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken){
        if (getUserID(authToken)==null) {return Unauthorized();}
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));
        if (song != null) {
            song.setId(newSong.getId());
            song.setLabel(newSong.getLabel());
            song.setArtist(newSong.getArtist());
            song.setReleased(newSong.getReleased());
            song.setTitle(newSong.getTitle());
            Song updatedSong = songRepository.save(song);
            return new ResponseEntity<>(updatedSong.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable (value = "id") Integer id, @RequestHeader("Authorization") String authToken){
        if (getUserID(authToken)==null) {return Unauthorized();}

        Song song = songRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Song", "id" , id));
        songRepository.delete(song);
        //return ResponseEntity.noContent().build();
        return new ResponseEntity<String>("Song with ID " + id + " has been deleted", HttpStatus.OK);
    }
}
