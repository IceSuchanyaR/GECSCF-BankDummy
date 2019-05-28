package gec.scf.dummy.security.rest;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gec.scf.dummy.common.utils.SecurityUtils;

@RestController
@RequestMapping(path = "/api")
public class PrivilegeController {

    @GetMapping(path = "/privileges")
    public ResponseEntity<Object> getPrivileges() {
        return ResponseEntity.ok(SecurityUtils.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }
}
