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
        artigo.setTitulo("Análise e Desenvolvimento de Sistemas - The end!");
        artigo.setAutor("Thaiza Nascimento");
        artigo.setResumo("Sessão: Conquistando meu diplominha.");
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
