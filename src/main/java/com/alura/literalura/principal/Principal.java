package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DadosLivros;
import com.alura.literalura.model.Livro;
import com.alura.literalura.model.Raiz;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import com.alura.literalura.service.ConsumoApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private ConsumoApi consumo = new ConsumoApi();
    private Scanner leitura = new Scanner(System.in);
    private DadosLivros dadosLivros = new DadosLivros();

    private final String ENDERECO = "https://gutendex.com//books/?search=";

    private LivroRepository repositorio;

    private AutorRepository autorRepositorio;

    public Principal(LivroRepository repositorio, AutorRepository autorRepositorio) {
        this.repositorio = repositorio;
        this.autorRepositorio = autorRepositorio;
    }

    public void exibeMenu() throws JsonProcessingException {
        var escolha = -1;
        while (escolha != 0) {
            var menu = """
                    
                    Escolha o número da sua opção:
                     1 - Buscar livro pelo título
                     2 - Listar os livros registrados
                     3 - Listar os autores registrados
                     4 - Listar os autores vivos em determinado ano
                     5 - Listar livros em determinado idioma
                     0 - Sair
                    """;

            System.out.println(menu);
            if (leitura.hasNextInt()) {
                escolha = leitura.nextInt();
                leitura.nextLine(); // Consome a linha nova restante
            } else {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                leitura.nextLine(); // Consome a entrada inválida
                continue;
            }
//            escolha = leitura.nextInt();
//            leitura.nextLine();
            try {

                switch (escolha) {
                    case 1:
                        buscarLivroPeloTitulo();
                        break;
                    case 2:
                        listarLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorAno();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            } catch (JsonProcessingException e) {
                System.out.println("Erro ao processar JSON: " + e.getMessage());
            }
        }
    }

    private void buscarLivroPeloTitulo() throws JsonProcessingException {
        System.out.print("Digite o título do livro a buscar: ");
        var tituloLivro = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + tituloLivro.replace(" ", "%20"));
        ObjectMapper mapper = new ObjectMapper();
        Raiz raiz = mapper.readValue(json, Raiz.class);

        List<Livro> livros = raiz.getResults();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado com o título: " + tituloLivro);
        } else if (livros.size() == 1) {
            Livro livro = livros.get(0);
            salvarOuAtualizarLivro(livro);
        } else {
            // Se houver múltiplos livros, permita que o usuário escolha um
            System.out.println("Múltiplos livros encontrados.");
            System.out.println("Escolha um:");
            for (int i = 0; i < livros.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, livros.get(i).getTitulo());
                System.out.println();
            }
            System.out.print("Digite o número do livro desejado: ");
            int escolhaLivro = leitura.nextInt();
            leitura.nextLine(); // Consome a nova linha deixada pelo nextInt()

            if (escolhaLivro > 0 && escolhaLivro <= livros.size()) {
                Livro livro = livros.get(escolhaLivro - 1);
                salvarOuAtualizarLivro(livro);
            } else {
                System.out.println("Opção inválida.");
            }
        }
    }

    private void salvarOuAtualizarLivro(Livro livro) {
        for (Autor autor : livro.getAutores()) {
            Optional<Autor> autorExistente = autorRepositorio.findByAutor(autor.getAutor());
            if (autorExistente.isPresent()) {
                autor.setId(autorExistente.get().getId());
            } else {
                autorRepositorio.save(autor);
            }
        }
        Optional<Livro> livroExistente = repositorio.findByTitulo(livro.getTitulo());
        if (livroExistente.isPresent()){
            Livro livroParaAtualizar = livroExistente.get();
            livroParaAtualizar.setAutores(livro.getAutores());
            livroParaAtualizar.setIdiomas(livro.getIdiomas());
            livroParaAtualizar.setBirthYears(livro.getBirthYears());
            livroParaAtualizar.setDeathYears(livro.getDeathYears());
            repositorio.save(livroParaAtualizar);
            System.out.println("Livro já existente no cadastro: " + livroParaAtualizar);
        } else {
            repositorio.save(livro);
            System.out.println("Livro salvo: " + livro);
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livrosRegistrados = repositorio.findAll();

        if (livrosRegistrados.isEmpty()) {
            System.out.println("Não há livros registrados");
        } else {
            System.out.println("Livros inseridos no Banco de Dados");
            for (Livro livro : livrosRegistrados) {
                System.out.println(livro);
            }
        }
    }

    private void listarAutoresRegistrados(){
            List<Autor> autoresRegistrados = autorRepositorio.findAll();
            if (autoresRegistrados.isEmpty()) {
                System.out.println("Não há autores registrados");
            } else {
                System.out.println("Autores inseridos no Banco de Dados");
                for (Autor autor : autoresRegistrados) {
                    System.out.println(autor);
                    List<Livro> livros = autor.getLivros();
                    if(livros.isEmpty()){
                        System.out.println(" Nenhum livro registrado para este autor");
                    }else {
                        System.out.println(" Livros:");
                        for (Livro livro: livros) {
                            System.out.println(" "+ livro.getTitulo());

                        }
                    }
                    System.out.println();
                }
            }
    }
    private void listarAutoresVivosPorAno() {
        System.out.print("Digite o ano: ");
        int ano = Integer.parseInt(leitura.nextLine());
        List<Autor> autoresRegistrados = autorRepositorio.findAll();
        List<Autor> autoresVivos = autoresRegistrados.stream()
                .filter(autor -> (autor.getBirth_year() <= ano && (autor.getDeath_year() == null || autor.getDeath_year() >= ano)))
                .toList();
        if (autoresVivos.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo no ano de " + ano);
        } else {
            System.out.println("Autores vivos no ano de " + ano +":");
            for (Autor autor : autoresVivos) {
                System.out.println(autor);
                System.out.println();
            }
        }
    }

    public void listarLivrosPorIdioma() {
        var menuIdiomas = """
                ES = Espanhol
                EN = Inglês
                FR = Francês
                PT = Português
                """;
        System.out.println(menuIdiomas);

//        System.out.println("Idiomas: ES = Espanhol, EN = Inglês, FR = Francês e PT = Português");
        String idioma = leitura.nextLine().toLowerCase();
        List<Livro> livrosRegistrados = repositorio.findAll();
        List<Livro> livrosPorIdioma = livrosRegistrados.stream()
                .filter(livro -> livro.getIdiomas().contains(idioma))
                .toList();
        if (livrosPorIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma " + idioma);
        } else {
            System.out.println("Livros no idioma " + idioma);
            for (Livro livro : livrosPorIdioma) {
                System.out.println(livro);
            }
        }
    }
}
