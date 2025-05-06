package dms.westside.portterr.alba.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbaData {
    //for getting terr ids
    Map<String, AlbaBorder> borders;

    //for getting address ids
    Map<String, Object> locations;

    //for getting address ids
    AlbaHtml html;
}
