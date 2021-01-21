package br.com.vinicius.trackingencomendasbr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.vinicius.trackingencomendasbr.entity.UserMessageEntity;

@Repository
public interface IUserMessageRepository extends JpaRepository<UserMessageEntity, Integer>{
	
	Optional<UserMessageEntity> findByIdentification(String identification);
	
}
