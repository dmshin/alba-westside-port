package dms.westside.portterr.alba.dto.getallterritories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dms.westside.portterr.alba.dto.AlbaData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllTerrResponse {
    AllTerrAlbaData data;
}

