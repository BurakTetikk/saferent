package com.saferent.service;

import com.saferent.dto.UserDTO;
import com.saferent.dto.request.RegisterRequest;
import com.saferent.entity.Role;
import com.saferent.entity.User;
import com.saferent.entity.enums.RoleType;
import com.saferent.exception.ConflictException;
import com.saferent.exception.ResourceNotFoundException;
import com.saferent.exception.message.ErrorMessage;
import com.saferent.mapper.UserMapper;
import com.saferent.repository.UserRepository;
import com.saferent.security.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public User getUserByEmail(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.EMAIL_NOT_FOUND_EXCEPTION)));


        return user;

    }

    public void saveUser(RegisterRequest registerRequest) {
        //DTO dan gelen email DB de daha önceden var mı?

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, registerRequest.getEmail()));
        }

        // Yeni kullanıcının rolünü default olarak "CUSTOMER" set ediyoruz
        Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);

        Set<Role> roles = new HashSet<>();

        roles.add(role);

        //DB ye gitmeden şifre encode yapılır
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // Yeni kullanıcının bilgilerini setleyip DBye gönderiyoruz
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setAddress(registerRequest.getAddress());
        user.setZipCode(registerRequest.getZipCode());
        user.setRoles(roles);

        userRepository.save(user);

    }

    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserDTO> userDTOS = userMapper.map(users);

        return userDTOS;
    }

    public UserDTO getPrincipal() {

        User user = getCurrentUser();

       UserDTO userDTO = userMapper.userToUserDTO(user);

       return userDTO;

    }



    public User getCurrentUser() {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));

        User user = getUserByEmail(email);

        return user;

    }





}
