package com.geekbrains.services;

import com.geekbrains.configs.SecurityConfig;
import com.geekbrains.entites.Role;
import com.geekbrains.entites.User;
import com.geekbrains.repositories.RoleRepository;
import com.geekbrains.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private SecurityConfig securityConfig;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setSecurityConfig(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    public User findByPhone(String phone) {
        return userRepository.findOneByPhone(phone);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByPhone(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(user.getPhone(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public boolean isUserExist(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public void addNewUser (String phone, String password, String first_name, String last_name, String email) {
        User newUser = new User();
        newUser.setPhone(phone);
        String encodePassword = securityConfig.passwordEncoder().encode(password);
        newUser.setPassword(encodePassword);
        newUser.setFirstName(first_name);
        newUser.setLastName(last_name);
        newUser.setEmail(email);
        addDefaultRoleToUser(newUser);
        userRepository.save(newUser);
    }

    public void addNewUser (User user){
        addDefaultRoleToUser(user);
        userRepository.save(user);
    }

    private void addDefaultRoleToUser(User user) {
        List<Role> defaultUserRoles = new ArrayList<>();
        defaultUserRoles.add(roleRepository.findOneByName("ROLE_CUSTOMER"));
        user.setRoles(defaultUserRoles);
    }

    public void registerExistingUser(String phone, String password, String first_name, String last_name, String email){
        User existingUser =findByPhone(phone);
        String encodePassword = securityConfig.passwordEncoder().encode(password);
        existingUser.setPassword(encodePassword);
        existingUser.setFirstName(first_name);
        existingUser.setLastName(last_name);
        existingUser.setEmail(email);
        userRepository.save(existingUser);
    }
}