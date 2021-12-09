package com.mobiquity.pojo;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

import static java.lang.String.valueOf;

/**
 * Plain old Java object to contain item details.
 *
 * @author Sachith Dickwella
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
     *
     */
    @Builder
    private Item(int index, double weight, @NotNull String currency, double price) {
        this.index = index;
        this.weight = weight;
        this.currency = currency;
        this.price = price;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Item.class.getSimpleName() + "(", ")")
                .add(valueOf(index))
                .add(valueOf(weight))
                .add(currency + price)
                .toString();
    }
}
