package vn.com.vng.mcrusprofile.util;

import java.lang.reflect.Type;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * 
 * @author sonnd
 *
 */
public final class JsonUtils {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private static final GsonBuilder GSON_BUILDER = new GsonBuilder().registerTypeAdapter(GrantedAuthority.class, new JsonDeserializer<GrantedAuthority>() {

		@Override
		public GrantedAuthority deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			if(json == null || !json.isJsonObject()) {
				return null;
			}
			JsonElement jsonRole = json.getAsJsonObject().get("role");
			if(jsonRole == null) {
				return null;
			}
			String role = jsonRole.getAsString();
			return new SimpleGrantedAuthority(role);
		}
	});
	private static final GsonBuilder DB_COMPAIRTIBLE_GSON_BUILDER = new GsonBuilder().serializeNulls()
			.registerTypeAdapter(Date.class, new DateLongFormatTypeAdapter());
    private static Gson GSON;
    private static Gson DB_COMPAIRTIBLE_GSON;
    
    static {
    	GSON = GSON_BUILDER.create();
    	
    	DB_COMPAIRTIBLE_GSON = DB_COMPAIRTIBLE_GSON_BUILDER.create();
    }
	
	private JsonUtils() {

	}

	public static final ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}
	
	public static final Gson getGson() {
		return GSON;
	}
	
	public static final Gson getDbCompatibleGson() {
		return DB_COMPAIRTIBLE_GSON;
	}
	
	public static final String writeValueAsStringNoException(Object val) {
		try {
			return JsonUtils.getObjectMapper().writeValueAsString(val);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

}
