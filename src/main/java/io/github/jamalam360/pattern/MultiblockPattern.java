package io.github.jamalam360.pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

/**
 * @author Jamalam360
 */
public record MultiblockPattern(Identifier identifier, Layer[] layers) {
    /**
     * @return the length of this pattern in the y-axis
     */
    public int height() {
        return layers.length;
    }

    /**
     * @return the length of this pattern in the x-axis
     */
    public int width() {
        return layers[0].rows[0].length();
    }

    /**
     * @return the length of this pattern in the z-axis
     */
    public int depth() {
        return layers[0].rows.length;
    }

    public static MultiblockPattern deserialize(Identifier id, JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        JsonArray layers = obj.get("layers").getAsJsonArray();
        Layer[] layerArray = new Layer[layers.size()];
        for (int i = 0; i < layers.size(); i++) {
            layerArray[i] = Layer.deserialize(layers.get(i));
        }

        return new MultiblockPattern(id, layerArray);
    }

    public record Layer(int min, int max, String[] rows) {
        public static MultiblockPattern.Layer deserialize(JsonElement json) {
            JsonObject obj = json.getAsJsonObject();
            JsonElement minObj = obj.get("min");
            JsonElement maxObj = obj.get("max");
            int min = 1;
            int max = 1;

            if (minObj != null) {
                min = minObj.getAsInt();
            }

            if (maxObj != null) {
                max = maxObj.getAsInt();
            }

            if (min > max) {
                throw new IllegalArgumentException("min > max");
            } else if (min < 1) {
                throw new IllegalArgumentException("min < 1");
            }

            JsonArray rowsObj = obj.get("rows").getAsJsonArray();
            String[] rows = new String[rowsObj.size()];

            for (int i = 0; i < rowsObj.size(); i++) {
                rows[i] = rowsObj.get(i).getAsString();
            }

            return new MultiblockPattern.Layer(min, max, rows);
        }
    }
}
