package br.com.vinicius.trackingencomendasbr.service;

import br.com.vinicius.trackingencomendasbr.entity.UserEntity;

public interface IUserService {

	public UserEntity findByIdentification(String identification);
	
	public UserEntity save(UserEntity user);
}
