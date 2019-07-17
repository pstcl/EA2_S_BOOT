package org.pstcl.ea.model.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the circle_master database table.
 * 
 */
@Entity
@Table(name="circle_master")
@NamedQuery(name="CircleMaster.findAll", query="SELECT c FROM CircleMaster c")
public class CircleMaster implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer crCode;
	private String circleName;
	public CircleMaster() {
	}


	@Id
	@Column(name="CR_CODE", unique=true, nullable=false)
	public Integer getCrCode() {
		return this.crCode;
	}

	public void setCrCode(Integer crCode) {
		this.crCode = crCode;
	}


	@Column(length=45)
	public String getCircleName() {
		return this.circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}



}