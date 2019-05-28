package dummy.user.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import dummy.common.annotation.EncryptedRequestParam;
import dummy.common.exception.DataNotFoundException;
import dummy.common.exception.VersionMismatchException;
import dummy.common.utils.SecurityUtils;
import dummy.security.model.User;
import dummy.security.model.UserEvent;
import dummy.security.model.UserRepository;

@RestController
@RequestMapping(path = "/api")
public class ChangePasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @JsonView({UserEvent.View.DetailForAllUser.class})
    @PostMapping(path = "/change-password")
    public ResponseEntity<User> getProfileUser(
            @EncryptedRequestParam String currentPassword,
            @EncryptedRequestParam String newPassword,
            @EncryptedRequestParam String confirmNewPassword
    ) throws DataNotFoundException, VersionMismatchException {
        User user = SecurityUtils.getUser();
        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            if (newPassword.equals(confirmNewPassword)) {
                if (!currentPassword.equals(newPassword)) {
                    Optional<User> existingUser = userRepository.findById(user.getId());
                    if (existingUser.isPresent()) {
                        user = existingUser.get();

                        user.setPassword(passwordEncoder.encode(newPassword));
                        user.setVersion(user.getVersion() + 1);
                        userRepository.save(user);
                        return ResponseEntity.ok(userRepository.findByUsername(user.getUsername()));
                    } else throw new DataNotFoundException("User not found.");
                }
                throw new VersionMismatchException("New & Current password must be different.");
            }
            throw new VersionMismatchException("New & Confirm password mismatch.");
        }
        throw new VersionMismatchException("Current password wrong.");
    }

}
