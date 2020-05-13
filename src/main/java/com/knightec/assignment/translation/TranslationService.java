package com.knightec.assignment.translation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TranslationService {

    @Autowired
    TranslationRepository translatorRepository;

    public Optional<Translation> getTranslation(String languageCode, String from){
      languageCode = languageCode.toLowerCase();
        return translatorRepository.findByLanguageCodeAndFrom(languageCode,from);
    }

    public void addTranslation(String languageCode,String from, String to) {
       languageCode = languageCode.toLowerCase();
        Optional<Translation> result = translatorRepository.findByLanguageCodeAndFrom(languageCode,from);
        if(result.isPresent()){
            throw new IllegalArgumentException("The combination of language code " + languageCode + " and from " + from + " already exists");
        } else {
            Translation translation = new Translation(languageCode,from,to);
            translatorRepository.save(translation);
        }
    }
}
