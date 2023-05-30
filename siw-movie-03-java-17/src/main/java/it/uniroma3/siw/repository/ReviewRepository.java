package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;

public interface ReviewRepository extends CrudRepository<Review,Long>{

    public Boolean  existsByCredentialsAndMovie(Credentials credentials,Movie movie);
    public Iterable<Review> findByMovieId(Long id);

    @Query("SELECT AVG(r.stars) FROM Review r WHERE r.movie.id = :movieId")
    public Integer getAverageStarsByMovieId(@Param("movieId") Long movieId);

    public List<Review> findReviewByCredentialsId(Long id);
    
}
