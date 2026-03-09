package com.techspire.project.service.impl;

import com.techspire.project.dto.CreateUserDto;
import com.techspire.project.dto.UpdateUserDto;
import com.techspire.project.exception.UserNotFoundException;
import com.techspire.project.model.Credential;
import com.techspire.project.model.User;
import com.techspire.project.repository.CredentialRepository;
import com.techspire.project.repository.UserRepository;
import com.techspire.project.repository.impl.CredentialRepositoryImpl;
import com.techspire.project.repository.impl.UserRepositoryImpl;
import com.techspire.project.service.UserService;
import com.techspire.project.util.PasswordUtil;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;

    public UserServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
        this.credentialRepository = new CredentialRepositoryImpl();
    }

    @Override
    public boolean createNewUser(CreateUserDto dto) throws Exception {
        if (dto == null) throw new IllegalArgumentException("CreateUserDto cannot be null");
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) throw new IllegalArgumentException("First name is required");
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) throw new IllegalArgumentException("Last name is required");
        if (dto.getIcNumber() == null || dto.getIcNumber().trim().isEmpty()) throw new IllegalArgumentException("IC number is required");
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) throw new IllegalArgumentException("Username is required");
        if (dto.getUserType() == null) throw new IllegalArgumentException("User type is required");
        if (dto.getLeaveCount() < 0) throw new IllegalArgumentException("Leave count cannot be negative");

        User existingUser = userRepository.findByIcNumber(dto.getIcNumber());
        if (existingUser != null) return false;

        if (credentialRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setIcNumber(dto.getIcNumber());
        user.setUserType(dto.getUserType());
        user.setLeaveCount(dto.getLeaveCount());

        boolean saved = userRepository.save(user);
        if (!saved) return false;

        User createdUser = userRepository.findByIcNumber(dto.getIcNumber());
        if (createdUser == null) return false;

        Credential credential = new Credential();
        credential.setUserId(createdUser.getId());
        credential.setUsername(dto.getUsername().trim());
        credential.setPasswordHash(PasswordUtil.hashPassword("Pass@123"));
        credential.setDefaultPassword(true);

        return credentialRepository.save(credential);
    }

    @Override
    public boolean updateUserDetails(UpdateUserDto dto) throws Exception {
        if (dto == null) throw new IllegalArgumentException("UpdateUserDto cannot be null");
        if (dto.getIcNumber() == null || dto.getIcNumber().trim().isEmpty()) throw new IllegalArgumentException("IC number is required");

        User user = userRepository.findByIcNumber(dto.getIcNumber());
        if (user == null) {
            throw new UserNotFoundException("User not found with IC number: " + dto.getIcNumber());
        }

        return userRepository.updateDetails(dto);
    }

    @Override
    public boolean deleteUser(String icNumber) throws Exception {
        if (icNumber == null || icNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("IC number is required");
        }

        User user = userRepository.findByIcNumber(icNumber);
        if (user == null) {
            throw new UserNotFoundException("User not found with IC number: " + icNumber);
        }

        return userRepository.deleteByIcNumber(icNumber);
    }

    @Override
    public boolean changePassword(String icNumber, String newPassword) throws Exception {
        if (icNumber == null || icNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("IC number is required");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password is required");
        }

        User user = userRepository.findByIcNumber(icNumber);
        if (user == null) {
            throw new UserNotFoundException("User not found with IC number: " + icNumber);
        }

        return credentialRepository.updatePasswordByUserId(
                user.getId(),
                PasswordUtil.hashPassword(newPassword),
                false
        );
    }

    @Override
    public User getUserById(Long id) throws Exception {
        User user = userRepository.findById(id);
        if (user == null) throw new UserNotFoundException("User not found with id: " + id);
        return user;
    }

    @Override
    public User getUserByIcNumber(String icNumber) throws Exception {
        User user = userRepository.findByIcNumber(icNumber);
        if (user == null) throw new UserNotFoundException("User not found with IC number: " + icNumber);
        return user;
    }

    @Override
    public List<User> checkDetails() throws Exception {
        return userRepository.findAll();
    }

    @Override
    public boolean addLeaveCount(String icNumber, int extraLeave) throws Exception {
        if (icNumber == null || icNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("IC number is required");
        }
        if (extraLeave < 0) {
            throw new IllegalArgumentException("Extra leave cannot be negative");
        }

        User user = userRepository.findByIcNumber(icNumber);
        if (user == null) {
            throw new RuntimeException("User not found with IC number: " + icNumber);
        }

        int updatedLeaveCount = user.getLeaveCount() + extraLeave;
        return userRepository.updateLeaveCount(user.getId(), updatedLeaveCount);
    }
}