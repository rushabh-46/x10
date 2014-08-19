package apgas.util;

import java.io.Serializable;

import apgas.BadPlaceException;
import apgas.Place;

/**
 * The {@link GlobalCell} class provides a mutable global reference.
 *
 * @param <T>
 *          the type of the reference
 */
public class GlobalCell<T> implements Serializable {
  private static final long serialVersionUID = -1791967739280529895L;

  /**
   * A {@link GlobalRef} to a {@link Cell} instance.
   */
  protected final GlobalRef<Cell<T>> ref = new GlobalRef<>(new Cell<T>());

  /**
   * Constructs a new {@link GlobalCell}.
   */
  public GlobalCell() {
  }

  /**
   * Returns the home {@link Place} of this {@link GlobalCell} instance.
   *
   * @return a place
   */
  public Place home() {
    return ref.home();
  }

  /**
   * Returns the current object in this {@link GlobalCell} instance.
   *
   * @return this global cell's content
   * @throws BadPlaceException
   *           if not invoked from the home place of the global cell
   */
  public T get() {
    return ref.get().get();
  }

  /**
   * Sets the object in this {@link GlobalCell} instance.
   *
   * @param t
   *          an object
   * @throws BadPlaceException
   *           if not invoked from the home place of the global cell
   */
  public void set(T t) {
    ref.get().set(t);
  }
}
