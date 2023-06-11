package it.uniroma3.siw.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private ReviewValidator reviewValidator;

    @Transactional
    @Override
    public Review newReview(Review review, BindingResult bindingResult, Long movieId) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        
		
        Movie movie = this.movieRepository.findById(movieId).get();
        review.setMovie(movie);
        review.setCredentials(this.credentialsRepository.findByUsername(userDetails.getUsername()).get());
		this.reviewValidator.validate(review, bindingResult);
		if (!bindingResult.hasErrors()) {
           // this.reviewRepository.save(review);
            movie.getReviews().add(review);
            this.movieRepository.save(movie); 
        }
        return review;
    }

    @Transactional
    @Override
    public Review findById(Long id) {
        return this.reviewRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Iterable<Review> findAll() {
        return this.reviewRepository.findAll();
    }

    @Transactional
    @Override
    public Iterable<Review> findByMovieId(Long id) {
        return this.findByMovieId(id);
    }

    @Transactional
    @Override
    public Iterable<Review> findByCredentialsUsername(String username) {
        Credentials credentials = this.credentialsRepository.findByUsername(username).get();
        return this.reviewRepository.findReviewByCredentialsId(credentials.getId());
    }

    @Transactional
    @Override
    public Review editReview(Long id, Review newReview, BindingResult bindingResult) {

        Review review=this.reviewRepository.findById(id).get();
		
		this.reviewValidator.validate(newReview, bindingResult);
		if (!bindingResult.hasErrors()) {
			review.setTitle(newReview.getTitle());
			review.setBodyText(newReview.getBodyText());
			review.setStars(newReview.getStars());
			//inserimento immagini
			this.reviewRepository.save(review); 
        }
        return review;
    }

    @Transactional
    @Override
    public Iterable<Review> findByCredentialsId(Long id) {
        return this.reviewRepository.findReviewByCredentialsId(id);
    }

    @Override
    public void delete(Long id) {
        this.reviewRepository.deleteById(id);
    }
    
}
