
package org.x4444.app1s;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

public class Utils {

    public static JSONObject getLocationInJson(Location loc) {
        JSONObject o = new JSONObject();
        try {
            o.put("prv", "gps");
            o.put("ts", loc.getTime());
            o.put("lat", loc.getLatitude());
            o.put("lon", loc.getLongitude());
            o.put("alt", loc.getAltitude());
            o.put("acc", loc.getAccuracy());
            o.put("brng", loc.getBearing());
            o.put("spd", loc.getSpeed());
            return o;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
