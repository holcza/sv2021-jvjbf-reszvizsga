package cinema;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private List<Movie> movies = new ArrayList<>();

    private AtomicLong idGenerator = new AtomicLong();

    private ModelMapper modelMapper;

    public MovieService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<MovieDTO> getMovies(Optional<String> title) {
        return movies.stream()
                .filter(m -> title.isEmpty() || m.getTitle().equalsIgnoreCase(title.get()))
                .map(m -> modelMapper.map(m, MovieDTO.class))
                .collect(Collectors.toList());
    }

    public MovieDTO getMovieById(long id) {

        return modelMapper.map(getMovieGivingId(id), MovieDTO.class);
    }

    public MovieDTO createMovie(CreateMovieCommand command) {

        int spaces = command.getMaxReservation();

        Movie movie =
                new Movie(
                        idGenerator.incrementAndGet(),command.getTitle(),command.getDate(),spaces,spaces);

        movies.add(movie);

        return modelMapper.map(movie,MovieDTO.class);
    }

    public MovieDTO createNewReservation(CreateReservationCommand command, long id) {

        Movie movie = getMovieGivingId(id);

        movie.reserveSeats(command.getNumberOfReservation());

        return modelMapper.map(movie,MovieDTO.class);

    }

    public MovieDTO updateMovie(UpdateDateCommand command, long id) {
        Movie movie = getMovieGivingId(id);

        movie.setDate(command.getDate());

        return modelMapper.map(movie,MovieDTO.class);
    }

    private Movie getMovieGivingId(long id){
        return movies.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ID can not be found"));
    }

    public void deleteAll() {
        movies.clear();
        idGenerator.set(0);
    }
}
