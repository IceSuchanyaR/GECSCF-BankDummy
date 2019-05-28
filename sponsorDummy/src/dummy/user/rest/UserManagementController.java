package gec.scf.dummy.user.rest;

import com.fasterxml.jackson.annotation.JsonView;
import gec.scf.dummy.common.exception.DataNotFoundException;
import gec.scf.dummy.common.exception.DuplicationException;
import gec.scf.dummy.common.exception.VersionMismatchException;
import gec.scf.dummy.common.utils.SecurityUtils;
import gec.scf.dummy.security.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@RestController
@PreAuthorize("hasAnyAuthority('MANAGE_ALL')")
@RequestMapping("/api")
public class UserManagementController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @JsonView({UserEvent.View.DetailForCommonUser.class})
    @GetMapping("/name")
    public String getName() {
        return "NAME";
    }

    @JsonView({UserEvent.View.DetailForCommonUser.class})
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getUsers(
            @RequestParam(required = false, defaultValue = "") String username,
            @RequestParam(required = false, defaultValue = "") String fullName,
            @RequestParam(required = false, defaultValue = "") String role,
            @RequestParam(required = false) Boolean status,
            Pageable pageable) {

        if (status == null)
            return ResponseEntity.ok(userRepository.findByUserLike(username, fullName, role));
        else
            return ResponseEntity.ok(userRepository.findByUserLike(username, fullName, role, status));
    }

    @JsonView({UserEvent.View.DetailForCommonUser.class})
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent())
            return ResponseEntity.ok(user.get());
        else
            throw new DataNotFoundException("User not found.");
    }

    @JsonView({UserEvent.View.DetailForCommonUser.class})
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestParam(required = false) String role,
            @Validated(UserEvent.Validation.Update.class) @RequestBody User user)
            throws VersionMismatchException, DataNotFoundException, DuplicationException {
        Optional<User> getting = userRepository.findById(id);
        User existingUser;

        if (getting.isPresent())
            existingUser = getting.get();
        else
            throw new DataNotFoundException("User not found.");

        if (getting.get().getVersion() != user.getVersion())
            throw new VersionMismatchException("Version mismatch.");

        if (!userRepository.findByEmail(user.getEmail()).getId().equals(existingUser.getId()))
            throw new DuplicationException("E-mail is already used.");

        if (role != null) {
            Role getRole = roleRepository.findByName(role);
            existingUser.setRoles(new HashSet<Role>(Collections.singletonList(getRole)));
        }

        existingUser.setEmail(user.getEmail());
        existingUser.setEnabled(user.getEnabled());
        existingUser.setBirthday(user.getBirthday());
        existingUser.setUpdateByUsername(SecurityUtils.getUser().getUsername());
        existingUser.setFullName(user.getFullName());
        existingUser.setVersion(existingUser.getVersion() + 1);
        existingUser.setUpdateBy(SecurityUtils.getUser().getId());

        userRepository.save(existingUser);
        return ResponseEntity.ok(userRepository.findByUsername(user.getUsername()));
    }

    @JsonView({UserEvent.View.DetailForCommonUser.class})
    @PostMapping("/users")
    public ResponseEntity<User> createUser(
            @RequestParam String role,
            @RequestParam String password,
            @Validated(UserEvent.Validation.Create.class) @RequestBody User user) throws DuplicationException {
        if (userRepository.findByUsername(user.getUsername()) != null)
            throw new DuplicationException("Username is already used.");

        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new DuplicationException("E-mail is already used.");

        User existUser = new User();
        existUser.setUsername(user.getUsername());
        existUser.setCreateByUsername(SecurityUtils.getUser().getUsername());
        existUser.setUpdateByUsername(SecurityUtils.getUser().getUsername());
        existUser.setPassword(passwordEncoder.encode(password));
        existUser.setFullName(user.getFullName());
        existUser.setEmail(user.getEmail());
        existUser.setBirthday(user.getBirthday());
        existUser.setEnabled(true);
        existUser.setCreateBy(SecurityUtils.getUser().getId());
        existUser.setUpdateBy(SecurityUtils.getUser().getId());

        if (role != null) {
            Role getRole = roleRepository.findByName(role);
            existUser.setRoles(new HashSet<Role>(Collections.singletonList(getRole)));
        }
        userRepository.save(existUser);
        return ResponseEntity.ok(userRepository.findByUsername(user.getUsername()));
    }
}
