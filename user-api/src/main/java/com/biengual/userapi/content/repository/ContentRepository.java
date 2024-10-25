package com.biengual.userapi.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.userapi.content.domain.ContentEntity;
import com.biengual.userapi.content.repository.custom.ContentRepositoryCustom;

public interface ContentRepository extends JpaRepository<ContentEntity, Long>, ContentRepositoryCustom {

}
