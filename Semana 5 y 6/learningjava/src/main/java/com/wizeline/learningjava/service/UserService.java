package com.wizeline.learningjava.service;

import com.wizeline.learningjava.model.ResponseDTO;

public interface UserService {

	ResponseDTO createUser(String user, String password);

	ResponseDTO login(String user, String password);

	ResponseDTO updatePassword(String user, String newPassword);

}