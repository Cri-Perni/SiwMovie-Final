package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;

public interface ReviewRepository extends CrudRepository<Review,Long>{

    public Boolean  existsByCredentialsAndMovie(Credentials credentials,Movie movie);
    public Iterable<Review> findByMovieId(Long id);
    
}
