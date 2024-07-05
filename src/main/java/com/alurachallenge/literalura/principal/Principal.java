package com.alurachallenge.literalura.principal;

import com.alurachallenge.literalura.model.*;
import com.alurachallenge.literalura.repository.AutorRepository;
import com.alurachallenge.literalura.repository.LibrosRepository;
import com.alurachallenge.literalura.service.ConsumoAPI;
import com.alurachallenge.literalura.service.ConvierteDatos;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class Principal {

    //INSTANCIAS
    Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    private static final String URL_BASE= "https://gutendex.com/books";
    private List<DatosLibros> datosLibros = new ArrayList<>();
    private List<Libros> libros;
    private List<Autor> autores;
    private LibrosRepository repositorioLibro;
    private AutorRepository repositorioAutor;

@Autowired
    public Principal(LibrosRepository repositorioLibro, AutorRepository repositorioAutor) {
        this.repositorioLibro = repositorioLibro;
        this.repositorioAutor = repositorioAutor;
    }


    public void muestraMenu(){
        var opcion = -1;
        while(opcion != 0){
            System.out.println("BIENVENIDO A LITERALURA");
            System.out.println("------MENÚ--------");
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion){
                case 1:
                    buscarLibroEnAPI();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    autoresVivosDeterminadoAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando el programa");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción incorrecta. Intentalo de nuevo");
            }
        }
    }



    private DatosBusqueda datosBusqueda(){
        System.out.println("Inserta el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine().replace(" ", "%20");
        var json = consumoAPI.obtenerDatos(URL_BASE + "/?search=" + nombreLibro);
        //System.out.println(json);//ay que comentar
        DatosBusqueda datos = conversor.obtenerDatos(json, DatosBusqueda.class);
        return datos;
    }


    private Libros agregarLibroBD(DatosLibros datosLibros, Autor autor) {
        Libros nuevoLibro = new Libros(datosLibros, autor);
        repositorioLibro.save(nuevoLibro);
        return nuevoLibro;
    }

    private void buscarLibroEnAPI() {
        DatosBusqueda datosBusqueda = datosBusqueda();

        if (!datosBusqueda.resultados().isEmpty()) {
            DatosLibros datosLibros = datosBusqueda.resultados().get(0);

            List<DatosAutor> autores = datosLibros.autor();
            DatosAutor primerAutor = autores.isEmpty() ? null : autores.get(0);

            Optional<Libros> libroBuscado = repositorioLibro.findByTitulo(datosLibros.titulo());

            if (libroBuscado.isPresent()) {
                System.out.println(libroBuscado.get());
                System.out.println("El libro ya existe en la BD. Intenta con otro libro");
            } else {
                Autor autorEncontrado = null;

                if (primerAutor != null) {
                    Optional<Autor> autorBuscado = repositorioAutor.findByNombre(primerAutor.nombre());

                    if (!autorBuscado.isPresent()) {
                        Autor autorNuevo = new Autor(primerAutor);
                        repositorioAutor.save(autorNuevo);
                        autorEncontrado = autorNuevo;
                    } else {
                        autorEncontrado = autorBuscado.get();
                    }
                }

                Libros libro = agregarLibroBD(datosLibros, autorEncontrado);
                System.out.println(libro);
            }
        } else {
            System.out.println("No se encontró el libro en la API, intenta con otro");
        }
    }


    private void listarLibrosRegistrados() {
       libros = repositorioLibro.findAll();
        libros.stream()
                .forEach(System.out::println);
    }


    private void listarAutoresRegistrados(){
    autores = repositorioAutor.findAll();
    autores.stream()
            .forEach(System.out::println);
    }

    private void autoresVivosDeterminadoAnio(){
        System.out.println("Ingresa el año vivio de autor(es) que desea buscar");
        var anioABuscar =  teclado.nextInt();
        teclado.nextLine();

        try{
            List<Autor> autoreVivosDeterminadaFecha = repositorioAutor.autoresVivosDeterminadoAnio(anioABuscar);
            if (!autoreVivosDeterminadaFecha.isEmpty()){
                autoreVivosDeterminadaFecha.stream().forEach(System.out::println);
            }else{
                System.out.println("No se encontraron autores en ese tiempo");
            }
        }catch (Exception e){
            throw new RuntimeException (e);
        }

    }

    private List<Libros> buscarLibrosPorIdioma(String idioma){
    var datoIdioma = Idioma.fromString(idioma);
        System.out.println("Idioma buscado: " + datoIdioma);

        List<Libros> librosPorIdioma = repositorioLibro.findByIdioma(datoIdioma);
        return librosPorIdioma;
    }

    private void listarLibrosPorIdioma(){
        System.out.println("Ingrese el idioma para buscar los libros: ");
        System.out.println("""
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """);
        var language = teclado.nextLine();
        switch (language){
            case "es":
                List<Libros> librosEspañol = buscarLibrosPorIdioma("[es]");
                librosEspañol.forEach(System.out::println);
                break;
            case "en":
                List<Libros> librosIngles = buscarLibrosPorIdioma("[en]");
                librosIngles.forEach(System.out::println);
                break;
            case "fr":
                List<Libros> librosFrances = buscarLibrosPorIdioma("[fr]");
                librosFrances.forEach(System.out::println);
                break;
            case "pt":
                List<Libros> librosPortugues = buscarLibrosPorIdioma("[pt]");
                librosPortugues.forEach(System.out::println);
                break;
            default:
                System.out.println("No hay libros almacenados en ese idioma");

        }
    }
}
