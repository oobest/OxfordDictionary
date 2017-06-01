package com.oobest.study.oxforddictionary.model.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class EntityUtils {

    private EntityUtils() {}

    public static final Gson gson = new GsonBuilder()
          //  .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
            .disableHtmlEscaping()
            .create();

//    private static class DateTimeTypeAdapter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
//
//        @Override
//        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
//            return new JsonPrimitive(src.toString());
//        }
//
//        @Override
//        public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//            return new DateTime(json.getAsString());
//        }
//
//    }

}
