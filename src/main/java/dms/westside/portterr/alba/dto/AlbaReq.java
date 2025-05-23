package dms.westside.portterr.alba.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dms.westside.portterr.alba.AlbaApplication;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbaReq {
    AlbaCmd get;
}
