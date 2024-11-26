package com.demos.graphql.resolver;

import com.demos.graphql.dto.MovieDTO;
import com.demos.graphql.model.Movie;
import com.demos.graphql.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class MovieResolver {

  private final MovieRepository movieRepository;

  public MovieResolver(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  @QueryMapping
  public List<Movie> getAllMovies() {
    return movieRepository.findAll();
  }

  @QueryMapping
  public Movie getMovie(@Argument Long id) {
    return movieRepository.findById(id).orElse(null);
  }

  @MutationMapping
  public Movie createMovie(@Argument MovieDTO movieDTO) {
    Movie movie = Movie.builder()
      .title(movieDTO.getTitle())
      .director(movieDTO.getDirector())
      .releaseYear(movieDTO.getReleaseYear())
      .build();
    System.out.println("Saving movie " + movieDTO.getTitle());
    return movieRepository.save(movie);
  }

  @MutationMapping
  public Movie updateMovie(@Argument MovieDTO movieDTO) {
    Movie originalData = null;
    Optional<Movie> originalOptData = movieRepository.findById(movieDTO.getId());
    if(originalOptData.isEmpty()) {
      throw new EntityNotFoundException("La película con id " + movieDTO.getId() + " no existe.");
    } else {
      originalData = originalOptData.get();
    }

    Movie movie = Movie.builder()
      .id(movieDTO.getId())
      .title(Objects.nonNull(movieDTO.getTitle()) ? movieDTO.getTitle() : originalData.getTitle())
      .director(Objects.nonNull(movieDTO.getDirector()) ? movieDTO.getDirector() : originalData.getDirector())
      .releaseYear(movieDTO.getReleaseYear() > 1800 ? movieDTO.getReleaseYear() : originalData.getReleaseYear())
      .build();
    return movieRepository.save(movie);
  }

  @MutationMapping
  public boolean deleteMovie(@Argument Long id) {
    if(movieRepository.existsById(id)) {
      movieRepository.deleteById(id);
      return true;
    } else {
      throw new EntityNotFoundException("La película con id " + id + " no existe.");
    }
  }

}
