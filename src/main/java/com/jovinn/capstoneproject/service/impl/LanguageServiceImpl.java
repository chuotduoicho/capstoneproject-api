package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Language;
import com.jovinn.capstoneproject.repository.LanguageRepository;
import com.jovinn.capstoneproject.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl implements LanguageService {
    @Autowired
    private LanguageRepository languageRepository;
    @Override
    public Language saveLanguage(Language language) {
        return languageRepository.save(language);
    }
}
