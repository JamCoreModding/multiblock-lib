package io.github.jamalam360.pattern;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.github.jamalam360.MultiblockLib;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;

/**
 * @author Jamalam360
 */
public class MultiblockResourceReloadListener extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public MultiblockResourceReloadListener() {
        super(new Gson(), "multiblock_patterns");
    }

    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        MultiblockPatterns.clear();
        prepared.forEach((id, element) -> {
            System.out.println("Loading multiblock pattern: " + id);
            MultiblockPatterns.add(MultiblockPattern.deserialize(id, element));
        });
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier("multiblocklib", "multiblock_patterns");
    }
}
