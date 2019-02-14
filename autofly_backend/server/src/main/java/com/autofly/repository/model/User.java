package com.autofly.repository.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class User {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String emailId;
	private String password;
	private String userType;
}
