package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {

    @InjectMocks
    private ScoreService scoreService;

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomUserUtil customUserUtil;

    @Mock
    private MovieRepository movieRepository;

    private long existingMovieId, nonExistingMovieId;
    private ScoreEntity scoreEntity;
    private MovieEntity movieEntity;
    private MovieDTO movieDTO;
    private ScoreDTO scoreDTO;
    private String existingUsername, nonExistingUsername;
    private UserEntity user;
    private List<UserDetailsProjection> userDetails;

    @BeforeEach
    void setUp() throws Exception {
        existingMovieId = 1L;
        nonExistingMovieId = 2L;

        movieEntity = MovieFactory.createMovieEntity();
        movieDTO = new MovieDTO(movieEntity);

        scoreDTO = ScoreFactory.createScoreDTO();

        userDetails = UserDetailsFactory.createCustomAdminUser(existingUsername);

        user = UserFactory.createUserEntity();

        Mockito.when(userRepository.searchUserAndRolesByUsername(existingUsername)).thenReturn(userDetails);
        Mockito.when(userRepository.searchUserAndRolesByUsername(nonExistingUsername)).thenReturn(new ArrayList<>());

        Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movieEntity));
        Mockito.when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

        Mockito.when(movieRepository.save(any())).thenReturn(movieEntity);

        Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(scoreEntity);

    }

    @Test
    public void saveScoreShouldReturnMovieDTO() {

        Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(existingUsername);

        MovieDTO resultScore = scoreService.saveScore(scoreDTO);

        Assertions.assertNotNull(resultScore);
        Assertions.assertEquals(resultScore.getId(), existingMovieId);
    }

    @Test
    public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

        Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(existingUsername);

        movieEntity.setId(nonExistingMovieId);
        movieDTO = new MovieDTO(nonExistingMovieId, "", 0.0, 0, "");

        scoreDTO = new ScoreDTO(movieDTO.getId(), 0.0);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            MovieDTO result = scoreService.saveScore(scoreDTO);
        });

    }
}
