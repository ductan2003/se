package com.elearningweb.library.service.impl;

import com.elearningweb.library.dto.PostDto;
import com.elearningweb.library.model.Post;
import com.elearningweb.library.service.UserService;
import com.elearningweb.library.converter.Converter;
import com.elearningweb.library.dto.UserDto;
import com.elearningweb.library.model.User;
import com.elearningweb.library.repository.RoleRepository;
import com.elearningweb.library.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    Converter converter;
    @Autowired
    public ModelMapper modelMapper;

    @Override
    public UserDto save(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER")));

        userRepository.save(user);
        return userDto;
    }


    @Override
    public UserDto getUser(String username) {
        return converter.userToDto(userRepository.findByUserName(username));
    }


    @Override
    public List<UserDto> getAllUsers() {
        return converter.listUserToDto(userRepository.findAll());
    }

    @Override
    public boolean checkUserName(String username) {
        return userRepository.findByUserName(username) != null;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByUserName(username);
    }


    @Override
    public UserDto updateUser(User user, String username, String firstName, String lastName, String password) {
        user.setUsername(username);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        User updateUser = userRepository.save(user);
        return this.modelMapper.map(updateUser, UserDto.class);
    }

    @Override
    public UserDto changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        User updateUser = userRepository.save(user);
        return this.modelMapper.map(updateUser, UserDto.class);
    }

    @Override
    public UserDto updateProfileImage(User user, String image) {
        user.setProfileImage(image);
        User updateUser = userRepository.save(user);
        return this.modelMapper.map(updateUser, UserDto.class);
    }


}

