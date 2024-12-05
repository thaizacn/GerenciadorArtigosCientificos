import model.Artigo;
import repository.MongoRepository;
import service.ArtigoService;

import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        MongoRepository repository = new MongoRepository("mongodb://localhost:27017", "GerenciadorArtigosCientificos");
        ArtigoService service = new ArtigoService(repository);

        // Criar um artigo
        Artigo artigo = new Artigo();
        artigo.setTitulo("Ciência da Computação");
        artigo.setAutor("Jane Doe");
        artigo.setResumo("Explorando o futuro da tecnologia.");
        artigo.setAnoPublicacao(2024);

        // Inserir artigo e PDF
        FileInputStream pdfInputStream = new FileInputStream("artigo.pdf");
        String artigoId = service.criarArtigo(artigo, pdfInputStream);
        System.out.println("Artigo criado com ID: " + artigoId);

        // Listar artigos
        service.listarArtigos().forEach(System.out::println);

        // Deletar artigo
        service.deletarArtigo(artigoId);
        System.out.println("Artigo deletado.");
    }
}