package dms.westside.portterr.alba.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbaTerritory {
    String number;
    String description;
    String id;
    String notes;
    String kind;
}