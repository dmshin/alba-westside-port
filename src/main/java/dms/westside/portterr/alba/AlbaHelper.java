package dms.westside.portterr.alba;

import dms.westside.portterr.alba.dto.AlbaAddress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AlbaHelper {

    public static boolean isDoorman(AlbaAddress albaAddress) {
        return albaAddress.getNotes().replaceAll("\\s", "").toUpperCase().matches(".*DOORMAN.*");
    }

    public static String addQueryParams(AlbaAddress albaAddress, String url) {
        String newUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("territory_id", encodeUtf8(albaAddress.getTerritory_id()))
                .queryParam("language_id", encodeUtf8(albaAddress.getLanguage_id()))
                .queryParam("status", encodeUtf8(albaAddress.getStatus()))
                .queryParam("lat", encodeUtf8(albaAddress.getLat()))
                .queryParam("lng", encodeUtf8(albaAddress.getLng()))
                .queryParam("full_name", encodeUtf8(albaAddress.getFull_name()))
                .queryParam("suite", encodeUtf8(albaAddress.getSuite()))
                .queryParam("address", encodeUtf8(albaAddress.getAddress()))
                .queryParam("city", encodeUtf8(albaAddress.getCity()))
                .queryParam("province", encodeUtf8(albaAddress.getProvince()))
                .queryParam("country", encodeUtf8(albaAddress.getCountry()))
                .queryParam("postcode", encodeUtf8(albaAddress.getPostcode()))
                .queryParam("telephone", encodeUtf8(albaAddress.getTelephone()))
                .queryParam("notes", encodeUtf8(albaAddress.getNotes()))
                .queryParam("notes_private", encodeUtf8(albaAddress.getNotes_private()))
                .build()
                .toUriString();

                return newUrl;
    }


    public static String encodeUtf8(String val) {
        try {
            return URLEncoder.encode(val, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Error encoding stuff", ex);
        }
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

    private static String parseValue(Document doc, String attr) {
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
