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
@Table(schema = "general", name="user")
@Data
@RequiredArgsConstructor
public class UserEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(
	        name = "userSequenceGenerator",
	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	                @Parameter(name = "sequence_name", value = "USER_SEQUENCE"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@GeneratedValue(generator = "userSequenceGenerator")
	private Integer id;

	@Column(nullable = false, unique = true)
	private @NonNull String identification;
	
	@ManyToOne
	@JoinColumn(name = "social_media_id")
	private @NonNull SocialMediaEntity socialMedia;

	public UserEntity() {
	}
	
	
}