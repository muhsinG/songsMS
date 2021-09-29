package SBApp.Controller;

import SBApp.Model.User;
import SBApp.Repo.UserRepository;

import com.netflix.ribbon.proxy.annotation.Http;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class UserControllerDI {

    private final UserRepository userRepository;

    public UserControllerDI(UserRepository uRepository) {
        this.userRepository = uRepository;
    }

    @PostMapping(value = "/auth",consumes = {MediaType.APPLICATION_JSON_VALUE})
    // "authorize nicht per GET!!
    public ResponseEntity<String> authorize(@RequestBody User newUser)
            throws Exception {
        String newUserId = newUser.getUserId();
        String newUserPassword = newUser.getPassword();
        if (newUser == null || newUserId == null ||
                newUserPassword == null) {
            return new ResponseEntity<String>("Declined: No such user!",
                    HttpStatus.UNAUTHORIZED);
        }

        List<User> repoUser = userRepository.findUserByUserId(newUserId);
        if(repoUser.isEmpty()){
            return new ResponseEntity<String>("Declined: No such user!", HttpStatus.UNAUTHORIZED);
        }
        if (repoUser.get(0).getUserId().equals(newUser.getUserId()) && repoUser.get(0).getPassword().equals(newUser.getPassword())) {
            String token = generateToken(repoUser.get(0));
            return new ResponseEntity<String> (token, HttpStatus.OK);
        }
        return new ResponseEntity<String> ("Declined!!", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getUserId")
    public ResponseEntity<String> getUserIdFromToken(@RequestHeader("Authorization") String token){
        List<User> users = userRepository.findUserByToken(token);
        if (users.isEmpty()) {
            //return null;
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(users.get(0).getUserId(), HttpStatus.OK);
    }

    @GetMapping("/checkUserId/{user}")
    public ResponseEntity<String> findUserId(@PathVariable(value = "user") String user){
        List<User> users = userRepository.findUserByUserId(user);
        if (users.isEmpty()) {
            //return null;
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(users.get(0).getUserId(), HttpStatus.OK);
    }


    private String generateToken(User user) {
        String generatedString = RandomStringUtils.randomAlphabetic(20);
      //  Model.User updateduser = userRepository.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("user", "id", user.getUserId()));
       // Model.User newuser = new Model.User.builder();
//        updateduser.setToken(generatedString);
//        updateduser.setUserId(user.getUserId());
//        updateduser.setFirstname(user.getFirstname());
//        updateduser.setLastname(user.getLastname());
//        updateduser.setPassword(user.getPassword());

        //userRepository.findUserByUserId(userid).get(0).setToken(generatedString);
//        user.setToken(generatedString);
//        userRepository.save(user);

        userRepository.updateUser(user.getUserId(), generatedString);
        return generatedString;
    }

    @GetMapping("/test")
    public ResponseEntity<String> authTest() {
        return new ResponseEntity<>("/auth/test called", HttpStatus.OK);
    }



}
