# 🗂️ Gerenciador de Artigos Científicos

Este projeto implementa uma aplicação Java que gerencia artigos científicos utilizando o MongoDB. Ele foi desenvolvido como parte da disciplina Desenvolvimento Distribuído de Software e possui as seguintes funcionalidades:

- **CRUD de Artigos Científicos:** Permite adicionar, visualizar, atualizar e excluir artigos.
- **Gerenciamento de Arquivos PDF:** Faz o upload e manipulação de arquivos PDF associados aos artigos utilizando GridFS.
- **Sistema de Replicação:** Configuração de replica set no MongoDB para tolerância a falhas e escalabilidade.

## 🔍 Sumário

- [Introdução](#introdução)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Configuração e Execução](#configuração-e-execução)
- [Configuração do MongoDB](#configuração-mongodb)
- [Funcionalidades Implementadas](#funcionalidades-implementadas)
- [Agradecimentos](#agradecimentos)

## 👣 Introdução
O projeto Gerenciador de Artigos Científicos foi desenvolvido como parte da disciplina Desenvolvimento Distribuído de Software, no último semestre da graduação na ADA. A aplicação utiliza o MongoDB como banco de dados, implementando um sistema de replica set para tolerância a falhas e escalabilidade.
Com foco em operações CRUD e no gerenciamento de arquivos PDF, o sistema é uma ferramenta prática para armazenar e organizar dados de artigos científicos, proporcionando um ambiente seguro, escalável e eficiente para lidar com dados estruturados e não estruturados.

## 🛠️ Tecnologias Utilizadas
- **Java**: Linguagem principal para implementação da aplicação.
- **MongoDB**: Banco de dados NoSQL para armazenar documentos JSON e arquivos não estruturados usando GridFS.
- **GridFS**: Sistema de gerenciamento de arquivos embutido no MongoDB.
- **Maven**: Gerenciador de dependências do projeto.

## ✨ Configuração e Execução

1. Clone o repositório:
   ```sh
   git clone https://github.com/seu-usuario/GerenciadorArtigosCientificos.git
   cd GerenciadorArtigosCientificos

2. Configurar o ambiente:
- Certifique-se de que o Java, Maven e MongoDB estão instalados e configurados no seu ambiente.

3. Configurar o MongoDB
- Antes de iniciar a aplicação, configure o MongoDB seguindo as instruções na próxima seção.

4. Instalar Dependências
   ```sh
   mvn clean install
  
5. Rodar a Aplicação
   ```sh
   mvn spring-boot:run
   
## 🛠️ Configuração do MongoDB

1. Criar Diretórios para o Replica Set
No sistema de arquivos, crie três diretórios:
   ```sh
   mkdir C:\data\GerenciadorArtigosCientificos
   mkdir C:\data\GerenciadorArtigosCientificos2
   mkdir C:\data\GerenciadorArtigosCientificos3

2. Iniciar os Nós do Replica Set
Execute os seguintes comandos em terminais separados:
   ```sh
   mongod --port 27017 --dbpath C:\data\GerenciadorArtigosCientificos --replSet rs0
   mongod --port 27018 --dbpath C:\data\GerenciadorArtigosCientificos2 --replSet rs0
   mongod --port 27019 --dbpath C:\data\GerenciadorArtigosCientificos3 --replSet rs0

3. Configurar o Replica Set
Conecte-se a um dos nós e configure o replica set:
   ```sh
   mongo --port 27017

 No shell do MongoDB:
   ```sh
   rs.initiate({
   _id: "rs0",
   members: [
     { _id: 0, host: "localhost:27017" },
     { _id: 1, host: "localhost:27018" },
     { _id: 2, host: "localhost:27019" }
   ]
  });
```
- Três replicas rodando via IntelliJ:
![image](https://github.com/user-attachments/assets/6cb278dd-afdd-4a1f-befa-6f4fb1e55489)

- Aplicação acessando e completando operações:
![image](https://github.com/user-attachments/assets/c2fb149f-0bb4-4b94-9ab6-80a24d7469d9)

- Evidências da inserção via MongoDB Compass:
![image](https://github.com/user-attachments/assets/546affd4-3eae-423a-aebd-0b9651bd7d7f)

## 🧩 Funcionalidades Implementadas
1. Adicionar Artigo: Permite criar um novo artigo no banco de dados.
2. Atualizar Artigo: Atualiza informações de um artigo existente.
3. Visualizar Artigos: Retorna todos os artigos cadastrados.
4. Excluir Artigo: Remove um artigo do banco de dados.
5. Upload de Arquivos PDF: Gerencia arquivos PDF associados aos artigos utilizando GridFS.

## 🎁 Agradecimentos
Este projeto foi desenvolvido com dedicação e foco para atender aos requisitos da disciplina de Desenvolvimento Distribuído de Software. Que ele seja um exemplo prático para quem busca aprender mais sobre a integração entre Java e MongoDB e sobre como desenvolver sistemas escaláveis e tolerantes a falhas.

⌨️ com ❤️ por Thai
