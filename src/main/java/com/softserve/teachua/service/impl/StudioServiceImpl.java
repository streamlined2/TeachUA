package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.controller.StudioResponse;
import com.softserve.teachua.dto.controller.SuccessCreatedStudio;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Studio;
import com.softserve.teachua.repository.StudioRepository;
import com.softserve.teachua.service.StudioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudioServiceImpl implements StudioService {
    private static final String STUDIO_ALREADY_EXIST = "Studio already exist with name: %s";
    private static final String STUDIO_NOT_FOUND_BY_ID = "Studio not found by id: %s";
    private static final String STUDIO_NOT_FOUND_BY_NAME = "Studio not found by name: %s";

    private final StudioRepository studioRepository;

    @Autowired
    public StudioServiceImpl(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    @Override
    public StudioResponse getStudioByProfileId(Long id) {
        Studio studio = getStudioById(id);
        return StudioResponse.builder()
                .id(studio.getId())
                .name(studio.getName())
                .build();
    }

    @Override
    public Studio getStudioById(Long id) {
        if (!isStudioExistById(id)) {
            String studioNotFoundById = String.format(STUDIO_NOT_FOUND_BY_ID, id);
            log.error(studioNotFoundById);
            throw new NotExistException(studioNotFoundById);
        }

        Studio studio = studioRepository.getById(id);
        log.info("**/getting studio by id = " + studio);
        return studio;
    }

    @Override
    public Studio getStudioByName(String name) {
        if (!isStudioExistByName(name)) {
            String studioNotFoundById = String.format(STUDIO_NOT_FOUND_BY_NAME, name);
            log.error(studioNotFoundById);
            throw new NotExistException(studioNotFoundById);
        }

        Studio studio = studioRepository.findByName(name);
        log.info("**/getting studio by name = " + name);
        return studio;
    }

    @Override
    public SuccessCreatedStudio addStudio(String name) {
        if (isStudioExistByName(name)) {
            String studioAlreadyExist = String.format(STUDIO_ALREADY_EXIST, name);
            log.error(studioAlreadyExist);
            throw new AlreadyExistException(studioAlreadyExist);
        }

        Studio studio = studioRepository.save(Studio
                .builder()
                .name(name)
                .build());

        log.info("**/adding new studio = " + studio);
        return SuccessCreatedStudio.builder()
                .id(studio.getId())
                .name(studio.getName())
                .build();
    }

    @Override
    public List<StudioResponse> getListOfStudios() {
        List<StudioResponse> studioResponses = studioRepository.findAll()
                .stream()
                .map(studio -> new StudioResponse(studio.getId(), studio.getName()))
                .collect(Collectors.toList());

        log.info("**/getting list of studios = " + studioResponses);
        return studioResponses;
    }

    private boolean isStudioExistById(Long id) {
        return studioRepository.existsById(id);
    }

    private boolean isStudioExistByName(String name) {
        return studioRepository.existsByName(name);
    }
}
