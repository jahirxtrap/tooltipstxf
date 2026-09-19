package com.jahirtrap.tooltips.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ResolvableIntReader {
    private static final FileToIdConverter CONVERTER = FileToIdConverter.registry(Registries.CONTEXT_INT_PROVIDER);
    private static final Map<Identifier, JsonElement> CACHE = new HashMap<>();
    private static PackResources vanillaData;

    public static int burnTime(ResolvableInt burnTime) {
        return value(source(burnTime));
    }

    public static int compostChance(ResolvableInt layers) {
        return chance(source(layers));
    }

    private static JsonElement source(ResolvableInt provider) {
        if (provider instanceof ResolvableInt.Constant constant) return new JsonPrimitive(constant.value());
        if (provider instanceof ResolvableInt.Reference reference) return read(reference.key().identifier());
        return JsonNull.INSTANCE;
    }

    private static int value(JsonElement element) {
        if (isNumber(element)) return element.getAsInt();
        if (element.isJsonPrimitive()) return value(read(Identifier.parse(element.getAsString())));
        if (!element.isJsonObject()) return 0;

        JsonObject object = element.getAsJsonObject();
        return switch (type(object)) {
            case "minecraft:constant" -> object.get("value").getAsInt();
            case "minecraft:div" -> divide(value(object.get("left")), value(object.get("right")));
            case "minecraft:conditional" -> value(object.get("on_false"));
            case "minecraft:number_dispatcher" -> value(object.get("default"));
            default -> 0;
        };
    }

    private static int chance(JsonElement element) {
        if (isNumber(element)) return element.getAsInt() >= 1 ? 100 : 0;
        if (element.isJsonPrimitive()) return chance(read(Identifier.parse(element.getAsString())));
        if (!element.isJsonObject()) return 0;

        JsonObject object = element.getAsJsonObject();
        return switch (type(object)) {
            case "minecraft:constant" -> object.get("value").getAsInt() >= 1 ? 100 : 0;
            case "minecraft:number_dispatcher" -> chance(object.get("default"));
            case "minecraft:weighted_list" -> weighted(object.getAsJsonArray("distribution"));
            default -> 0;
        };
    }

    private static int weighted(JsonArray distribution) {
        int total = 0, adding = 0;
        for (JsonElement entry : distribution) {
            JsonObject object = entry.getAsJsonObject();
            int weight = object.has("weight") ? object.get("weight").getAsInt() : 1;
            total += weight;
            if (value(object.get("data")) >= 1) adding += weight;
        }
        return total == 0 ? 0 : Math.round(adding * 100f / total);
    }

    private static JsonElement read(Identifier id) {
        return CACHE.computeIfAbsent(id, key -> {
            var resource = vanillaData().getResource(PackType.SERVER_DATA, CONVERTER.idToFile(key));
            if (resource == null) return JsonNull.INSTANCE;
            try (Reader reader = new InputStreamReader(resource.get(), StandardCharsets.UTF_8)) {
                return StrictJsonParser.parse(reader);
            } catch (Exception ignore) {
                return JsonNull.INSTANCE;
            }
        });
    }

    private static PackResources vanillaData() {
        if (vanillaData == null) vanillaData = ServerPacksSource.createVanillaPackSource().fullResources();
        return vanillaData;
    }

    private static boolean isNumber(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber();
    }

    private static String type(JsonObject object) {
        return object.has("type") ? Identifier.parse(object.get("type").getAsString()).toString() : "";
    }

    private static int divide(int left, int right) {
        return right == 0 ? 0 : left / right;
    }
}
