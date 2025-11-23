package gym.vitae.views.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * @param <E> Especifica el tipo de elemento que posiblemente sea un View
 */
public class UndoRedo<E> implements Iterable<E> {

  private final Deque<E> stack1;
  private final Deque<E> stack2;

  public UndoRedo() {
    stack1 = new ArrayDeque<>();
    stack2 = new ArrayDeque<>();
  }

  public void add(E item) {
    stack1.addLast(item);
    stack2.clear();
  }

  public E undo() {
    if (stack1.size() > 1) {
      stack2.addLast(stack1.removeLast());
      return stack1.peekLast();
    } else {
      return null;
    }
  }

  public E redo() {
    if (!stack2.isEmpty()) {
      E item = stack2.removeLast();
      stack1.addLast(item);
      return item;
    } else {
      return null;
    }
  }

  public E getCurrent() {
    if (stack1.isEmpty()) {
      return null;
    } else {
      return stack1.peekLast();
    }
  }

  public boolean isUndoAble() {
    return stack1.size() > 1;
  }

  public boolean isRedoAble() {
    return !stack2.isEmpty();
  }

  public void clear() {
    stack1.clear();
    stack2.clear();
  }

  public void clearRedo() {
    stack2.clear();
  }

  @Override
  public Iterator<E> iterator() {
    return new MyIterator();
  }

  private class MyIterator implements Iterator<E> {

    private final Iterator<E> it1 = stack1.iterator();
    private final Iterator<E> it2 = stack2.iterator();

    @Override
    public boolean hasNext() {
      return it1.hasNext() || it2.hasNext();
    }

    @Override
    public E next() {
      if (it1.hasNext()) {
        return it1.next();
      } else {
        return it2.next();
      }
    }
  }
}
