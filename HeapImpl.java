import java.util.Arrays;

class HeapImpl<T extends Comparable<? super T>> implements Heap<T> {
    private static final int INITIAL_CAPACITY = 128;
    private T[] _storage;
    private int _numElements;

    @SuppressWarnings("unchecked")
    public HeapImpl () {
        _storage = (T[]) new Comparable[INITIAL_CAPACITY];
        _numElements = 0;
    }

    /**
	 * Adds to the heap
	 * @param data is what's added to the heap
	 */
    @SuppressWarnings("unchecked")
    public void add (T data) {
        if (_numElements + 1 > _storage.length) {
        	int newCapacity = _storage.length * 2;
            T[] newStorage = (T[]) new Comparable[newCapacity];
            
            for (int i = 0; i < _storage.length; i++) {
                newStorage[i] = _storage[i];
            }

            _storage = newStorage;
        }
        _storage[_numElements] = data;
        _numElements++;

        int index = _numElements - 1;
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (_storage[parentIndex].compareTo(_storage[index]) >= 0) {
                break;
            }
            T temp = _storage[parentIndex];
            _storage[parentIndex] = _storage[index];
            _storage[index] = temp;

            index = parentIndex;
        }
    }

    /**
	 * Removes the first in the heap
	 */
    public T removeFirst () {
        if (_numElements == 0) {
            return null;
        }
        T firstElement = _storage[0];
        _storage[0] = _storage[--_numElements];
        _storage[_numElements] = null;
        int index = 0;
        while (true) {
            int childIndex = index * 2 + 1;
            if (childIndex >= _numElements) {
                break;
            }
            if (childIndex + 1 < _numElements &&
                _storage[childIndex].compareTo(_storage[childIndex + 1]) < 0) {
                childIndex++;
            }
            if (_storage[childIndex].compareTo(_storage[index]) > 0) {
                T temp = _storage[childIndex];
                _storage[childIndex] = _storage[index];
                _storage[index] = temp;

                index = childIndex;
            } else {
                break;
            }
        }

        return firstElement;
    }

    public int size () {
        return _numElements;
    }
}