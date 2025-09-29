package com.smartbookkeeping.service;

import com.smartbookkeeping.domain.entity.User;

public interface UserService {

    User findByUsername(String username);

    User findById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);
}