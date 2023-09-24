package com.example.springsecuritysample.service;

import com.example.springsecuritysample.model.entity.Users;
import com.example.springsecuritysample.repository.UsersRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService implements UserDetailsService {
    public final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usersRepository.findByEmail(email);
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public Optional<Users> findById(Long id) {
        return usersRepository.findById(id);
    }

    @PreAuthorize("#users.get().email != authentication.name")
    public void deleteById(Optional<Users> users) {
        usersRepository.deleteById(users.get().getId());
    }

    @Transactional
    public void registerUser(Users users) {
        usersRepository.saveAndFlush(users);
    }
}
