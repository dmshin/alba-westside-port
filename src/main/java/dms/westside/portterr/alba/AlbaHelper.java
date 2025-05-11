package dms.westside.portterr.alba;

import dms.westside.portterr.alba.dto.AlbaAddress;
import dms.westside.portterr.alba.dto.AlbaTerritory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AlbaHelper {

    public static boolean isDoorman(AlbaAddress albaAddress) {
        return !albaAddress.getNotes().toUpperCase().matches(".* VIRTUAL DOORMAN.*") && albaAddress.getNotes().replaceAll("\\s", "").toUpperCase().matches(".*DOORMAN.*");
    }

    public static String addQueryParams(AlbaAddress albaAddress, String url) {
        String newUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("territory_id", albaAddress.getTerritory_id())
                .queryParam("language_id", albaAddress.getLanguage_id())
                .queryParam("status", albaAddress.getStatus())
                .queryParam("lat", albaAddress.getLat())
                .queryParam("lng", albaAddress.getLng())
                .queryParam("full_name", albaAddress.getFull_name())
                //.queryParam("full_name", albaAddress.getFull_name())
                .queryParam("suite", albaAddress.getSuite())
                .queryParam("address", albaAddress.getAddress())
                .queryParam("city", albaAddress.getCity())
                .queryParam("province", albaAddress.getProvince())
                .queryParam("country", albaAddress.getCountry())
                .queryParam("postcode", albaAddress.getPostcode())
                .queryParam("telephone", albaAddress.getTelephone())
                .queryParam("notes", albaAddress.getNotes())
                .queryParam("notes_private", albaAddress.getNotes_private())
                .build()
                .toUriString();

                return newUrl;
    }



    public static AlbaTerritory parseTerritory(String terrId, String terrHtml) {

        Document doc = Jsoup.parse(terrHtml);

        AlbaTerritory albaTerritory = new AlbaTerritory();
        albaTerritory.setId(terrId);

        albaTerritory.setNumber(parseAttrValue(doc, "number"));
        albaTerritory.setDescription(parseAttrValue(doc, "description"));
        albaTerritory.setNotes(parseValue(doc, "notes"));
        albaTerritory.setKind(parseSelectValue(doc, "kind"));
        return albaTerritory;
    }

    public static AlbaAddress parseAddress(String addressId, String addressHtml) {

        Document doc = Jsoup.parse(addressHtml);
        AlbaAddress albaAddress = new AlbaAddress();
        albaAddress.setId(addressId);

        //text fields
        albaAddress.setLat(parseValue(doc, "lat"));
        albaAddress.setLng(parseValue(doc, "lng"));
        albaAddress.setFull_name(parseValue(doc, "full_name"));
        albaAddress.setSuite(parseValue(doc, "suite"));
        albaAddress.setAddress(parseValue(doc, "address"));
        albaAddress.setCity(parseValue(doc, "city"));
        albaAddress.setProvince(parseValue(doc, "province"));
        albaAddress.setCountry(parseValue(doc, "country"));
        albaAddress.setPostcode(parseValue(doc, "postcode"));
        albaAddress.setTelephone(parseValue(doc, "telephone"));
        albaAddress.setNotes(parseValue(doc, "notes"));
        albaAddress.setNotes_private(parseValue(doc, "notes_private"));

        //selects
        albaAddress.setStatus(parseSelectValue(doc, "status"));
        albaAddress.setLanguage_id(parseSelectValue(doc, "language_id"));

        return albaAddress;
    }

    private static String parseAttrValue(Document doc, String attr) {
        //works for <input name=attr value=value/>
        return doc.getElementsByAttributeValue("name", attr).first().attribute("value").getValue();
    }

    private static String parseValue(Document doc, String attr) {
        //works for <input name=attr>value</input>
        return doc.getElementsByAttributeValue("name", attr).first().val();
    }

    private static String parseSelectValue(Document doc, String attr) {
        Element select = doc.getElementsByAttributeValue("name", attr).first();
        if(select.getElementsByAttribute("selected").size() > 0) {
            return select.getElementsByAttribute("selected").first().val();
        } else {
            return "";
        }
    }
}
