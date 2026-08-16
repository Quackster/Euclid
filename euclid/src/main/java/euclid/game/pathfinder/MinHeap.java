package euclid.game.pathfinder;

public class MinHeap<T extends Comparable<T>> {

    private int count;
    private int capacity;
    private T[] array;

    public int getCount() {
        return count;
    }

    public MinHeap() {
        this(16);
    }

    @SuppressWarnings("unchecked")
    public MinHeap(int capacity) {
        this.count = 0;
        this.capacity = capacity;
        this.array = (T[]) new Comparable[capacity];
    }

    public void buildHeap() {
        for (int position = (count - 1) >> 1; position >= 0; position--) {
            minHeapify(position);
        }
    }

    public void add(T item) {
        count++;
        if (count > capacity) {
            doubleArray();
        }
        array[count - 1] = item;
        int position = count - 1;
        int parentPosition = (position - 1) >> 1;

        while (position > 0 && array[parentPosition].compareTo(array[position]) > 0) {
            T temp = array[position];
            array[position] = array[parentPosition];
            array[parentPosition] = temp;
            position = parentPosition;
            parentPosition = (position - 1) >> 1;
        }
    }

    private void doubleArray() {
        capacity <<= 1;
        @SuppressWarnings("unchecked")
        T[] tempArray = (T[]) new Comparable[capacity];
        System.arraycopy(array, 0, tempArray, 0, count);
        array = tempArray;
    }

    public T peek() {
        if (count == 0) throw new IllegalStateException("Heap is empty");
        return array[0];
    }

    public T extractFirst() {
        if (count == 0) throw new IllegalStateException("Heap is empty");
        T temp = array[0];
        array[0] = array[count - 1];
        count--;
        minHeapify(0);
        return temp;
    }

    private void minHeapify(int position) {
        while (true) {
            int left = (position << 1) + 1;
            int right = left + 1;
            int minPosition;

            if (left < count && array[left].compareTo(array[position]) < 0) {
                minPosition = left;
            } else {
                minPosition = position;
            }

            if (right < count && array[right].compareTo(array[minPosition]) < 0) {
                minPosition = right;
            }

            if (minPosition != position) {
                T mheap = array[position];
                array[position] = array[minPosition];
                array[minPosition] = mheap;
                position = minPosition;
            } else {
                return;
            }
        }
    }
}
