package it.uniroma3.siw.interfacce;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Movie;

public interface movieService {

    public Movie newMovie(Movie movie, MultipartFile image, BindingResult bindingResult);

    public Movie findById(Long id);

    public void delete(Long id);

    public Movie update(Long id, Movie movie, MultipartFile image, BindingResult bindingResult);

    public Iterable<Movie> findAll();

    public Movie setDirector(Long directorId, Long movieId);

    public Integer getAverageStars(Long id);

    public Iterable<Movie> findAllByYear(int year);

    
    
}
