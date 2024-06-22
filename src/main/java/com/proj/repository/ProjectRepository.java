package com.proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proj.entities.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{

}
