package dms.westside.portterr.alba.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbaAddress {
    String mod;
    String cmd;
    String id;

    String territory_id;

    String lat;
    String lng;
    String status;
    String language_id;
    String full_name;
    String suite;
    String address;
    String city;
    String province;
    String country;
    String postcode;
    String telephone;
    String notes;
    String notes_private;

}
