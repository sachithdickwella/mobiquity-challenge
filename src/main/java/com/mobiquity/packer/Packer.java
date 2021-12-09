package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.pojo.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.AbstractMap.SimpleImmutableEntry;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author Sachith Dickwella
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Packer {

    /**
     *
     */
    @NotNull
    public static String pack(@NotNull String filePath) throws APIException {
        var packer = new Packer();
        var packages = packer.mapFile(filePath);

        System.out.println(packages);
        return null;
    }

    /**
     *
     */
    @NotNull
    private Map<Integer, List<Item>> mapFile(@NotNull String filePath) throws APIException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, UTF_8))) {
            return reader.lines().map(line -> {
                var split = line.split(":");
                var items = Pattern.compile("\\s+\\((\\d+),([0-9]+.[0-9]+),(\\p{Sc})([0-9]+(?:\\.[0.9]+)?)\\)")
                        .matcher(split[1])
                        .results()
                        .map(res -> Item.builder()
                                .index(parseInt(res.group(1)))
                                .weight(parseDouble(res.group(2)))
                                .currency(res.group(3))
                                .price(parseDouble(res.group(4)))
                                .build()).collect(toList());

                return new SimpleImmutableEntry<>(parseInt(split[0].trim()), items);
            }).collect(toMap(SimpleImmutableEntry::getKey, SimpleImmutableEntry::getValue));
        } catch (IOException ex) {
            throw new APIException(format("Error when reading '%s'", filePath), ex);
        }
    }
}
