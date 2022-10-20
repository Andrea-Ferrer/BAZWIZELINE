package com.wizeline.learningjava.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.wizeline.learningjava.repository.UserRepository;
import com.wizeline.learningjava.model.ErrorDTO;
import com.wizeline.learningjava.model.ResponseDTO;
import com.wizeline.learningjava.model.UserDTO;
import com.wizeline.learningjava.utils.Utils;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public ResponseDTO login(String user, String password) {
		LOGGER.info("Inicia procesamiento en capa de negocio");
		ResponseDTO response = new ResponseDTO();
		String result = "";
		if (Utils.validateNullValue(user) && Utils.validateNullValue(password)) {
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(
					Criteria.where("user").is(user),
					Criteria.where("password").is(password)));
			if (mongoTemplate.findOne(query, UserDTO.class) != null) {
				response.setCode("OK000");
				result = "Usuario encontrado";
				response.setStatus(result);
			} else {
				ErrorDTO error = new ErrorDTO();
				error.setErrorCode("ER004");
				error.setMessage(
						"Error usuario o contraseña no validos");
				response.setErrors(error);
				response.setCode("ER004");
				response.setStatus("fail");
			}
		} else {
			ErrorDTO error = new ErrorDTO();
			error.setErrorCode("ER000");
			error.setMessage(
					"Los campos user y password no pueden ser nulos o vacios");
			response.setErrors(error);
			response.setCode("ERR000");
			response.setStatus(result);
		}
		return response;
	}

	@Override
	public ResponseDTO createUser(String user, String password) {
		LOGGER.info("Inicia procesamiento de guardado de cliente en base de datos");
		ResponseDTO response = new ResponseDTO();
		String result = "fail";
		if (Utils.validateNullValue(user) && Utils.validateNullValue(password)) {
			if (Utils.isPasswordValid(password)) {
				Query query = new Query();
				query.addCriteria(Criteria.where("user").is(user));
				if (mongoTemplate.findOne(query, UserDTO.class) == null) {
					UserDTO usuario = new UserDTO();
					usuario.setUser(user);
					usuario.setPassword(password);
					mongoTemplate.save(usuario);
					response.setCode("OK000");
					result = "success";
					response.setStatus(result);
				} else {
					ErrorDTO error = new ErrorDTO();
					error.setErrorCode("ER002");
					error.setMessage("El usuario ya se encuentra registrado");
					response.setErrors(error);
					response.setCode("ER002");
					response.setStatus("fail");
				}

			} else {
				ErrorDTO error = new ErrorDTO();
				error.setErrorCode("ER001");
				error.setMessage(
						"El password debe contener al menos un digito del 1 al 9, una letra minuscula, una letra mayuscula, un caracter especial (?=.*[@#$]) y una longitud de entre 6 a 10 caracteres");
				response.setErrors(error);
				response.setCode("ER001");
				response.setStatus("fail");
			}

		} else {
			ErrorDTO error = new ErrorDTO();
			error.setErrorCode("ER000");
			error.setMessage(
					"Los campos user y password no pueden ser nulos o vacios");
			response.setErrors(error);
			response.setCode("ERR000");
			response.setStatus(result);
		}
		return response;
	}

	@Override
	public ResponseDTO updatePassword(String user, String newPassword) {
		LOGGER.info("Inicia procesamiento de guardado de cliente en base de datos");
		ResponseDTO response = new ResponseDTO();
		String result = "fail";
		if (Utils.validateNullValue(user) && Utils.validateNullValue(newPassword)) {
			if (Utils.isPasswordValid(newPassword)) {
				Query query = new Query();
				query.addCriteria(Criteria.where("user").is(user));
				if (mongoTemplate.findOne(query, UserDTO.class) != null) {
					mongoTemplate.updateFirst(query, Update.update("password", newPassword), UserDTO.class);
					response.setCode("OK000");
					result = "success";
					response.setStatus(result);
				} else {
					ErrorDTO error = new ErrorDTO();
					error.setErrorCode("ER002");
					error.setMessage("El usuario no se encuentra registrado");
					response.setErrors(error);
					response.setCode("ER002");
					response.setStatus("fail");
				}
			} else {
				ErrorDTO error = new ErrorDTO();
				error.setErrorCode("ER001");
				error.setMessage(
						"El password debe contener al menos un digito del 1 al 9, una letra minuscula, una letra mayuscula, un caracter especial (?=.*[@#$]) y una longitud de entre 6 a 10 caracteres");
				response.setErrors(error);
				response.setCode("ER001");
				response.setStatus("fail");
			}

		} else {
			ErrorDTO error = new ErrorDTO();
			error.setErrorCode("ER000");
			error.setMessage(
					"Los campos user y password no pueden ser nulos o vacios");
			response.setErrors(error);
			response.setCode("ERR000");
			response.setStatus(result);
		}
		return response;
	}

}