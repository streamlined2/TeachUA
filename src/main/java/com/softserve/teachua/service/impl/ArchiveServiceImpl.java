package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.exception.JsonWriteException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.repository.ArchiveRepository;
import com.softserve.teachua.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {
    private static final String JSON_WRITE_EXCEPTION = "Model with name: %s can't be converted to json";

    private final ArchiveRepository archiveRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ArchiveServiceImpl(ArchiveRepository archiveRepository, ObjectMapper objectMapper) {
        this.archiveRepository = archiveRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Archive> findArchivesByClassName(String className) {
        log.info("**/finding list of Archives by class name = {}", className);
        return archiveRepository.findAllByClassName(className);
    }

    @Override
    public List<Archive> findAllArchives() {
        log.info("**/getting list of all Archives");
        return archiveRepository.findAll();
    }

    @Override
    public <T extends Archivable> T saveModel(T model) {
        Archive archive;
        try {
             archive = new Archive()
                    .withClassName(model.getClass().getName())
                    .withData(objectMapper.writeValueAsString(model));
        } catch (JsonProcessingException e) {
            throw new JsonWriteException(String.format(JSON_WRITE_EXCEPTION, model.getClass().getName()));
        }
        log.info("**/Model {} adding to archive", model.getClass().getName());
        archiveRepository.save(archive);
        return model;
    }
}
