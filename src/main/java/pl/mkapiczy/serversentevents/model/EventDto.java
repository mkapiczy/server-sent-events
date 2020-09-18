package pl.mkapiczy.serversentevents.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto implements Serializable {
    private String type;
    private Map<String, Object> body;
}
