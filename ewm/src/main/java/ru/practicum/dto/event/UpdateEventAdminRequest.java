package ru.practicum.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.utility.EventAdminState;
import ru.practicum.utility.Location;

import javax.validation.constraints.Size;


@Builder
@Data
public class UpdateEventAdminRequest {

    @Size(max = 2000)
    @Size(min = 20)
    private String annotation;
    private long category;
    @Size(max = 7000)
    @Size(min = 20)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    private EventAdminState stateAction;
    @Size(max = 120)
    @Size(min = 3)
    private String title;

}
