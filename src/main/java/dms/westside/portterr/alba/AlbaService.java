package dms.westside.portterr.alba;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import dms.westside.portterr.alba.dto.AlbaAddress;
import dms.westside.portterr.alba.dto.AlbaResponse;
import dms.westside.portterr.alba.dto.AlbaTerritory;
import dms.westside.portterr.alba.dto.getallterritories.AllTerrResponse;
import dms.westside.portterr.alba.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class AlbaService {


    @Value("#{new Boolean('${dryrun:true}')}")
    Boolean dryrun;


    @Value("#{new Integer('${territory.limit:1}')}")
    Integer territoryLimit; //for testing purposes; limit how many territory are processed

    @Value("#{new Integer('${addresses.per.territory.limit:1}')}")
    Integer addrPerTerrLimit;  //for testing purposes; limit how many address per terr are processed

    @Value("${territory.ids.to.skip}")
    List<String> skipTerrIds;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpEntity httpEntity;

    @Autowired
    HttpEntity httpEntity2;

    enum URL {
        GET_ALL_TERRS ("https://www.mcmxiv.com/alba/ts?mod=territories&cmd=search&kinds[]=0&kinds[]=1&kinds[]=2&q=&sort=number&order=asc"),
        GET_TERR_ADDRESSES ("https://www.mcmxiv.com/alba/ts?mod=addresses&cmd=search&acids=4807&exp=false&npp=100&cp=currPage&tid=terrId&lid=0&display=1,2,3,4,5,6&onlyun=false&q=&sort=id&order=desc&lat=&lng="),
        GET_ADDRESS("https://www.mcmxiv.com/alba/ts?mod=addresses&cmd=edit&lat=&lng=&id=addressId"),
        UPDATE_ADDRESS("https://www.mcmxiv.com/alba/ts?mod=addresses&cmd=save&id=addressId"),
        CREATE_TERR("https://www.mcmxiv.com/alba/ts?mod=territories&cmd=add&border=43.30630930129742+-73.40916769484804%2C38.44405948429021+-73.40916769484804%2C38.44405948429021+-79.70028613734749%2C43.30630930129742+-79.70028613734749"),
        GET_TERR("https://www.mcmxiv.com/alba/ts?mod=territories&cmd=edit&id=territoryId");


        public String url;
         URL(String url) {
            this.url = url;
        }

        public String value() {
             return url;
        }
    }


    public void doTheDeed() {

        Logger.log("************************************");
        Logger.log("Doing the deed.  " + (dryrun ? " DRY RUN MODE" : "DOING THE REAL DEAL!"));
        Logger.log("************************************");


        Set<String> terrIds = getAllTerritories();

        int totalTerrCount = terrIds.size();
        int terrCount = 1;
        for(String terrId : terrIds) {
            Logger.log ("******** Processing Territory Id: " + terrId + " - " + terrCount + " of " + totalTerrCount + " ********" );

            if(skipTerrIds != null && skipTerrIds.contains(terrId)) {
                Logger.log ("This terrId is in the skip list.  Ignoring...onto the next.");
                continue;
            }

            Logger.log("Getting territory details...");
            AlbaTerritory albaTerritory = getTerritory(terrId);
            Logger.log("Success! Territory " + albaTerritory.getNumber() + " | " + albaTerritory.getDescription() + " retrieved.");

            Set<String> addressIds = getTerritoryAddressIds(terrId);
            int totalAddressCount = addressIds != null ? addressIds.size() : 0;
            Logger.log("Number of addresses: " + totalAddressCount);
            boolean newLetterTerrCreated = false;
            String letterTerrId = null;
            int addressCount = 1;
            for(String addressId : addressIds) {
                Logger.log("***** Getting details for address id: " + addressId + " - " + addressCount + " of " + totalAddressCount + " *****");
                AlbaAddress albaAddress = getAlbaAddress(addressId);
                Logger.log("Success! Details retrieved for " + albaAddress.getFull_name() +  " living at " + albaAddress.getAddress());
                boolean isLetterAddress = AlbaHelper.isDoorman(albaAddress);
                Logger.log("Is letter address? " + isLetterAddress);
                if(isLetterAddress) {
                    if(!newLetterTerrCreated) {
                        Logger.log("Letters territory being created for " + albaTerritory.getNumber());
                        letterTerrId = createLetterTerritory(albaTerritory);
                        newLetterTerrCreated = true;
                    }
                    albaAddress.setTerritory_id(letterTerrId);
                    updateAddress(albaAddress);
                }
                if(addressCount >= addrPerTerrLimit) {
                    //for testing purposes to process only some
                    break;
                }
                addressCount++;
            }
            if(terrCount >= territoryLimit) {
                //for testing purposes to process only some
                return;
            }
            terrCount++;
        }

    }

    public AlbaTerritory getTerritory(String terrId) {
        String url = URL.GET_TERR.value().replaceAll("territoryId", terrId);

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AlbaResponse.class);
        AlbaResponse resp = responseEntity.getBody();
        String terrHtml = resp.getData().getHtml().getTerritory().values().iterator().next();
        return AlbaHelper.parseTerritory(terrId, terrHtml);
    }

    public String createLetterTerritory(AlbaTerritory albaTerritory) {

        if(dryrun) {
            Logger.log("DRY RUN: Not actually creating letter territory.  Returning terr id:dryRunTerrId");
            return "dryRunTerrId";
        }

        String baseUrl = URL.CREATE_TERR.value();
        String newTerrName = "Letters " + albaTerritory.getNumber();

        Logger.log("Creating new letter territory for " + albaTerritory.getId() + " " + albaTerritory.getNumber() + " " + albaTerritory.getDescription());

        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("number", newTerrName)
                .queryParam("description", albaTerritory.getDescription())
                .queryParam("notes", "")
                .queryParam("kind", "1")
                .build()
                .toUriString();

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AlbaResponse.class);
        AlbaResponse resp = responseEntity.getBody();
        String newTerrId = resp.getData().getHtml().getTerritories().keySet().iterator().next();

        Logger.log("SUCCESS! New territory created: " + newTerrId);
        return newTerrId;

    }



    public Set<String> getAllTerritories() {

        String url = URL.GET_ALL_TERRS.value();

        ResponseEntity<AllTerrResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AllTerrResponse.class);
        AllTerrResponse resp = responseEntity.getBody();
        return resp.getData().getBorders().keySet();
    }

    public Set<String> getTerritoryAddressIds(String terrId) {

        Set<String> result = new HashSet<>();

        boolean moreAddressExist = true;
        int page = 1;
        do {
            Set<String> pageAddressIds = getTerritoryAddressIdsForOnePage(terrId, page);
            if(pageAddressIds != null) {
                result.addAll(pageAddressIds);
                page++;
            } else {
                moreAddressExist = false;
            }
        } while ( moreAddressExist);

        return result;
    }

    private Set<String> getTerritoryAddressIdsForOnePage(String terrId, Integer pageNum) {

        String url = URL.GET_TERR_ADDRESSES.value().replaceAll("terrId", terrId).replaceAll("currPage", pageNum.toString());

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AlbaResponse.class);
        AlbaResponse resp = responseEntity.getBody();
        return resp.getData().getLocations() == null ? null : resp.getData().getLocations().keySet();
    }


    public void updateAddress(AlbaAddress albaAddress) {

        if(dryrun) {
            Logger.log("DRY RUN: Moving address to territory id:" + albaAddress.getTerritory_id());
            return;
        }

        Logger.log("Updating address " + albaAddress.getId() + " " + albaAddress.getFull_name() + " at " + albaAddress.getAddress() + " to terr id " + albaAddress.getTerritory_id());

        String url = URL.UPDATE_ADDRESS.value().replaceAll("addressId", albaAddress.getId());

        url = AlbaHelper.addQueryParams(albaAddress, url);

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AlbaResponse.class);
        Logger.log("SUCCESS!  Address updated.");
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
