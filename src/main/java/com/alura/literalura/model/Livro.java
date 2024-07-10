package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "titulos")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("title")
    @Column(unique = true)
    private String titulo;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "titulo_autores",
            joinColumns = @JoinColumn(name="titulo_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    @JsonAlias("authors")
    private List<Autor> autores;


    @Column(name = "autores")
    private String autoresFormatados;

    @JsonAlias("languages")
    @Column(name = "idiomas")
    private String idiomas;

    @Column(name = "birth_years")
    private String birthYears;

    @Column(name = "death_years")
    private String deathYears;

    @Column(name = "download_count")
    private String downloadCount;

    public Livro () {

    }

    @JsonCreator
    public Livro(
           @JsonProperty("title") String titulo,
           @JsonProperty("download_count") String downloadCount,
           @JsonProperty("authors") List<Autor> autores,
           @JsonProperty ("languages") List<String> idiomas) {
        this.titulo = titulo;
        this.autores = autores;
        this.idiomas = String.join(",", idiomas);
        this.downloadCount = downloadCount;
        this.autoresFormatados = formatAutores(autores);
        this.birthYears = autores.stream()
                .map(a -> a.getBirth_year() != null ? a.getBirth_year().toString() : "").collect(Collectors.joining(","));
        this.deathYears = autores.stream()
                .map(a -> a.getDeath_year() != null ? a.getDeath_year().toString() : "").collect(Collectors.joining(","));
    }

    private String formatAutores(List<Autor > autores ) {
        return autores.stream().map(Autor::getAutor).collect(Collectors.joining(","));
    }

    public Long getId() {
        return id;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public void setBirthYears(String birthYears) {
        this.birthYears = birthYears;
    }

    public void setDeathYears(String deathYears) {
        this.deathYears = deathYears;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }


    public String getIdiomas() {
        return idiomas;
    }

    public String getBirthYears() {
        return birthYears;
    }

    public String getDeathYears() {
        return deathYears;
    }

    public String getDownloadCount() {
        return downloadCount;
    }
    public void setDownloadCount(String downloadCount) {
        this.downloadCount = downloadCount;
    }
    //    public List<Autor> getAutores() {
//        return List.of(autores.split(",")).stream()
//                .map(str -> {
//                    String[] parts = str.split(":");
//                    return new Autor(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
//                }).collect(Collectors.toList());
//    }
//
//    public List<String> getIdioma() {
//        return List.of(idioma.split(","));
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return Objects.equals(id, livro.id) &&
                Objects.equals(titulo, livro.titulo) &&
                Objects.equals(autoresFormatados, livro.autoresFormatados) &&
                Objects.equals(idiomas, livro.idiomas) &&
                Objects.equals(downloadCount, livro.downloadCount) ;
 //               Objects.equals(birthYears, livro.birthYears) &&
 //               Objects.equals(deathYears, livro.deathYears) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, autoresFormatados, idiomas, downloadCount);
//        return Objects.hash(id, titulo, autoresFormatados, idiomas, downloadCount, birthYears, deathYears);
    }

    @Override
    public String toString() {
        return "Livro: " +
                "id= " +id+ "\n" +
                "Título= " + titulo + "\n" +
                "Autores= " + autoresFormatados + "\n" +
                "Idioma= " + idiomas + "\n" +
                "Número de downloads= " + downloadCount +"\n" ;
//                ", birthYears= " + birthYears +
//                ", deathYears= " + deathYears;
    }
}
