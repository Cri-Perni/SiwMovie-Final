package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.interFace.ArtistService;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import jakarta.transaction.Transactional;
@Service
public class ArtistServiceImpl implements ArtistService{

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private ArtistValidator artistValidator;

    @Transactional
    @Override
    public Artist newArtist(Artist artist, MultipartFile image, BindingResult bindingResult) throws IOException{

        this.artistValidator.validate(artist, bindingResult);
		if (!bindingResult.hasErrors()) {
			try {
				String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
				artist.setImage(base64Image);
			} catch (IOException e) {}
			this.artistRepository.save(artist);
            return artist;
        }else throw new IOException();
        
        
    }

    @Transactional
    @Override
    public Artist findById(Long id) {
        return this.artistRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Iterable<Artist> findAll() {
        return this.artistRepository.findAll();
    }

    @Transactional
    @Override
    public Iterable<Artist> findByName(String name) {
        String UpperName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return this.artistRepository.findByName(UpperName);
    }

    @Transactional
    @Override
    public Artist update(Long id, Artist newArtist, MultipartFile image, BindingResult bindingResult) throws IOException {
        
		this.artistValidator.validate(newArtist, bindingResult);
		if (!bindingResult.hasFieldErrors()) {
            Artist artist = this.artistRepository.findById(id).get();
			artist.setName(newArtist.getName());
			artist.setSurname(newArtist.getSurname());
			artist.setDateOfBirth(newArtist.getDateOfBirth());
			artist.setDateOfDeath(newArtist.getDateOfDeath());
			// inserimento immagini
			try {
                if(image.getBytes().length != 0){
				String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
				artist.setImage(base64Image);}
			} catch (IOException e) {
			}

			this.artistRepository.save(artist);
             return artist;
        }else throw new IOException();
           
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Artist artist = this.artistRepository.findById(id).get();

		for (Movie movie : artist.getDirectorOf()) {
			movie.setDirector(null);
		}

		for (Movie movie : artist.getActorOf()) {
			movie.getActors().remove(artist);
		}

		this.artistRepository.delete(artist);
    }
    
}
