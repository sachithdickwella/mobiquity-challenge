package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.pojo.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.AbstractMap.SimpleImmutableEntry;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * The class which implements and expose the core functionality off the class library.
 *
 * @author Sachith Dickwella
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Packer {

    /**
     * Decode the item details from the file provided via the parameter {@code filePath}
     * and calculate the output respective to each of the items.
     * <br/>
     * <br/>
     * <b>Example input data:</b>
     * <pre>{@code
     * 81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)
     * 8 : (1,15.3,€34)
     * 75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)
     * 56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)
     * }</pre>
     * <b>Example output string:</b>
     * <pre>{@code
     * 4
     * -
     * 2,7
     * 8,9
     * }</pre>
     * <p>
     * Core implementation to calculate the output from input reside inside this method
     * along with the support of another two instances methods {@link #mapToItem(String)}
     * and {@link #selectItems(Map.Entry)}.
     * <p>
     *
     * @param filePath of the input data file as a {@link String}. This parameter expects
     *                 an absolute path to a data file and user has to take care of any
     *                 non-standard characters in the path string.
     * @return an instance of {@link String} which represents single row result of input
     * file by a newline. If there's no value for a particular input value, that mimics
     * by a single {@code '-'} character in that line.
     * @throws APIException when data validation from the input file failed or other IO
     *                      operation failed during the same file access.
     *                      <p>
     *                      Throwable defined here actually nto mandatory hence it's a
     *                      {@link RuntimeException} Keeping the declaration of throwable
     *                      to retain the original signature, no need to handle explicitly.
     */
    @NotNull
    public static String pack(@NotNull String filePath) throws APIException {
        var pack = new Packer();

        try (Stream<String> lines = Files.lines(new File(filePath).toPath())) {
            return lines
                    .filter(not(String::isBlank))
                    /*
                     * Map each of the input file lines into {@link Map.Entry<Integer, List>},
                     * so, process items in the downstream much easier access.
                     */
                    .map(pack::mapToItem)
                    .map(entry -> Optional.of(pack.selectItems(entry)
                                    .map(Item::getIndex)
                                    /*
                                     * This sorting is unnecessary if the output index order
                                     * doesn't matter. Anyway, not much expensive operation
                                     * comparing the cost it would take if, haven't order by
                                     * the price and weight at first-hand in 'mapToItem(String)'
                                     * method discuss later.
                                     */
                                    .sorted()
                                    .map(String::valueOf)
                                    .collect(joining(",")))
                            .filter(not(String::isBlank))
                            .orElse("-"))
                    .collect(joining("\n"));
        } catch (IOException ex) {
            throw new APIException(format("Error when reading '%s'", filePath), ex);
        }
    }

    /**
     * Decode the {@code line} parameter data into a {@link Map.Entry} which holds an {@link Integer}
     * key and {@link List} of {@link Item} value which represent the weight limit and following item
     * list respectively mapped to the {@code Map.Entry} data structure.
     * <p>
     * These {@link Map.Entry}s never going to become full-fledged {@link Map} hence they are treated
     * in a {@link Stream} pipeline and mid-stream operators extract only the required elements from
     * the datasets.
     * <br/>
     * <br/>
     * <b>Behavior (For single line):</b>
     * <br/>
     * <ol>
     * <li>Parameter {@code line }get split by {@code ':'} character to extract weight limit at
     * the beginning of each line, trim it and set as the return {@link Map.Entry} key.</li>
     * <li>Use regular-expression to extract item details from second half of the split line and map
     * the matching content into new {@link Item} objects respectively, retaining the data type.</li>
     * <li>As item list extraction happens, sort the items by descending {@link Item#getPrice()} and
     * ascending {@link Item#getWeight()} order for main item selection calculation.
     * <br/>
     * <br/>
     * By doing so, item selection process' time-complexity have reduced by a significant amount. It's
     * worst case scenario brought down to O(n) from potential O(n^2) (quadratic complexity).
     * <br/>
     * <br/>
     * </li>
     * <li>Return the final outcome of data extraction, sorting of the items and weight limit packed
     * into a {@link Map.Entry}.</li>
     * </ol>
     *
     * @param line extracted from the input file which contains the weight limit and items details with
     *             miscellaneous characters that does not participate in the process.
     * @return an instance of {@link Map.Entry} with decoded line information from the input file lines.
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
                        .sorted(
                                comparing(Item::getPrice, reverseOrder())
                                        .thenComparing(Item::getWeight, naturalOrder()))
                        .collect(toList());

                return new SimpleImmutableEntry<>(parseInt(split[0].trim()), items);
            } else {
                throw new APIException("Item list and weight limit not defined properly");
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            throw new APIException(format("Item list '%s' invalid", line), ex);
        }
    }

    /**
     * Select the items from the give line, a.k.a. {@link Map.Entry} base off the weight
     * limit and given item list details.
     * <p>
     * Given sorted list from upstream {@link #mapToItem(String)} method, here only has
     * to search from top of the {@link Item}s list to the bottom.
     * <p>
     * In the worst case  scenario, matching items will be found in first few elements in
     * the list. But, as the priority has given to the {@link Item#getPrice()} during sorting,
     * there's no way to determine correct accumulated weight from the items without checking
     * the entire list of items no matter what is the price.
     * <p>
     * Here, the operation is simple. Keep the sum of weights allowed to be in the package
     * off stream variable (count) and as stream continues, filter-out the items that would
     * exceed the package's weight limit, filtered-in items pass through the downstream, while
     * updating the sum of weights (count) to check for next item from the upstream.
     * <br/>
     * <br/>
     * <b>Note:</b>
     * Decorated with {@link SuppressWarnings} annotation to ignore the sonarlint warning of
     * using {@link Stream#peek(Consumer)} method despite what said in the javadoc. At here,
     * we keep careful consideration of usage of {@code peek()}, thus, ignore the warning.
     *
     * @param entry an instance of {@link Map.Entry} (key-value) pair, weight limit as the
     *              key and {@link List} of {@link Item}s as the value.
     * @return a {@link Stream} of {@link Item}s chosen by given condition to include in a
     * package to send.
     */
    @SuppressWarnings("java:S3864")
    @NotNull
    private Stream<Item> selectItems(@NotNull Map.Entry<Integer, List<Item>> entry) {
        var count = new AtomicReference<>(0D);

        return entry.getValue()
                .stream()
                .filter(item -> count.get() + item.getWeight() <= entry.getKey())
                .peek(item -> count.accumulateAndGet(item.getWeight(), Double::sum));
    }
}
