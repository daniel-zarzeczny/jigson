package io.jigson.core.flow;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import io.jigson.config.Context;
import io.jigson.json.pipe.JsonPipe;
import io.jigson.pipe.UnitaryFlow;
import io.jigson.utils.CriterionUtils;
import io.jigson.utils.JsonUtils;
import io.jigson.utils.PathUtils;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class KeepFlow implements UnitaryFlow<JsonElement> {

    private final Query query;
    private final Context context;

    public KeepFlow(final Query query, final Context context) {
        this.query = query.fork();
        this.context = context;
    }

    public JsonElement flow(final JsonElement jsonElement) {

        final JsonElement rootElement = jsonElement.deepCopy();
        JsonElement currentElement = rootElement;
        String currentPath = query.nextPath();

        while (isNotBlank(currentPath) && !currentElement.isJsonNull()) {

            final JsonElement resultElement = handle(currentElement, currentPath);

            if (resultElement.isJsonNull()) {
                return resultElement;
            } else {
                if (resultElement.isJsonArray() && currentElement.isJsonArray()) {
                    final JsonArray resultArray = resultElement.getAsJsonArray();
                    final JsonArray currentArray = JsonUtils.clearJsonArray(currentElement.getAsJsonArray());
                    resultArray.forEach(currentArray::add);
                    resultArray.isJsonNull();
                }
                currentElement = resultElement;
                currentPath = query.nextPath();
            }
        }
        return rootElement;
    }

    private JsonElement handle(final JsonElement jsonElement, final String path) {
        if (Objects.isNull(jsonElement) || jsonElement.isJsonNull()) {
            return JsonNull.INSTANCE;
        } else if (jsonElement.isJsonObject()) {
            return handleObject(jsonElement.getAsJsonObject(), path);
        } else if (jsonElement.isJsonArray()) {
            return handleArray(jsonElement.getAsJsonArray(), path);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private JsonElement handleObject(final JsonObject jsonObject, final String path) {

        final String criterion = CriterionUtils.findCriterion(path);

        if (CriterionUtils.isCriterion(criterion)) {
            final JsonElement currentElement = JsonUtils.getPropertyByPath(jsonObject, path);

            final JsonElement resultElement =
                    JsonPipe.from(currentElement)
                            .withContext(context)
                            .filter(criterion)
                            .get()
                            .orElse(JsonNull.INSTANCE);
            JsonUtils.addOrRemoveObjectAttribute(jsonObject, resultElement, path);
            return resultElement;
        } else {
            final String propertyName = PathUtils.findPropertyName(path);
            if (!jsonObject.has(propertyName)) {
                return JsonNull.INSTANCE;
            } else {
                return jsonObject.get(propertyName);
            }
        }
    }

    private JsonElement handleArray(final JsonArray jsonArray, final String path) {

        final JsonArray resultArray = new JsonArray();

        // keeps JsonObjects meeting criterion and all other JsonElements
        for (int i = 0; i < jsonArray.size(); ++i) {
            final JsonElement currentElement = jsonArray.get(i);
            if (currentElement.isJsonObject()) {
                final JsonElement resultElement =
                        handleObject(currentElement.getAsJsonObject(), path).isJsonNull() ? JsonNull.INSTANCE : currentElement;
                if (!resultElement.isJsonNull()) {
                    resultArray.add(resultElement);
                }
            } else {
                resultArray.add(currentElement);
            }
        }

        return resultArray.size() == 0 ? JsonNull.INSTANCE : resultArray;
    }
}
