package com.softserve.teachua.service;

import com.softserve.teachua.converter.ClubToClubResponseConverter;
import com.softserve.teachua.converter.ContactsStringConverter;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.club.SuccessCreatedClub;
import com.softserve.teachua.dto.club.SuccessUpdatedClub;
import com.softserve.teachua.dto.contact_data.ContactDataResponse;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.dto.user.UserResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.IncorrectInputException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.LocationRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.impl.ClubServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ArchiveService archiveService;

    @Mock
    private ClubToClubResponseConverter clubToClubResponseConverter;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClubServiceImpl clubService;

    private Club club;
    private ClubProfile clubProfile;
    private ClubResponse clubResponse;
    private User user;

    private final long EXISTING_ID = 1L;
    private final long NOT_EXISTING_ID = 500L;

    private final String EXISTING_NAME = "Existing Club Name";
    private final String NOT_EXISTING_NAME = "Not Existing Club Name";
    private final String NEW_NAME = "New Club Name";

    private final String CONTACTS = "123456789";
    private final String DESCRIPTION = "Description Text";
    private final List<String> CATEGORIES = Arrays.asList("Category1", "Category2");

    private final Long USER_EXISTING_ID = 1L;
    private final Long USER_NOT_EXISTING_ID = 100L;
    private final String USER_EXISTING_NAME = "User Existing Name";
    private final String USER_EXISTING_LASTNAME = "User Existing LastName";


    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(USER_EXISTING_ID)
                .firstName(USER_EXISTING_NAME)
                .lastName(USER_EXISTING_LASTNAME)
                .build();
        club = Club.builder()
                .id(EXISTING_ID)
                .name(EXISTING_NAME)
                .user(user)
                .build();
        clubProfile = ClubProfile.builder()
                .name(NEW_NAME)
                .description(DESCRIPTION)
                .contacts(CONTACTS)
                .categoriesName(CATEGORIES)
                .build();
        clubResponse = ClubResponse.builder()
                .id(EXISTING_ID)
                .name(EXISTING_NAME)
                .build();
    }

    @Test
    void getClubProfileByExistingId() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));
        when(clubToClubResponseConverter.convertToClubResponse(club)).thenReturn(clubResponse);

        ClubResponse actual = clubService.getClubProfileById(EXISTING_ID);
        assertEquals(club.getName(), actual.getName());
    }

    @Test
    void getClubProfileByNotExistingId() {
        when(clubRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            clubService.getClubProfileById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void getClubByExistingId() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));

        Club actual = clubService.getClubById(EXISTING_ID);
        assertEquals(club, actual);
    }

    @Test
    void getClubByNotExistingId() {
        when(clubRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            clubService.getClubById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void getClubByName() {
        when(clubRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(club));

        Club actual = clubService.getClubByName(EXISTING_NAME);
        assertEquals(club, actual);
    }

    @Test
    void getClubByNotExistingName() {
        when(clubRepository.findByName(NOT_EXISTING_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            clubService.getClubByName(NOT_EXISTING_NAME);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void updateClubByExistingId() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));
        when(clubRepository.save(any())).thenReturn(club);
        when(dtoConverter.convertToEntity(ClubResponse.builder().name(NEW_NAME).build(), club))
                .thenReturn(Club.builder().name(NEW_NAME).build());
        when(dtoConverter.convertToDto(club, SuccessUpdatedClub.class))
                .thenReturn(SuccessUpdatedClub.builder().name(NEW_NAME).build());

        SuccessUpdatedClub actual = clubService.updateClub(EXISTING_ID, ClubResponse.builder().name(NEW_NAME).build());
        assertEquals(clubProfile.getName(), actual.getName());
    }

    @Test
    void updateClubByNotExistingId() {
        when(clubRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            clubService.updateClub(NOT_EXISTING_ID, ClubResponse.builder().name(NEW_NAME).build());
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void getClubProfileByExistingName() {
        when(clubRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(club));
        when(clubToClubResponseConverter.convertToClubResponse(club)).thenReturn(clubResponse);

        ClubResponse actual = clubService.getClubProfileByName(EXISTING_NAME);
        assertEquals(club.getName(), actual.getName());
    }

    @Test
    void getClubProfileByNotExistingName() {
        when(clubRepository.findByName(NOT_EXISTING_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            clubService.getClubProfileByName(NOT_EXISTING_NAME);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void deleteClubByExistingId() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));
        when(archiveService.saveModel(club)).thenReturn(club);
        doNothing().when(clubRepository).deleteById(EXISTING_ID);
        doNothing().when(clubRepository).flush();
        when(dtoConverter.convertToDto(club, ClubResponse.class)).thenReturn(clubResponse);
        when(clubToClubResponseConverter.convertToClubResponse(club)).thenReturn(clubResponse);
        when(dtoConverter.convertToEntity(clubResponse, club)).thenReturn(club);

        ClubResponse actual = clubService.deleteClubById(EXISTING_ID);
        assertEquals(club.getName(), actual.getName());
    }

    @Test
    void deleteClubByNotExistingId() {
        when(clubRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            clubService.deleteClubById(NOT_EXISTING_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void addClub() {
        Club newClub = Club.builder().name(NEW_NAME).build();

        when(clubRepository.existsByName(NEW_NAME)).thenReturn(false);
        when(dtoConverter.convertToEntity(clubProfile, new Club())).thenReturn(newClub);
        when(clubRepository.save(any())).thenReturn(newClub);
        when(dtoConverter.convertToDto(newClub, SuccessCreatedClub.class))
                .thenReturn(SuccessCreatedClub.builder().name(NEW_NAME).build());

        SuccessCreatedClub actual = clubService.addClub(clubProfile);
        assertEquals(clubProfile.getName(), actual.getName());
    }

    @Test
    void addClubIfExist() {
        when(clubRepository.existsByName(NEW_NAME)).thenReturn(true);

        assertThatThrownBy(() -> {
            clubService.addClub(clubProfile);
        }).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void addClubWithEmptyData() {
        assertThatThrownBy(() -> {
            clubService.addClub(ClubProfile.builder().build());
        }).isInstanceOf(IncorrectInputException.class);
    }

    @Test
    void getListOfClubs() {
        when(clubToClubResponseConverter.convertToClubResponse(club)).thenReturn(clubResponse);
        when(clubRepository.findAll()).thenReturn(Collections.singletonList(club));

        List<ClubResponse> actual = clubService.getListOfClubs();
        assertEquals(clubResponse.getName(), actual.get(0).getName());
    }

    @Test
    void getListClubsByExistingUserId() {
        when(clubToClubResponseConverter.convertToClubResponse(club)).thenReturn(clubResponse);
        when(clubRepository.findAllByUserId(USER_EXISTING_ID)).thenReturn(Collections.singletonList(club));

        List<ClubResponse> actual = clubService.getListClubsByUserId(USER_EXISTING_ID);
        assertEquals(clubResponse.getName(), actual.get(0).getName());
        assertEquals(clubResponse.getUserId(), actual.get(0).getUserId());
    }

//    @Test
//    void getSimilarClubsByCategoryName() {
//        Page<Club> clubs = new PageImpl<>(Arrays.asList(club4, club5));
//
//        when(clubRepository.findTop2ByCategoryName(1L, "Спортивні секції", "Lviv", PageRequest.of(0, 2)))
//                .thenReturn(clubs);
//        for (Club c : clubs
//        ) {
//            when(dtoConverter.convertToDto(c, ClubResponse.class)).thenReturn(ClubResponse.builder().name(c.getName()).build());
//        }
//
//        List<ClubResponse> actual = clubService.getSimilarClubsByCategoryName(new SimilarClubProfile(1L, "Спортивні секції", "Lviv"));
//        assertEquals(clubs.getSize(), actual.size());
//    }
//
//    @Test
//    void getClubsBySearchParameters() {
//        SearchClubProfile searchClubProfile = new SearchClubProfile("", "Lviv", "", "", "Спортивні секції");
//        Page<Club> clubs = new PageImpl<>(Arrays.asList(club4, club5));
//        Pageable pageable = PageRequest.of(0, 2);
//        when(clubRepository.findAllByParameters(searchClubProfile.getClubName(),
//                searchClubProfile.getCityName(),
//                searchClubProfile.getDistrictName(),
//                searchClubProfile.getStationName(),
//                searchClubProfile.getCategoryName(),
//                pageable)).thenReturn(clubs);
//        for (Club c : clubs
//        ) {
//            when(dtoConverter.convertToDto(c, ClubResponse.class)).thenReturn(ClubResponse.builder().name(c.getName()).build());
//        }
//
//        Page<ClubResponse> actual = clubService.getClubsBySearchParameters(searchClubProfile, pageable);
//
//        assertEquals(clubs.getSize(), actual.getSize());
//        assertEquals(clubs.getContent().get(0).getName(), actual.getContent().get(0).getName());
//        assertEquals(clubs.getContent().get(1).getName(), actual.getContent().get(1).getName());
//    }
//
//    @Test
//    void getPossibleClubByName() {
//        Page<Club> clubs = new PageImpl<>(Arrays.asList(club4, club5, club6));
//
//        when(clubRepository.findTop3ByName("Кл", "Lviv", PageRequest.of(0, 3))).thenReturn(clubs);
//        when(dtoConverter.convertToDto(club4, SearchPossibleResponse.class)).thenReturn(SearchPossibleResponse.builder().name(club4.getName()).build());
//        when(dtoConverter.convertToDto(club5, SearchPossibleResponse.class)).thenReturn(SearchPossibleResponse.builder().name(club5.getName()).build());
//        when(dtoConverter.convertToDto(club6, SearchPossibleResponse.class)).thenReturn(SearchPossibleResponse.builder().name(club6.getName()).build());
//
//        List<SearchPossibleResponse> actual = clubService.getPossibleClubByName("Кл", "Lviv");
//        assertEquals(club4.getName(), actual.get(0).getName());
//    }

}
