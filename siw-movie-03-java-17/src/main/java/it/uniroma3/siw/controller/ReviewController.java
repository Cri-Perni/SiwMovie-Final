package it.uniroma3.siw.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import jakarta.validation.Valid;



@Controller
public class ReviewController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/user/formNewReview/{id}")
    public String formNewReview(@PathVariable("id") Long id,Model model){
        model.addAttribute("review", new Review());
        model.addAttribute("movie", this.movieService.findById(id));
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //User user = (User) authentication.getPrincipal();

        return "/user/formNewReview.html";
    }

    @PostMapping("/user/addReview/{id}")
	public String newReview(@Valid @ModelAttribute("review") Review review, BindingResult bindingResult,
    @PathVariable("id") Long mId, Model model) {

        Review newReview = this.reviewService.newReview(review, bindingResult, mId);

        if(newReview != null){
			model.addAttribute("review", newReview);
			return "review.html";
		} else {
            model.addAttribute("movie", this.movieService.findById(mId));
			return "/user/formNewReview.html"; 
		}
	}

    @GetMapping("/review/{id}")
	public String getReview(@PathVariable("id") Long id, Model model) {
		model.addAttribute("review", this.reviewService.findById(id));
		return "review.html";
	}

    @GetMapping("/reviews")
    public String listReview(Model model){
        model.addAttribute("reviews", this.reviewService.findAll());
        return "reviews.html";
    }

    @GetMapping("/movieReviews/{id}")
    public String allMovieReviews(@PathVariable("id") Long id, Model model){
        model.addAttribute("reviews", this.reviewService.findByMovieId(id));
        return "reviews.html";
    }

    @GetMapping("/admin/deleteReview/{movieId}/{reviewId}")
    public String deleteReview(@PathVariable("movieId") Long movieId, @PathVariable("reviewId") Long reviewId, Model model){
        this.reviewService.delete(reviewId);
        model.addAttribute("movie", this.movieService.findById(movieId));
        return "movie.html";
    }

    @GetMapping("/user/userReviews/{userName}")
    public String getUserReviews(@PathVariable("userName") String username, Model model) {
        model.addAttribute("reviews", this.reviewService.findByCredentialsUsername(username));
        return "/user/userReviews.html";
    }

    @GetMapping("/user/formEditReview/{reviewId}")
    public String formEditReview(@PathVariable("reviewId") Long id, Model model){
        model.addAttribute("review", this.reviewService.findById(id));
        return "/user/editReview.html";
    }

    @PostMapping("user/editReview/{reviewId}")
	public String editReview(@PathVariable("reviewId") Long id,@ModelAttribute Review newReview,  BindingResult bindingResult, Model model){

        Review review = this.reviewService.editReview(id, newReview, bindingResult);
        if(review != null){
			model.addAttribute("reviews", this.reviewService.findByCredentialsId(review.getCredentials().getId()));
			return "/user/userReviews.html";
		} else {
			model.addAttribute("review", this.reviewService.findById(id));
			return "/user/editReview.html"; 
		}

	}


    
}
