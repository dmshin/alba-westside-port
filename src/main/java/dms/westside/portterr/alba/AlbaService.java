package dms.westside.portterr.alba;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dms.westside.portterr.alba.dto.AlbaAddress;
import dms.westside.portterr.alba.dto.AlbaResponse;
import dms.westside.portterr.alba.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class AlbaService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpEntity httpEntity;

    enum URL {
        GET_ALL_TERRS ("https://www.mcmxiv.com/alba/ts?mod=territories&cmd=search&kinds[]=0&kinds[]=1&kinds[]=2&q=&sort=number&order=asc"),
        GET_TERR_ADDRESSES ("https://www.mcmxiv.com/alba/ts?mod=addresses&cmd=search&acids=4807&exp=false&npp=25&cp=1&tid=terrId&lid=0&display=1,2,3,4,5,6&onlyun=false&q=&sort=id&order=desc&lat=&lng="),
        GET_ADDRESS("https://www.mcmxiv.com/alba/ts?mod=addresses&cmd=edit&lat=&lng=&id=addressId"),
        UPDATE_ADDRESS("https://www.mcmxiv.com/alba/ts?mod=addresses&cmd=save&id=addressId"),
        CREATE_TERR("https://www.mcmxiv.com/alba/ts?mod=territories&cmd=add&border=43.30630930129742+-73.40916769484804%2C38.44405948429021+-73.40916769484804%2C38.44405948429021+-79.70028613734749%2C43.30630930129742+-79.70028613734749");

        public String url;
         URL(String url) {
            this.url = url;
        }

        public String value() {
             return url;
        }
    }


    public void doTheDeed() {

        //TODO: remove; testing only
        //createLetterTerritory("");

        Set<String> terrIds = getAllTerritories();
        for(String terrId : terrIds) {

            Set<String> addressIds = getTerritoryAddressIds(terrId);
            boolean newDoormanTerrCreated = false;
            String doormanTerrId;
            for(String addressId : addressIds) {

                AlbaAddress albaAddress = getAlbaAddress(addressId);

                //TODO: remove this; testing only
                //albaAddress.setTerritory_id("815972");
                //albaAddress.setFull_name("Daniel Paul Pimenta");

                boolean isDoorman = AlbaHelper.isDoorman(albaAddress);
                if(isDoorman) {
                    if(!newDoormanTerrCreated) {
                        //doormanTerrId = createLetterTerritory(terrId);
                        newDoormanTerrCreated = true;
                    }
                    //updateAddress(albaAddress);
                }
            }
        }

    }

    public String createLetterTerritory(String terrName) {
        String baseUrl = URL.CREATE_TERR.value();

        //get territory by id from alba
        //get number and description

        String newTerrName = "Letters " + terrName;

        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("number", AlbaHelper.encodeUtf8(newTerrName))
                .queryParam("description", "daniel7desc")
                .queryParam("notes", "")
                .queryParam("kind", "1")
                .build()
                .toUriString();


        Logger.log(url);

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AlbaResponse.class);
        AlbaResponse resp = responseEntity.getBody();

        return null;

    }



    public Set<String> getAllTerritories() {

        String url = URL.GET_ALL_TERRS.value();

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AlbaResponse.class);
        AlbaResponse resp = responseEntity.getBody();
        return resp.getData().getBorders().keySet();
    }

    public Set<String> getTerritoryAddressIds(String terrId) {

        String url = URL.GET_TERR_ADDRESSES.value().replaceAll("terrId", terrId);

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AlbaResponse.class);
        AlbaResponse resp = responseEntity.getBody();
        return resp.getData().getLocations().keySet();
    }


    public void updateAddress(AlbaAddress albaAddress) {
        String url = URL.UPDATE_ADDRESS.value().replaceAll("addressId", albaAddress.getId());

        url = AlbaHelper.addQueryParams(albaAddress, url);

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AlbaResponse.class);
        AlbaResponse resp = responseEntity.getBody();

    }

    public String getAddressHtml(String addressId) {
        String url = URL.GET_ADDRESS.value().replaceAll("addressId", addressId);

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AlbaResponse.class);
        AlbaResponse resp = responseEntity.getBody();
        return resp.getData().getHtml().getAddress().values().iterator().next();
    }

    public AlbaAddress getAlbaAddress(String addressId) {
        String addressHtml = getAddressHtml(addressId);
        return AlbaHelper.parseAddress(addressId, addressHtml);
    }


}
