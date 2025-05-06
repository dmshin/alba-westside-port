package dms.westside.portterr.alba;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dms.westside.portterr.alba.dto.AlbaResponse;
import dms.westside.portterr.alba.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        GET_ADDRESS("https://www.mcmxiv.com/alba/ts?mod=addresses&cmd=edit&lat=&lng=&id=addressId");

        public String url;
         URL(String url) {
            this.url = url;
        }

        public String value() {
             return url;
        }
    }


    public void doTheDeed() {

        Set<String> terrIds = getAllTerritories();
        for(String terrId : terrIds) {

            Set<String> addressIds = getTerritoryAddressIds(terrId);
            boolean newDoormanTerrCreated = false;
            String doormanTerrId;
            for(String addressId : addressIds) {
                String address = getAddress(addressId);
                boolean isDoorman = AlbaHelper.isDoorman(address);
                if(isDoorman) {
                    if(!newDoormanTerrCreated) {
                        //doormanTerrId = create new territory
                    }
                    //move address to new territory
                }
            }
        }

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

    public String getAddress(String addressId) {
        String url = URL.GET_ADDRESS.value().replaceAll("addressId", addressId);

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AlbaResponse.class);
        AlbaResponse resp = responseEntity.getBody();
        return resp.getData().getHtml().getAddress().values().iterator().next();

    }


}
