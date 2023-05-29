package it.uniroma3.siw.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import jakarta.validation.Valid;

@Controller
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewValidator reviewValidator;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    CredentialsRepository credentialsRepository;

    @GetMapping("/formNewReview/{id}")
    public String formNewReview(@PathVariable("id") Long id,Model model){
        model.addAttribute("review", new Review());
        model.addAttribute("movie", this.movieRepository.findById(id).get());
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //User user = (User) authentication.getPrincipal();

        return "formNewReview.html";
    }

    @PostMapping("/review/{id}")
	public String newReview(@Valid @ModelAttribute("review") Review review, BindingResult bindingResult,
    @PathVariable("id") Long mId, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        
		
        Movie movie = this.movieRepository.findById(mId).get();
        review.setMovie(movie);
        review.setCredentials(this.credentialsRepository.findByUsername(userDetails.getUsername()).get());
		this.reviewValidator.validate(review, bindingResult);
		if (!bindingResult.hasErrors()) {
           // this.reviewRepository.save(review);
            movie.getReviews().add(review);
            this.movieRepository.save(movie); 
			model.addAttribute("review", review);
			return "review.html";
		} else {
            model.addAttribute("movie", movie);
			return "formNewReview.html"; 
		}
	}

    @GetMapping("/review/{id}")
	public String getReview(@PathVariable("id") Long id, Model model) {
		model.addAttribute("review", this.reviewRepository.findById(id).get());
		return "review.html";
	}

    @GetMapping("/reviews")
    public String listReview(Model model){
        model.addAttribute("reviews", this.reviewRepository.findAll());
        return "reviews.html";
    }

    @GetMapping("/movieReviews/{id}")
    public String allMovieReviews(@PathVariable("id") Long id, Model model){
        model.addAttribute("reviews", this.reviewRepository.findByMovieId(id));
        return "reviews.html";
    }

    @GetMapping("/admin/deleteReview/{movieId}/{reviewId}")
    public String deleteReview(@PathVariable("movieId") Long movieId, @PathVariable("reviewId") Long reviewId, Model model){
        this.reviewRepository.deleteById(reviewId);
        model.addAttribute("movie", this.movieRepository.findById(movieId).get());
        return "movie.html";
    }
    



    
}
