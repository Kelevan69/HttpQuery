import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private final String path;
    private final Map<String, String> queryParams = new HashMap<>();

    public Request(String url) {
        String[] parts = url.split("\\?", 2);
        this.path = parts[0];

        // Если есть параметры в строке запроса, парсим их с помощью URLEncodedUtils
        if (parts.length > 1) {
            String query = parts[1];
            List<NameValuePair> params = URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
            for (NameValuePair param : params) {
                queryParams.put(param.getName(), param.getValue());
            }
        }
    }

    public String getQueryParam(String name) {
        return queryParams.get(name);
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getPath() {
        return path;
    }
}
