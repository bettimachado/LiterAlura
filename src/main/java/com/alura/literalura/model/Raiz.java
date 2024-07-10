package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Raiz {
    private final int count;
    private final Object next;
    private final Object previous;
    private final List<Livro> results;

    @JsonCreator
    public Raiz(
            @JsonProperty("count") int count,
            @JsonProperty("next") Object next,
            @JsonProperty("previous") Object previous,
            @JsonProperty("results")  List<Livro> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public Object getNext() {
        return next;
    }

    public Object getPrevious() {
        return previous;
    }

    public List<Livro> getResults() {
        return results;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Raiz raiz = (Raiz) o;
        return count == raiz.count && Objects.equals(next, raiz.next) && Objects.equals(previous, raiz.previous) && Objects.equals(results, raiz.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, next, previous, results);
    }

    @Override
    public String toString() {
        return "Raiz{" +
                "count=" + count +
                ", next=" + next +
                ", previous=" + previous +
                ", results=" + results +
                '}';
    }
}
