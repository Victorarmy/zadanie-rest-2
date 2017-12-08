package pl.javastart.minifilmweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController("/api")
public class MovieRestController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        System.out.println("Odpytanie o wszystkie filmy");
        return movieRepository.findAll();
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity getMovie(@PathVariable long id) {
        Movie foundMovie = movieRepository.findOne(id);
        if (foundMovie == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(foundMovie);
    }

    @PostMapping("/movies")
    public ResponseEntity addNewMovie(@RequestBody Movie newMovie) {

        if (isExistingTheSameMovieInDatabase(newMovie)) {
            return ResponseEntity
                    .unprocessableEntity()
                    .build();
        }

        Movie savedMovie = movieRepository.save(newMovie);
        URI uri= getURIForSavedMovie(savedMovie);
        return ResponseEntity
                .created(uri)
                .build();
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity changeFilm(@PathVariable long id) {
        Movie foundMovie = movieRepository.findOne(id);
        if (foundMovie == null) {
            return ResponseEntity.notFound().build();
        }


    }

    private boolean isExistingTheSameMovieInDatabase(@RequestBody Movie newMovie) {
        Movie movieFoundByTheSameTitle = movieRepository.findByTitle(newMovie.getTitle());
        if (movieFoundByTheSameTitle != null) {
            return movieFoundByTheSameTitle.equals(newMovie);
        }
        return false;
    }

    private URI getURIForSavedMovie(Movie savedMovie) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMovie.getId()).toUri();
    }
}
