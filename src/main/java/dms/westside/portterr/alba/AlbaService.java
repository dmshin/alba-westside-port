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


@Service
public class AlbaService {

    @Autowired
    RestTemplate restTemplate;

    public void makeHttpRequest() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //List<MediaType> accept = new ArrayList(1);
        //accept.add(MediaType.APPLICATION_JSON);
        //accept.add(MediaType.text_Jb)
       // headers.setAccept(accept);
        headers.add("Cookie", "Alba3=r8n08tkjt3mr0gjdr5st4lu5ca;alba_an=manhattanportuguese;alba_un=daniels");
        headers.add("Accept", "application/json, text/javascript, */*; q=0.01");
        /*
        headers.add("Accept-Encoding", "deflate, br, zstd");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");
        headers.add("Priority", "u=1, i");
        headers.add("sec-ch-ua", "\"Chromium\";v=\"134\", \"Not:A-Brand\";v=\"24\", \"Google Chrome\";v=\"134\"");
        headers.add("sec-ch-ua-mobile", "?0'");
        headers.add("sec-ch-ua-platform", "macOS");
        headers.add("sec-fetch-dest", "empty");
        headers.add("sec-fetch-mode", "cors");
        headers.add("sec-fetch-site", "same-origin");
        headers.add("Referer", "https://www.mcmxiv.com/alba/territories2");
         */



        // Create HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //String url = "https://www.mcmxiv.com/alba/ts?mod=territories&cmd=search&kinds%5B%5D=0&kinds%5B%5D=1&kinds%5B%5D=2&q=&sort=number&order=asc";
        String url = "https://www.mcmxiv.com/alba/ts?mod=territories&cmd=search&kinds[]=0&kinds[]=1&kinds[]=2&q=&sort=number&order=asc";

        //String body = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        //Logger.log(body);

        //ObjectNode node = restTemplate.exchange(url, HttpMethod.GET, entity, ObjectNode.class).getBody();
        //JsonNode jNode = node.get("data").get("borders");
        //Logger.log(jNode.iterator().next().toString());
        //AlbaResponse resp  = restTemplate.exchange(url, HttpMethod.GET, entity, AlbaResponse.class).getBody();

        ResponseEntity<AlbaResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, AlbaResponse.class);
        AlbaResponse resp = responseEntity.getBody();
        Logger.log(resp.getReq().getGet().getCmd());
        //Logger.log(resp.getReq().getGet().getMod());
        //Logger.log(resp.getData().getHtml().getAddresses());
        //resp.getData().getBorders().keySet().stream().forEach(e -> Logger.log(e.toString()));


    }

}
