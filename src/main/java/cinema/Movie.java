package cinema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    private long id;

    private String title;

    private LocalDateTime date;

    private int maxReservation;

    private int freeSpaces;

    public void reserveSeats(int numberOfReserves){
        if (freeSpaces >=numberOfReserves){
            freeSpaces -= numberOfReserves;
        } else {
            throw new IllegalStateException("Not enough number of free seats");
        }
    }

}
