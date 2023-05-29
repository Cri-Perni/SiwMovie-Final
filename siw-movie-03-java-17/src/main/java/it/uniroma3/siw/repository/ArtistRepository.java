package it.uniroma3.siw.repository;


import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Artist;

public interface ArtistRepository extends CrudRepository<Artist, Long> {

	public boolean existsByNameAndSurname(String name, String surname);	

	public boolean existsByNameAndSurnameAndDateOfBirth(String name, String surname, LocalDate dateOfBirth);

	@Query(value="select * "
			+ "from artist a "
			+ "where a.id not in "
			+ "(select actors_id "
			+ "from movie_actors "
			+ "where movie_actors.starred_movies_id = :movieId)", nativeQuery=true)
	public Iterable<Artist> findActorsNotInMovie(@Param("movieId") Long id);

	public Iterable<Artist> findByName(String name);


}