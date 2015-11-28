package ninja.stavola.friendcaster.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ArrayRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final List<T> list;

    public ArrayRecyclerAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected T getElement(int i) {
        return list.get(i);
    }

    public void add(T element) {
        list.add(element);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection) {
        list.addAll(collection);
        notifyDataSetChanged();
    }

    public void addAll(T... elements) {
        Collections.addAll(list, elements);
        notifyDataSetChanged();
    }

    public void insert(T element, int index) {
        list.add(index, element);
        notifyDataSetChanged();
    }

    public void remove(T element) {
        list.remove(element);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void sort(Comparator<T> comparator) {
        Collections.sort(list, comparator);
        notifyDataSetChanged();
    }
}

