package it.uniroma3.siw.service;

import org.springframework.validation.BindingResult;

import it.uniroma3.siw.model.Review;

public interface ReviewService {

    public Review newReview(Review review, BindingResult bindingResult, Long movieId);

    public Review findById(Long id);

    public Iterable<Review> findAll();

    public Iterable<Review> findByMovieId(Long id);

    public Iterable<Review> findByCredentialsUsername(String username);

    public Iterable<Review> findByCredentialsId(Long id);

    public Review editReview (Long id, Review newReview,  BindingResult bindingResult);

    public void delete(Long id);
    
}
