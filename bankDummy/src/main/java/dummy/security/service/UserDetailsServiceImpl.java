package gec.scf.dummy.security.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import gec.scf.dummy.security.model.Role;
import gec.scf.dummy.security.model.User;
import gec.scf.dummy.security.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user != null) {
			Set<SimpleGrantedAuthority> authorities = new HashSet<>();
			for (Role role : user.getRoles()) {
				Set<SimpleGrantedAuthority> innerAuthorities = role.getPrivileges()
						.stream().map(permission -> new SimpleGrantedAuthority(
								permission.getName()))
						.collect(Collectors.toSet());
				authorities.addAll(innerAuthorities);
			}
			user.setAuthorities(authorities);

			return user;
		}

		throw new UsernameNotFoundException(username);
	}
}