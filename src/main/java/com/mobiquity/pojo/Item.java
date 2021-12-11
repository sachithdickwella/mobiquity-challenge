package com.mobiquity.pojo;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * Plain old Java object to contain extracted item details and initialize
 * as immutable instances via builder pattern implementation.
 * <p>
 * Builder pattern implementation enforced via Lombok {@link Builder} and
 * {@link Data} annotations.
 * <p>
 * Without Lombok builder pattern implementation;
 *
 * <pre>{@code
 *  public class Item {
 *
 *      private int index;
 *
 *      private double weight;
 *
 *      // ... (omitted for brevity)
 *
 *      public static Builder builder() { return new Builder(); }
 *
 *      public static class Builder {
 *
 *          private Item item;
 *
 *          private Builder() { this.item = new Item(); }
 *
 *          public Builder index(int index) {
 *              this.item.index = index;
 *              return this;
 *          }
 *
 *          public Builder weight(double weight) {
 *              this.item.weight = weight;
 *              return this;
 *          }
 *
 *          public Item build() { return this.item; }
 *      }
 *  }
 * }</pre>
 * <p>
 * In this example, we just show how the Lombok internally generate this
 * boilerplate code for us and keep the code clean and concise.
 * <p>
 * As mentioned before, this implementation of {@link Item} is immutable
 * hence, no modification for the {@code Item} instances once initialized
 * with the decoded data.
 *
 * @author Sachith Dickwella
 * @version 1.0.0
 */
@Data
public class Item {

    /**
     * Index number of the item of the package.
     */
    private int index;
    /**
     * Weight of the item of the package.
     */
    private double weight;
    /**
     * Currency of the price specified of the
     * package.
     */
    private String currency;
    /**
     * Price of the package.
     */
    private double price;

    /**
     * All-args constructor to implement builder pattern via {@link Builder} annotation
     * rather than implement it manually.
     *
     * @param index    of the item as an {@code int}.
     * @param weight   of the item as a {@code double}.
     * @param currency symbol of the item price as {@link String}.
     * @param price    of the item as a {@code double}.
     */
    @Builder
    private Item(int index, double weight, @NotNull String currency, double price) {
        this.index = index;
        this.weight = weight;
        this.currency = currency;
        this.price = price;
    }
}
