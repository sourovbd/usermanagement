package com.aes.corebackend.service;

import com.aes.corebackend.dto.UserCredentialDTO;
import com.aes.corebackend.entity.UserCredential;
import com.aes.corebackend.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserCredentialService {

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    public boolean save(UserCredential userCredential) {
        try {
            //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
            userCredentialRepository.save(userCredential);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(UserCredential userCredential) {
        try {
            UserCredential userCredential1 = userCredentialRepository.findByEmployeeId(userCredential.getEmployeeId());
            userCredential1.setPassword(userCredential.getPassword());
            //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
            userCredentialRepository.save(userCredential1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean verifyPassword(UserCredentialDTO userCredentialDTO) {
        try {
            UserCredential userCredential = userCredentialRepository.findByEmployeeId(userCredentialDTO.getEmployeeId());
            if (userCredential.getPassword().equals(userCredentialDTO.getPassword())) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
