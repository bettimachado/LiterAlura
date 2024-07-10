package com.alura.literalura.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "autores")
@JsonIgnoreProperties(ignoreUnknown = true)

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("name")
    private String autor;

    @JsonAlias("birth_year")
    private Integer birth_year;

    @JsonAlias("death_year")
    private Integer death_year;

    @ManyToMany(mappedBy = "autores", fetch=FetchType.EAGER)
    private List<Livro> livros;

    public Autor() {
    }

    @JsonCreator
    public Autor(
            @JsonProperty("name") String autor,
            @JsonProperty("birth_year") Integer birth_year,
            @JsonProperty("death_year") Integer death_year){
        this.autor = autor;
        this.birth_year = birth_year;
        this.death_year = death_year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public Integer getBirth_year() {
        return birth_year;
    }

    public Integer getDeath_year() {
        return death_year;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return Objects.equals(id, autor.id) &&
                Objects.equals(autor, autor.autor) &&
                Objects.equals(birth_year, autor.birth_year) &&
                Objects.equals(death_year, autor.death_year);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id,autor, birth_year, death_year);
    }

    @Override
    public String toString() {
        return  "Autor:" +
                "id= " + id + "\n" +
                "Autor= " + autor + "\n" +
                "Ano de nascimento= " + birth_year + "\n" +
                "Ano_falecimento= " + death_year;

    }
}



