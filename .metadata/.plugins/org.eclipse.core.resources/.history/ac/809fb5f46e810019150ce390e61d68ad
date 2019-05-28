package gec.scf.dummy.user.rest;

import com.fasterxml.jackson.annotation.JsonView;
import gec.scf.dummy.common.exception.DataNotFoundException;
import gec.scf.dummy.security.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@PreAuthorize("hasAnyAuthority('MANAGE_ALL')")
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @JsonView({UserEvent.View.DetailForCommonUser.class})
    @GetMapping("/roles}")
    public ResponseEntity<Iterable<Role>> getRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @JsonView({UserEvent.View.DetailForCommonUser.class})
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleByUser(@PathVariable Long id) throws DataNotFoundException {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent())
            return ResponseEntity.ok(role.get());
        else
            throw new DataNotFoundException("Role not found.");
    }


}
