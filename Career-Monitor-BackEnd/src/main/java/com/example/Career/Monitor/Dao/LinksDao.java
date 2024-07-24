package com.example.Career.Monitor.Dao;

import com.example.Career.Monitor.model.Links;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinksDao extends JpaRepository<Links,Integer>{

}

