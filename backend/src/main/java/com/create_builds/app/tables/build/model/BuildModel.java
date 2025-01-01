package com.create_builds.app.tables.build.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="build")
public class BuildModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="user_id")
	private Integer user_id;
	@Column(name="title")
	private String title;
	@Column(name="description")
	private String description;
	@Column(name="img_links")
	private String[] img_links;
	@Column(name="nbt")
	private String nbt;
	@Column(name="upvotes")
	private Integer upvotes;
}
