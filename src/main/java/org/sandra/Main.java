package org.sandra;

import org.apache.hc.core5.http.ParseException;
import org.sandra.Models.Author;
import org.sandra.Models.Movies;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        Scanner scanner = new Scanner(System.in);

        // ServiceManager-objekt
        ServiceManager<Movies> movieService = new ServiceManager<>();
        ServiceManager<Author> authorService = new ServiceManager<>();

        //Meny
        while (true) {
            System.out.println("\nVälj ett alternativ:");
            System.out.println("1. Hämta alla filmer");
            System.out.println("2. Hämta en film efter ID");
            System.out.println("3. Lägg till en ny film");
            System.out.println("4. Lägg till en ny författare");
            System.out.println("5. Uppdatera en film");
            System.out.println("6. Ta bort en film");
            System.out.println("7. Avsluta");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // GET LISTA: Hämta alla filmer
                    List<Movies> moviesList = movieService.sendGetRequestList(
                            "http://enskilduppgift3-env.eba-dep3smvq.eu-north-1.elasticbeanstalk.com/movies",
                            new com.fasterxml.jackson.core.type.TypeReference<List<Movies>>() {
                            }
                    );

                    //Iterera över filmer
                    for (Movies movie : moviesList) {
                        System.out.println("Titel: " + movie.getTitle());
                        System.out.println("År: " + movie.getYear());

                        // Författarens information om den finns
                        if (movie.getAuthor() != null) {
                            System.out.println("Författare: " + movie.getAuthor().getName());
                            System.out.println("Författarens ålder: " + movie.getAuthor().getAge());
                        } else {
                            System.out.println("Ingen författare kopplad till filmen.");
                        }

                        System.out.println("--------------------");
                    }
                    break;

                case 2:
                    // GET ID: Hämta en film efter ID
                    System.out.println("Ange filmens ID: ");
                    long movieId = scanner.nextLong();
                    scanner.nextLine();

                    Movies movieById = movieService.sendGetRequest(
                            "http://enskilduppgift3-env.eba-dep3smvq.eu-north-1.elasticbeanstalk.com/movies/" + movieId,
                            Movies.class
                    );

                    // Kontrollera om filmen finns
                    if (movieById == null) {
                        break;
                    }

                    //Filmens information
                    System.out.println("Titel: " + movieById.getTitle());
                    System.out.println("År: " + movieById.getYear());

                    // Författarens information
                    if (movieById.getAuthor() != null) {
                        System.out.println("Författare: " + movieById.getAuthor().getName());
                        System.out.println("Författarens ålder: " + movieById.getAuthor().getAge());
                    }
                    break;

                case 3:
                    // POST: Skapa en film kopplad till befintlig författare
                    System.out.println("Ange filmens titel: ");
                    String title = scanner.nextLine();

                    System.out.println("Ange filmens år: ");
                    int year = scanner.nextInt();

                    Author myAuth = new Author();
                    System.out.println("Ange författarens ID");
                    myAuth.setId(scanner.nextLong());
                    scanner.nextLine();

                    Movies newMovie = new Movies(title, year);
                    newMovie.setAuthor(myAuth);

                    Movies savedMovie = movieService.sendPostRequest(
                            "http://enskilduppgift3-env.eba-dep3smvq.eu-north-1.elasticbeanstalk.com/movies",
                            newMovie,
                            Movies.class
                    );

                    if (savedMovie != null) {
                        System.out.println("Sparad film: " + savedMovie.getTitle() + " med ID: " + savedMovie.getId());
                    }
                    break;

                case 4:
                    //POST: Lägg till en ny författare
                    System.out.println("Ange författarens namn");
                    String authorName = scanner.nextLine();

                    System.out.println("Ange författarens ålder");
                    int authorAge = scanner.nextInt();
                    scanner.nextLine();

                    Author newAuthor = new Author();
                    newAuthor.setName(authorName);
                    newAuthor.setAge(authorAge);

                    Author savedAuthor = authorService.sendPostRequest (
                            "http://enskilduppgift3-env.eba-dep3smvq.eu-north-1.elasticbeanstalk.com/author",
                            newAuthor,
                            Author.class
                    );

                    if (savedAuthor != null){
                        System.out.println("Ny författare sparad: " + savedAuthor.getName() + "Med ID: " + savedAuthor.getId());
                    }
                    break;

                case 5:
                    //PUT: Uppdatera en film
                    System.out.println("Ange ID på filmen som ska uppdateras: ");
                    long updateId = scanner.nextLong();
                    scanner.nextLine();

                    Movies movieToUpdate = movieService.sendGetRequest(
                            "http://enskilduppgift3-env.eba-dep3smvq.eu-north-1.elasticbeanstalk.com/movies/" + updateId,
                            Movies.class
                    );

                    System.out.println("Ange ny titel: ");
                    String newTitle = scanner.nextLine();
                    System.out.println("Ange nytt år: ");
                    int newYear = scanner.nextInt();
                    scanner.nextLine();

                    movieToUpdate.setTitle(newTitle);
                    movieToUpdate.setYear(newYear);

                    Movies updatedMovie = movieService.sendPutRequest(
                            "http://enskilduppgift3-env.eba-dep3smvq.eu-north-1.elasticbeanstalk.com/movies/" + updateId,
                            movieToUpdate,
                            Movies.class
                    );

                    System.out.println("Uppdaterad film: " + updatedMovie.getTitle() + " från år: " + updatedMovie.getYear());
                    break;

                case 6:
                    // Ta bort en film (DELETE)
                    System.out.println("Ange ID på filmen som ska tas bort: ");
                    long deleteId = scanner.nextLong();
                    scanner.nextLine();

                    movieService.sendDeleteRequest( "http://enskilduppgift3-env.eba-dep3smvq.eu-north-1.elasticbeanstalk.com/movies/" + deleteId);
                    System.out.println("Filmen med ID " + deleteId + " har raderats.");
                    break;

                case 7:
                    //Avsluta programmet
                    System.out.println("Avslutar programmet");
                    scanner.close();
                    return;

                default:
                    System.out.println("Ogiltigt val, försök igen");
            }
        }
    }
}