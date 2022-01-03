package alphonse.util;

import alphonse.scripts.Script;
import alphonse.util.farming.Patch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Database {
    private final static Logger LOGGER = Logger.getLogger("db");

    private static int accountId = -1;

    private Database() {}

    public static int getAccountId(String db, String pbName) {
        try {
            URL url = new URL("http://www.althephonse.com/osrs/getUserId.php");
            StringBuilder postData = new StringBuilder();
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("db", db);
            params.put("pbName", pbName);
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Panel", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);
            String response = sb.toString();

            conn.disconnect();
            accountId = Integer.parseInt(response);
            return Integer.parseInt(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getValues(String db, String table) {
        try {
            if (getAccountId() == -1) {
                Script.stopScript("trying to get database values without having gotten account id");
                return "";
            }
            //LOGGER.info("getting value (db = " + db + ", table = " + table);
            URL url = new URL("http://www.althephonse.com/osrs/getValues.php");
            StringBuilder postData = new StringBuilder();
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("db", db);
            params.put("table", table);
            params.put("user", String.valueOf(getAccountId()));
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Panel", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);
            String response = sb.toString();
            LOGGER.info(response);
            conn.disconnect();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setNextHarvest(Patch patch, String value) {
        try {
            String patchTable;
            switch (patch) {
                case FLOWER:
                    patchTable = "flower_settings";
                    break;

                case ALLOTMENT:
                    patchTable = "allotment_settings";
                    break;

                case HOPS:
                    patchTable = "hop_settings";
                    break;

                case TREE:
                    patchTable = "tree_settings";
                    break;

                case FRUIT_TREE:
                    patchTable = "fruit_tree_settings";
                    break;

                case HERB:
                    patchTable = "herb_settings";
                    break;

                default:
                    Script.stopScript("Database#setNextHarvest did not recognize patch: " + patch);
                    return;
            }


            URL url = new URL("http://www.althephonse.com/osrs/setNextHarvest.php");
            StringBuilder postData = new StringBuilder();
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("userId", accountId);
            params.put("patch", patchTable);
            params.put("values", value);
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Panel", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);
            String response = sb.toString();
            LOGGER.info("response from server: " + response);
            conn.disconnect();
        } catch (Exception e) {
            LOGGER.info("error from database");
            e.printStackTrace();
        }
    }

    public static int getAccountId() {
        return accountId;
    }
}
