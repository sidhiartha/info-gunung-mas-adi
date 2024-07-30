package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.json.JSONArray;

public class ApiResponse {
    public String output;
    @SerializedName("error_code")
    public int errorCode = -1;
    public JSONArray maps;

    @SerializedName("pm_id")
    public JSONArray pmId;
    public String cuaca;

    public String toJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }
}
