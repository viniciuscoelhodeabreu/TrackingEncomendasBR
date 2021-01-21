package br.com.vinicius.trackingencomendasbr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(schema = "general", name = "user_package")
@Data
@RequiredArgsConstructor
public class UserPackageEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(
	        name = "userPackageSequenceGenerator",
	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	                @Parameter(name = "sequence_name", value = "USER_PACKAGE_SEQUENCE"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@GeneratedValue(generator = "userPackageSequenceGenerator")
	private Integer id;

	@Column(length = 60, nullable = false)
	private @NonNull String trackingCode;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private @NonNull UserEntity user;
	
	@ManyToOne
	@JoinColumn(name = "shipping_company_id")
	private @NonNull ShippingCompanyEntity shippingCompany;
	
	public UserPackageEntity() {
	}
}