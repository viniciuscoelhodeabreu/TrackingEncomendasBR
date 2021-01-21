package br.com.vinicius.trackingencomendasbr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.vinicius.trackingencomendasbr.entity.ShippingCompanyEntity;

@Repository
public interface IShippingCompanyRepository extends JpaRepository<ShippingCompanyEntity, Integer> {

}
