package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.tests.MovieFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {

    @InjectMocks
    private MovieService service;

    @Mock
    private MovieRepository repository;

    private long existingMovieId, nonExistingMovieId;
    private String title;
    private MovieEntity movie;
    private PageImpl<MovieEntity> page;
    private MovieDTO movieDTO;

    @BeforeEach
    void setUp() throws Exception {

        title = "Test Movie";
        movie = MovieFactory.createMovieEntity();
        page = new PageImpl<>(List.of(movie));
        existingMovieId = 1L;
        nonExistingMovieId = 2L;

        Mockito.when(repository.searchByTitle(any(), (Pageable) any())).thenReturn(page);

        Mockito.when(repository.findById(existingMovieId)).thenReturn(Optional.of(movie));

    }

    @Test
    void findAllShouldReturnPagedMovieDTO() {

        Pageable pageable = PageRequest.of(0, 12);

        Page<MovieDTO> result = service.findAll(title, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getSize(), 1);
        Assertions.assertEquals(result.iterator().next().getTitle(), title);

    }

    @Test
    void findByIdShouldReturnMovieDTOWhenIdExists() {

        MovieDTO result = service.findById(existingMovieId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingMovieId);
        Assertions.assertEquals(result.getTitle(), movie.getTitle());


    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    }

    @Test
    void insertShouldReturnMovieDTO() {
    }

    @Test
    void updateShouldReturnMovieDTOWhenIdExists() {
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependentId() {
    }
}
