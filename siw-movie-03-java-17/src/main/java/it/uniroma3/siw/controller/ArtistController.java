package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.Base64;

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

import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import jakarta.validation.Valid;

@Controller
public class ArtistController {
	
	@Autowired 
	private ArtistRepository artistRepository;
	@Autowired
	private ArtistValidator artistValidator;

	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}
	
	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}
	
	@PostMapping("/admin/artist")
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist,@RequestParam("imageArtist") MultipartFile image,BindingResult bindingResult, Model model) {

		this.artistValidator.validate(artist, bindingResult);
		if (!bindingResult.hasErrors()) {
			try {
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                artist.setImage(base64Image);
            } catch (IOException e) {}
			this.artistRepository.save(artist); 
			model.addAttribute("artist", artist);
			return "artist.html";
		} else {
			//model.addAttribute("messaggioErrore", "Questo artista esiste già");
			return "admin/formNewArtist.html"; 
		}
	}

	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistRepository.findById(id).get());
		return "artist.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		return "artists.html";
	}

	@GetMapping("/formSearchArtists")
	public String formSearchArtists(Model model){
		model.addAttribute("artists", this.artistRepository.findAll());
		return "formSearchArtists.html";
	}
	
	@PostMapping("/searchArtists")
	public String searchArtists(Model model, @RequestParam String name) {
		String UpperName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		model.addAttribute("artists", this.artistRepository.findByName(UpperName));
		return "foundArtists.html";
	}
	@GetMapping("/admin/manageArtists")
	public String manageArtists(Model model){
		model.addAttribute("artists", this.artistRepository.findAll());
		return "/admin/manageArtists.html";
	}

	@GetMapping(value="/admin/formUpdateArtist/{id}")
	public String formUpdateArtist(@PathVariable("id") Long id,Model model) {
		model.addAttribute("artist", this.artistRepository.findById(id).get());
		return "/admin/formUpdateArtist.html";
	}

	@GetMapping("/admin/formChangeArtist/{artistId}")
	public String formChangeArtist(@PathVariable("artistId") Long id, Model model){
		model.addAttribute("artist", this.artistRepository.findById(id).get());
		return "/admin/updateArtist.html";
	}

	@PostMapping("/admin/changeArtist/{artistId}")
	public String changeArtist(@PathVariable("artistId") Long id,@Valid @ModelAttribute Artist newArtist,@RequestParam("imageArtist") MultipartFile image, BindingResult bindingResult, Model model){

		Artist artist=this.artistRepository.findById(id).get();
		this.artistValidator.validate(newArtist, bindingResult);
		if (!bindingResult.hasFieldErrors()) {
			artist.setName(newArtist.getName());
			artist.setSurname(newArtist.getSurname());
			artist.setDateOfBirth(newArtist.getDateOfBirth());
			artist.setDateOfDeath(newArtist.getDateOfDeath());
			//inserimento immagini
			try {
				
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                artist.setImage(base64Image);
            }catch (IOException e) {}
			
			this.artistRepository.save(artist); 
			model.addAttribute("artist", artist);
			return "/admin/formUpdateArtist.html";
		} else {
			model.addAttribute("artist", this.artistRepository.findById(id).get());
			return "/admin/updateArtist.html"; 
		}

	}

	@GetMapping("/admin/removeArtist/{artistId}")
	public String removeMovie(@PathVariable("artistId") Long id,Model model){

		Artist artist = this.artistRepository.findById(id).get();

		for(Movie movie : artist.getDirectorOf()){
			movie.setDirector(null);
		}

		for(Movie movie : artist.getActorOf()){
			movie.getActors().remove(artist);
		}

		this.artistRepository.delete(artist);
		
		model.addAttribute("artists", this.artistRepository.findAll());
		return "/admin/manageArtists.html";
	}


}
