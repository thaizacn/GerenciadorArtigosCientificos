import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Arrays;

import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import com.mongodb.client.*;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;

public class GerenciadorArtigos {
    private JFrame frame;
    private JTextField idField, tituloField, autorField, resumoField, anoField, buscarField;
    private JTextArea outputArea;
    private JButton criarButton, lerButton, atualizarButton, deletarButton, selecionarArquivoButton, uploadButton, buscarButton, baixarButton;
    private File arquivoSelecionado;
    private static MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public GerenciadorArtigos() {
        // Inicializar conexão com o MongoDB
        mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(new ServerAddress("localhost", 27017))))
                        .build()
        );
        database = mongoClient.getDatabase("GerenciadorArtigosCientificos");
        collection = database.getCollection("artigos");

        // Configurar a interface gráfica
        configurarInterface();
    }

    private void configurarInterface() {
        frame = new JFrame("Gerenciador de Artigos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        idField = new JTextField(20);
        tituloField = new JTextField(20);
        autorField = new JTextField(20);
        resumoField = new JTextField(20);
        anoField = new JTextField(20);
        buscarField = new JTextField(20);
        outputArea = new JTextArea(10, 30);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        criarButton = new JButton("Criar");
        lerButton = new JButton("Ler");
        atualizarButton = new JButton("Atualizar");
        deletarButton = new JButton("Deletar");
        selecionarArquivoButton = new JButton("Selecionar Arquivo");
        uploadButton = new JButton("Upload");
        buscarButton = new JButton("Buscar");
        baixarButton = new JButton("Baixar");

        adicionarComponentesAoFrame(gbc, scrollPane);

        adicionarListenersAosBotoes();

        frame.pack();
        frame.setVisible(true);
    }

    private void adicionarComponentesAoFrame(GridBagConstraints gbc, JScrollPane scrollPane) {
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos de entrada
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        frame.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; frame.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        frame.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; frame.add(tituloField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        frame.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1; frame.add(autorField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        frame.add(new JLabel("Resumo:"), gbc);
        gbc.gridx = 1; frame.add(resumoField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        frame.add(new JLabel("Ano de publicação:"), gbc);
        gbc.gridx = 1; frame.add(anoField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        frame.add(new JLabel("Buscar:"), gbc);
        gbc.gridx = 1; frame.add(buscarField, gbc);

        // Botões
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel botoesLinha1 = new JPanel(new GridLayout(1, 4, 5, 5));
        botoesLinha1.add(criarButton);
        botoesLinha1.add(lerButton);
        botoesLinha1.add(atualizarButton);
        botoesLinha1.add(deletarButton);
        frame.add(botoesLinha1, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JPanel botoesLinha2 = new JPanel(new GridLayout(1, 4, 5, 5));
        botoesLinha2.add(selecionarArquivoButton);
        botoesLinha2.add(uploadButton);
        botoesLinha2.add(buscarButton);
        botoesLinha2.add(baixarButton);
        frame.add(botoesLinha2, gbc);

        // Área de saída
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(scrollPane, gbc);
    }


    private void adicionarBotoesAoFrame(GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = 4;
        frame.add(criarButton, gbc);
        gbc.gridx = 1; frame.add(lerButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        frame.add(atualizarButton, gbc);
        gbc.gridx = 1; frame.add(deletarButton, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        frame.add(selecionarArquivoButton, gbc);
        gbc.gridx = 1; frame.add(uploadButton, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        frame.add(buscarButton, gbc);
        gbc.gridx = 1; frame.add(baixarButton, gbc);
    }

    private void adicionarListenersAosBotoes() {
        criarButton.addActionListener(e -> criarArtigo());
        lerButton.addActionListener(e -> lerArtigo());
        atualizarButton.addActionListener(e -> atualizarArtigo());
        deletarButton.addActionListener(e -> deletarArtigo());
        selecionarArquivoButton.addActionListener(e -> selecionarArquivo());
        uploadButton.addActionListener(e -> fazerUploadArquivo());
        buscarButton.addActionListener(e -> buscarArtigos());
        baixarButton.addActionListener(e -> baixarArquivo());
    }

    private void criarArtigo() {
        try {
            Document document = new Document("_id", idField.getText())
                    .append("titutlo", tituloField.getText())
                    .append("autor", autorField.getText())
                    .append("resumo", resumoField.getText())
                    .append("anoPublicacao", anoField.getText());
            collection.insertOne(document);
            outputArea.setText("Artigo criado: " + document.toJson());
        } catch (Exception e) {
            outputArea.setText("Erro ao criar artigo: " + e.getMessage());
        }
    }

    private void lerArtigo() {
        try {
            Document query = new Document("_id", idField.getText());
            Document document = collection.find(query).first();
            outputArea.setText(document != null ? "Artigo encontrado: " + document.toJson() : "Artigo não encontrado.");
        } catch (Exception e) {
            outputArea.setText("Erro ao artigo Artigo: " + e.getMessage());
        }
    }

    private void atualizarArtigo() {
        try {
            Document query = new Document("_id", idField.getText());
            Document update = new Document("$set", new Document("titutlo", tituloField.getText())
                    .append("autor", autorField.getText())
                    .append("resumo", resumoField.getText())
                    .append("anoPublicacao", anoField.getText()));
            collection.updateOne(query, update);
            outputArea.setText("Artigo atualizado com sucesso.");
        } catch (Exception e) {
            outputArea.setText("Erro ao atualizar artigo: " + e.getMessage());
        }
    }

    private void deletarArtigo() {
        try {
            Document query = new Document("_id", idField.getText());
            collection.deleteOne(query);
            outputArea.setText("Artigo deletado com sucesso.");
        } catch (Exception e) {
            outputArea.setText("Erro ao deletar artigo: " + e.getMessage());
        }
    }

    private void selecionarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            arquivoSelecionado = fileChooser.getSelectedFile();
            outputArea.setText("Arquivo selecionado: " + arquivoSelecionado.getName());
        }
    }

    private void fazerUploadArquivo() {
        try {
            if (arquivoSelecionado != null) {
                byte[] fileData = new byte[(int) arquivoSelecionado.length()];
                new FileInputStream(arquivoSelecionado).read(fileData);
                Document fileDocument = new Document("nomeArquivo", arquivoSelecionado.getName())
                        .append("dadosArquivo", fileData);
                collection.insertOne(fileDocument);
                outputArea.setText("Arquivo enviado com sucesso.");
            } else {
                outputArea.setText("Selecione um arquivo primeiro.");
            }
        } catch (Exception e) {
            outputArea.setText("Erro ao enviar arquivo: " + e.getMessage());
        }
    }

    private void buscarArtigos() {
        try {
            // Realiza busca textual usando $text
            Document query = new Document("$text", new Document("$search", buscarField.getText()));
            FindIterable<Document> resultados = collection.find(query);
            StringBuilder resultado = new StringBuilder();
            for (Document doc : resultados) {
                resultado.append(doc.toJson()).append("\n");
            }
            outputArea.setText(resultado.toString());
        } catch (Exception e) {
            outputArea.setText("Erro ao buscar artigos: " + e.getMessage());
        }
    }



    private void baixarArquivo() {
        try {
            // Substituir `idField.getText()` pelo identificador correto (ObjectId)
            Document query = new Document("_id", new ObjectId(idField.getText()));
            Document doc = collection.find(query).first();
            if (doc != null) {
                Binary fileData = doc.get("dadosArquivo", Binary.class); // Certifique-se de usar Binary.class
                String nomeArquivo = doc.getString("nomeArquivo");
                if (fileData != null) {
                    FileOutputStream fos = new FileOutputStream(new File(nomeArquivo));
                    fos.write(fileData.getData());
                    fos.close();
                    outputArea.setText("Arquivo baixado: " + nomeArquivo);
                } else {
                    outputArea.setText("Erro: dados do arquivo estão nulos.");
                }
            } else {
                outputArea.setText("Erro: Arquivo não encontrado.");
            }
        } catch (Exception e) {
            outputArea.setText("Erro ao baixar arquivo: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        new GerenciadorArtigos();
    }
}
