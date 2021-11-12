package com.my.hmc.repository;

import com.my.hmc.domain.Language;
import com.my.hmc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    @Query("select l.name from Language l where l.user.id = :userId")
    List<String> findByUserId(Long userId);

    List<Language> findAllByName(String name);
}
