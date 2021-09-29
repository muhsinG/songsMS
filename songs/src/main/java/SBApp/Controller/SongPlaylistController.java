package SBApp.Controller;


import SBApp.Exception.ResourceNotFoundException;
import SBApp.Model.SongList;
import SBApp.Repo.SongListRepository;
import SBApp.Repo.SongRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/playlists")
public class SongPlaylistController {

    @Autowired
    private RestTemplate restTemplate;

    private final SongListRepository songListRepository;
    private final SongRepository songRepository;

    public SongPlaylistController (SongListRepository slRepository, SongRepository sRepository) {
        this.songListRepository = slRepository;
        this.songRepository=sRepository;
    }

    /** Helper Method: return unauthorized */
    private void checkAuth(String authToken){if (getUserID(authToken)==null) {Unauthorized();}}
    private ResponseEntity<String> Unauthorized(){
        return new ResponseEntity<String>("Unauthorized: Token not valid", HttpStatus.UNAUTHORIZED);
    }

    private String getUserID(String token){
        HttpHeaders header = new HttpHeaders();

        header.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<String> responseEntity = restTemplate.exchange("http://auth/getUserId", HttpMethod.GET, entity, String.class);

        return responseEntity.getBody();
    }
    private String checkUserId(String user){
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(header);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user", user);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://auth/checkUserId/{user}", String.class, params);
        return responseEntity.getBody();
    }

    @GetMapping(value = "/playlists",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllSongLists(@RequestHeader("Authorization") String authHeader, @RequestParam("userId") String userId) throws IOException {
        // TODO: authorization first - done
        //                  if user is authorized: - done
        //                  check if userId exist - done
        //                  send all Songlist from that user - done
        //                  send only the public songList from other users - done
        String userIdFromToken = getUserID(authHeader);
        if (userIdFromToken==null){return Unauthorized();}

        if (checkUserId(userId)==null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        if(userId.equals(userIdFromToken)){
            return new ResponseEntity<>(songListRepository.findByOwnerId(userId), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(songListRepository.findByOwnerIdPublic(userId), HttpStatus.OK);
        }
        //return new ResponseEntity<>("Test", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{songListId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllSongLists(@PathVariable(value = "songListId") Integer songListId, @RequestHeader("Authorization") String authHeader) throws IOException {
        // TODO: authorization first then check if the songList belong to the user or isPublic

        String userIdFromToken = getUserID(authHeader);
        if (userIdFromToken==null){return Unauthorized();}

        SongList sList = songListRepository.findBySongListId(songListId).get(0);
        if(sList.getOwnerId().equals(userIdFromToken)){
            return new ResponseEntity<>(sList, HttpStatus.OK);
        }
        else if(sList.getIsPrivate()==false){
            return new ResponseEntity<>(sList, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addSongList(@RequestBody SongList newSongList){
        // TODO: owner will be retrieved after authorizing owner
        //                  newSongList.setOwnerId("from somewhere");
        songListRepository.save(newSongList);
        return new ResponseEntity<>(newSongList.getSongListId(), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{songListId}")
    public ResponseEntity<?> deleteSongList(@PathVariable(value = "songListId") Integer songListId,
                                            @RequestHeader(value = "Authorization") String authHeader){
        // TODO: User können nur eigene Songlisten löschen,
        //  nicht die der anderen User, auch keine fremden, public Songlisten

        String userIdFromToken = getUserID(authHeader);
        if (userIdFromToken==null){return Unauthorized();}

        SongList songList = songListRepository.findById(songListId)
                .orElseThrow(()->new ResourceNotFoundException("SongList", "id", songListId));

        if (songList.getOwnerId().equals(userIdFromToken) == false){
            return new ResponseEntity<String>("This playlist does not belong to you", HttpStatus.UNAUTHORIZED);
        }
        songListRepository.delete(songList);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{songlistid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSongPlaylist(@RequestBody SongList newplaylist,
                                                @PathVariable(value = "songlistid") Integer songlistid,
                                                @RequestHeader(value="Authorization") String authHeader){
        String userIdFromToken = getUserID(authHeader);
        if (userIdFromToken==null){return Unauthorized();}

        List<SongList> songlistslist = songListRepository.findBySongListId(songlistid);
        if (songlistslist.isEmpty()) {return new ResponseEntity<>("Playlist " + songlistid + " not found", HttpStatus.NOT_FOUND);}

        SongList playlist = songlistslist.get(0);
        playlist.setName(newplaylist.getName());
        playlist.setIsPrivate(newplaylist.getIsPrivate());
        playlist.setOwnerId(newplaylist.getOwnerId());
        playlist.setSongListId(newplaylist.getSongListId());
        playlist.setSongList(newplaylist.getSongList());

        SongList updatedplaylist = songListRepository.save(playlist);
        return new ResponseEntity<>(updatedplaylist, HttpStatus.OK);
    }


}
