package service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.model.Filters;
import model.Artigo;
import org.bson.Document;
import org.bson.types.ObjectId;
import repository.MongoRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ArtigoService {
    private final MongoRepository repository;

    public ArtigoService(MongoRepository repository) {
        this.repository = repository;
    }

    public String criarArtigo(Artigo artigo, InputStream pdfInputStream) {
        GridFSBucket gridFSBucket = repository.getGridFSBucket();

        ObjectId pdfId = gridFSBucket.uploadFromStream(artigo.getTitulo() + ".pdf", pdfInputStream);

        Document document = new Document("titulo", artigo.getTitulo())
                .append("autor", artigo.getAutor())
                .append("resumo", artigo.getResumo())
                .append("anoPublicacao", artigo.getAnoPublicacao())
                .append("pdfId", pdfId.toHexString());
        repository.getArtigosCollection().insertOne(document);

        return document.getObjectId("_id").toHexString();
    }

    public List<Artigo> listarArtigos() {
        List<Artigo> artigos = new ArrayList<>();
        for (Document doc : repository.getArtigosCollection().find()) {
            Artigo artigo = new Artigo();
            artigo.setId(doc.getObjectId("_id").toHexString());
            artigo.setTitulo(doc.getString("titulo"));
            artigo.setAutor(doc.getString("autor"));
            artigo.setResumo(doc.getString("resumo"));
            artigo.setAnoPublicacao(doc.getInteger("anoPublicacao"));
            artigo.setPdfId(doc.getString("pdfId"));
            artigos.add(artigo);
        }
        return artigos;
    }

    public void deletarArtigo(String id) {
        Document artigo = repository.getArtigosCollection().findOneAndDelete(Filters.eq("_id", new ObjectId(id)));
        if (artigo != null) {
            String pdfId = artigo.getString("pdfId");
            repository.getGridFSBucket().delete(new ObjectId(pdfId));
        }
    }
}
