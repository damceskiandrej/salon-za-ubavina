package mk.ukim.finki.web_seminarska.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TimeSlot {
    private LocalDateTime startTime;
    private boolean isAvailable;
}
