package com.biengual.userapi.content.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<ContentEntity, Long>, ContentRepositoryCustom {
}
