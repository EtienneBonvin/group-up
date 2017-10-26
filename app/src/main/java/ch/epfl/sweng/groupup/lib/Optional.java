package ch.epfl.sweng.groupup.lib;

import java.util.NoSuchElementException;

/**
 * The Optional construct allows to deal elegantly with absent values.
 * This generic class represents a Optional monad that can be created to be empty or non-empty
 * and provides the basic methods to work with them.
 * @author xavierpantet
 * @param <T>
 */
public final class Optional<T> {
    private final T content;
    private final boolean empty;

    /**
     * Creates a new non-empty Optional.
     * @param element the value to be wrapped into a new Optional
     */
    private Optional(T element){
        content = element;
        empty = false;
    }

    /**
     * Creates a new empty Optional.
     */
    private Optional(){
        content = null;
        empty = true;
    }

    /**
     * Returns an empty Optional.
     * @return an empty Optional
     */
    public static <T> Optional<T> empty(){
        return new Optional<>();
    }

    /**
     * Wraps a value into an Optional an returns it or returns an empty optional if the value is null.
     * @param element the value to be wrapped into a new Optional
     * @return the Optional representing the given value
     */
    public static <T> Optional<T> from(T element){
        if(element == null)
            return Optional.empty();
        else
            return new Optional<>(element);
    }

    /**
     * Returns true if the Optional is empty, false otherwise.
     * @return whether the Optional is empty or not
     */
    public boolean isEmpty(){
        return empty;
    }

    /**
     * Returns the value wrapped into the Optional or throw a NoSuchElementException if the Optional is empty.
     * @return the value wrapped into the Optional
     * @throws NoSuchElementException if the Optional is empty
     */
    public T get() throws NoSuchElementException {
        if(empty)
            throw new NoSuchElementException();
        else
            return content;
    }

    /**
     * Returns the value wrapped into the Optional or an alternative value if the Optional is empty.
     * @param other the alternative value to return in case the Optional is empty
     * @return the value wrapped into the Optional if non-empty or the alternative value if empty
     */
    public T getOrElse(T other) {
        if(empty)
            return other;
        else
            return content;

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public String toString(){
        if(empty)
            return "Optional.empty";
        else
            return "Optional[" + content.toString() + "]";
    }

    @SuppressWarnings({"unchecked", "ConstantConditions", "SimplifiableIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Optional<T> other = (Optional<T>) obj;
        if(empty && other.empty)
            return true;
        else{
            if(empty || other.empty)
                return false;
            else
                return content.equals(other.content);
        }
    }
}