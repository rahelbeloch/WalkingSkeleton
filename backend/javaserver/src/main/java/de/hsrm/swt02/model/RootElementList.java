package de.hsrm.swt02.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class represents a RootElement List for Rest.
 */
public class RootElementList implements List<RootElement> {

    private List<RootElement> myList;

    /**
     * Constructor for RootElementList.
     */
    public RootElementList() {
        myList = new LinkedList<RootElement>();
    }

    @SuppressWarnings("unchecked") @Override
    public boolean addAll(@SuppressWarnings("rawtypes") Collection c) {
        return myList.addAll((Collection<RootElement>) c);
    }

    @SuppressWarnings("unchecked") @Override
    public boolean addAll(int index, @SuppressWarnings("rawtypes") Collection c) {
        return myList.addAll(index, (Collection<RootElement>) c);
    }

    @Override
    public void clear() {
        myList.clear();
    }

    @Override
    public boolean contains(Object o) {
        return myList.contains(o);
    }

    @SuppressWarnings("unchecked") @Override
    public boolean containsAll(@SuppressWarnings("rawtypes") Collection c) {
        return myList.containsAll((Collection<RootElement>) c);
    }

    @Override
    public int indexOf(Object o) {
        return myList.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return myList.isEmpty();
    }

    @Override
    public Iterator<RootElement> iterator() {
        return myList.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return myList.lastIndexOf(o);
    }

    @Override
    public ListIterator<RootElement> listIterator() {
        return myList.listIterator();
    }

    @Override
    public ListIterator<RootElement> listIterator(int index) {
        return myList.listIterator(index);
    }

    @Override
    public boolean remove(Object o) {
        return myList.remove(o);
    }

    @SuppressWarnings("unchecked") @Override
    public boolean removeAll(@SuppressWarnings("rawtypes") Collection c) {
        return myList.removeAll((Collection<RootElement>) c);
    }

    @SuppressWarnings("unchecked") @Override
    public boolean retainAll(@SuppressWarnings("rawtypes") Collection c) {
        return myList.retainAll((Collection<RootElement>) c);
    }

    @Override
    public int size() {
        return myList.size();
    }

    @Override
    public List<RootElement> subList(int fromIndex, int toIndex) {
        return myList.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return myList.toArray();
    }

    @SuppressWarnings("unchecked") @Override
    public Object[] toArray(Object[] a) {
        return myList.toArray(a);
    }

    @Override
    public boolean add(RootElement e) {
        return myList.add(e);
    }

    @Override
    public void add(int index, RootElement element) {
        myList.add(index, element);
    }

    @Override
    public RootElement get(int index) {
        return myList.get(index);
    }

    @Override
    public RootElement remove(int index) {
        return myList.remove(index);
    }

    @Override
    public RootElement set(int index, RootElement element) {
        return myList.set(index, element);
    }

}
