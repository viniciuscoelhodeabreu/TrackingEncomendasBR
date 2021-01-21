package br.com.vinicius.trackingencomendasbr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.vinicius.trackingencomendasbr.entity.UserEntity;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Integer>{

	Optional<UserEntity> findByIdentification(String identification);
}
