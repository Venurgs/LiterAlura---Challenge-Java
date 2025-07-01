package com.example.literatura;

import com.example.literatura.dto.ApiResponse;
import com.example.literatura.dto.BookData;
import com.example.literatura.model.Autor;
import com.example.literatura.model.Livro;
import com.example.literatura.repository.AutorRepository;
import com.example.literatura.repository.LivroRepository;
import com.example.literatura.service.ApiConsumer;
import com.example.literatura.service.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

    private static final String API_BASE_URL = "https://gutendex.com/books/?search=";
    private final ApiConsumer apiConsumer = new ApiConsumer();
    private final DataConverter dataConverter = new DataConverter();
    private final Scanner scanner = new Scanner(System.in);

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    @Autowired
    public LiteraturaApplication(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraturaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        int option = -1;
        while (option != 0) {
            displayMenu();
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1: searchBookByTitle(); break;
                    case 2: listRegisteredBooks(); break;
                    case 3: listRegisteredAuthors(); break;
                    case 4: listLivingAuthorsInYear(); break;
                    case 5: listBooksByLanguage(); break;
                    case 0: System.out.println("Cerrando la aplicación..."); break;
                    default: System.out.println("Opción inválida. Intente de nuevo."); break;
                }
            } else {
                System.out.println("Por favor, ingrese un número válido.");
                scanner.next();
            }
        }
    }

    private void displayMenu() {
        System.out.println("\nElija una opción:");
        System.out.println("1- Buscar libro por título");
        System.out.println("2- Listar libros registrados");
        System.out.println("3- Listar autores registrados");
        System.out.println("4- Listar autores vivos en un determinado año");
        System.out.println("5- Listar libros por idioma");
        System.out.println("0- Salir");
    }

    private void searchBookByTitle() {
        System.out.print("Escriba el nombre del libro a buscar: ");
        String title = scanner.nextLine();
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String fullUrl = API_BASE_URL + encodedTitle;

        try {
            String jsonResponse = apiConsumer.getData(fullUrl);
            if (jsonResponse == null || jsonResponse.isEmpty() || jsonResponse.contains("\"count\":0")) {
                System.out.println("No se encontraron libros con ese título en la API.");
                return;
            }

            ApiResponse apiResponse = dataConverter.getData(jsonResponse, ApiResponse.class);

            Optional<BookData> foundBookData = apiResponse.results().stream().findFirst();

            if (foundBookData.isPresent()) {
                BookData bookData = foundBookData.get();

                if (livroRepository.findByTituloIgnoreCase(bookData.title()).isPresent()) {
                    System.out.println("\nEl libro '" + bookData.title() + "' ya se encuentra registrado.");
                    return;
                }

                Autor autor;
                if (bookData.authors() == null || bookData.authors().isEmpty()) {
                    Optional<Autor> autorDesconocido = autorRepository.findByNomeIgnoreCase("Desconocido");
                    if (autorDesconocido.isPresent()) {
                        autor = autorDesconocido.get();
                    } else {
                        autor = new Autor("Desconocido", null, null);
                    }
                } else {
                    String authorName = bookData.authors().get(0).name();
                    Optional<Autor> autorExistente = autorRepository.findByNomeIgnoreCase(authorName);
                    if (autorExistente.isPresent()) {
                        autor = autorExistente.get();
                    } else {
                        autor = new Autor(
                                bookData.authors().get(0).name(),
                                bookData.authors().get(0).birthYear(),
                                bookData.authors().get(0).deathYear()
                        );
                    }
                }

                String idioma = bookData.languages().isEmpty() ? "un" : bookData.languages().get(0);
                Livro livro = new Livro(bookData.title(), idioma, autor);

                livroRepository.save(livro);

                System.out.println("\n¡Libro guardado con éxito!");
                System.out.println(livro);

            } else {
                System.out.println("No se pudo encontrar un libro válido en la respuesta de la API.");
            }
        } catch (RuntimeException e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
        }
    }

    private void listRegisteredBooks() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void listRegisteredAuthors() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listLivingAuthorsInYear() {
        System.out.print("Ingrese el año para buscar autores vivos: ");
        int ano = scanner.nextInt();
        scanner.nextLine();

        List<Autor> autoresVivos = autorRepository.findAutoresVivosEmAno(ano);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + ano + ".");
        } else {
            autoresVivos.forEach(System.out::println);
        }
    }

    private void listBooksByLanguage() {
        System.out.print("Ingrese el idioma (ej: es, en, fr, pt): ");
        String idioma = scanner.nextLine();

        List<Livro> livrosPorIdioma = livroRepository.findByIdioma(idioma);
        if (livrosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma '" + idioma + "'.");
        } else {
            livrosPorIdioma.forEach(System.out::println);
        }
    }
}