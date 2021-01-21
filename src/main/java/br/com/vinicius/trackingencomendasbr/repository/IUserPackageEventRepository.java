package br.com.vinicius.trackingencomendasbr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.vinicius.trackingencomendasbr.entity.UserPackageEventEntity;

@Repository
public interface IUserPackageEventRepository extends JpaRepository<UserPackageEventEntity, Integer>{

	@Query("select up from UserPackageEventEntity up where up.id = (select max(id) from UserPackageEventEntity where userPackage.id = :packageId)")
	Optional<UserPackageEventEntity> findLastByUserPackageId(@Param("packageId") Integer packageId);
	
	@Query("select up from UserPackageEventEntity up where up.userPackage.id = :packageId")
	List<UserPackageEventEntity> findByPackageId(@Param("packageId") Integer packageId);
}
