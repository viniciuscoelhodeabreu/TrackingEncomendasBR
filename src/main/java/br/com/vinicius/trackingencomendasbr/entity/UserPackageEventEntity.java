package br.com.vinicius.trackingencomendasbr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(schema = "general", name = "user_package_event")
@Data
@RequiredArgsConstructor
public class UserPackageEventEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(
	        name = "userPackageEventSequenceGenerator",
	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	                @Parameter(name = "sequence_name", value = "USER_PACKAGE_EVENT_SEQUENCE"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@GeneratedValue(generator = "userPackageEventSequenceGenerator")
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	private @NonNull Date time;
	
	private @NonNull String last;
	
	@ManyToOne
	@JoinColumn(name = "user_package_id")
	private @NonNull UserPackageEntity userPackage;
	
	
	public UserPackageEventEntity() {
	}
}
