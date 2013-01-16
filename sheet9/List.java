package sheet9;

/**
 * @author Frido Koch
 */
public class List<T> {
    public final T       head;
    public final List<T> tail;

    public List(T head) {
        this.head = head;
        this.tail = null;
    }

    private List(T head, List<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    /**
     * A new list where the given element has been appended.
     */
    public List<T> append(T elem)	{
        if (tail != null) {
            return new List<T>(head, tail.append(elem));
        }
        return new List<T>(head, new List<T>(elem));
    }
    
    /**
     * Whether this and the given list are equal in terms of their
     * elements.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof List) {
            List<?> that = (List<?>) obj;
            return head.equals(that.head) &&
                   (tail == null && that.tail == null ||
                    tail != null && tail.equals(that.tail));
        }
        return false;
    }

    /**
     * Comma-separated string representation of this list.
     */
    @Override
    public String toString() {
        String result = head.toString();
        if (tail != null) {
            result += ',' + tail.toString();
        }
        return result;
    }
}
