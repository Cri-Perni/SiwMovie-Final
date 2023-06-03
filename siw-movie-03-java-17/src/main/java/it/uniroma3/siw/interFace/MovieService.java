package it.uniroma3.siw.interFace;

import java.io.IOException;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Movie;

public interface MovieService {

    public Movie newMovie(Movie movie, MultipartFile image, BindingResult bindingResult) throws IOException;

    public Movie findById(Long id);

    public void delete(Long id);

    public Movie update(Long id, Movie movie, MultipartFile image, BindingResult bindingResult) throws IOException;;

    public Iterable<Movie> findAll();

    public Movie setDirector(Long directorId, Long movieId);

    public Integer getAverageStars(Long id);

    public Iterable<Movie> findAllByYear(int year);

    public void save (Movie movie);

    public Movie removeActor(Long actorId, Long movieId);

    public Movie addActor(Long actorId, Long movieId);



    
    
}
