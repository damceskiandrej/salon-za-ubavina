package mk.ukim.finki.web_seminarska.model.exceptions;

public class InvalidReservationException extends RuntimeException{

        public InvalidReservationException() {

            super("Invalid user credentials");
        }


}
