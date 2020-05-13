package com.knightec.assignment.translation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    Optional<Translation> findByLanguageCodeAndFrom(String languageCode, String from);
}
