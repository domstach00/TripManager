package com.example.tripmanager.shared.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A generic builder for constructing objects with chainable modifications.
 *
 * @param <T> the type of object to build.
 */
public class GenericBuilder<T> {
    /**
     * A supplier that provides a new instance of T.
     */
    private final Supplier<T> instantiator;

    /**
     * A list of modifiers (operations) to be applied to the instance.
     */
    private final List<Consumer<T>> instanceModifiers = new ArrayList<>();

    /**
     * Static factory method to create a new GenericBuilder.
     *
     * @param <T> the type of object to build.
     * @param instantiator A supplier that creates a new instance of T.
     * @return A new instance of GenericBuilder.
     */
    public static <T> GenericBuilder<T> of(Supplier<T> instantiator) {
        return new GenericBuilder<>(instantiator);
    }

    /**
     * Constructs a GenericBuilder with the provided instantiator. SomeObj::new
     *
     * @param instantiator A supplier that creates a new instance of T. Must not be null.
     */
    public GenericBuilder(Supplier<T> instantiator) {
        if (instantiator == null) {
            throw new IllegalArgumentException("Instantiator cannot be null");
        }
        this.instantiator = instantiator;
    }

    /**
     * Adds a modification to be applied on the object during building.
     *
     * @param <U> The type of the value to be set.
     * @param consumer A BiConsumer that takes an instance of T and a value of U, and modifies the instance.
     * @param value The value to apply using the consumer.
     * @return The current GenericBuilder instance for chainable calls.
     */
    public <U> GenericBuilder<T> with(BiConsumer<T, U> consumer, U value) {
        if (consumer == null) {
            throw new IllegalArgumentException("Consumer cannot be null");
        }
        Consumer<T> c = (instance) -> consumer.accept(instance, value);
        // Add a lambda that applies the consumer to the instance with the provided value.
        this.instanceModifiers.add(c);
        return this;
    }

    /**
     * Builds the final object by creating an instance and applying all modifications.
     * Note: This builder clears its modifications after build() is called,
     * meaning it is intended for single-use. To allow reusability, consider removing the clear() operation.
     *
     * @return The fully built and modified instance of T.
     */
    public T build() {
        T value = buildWithoutClearing();
        // Clear the modifications to avoid reusing them on subsequent builds.
        reset();
        return value;
    }

    /**
     * Builds an instance without clearing the list of modifications.
     * This method allows multiple objects to be built with the same modifications.
     *
     * @return A new instance of T with the applied modifications.
     */
    public T buildWithoutClearing() {
        T instance = instantiator.get();
        instanceModifiers.forEach(modifier -> modifier.accept(instance));
        return instance;
    }

    /**
     * Resets the builder by clearing all stored modifications.
     * This method is useful when you want to reuse the builder for different objects
     * without carrying over previous modifications.
     */
    public void reset() {
        instanceModifiers.clear();
    }
}
