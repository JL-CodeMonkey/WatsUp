package com.example.spring_boot_blog_application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_boot_blog_application.models.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
