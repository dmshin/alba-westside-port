package dms.westside.portterr.alba.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbaHtml {
    Map<String, String> address;
    Map<String, String> territory;

    //In getTerritory response, this needs to be a Map.  In getAllTerritories(), this is a string; actually not needed in getAllTerritories
    Map<String, String> territories;
}
