package it.uniroma3.siw.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import it.uniroma3.siw.interfacce.MovieService;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.validation.Valid;

@Controller
public class MovieController {
	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private MovieService movieService;

	@Autowired
	private ArtistRepository artistRepository;

	@GetMapping(value = "/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	@GetMapping(value = "/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value = "/admin/indexMovie")
	public String indexMovie() {
		return "admin/indexMovie.html";
	}

	@GetMapping(value = "/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAll());
		return "admin/manageMovies.html";
	}

	@GetMapping(value = "/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId,
			Model model) {

		/*Artist director = this.artistRepository.findById(directorId).get();
		Movie movie = this.movieRepository.findById(movieId).get();
		movie.setDirector(director);
		this.movieRepository.save(movie);*/

		model.addAttribute("movie", this.movieService.setDirector(directorId, movieId));
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value = "/admin/addDirector/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", artistRepository.findAll());
		model.addAttribute("movie", this.movieService.findById(id));
		return "admin/directorsToAdd.html";
	}

	@PostMapping("/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, @RequestParam("imageMovie") MultipartFile image,
			BindingResult bindingResult, Model model) {

		/*this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			try {
				String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
				movie.setImage(base64Image);
			} catch (IOException e) {
			}
			this.movieRepository.save(movie);
			model.addAttribute("movie", movie);
			return "movie.html";
		} else {
			return "admin/formNewMovie.html";
		}*/

		Movie newMovie = this.movieService.newMovie(movie, image, bindingResult);

		if (newMovie != null) {
			// prova salvtaggio immagine
			model.addAttribute("movie", movie);
			return "movie.html";
		} else {
			return "admin/formNewMovie.html";
		}
	}

	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		model.addAttribute("stars",this.movieService.getAverageStars(id));
		return "movie.html";
	}

	@GetMapping("/movie")
	public String getMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAll());
		return "movies.html";
	}

	@GetMapping("/formSearchMovies")
	public String formSearchMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAll());
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) {
		model.addAttribute("movies",this.movieService.findAllByYear(year));
		return "foundMovies.html";
	}

	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		//List<Artist> actorsToAdd = this.actorsToAdd(id);
		model.addAttribute("actorsToAdd", this.artistRepository.findActorsNotInMovie(id));
		model.addAttribute("movie", this.movieService.findById(id));

		return "admin/actorsToAdd.html";
	}

	@GetMapping(value = "/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId,
			Model model) {
		/*Movie movie = this.movieRepository.findById(movieId).get();
		Artist actor = this.artistRepository.findById(actorId).get();
		Set<Artist> actors = movie.getActors();
		actors.add(actor);
		this.movieRepository.save(movie);*/


		model.addAttribute("movie", this.movieService.addActor(actorId, movieId));
		model.addAttribute("actorsToAdd", this.artistRepository.findActorsNotInMovie(movieId));

		return "admin/actorsToAdd.html";
	}

	@GetMapping(value = "/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId,
			Model model) {
		/*Movie movie = this.movieRepository.findById(movieId).get();
		Artist actor = this.artistRepository.findById(actorId).get();
		Set<Artist> actors = movie.getActors();
		actors.remove(actor);
		this.movieRepository.save(movie);*/

		

		model.addAttribute("movie", this.movieService.removeActor(actorId, movieId));
		model.addAttribute("actorsToAdd", this.artistRepository.findActorsNotInMovie(movieId));

		return "admin/actorsToAdd.html";
	}

	@GetMapping("/admin/formChangeMovie/{movieId}")
	public String formChangeMovie(@PathVariable("movieId") Long id, Model model) {
		model.addAttribute("movie", this.movieRepository.findById(id).get());
		return "/admin/updateMovie.html";
	}

	@PostMapping("/admin/changeMovie/{movieId}")
	public String changeMovie(@PathVariable("movieId") Long id, @Valid @ModelAttribute Movie newMovie,
			@RequestParam("imageMovie") MultipartFile image, BindingResult bindingResult, Model model) {

		/*Movie movie = this.movieRepository.findById(id).get();

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
			model.addAttribute("movie", movie);
			return "/admin/formUpdateMovie.html";
		} else {
			model.addAttribute("movie", this.movieRepository.findById(id).get());
			return "/admin/updateMovie.html";
		}*/

		Movie movie = this.movieService.update(id,  newMovie, image, bindingResult);

		if (movie != newMovie && movie!=null) {

			model.addAttribute("movie", movie);
			return "/admin/formUpdateMovie.html";
		} else {
			model.addAttribute("movie", this.movieService.findById(id));
			return "/admin/updateMovie.html";
		}

	}

	@GetMapping("/admin/removeMovie/{movieId}")
	public String removeMovie(@PathVariable("movieId") Long id, Model model) {

		/*Movie movie = this.movieRepository.findById(id).get();

		for (Artist actor : movie.getActors()) {
			actor.getActorOf().remove(movie);
		}

		this.movieRepository.delete(movie);*/
		this.movieService.delete(id);

		model.addAttribute("movies",this.movieService.findAll());
		return "/admin/manageMovies.html";
	}

}
