package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.pojo.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.AbstractMap.SimpleImmutableEntry;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Class implements and expose the core functionality off the library.
 *
 * @author Sachith Dickwella
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Packer {

    /**
     * @param filePath
     * @return
     */
    @NotNull
    public static String pack(@NotNull String filePath) throws APIException {
        var pack = new Packer();

        try (Stream<String> lines = Files.lines(new File(filePath).toPath())) {
            return lines
                    .filter(line -> !line.isBlank())
                    .map(pack::mapToItem)
                    .map(pack::selectItems)
                    .collect(joining("\n"));
        } catch (IOException ex) {
            throw new APIException(format("Error when reading '%s'", filePath), ex);
        }
    }

    /**
     * @param line
     * @return
     */
    @NotNull
    private Map.Entry<Integer, List<Item>> mapToItem(@NotNull String line) {
        try {
            var split = line.split(":");
            if (split.length == 2) {
                var items = Pattern.compile("\\s+\\((\\d+),([0-9]+.[0-9]+),(\\p{Sc})([0-9]+(?:\\.[0.9]+)?)\\)")
                        .matcher(split[1])
                        .results()
                        .map(result -> Item.builder()
                                .index(parseInt(result.group(1)))
                                .weight(parseDouble(result.group(2)))
                                .currency(result.group(3))
                                .price(parseDouble(result.group(4)))
                                .build())
                        .sorted(comparing(Item::getPrice, reverseOrder())
                                .thenComparing(Item::getWeight, naturalOrder()))
                        .collect(toList());

                return new SimpleImmutableEntry<>(parseInt(split[0].trim()), items);
            } else {
                throw new APIException("Item list and weight limit not defined properly");
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            throw new APIException(format("Item data '%s' invalid", line), ex);
        }
    }

    /**
     * @param entry
     * @return
     */
    @NotNull
    private String selectItems(@NotNull Map.Entry<Integer, List<Item>> entry) {
        var selected = new LinkedList<Item>();

        entry.getValue().forEach(item -> {
            var sum = selected.stream()
                    .mapToDouble(Item::getWeight)
                    .sum();

            if (sum + item.getWeight() <= entry.getKey()) selected.add(item);
        });

        return selected.isEmpty() ? "-" : selected.stream()
                .map(Item::getIndex)
                .sorted()
                .map(String::valueOf)
                .collect(joining(","));
    }
}
