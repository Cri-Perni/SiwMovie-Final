package it.uniroma3.siw.service;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.interfacce.movieService;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class movieServiceImpl implements movieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private MovieValidator movieValidator;

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional

    public Movie newMovie(Movie movie, MultipartFile image, BindingResult bindingResult) {
        this.movieValidator.validate(movie, bindingResult);
        if (bindingResult.hasErrors()) {
            try {
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                movie.setImage(base64Image);
            } catch (IOException e) {
            }
            this.movieRepository.save(movie);
        }
        return movie;
    }

    @Transactional
    @Override
    public Movie findById(Long id) {
        return this.movieRepository.findById(id).get();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Movie movie = this.movieRepository.findById(id).get();

        for (Artist actor : movie.getActors()) {
            actor.getActorOf().remove(movie);
        }

        this.movieRepository.delete(movie);
    }

    @Transactional
    @Override
    public Movie update(Long id, Movie newMovie, MultipartFile image, BindingResult bindingResult) {

        Movie movie = this.movieRepository.findById(id).get();

        this.movieValidator.validate(newMovie, bindingResult);
        if (!bindingResult.hasFieldErrors()) {
            movie.setTitle(newMovie.getTitle());
            movie.setYear(newMovie.getYear());
            // inserimento immagini
            try {
                if (image.getBytes().length != 0) {
                    String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                    movie.setImage(base64Image);
                } else {
                    movie.setImage(movie.getImage());
                }
            } catch (IOException e) {
            }
            this.movieRepository.save(movie);

        }
        return movie;
    }

    @Transactional
    @Override
    public Iterable<Movie> findAll() {
        return this.movieRepository.findAll();
    }

    @Override
    public Movie setDirector(Long directorId, Long movieId) {
        Artist director = this.artistRepository.findById(directorId).get();
		Movie movie = this.movieRepository.findById(movieId).get();
		movie.setDirector(director);
		this.movieRepository.save(movie);
        return movie;
    }

    @Transactional
    @Override
    public Integer getAverageStars(Long id) {
        return this.reviewRepository.getAverageStarsByMovieId(id);
    }

    @Transactional
    @Override
    public Iterable<Movie> findAllByYear(int year) {
        return this.movieRepository.findByYear(year);
    }

    private List<Artist> actorsToAdd(Long movieId) {
		List<Artist> actorsToAdd = new ArrayList<>();

		for (Artist a : artistRepository.findActorsNotInMovie(movieId)) {
			actorsToAdd.add(a);
		}
		return actorsToAdd;
	}

}
