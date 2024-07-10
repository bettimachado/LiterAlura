package com.alura.literalura.model;

import java.util.ArrayList;
import java.util.List;

public class DadosLivros {
    private List<Livro> livrosBuscados;

    public DadosLivros() {
        this.livrosBuscados = new ArrayList<>();
    }

    public void adicionarLivro(Livro livro) {
        this.livrosBuscados.add(livro);
    }

    public List<Livro> getLivrosBuscados() {
        return livrosBuscados;
    }
}