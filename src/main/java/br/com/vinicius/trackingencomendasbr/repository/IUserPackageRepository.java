package br.com.vinicius.trackingencomendasbr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.vinicius.trackingencomendasbr.entity.UserPackageEntity;

@Repository
public interface IUserPackageRepository extends JpaRepository<UserPackageEntity, Integer> {

	Optional<UserPackageEntity> findByTrackingCodeAndUserId(String trackingCode, Integer userId);
	
}
