package date;

//класс узла списка
public class Node<E> {
    private final E date;
    private Node<E> prev;
    private Node<E> next;

    public Node(E e) {
        this.date = e;
    }

    public Node<E> getPrev() {
        return prev;
    }

    public void setPrev(Node<E> prev) {
        this.prev = prev;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public E getDate() {
        return date;
    }

}
