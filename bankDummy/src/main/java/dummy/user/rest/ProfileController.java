package dummy.user.rest;

import com.fasterxml.jackson.annotation.JsonView;
import gec.scf.dummy.common.utils.SecurityUtils;
import gec.scf.dummy.security.model.User;
import gec.scf.dummy.security.model.UserEvent;
import gec.scf.dummy.security.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;


    @JsonView({UserEvent.View.DetailForAllUser.class})
    @GetMapping(path = "/profile")
    public ResponseEntity<User> getProfileUser() {
        return ResponseEntity.ok(userRepository.findByUsername(SecurityUtils.getUser().getUsername()));
    }

    @JsonView({UserEvent.View.DetailForAllUser.class})
    @PostMapping(path = "/profile")
    public ResponseEntity<User> updateProfileUser(@RequestBody User user) {
        User userExisting = userRepository.findByUsername(SecurityUtils.getUser().getUsername());
        userExisting.setEmail(user.getEmail());
        userExisting.setFullName(user.getFullName());
        userExisting.setVersion(userExisting.getVersion() + 1);
        userRepository.save(userExisting);
        return ResponseEntity.ok(userRepository.findByUsername(SecurityUtils.getUser().getUsername()));
    }


}
