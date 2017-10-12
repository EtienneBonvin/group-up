package ch.epfl.sweng.groupup.lib;

import java.util.NoSuchElementException;
//import java.util.function.Function;

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
        return new Optional<T>();
    }

    /**
     * Wraps a value into an Optional an returns it or returns an empty optional if the value is null.
     * @param element the value to be wrapped into a new Optional
     * @return the Optional representing the given value
     */
    public static <T> Optional<T> from(T element){
        if(element == null)
            return Optional.<T>empty();
        else
            return new Optional<T>(element);
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
     * @throws NoSuchElementException
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

    /**
     * Applies a given function to the value wrapped into the Optional and wraps it into a new Optional.
     * If the Optional is empty, returns an empty Optional.
     * @param f the function to apply to the value
     * @return a new Optional containing the new value
     */
    /*public <U> Optional<U> map(Function<T, U> f) {
        if(empty)
            return Optional.empty();
        else
            return Optional.<U>from(f.apply(content));
    }*/

    /**
     * Applies a given function to the value wrapped into the Optional and wraps it into a new Optional.
     * If the Optional is empty, returns an empty Optional.
     * Note: This method allows Optionals to be monads.
     * @param f the function to apply to the value
     * @return a new Optional containing the new value
     */
    /*public <U> Optional<U> flatMap(Function<T, Optional<U>> f){
        if(empty)
            return Optional.empty();
        else
            return f.apply(content);
    }*/

    /**
     * Applies a predicate p to an Optional and returns the result.
     * If the Optional is empty, the methods always returns false
     * @param p the predicate to apply on the value wrapped into the Optional
     * @return
     */
    /*public boolean match(Function<T, Boolean> p){
        if(empty)
            return false;
        else
            return p.apply(content);
    }*/

    @Override
    public String toString(){
        if(empty)
            return "Optional.empty";
        else
            return "Optional[" + content.toString() + "]";
    }

    @SuppressWarnings("unchecked")
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