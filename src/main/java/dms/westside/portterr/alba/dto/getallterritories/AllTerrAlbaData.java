package dms.westside.portterr.alba.dto.getallterritories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dms.westside.portterr.alba.dto.AlbaBorder;
import dms.westside.portterr.alba.dto.AlbaHtml;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllTerrAlbaData {
    //for getting terr ids
    Map<String, AlbaBorder> borders;
}