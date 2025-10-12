# MEDIX
## Desafio Sprint 1 – MEDIX
Medix é uma plataforma que visa criar um ecossistema integrado para conectar de forma inteligente hospitais a pacientes. Ela busca resolver a fragmentação e ineficiência do ecossistema de saúde, onde os pacientes não têm acesso a informações centralizadas sobre a disponibilidade de hospitais, especialidades ou tempo de espera.

---
## Integrantes do grupo
* **Arthur Thomas Mariano de Souza (RM 561061)** - Responsável pelas matérias de Iot e .NET
* **Davi Cavalcanti Jorge (RM 559873)** - Responsável pelas matérias de Compliance and Q.A, DevOps e Mobile
* **Mateus da Silveira Lima (RM 559728)** - Responsável pelas matérias de Banco de Dados, Java e Mobile

---
## Problema e Solução
O problema principal é a lacuna de conexão e informação no ecossistema de saúde, que afeta tanto pacientes quanto instituições. Para os pacientes, a busca por atendimento é ineficiente e leva a peregrinações desnecessárias, resultando em superlotação de unidades e atrasos no tratamento. Para hospitais e clínicas, a falta de ferramentas inteligentes para gestão e previsão de demanda causa altos custos e sobrecarga das equipes.

A plataforma Medix ataca a raiz desse problema com três objetivos centrais:
1.  **Empoderar o Paciente:** Oferecer um aplicativo móvel para que pacientes encontrem unidades de saúde, verifiquem a disponibilidade de recursos e tomem decisões informadas.
2.  **Otimizar a Gestão Hospitalar:** Fornecer às instituições de saúde uma plataforma para gerenciar recursos em tempo real e analisar o fluxo de pacientes para prever picos de demanda.
3.  **Criar um Ecossistema Integrado:** Construir uma ponte tecnológica que conecte as necessidades dos pacientes com a capacidade das instituições.

---
## Público-Alvo
* **Clientes (Quem Compra):** Gestores de hospitais, administradores de clínicas privadas e redes de saúde que buscam otimizar a operação e reduzir custos.
* **Usuários (Quem Usa a Solução):**
    * **Pacientes e seus Familiares:** Indivíduos que procuram atendimento médico de forma ágil e transparente.
    * **Equipes Médicas e Administrativas:** Colaboradores das unidades de saúde que utilizam os painéis de gestão para otimizar seu trabalho diário.

---
## Como Rodar a Aplicação

**Requisitos:**

- IntelliJ IDEA instalado
- Plugin Lombok com annotation processors ativado

1. **Clone o projeto**
```
git clone https://github.com/challengeoracle/sprint-1-java
``` 

2. **Abra o projeto no IntelliJ IDEA.**

3. **Abra o arquivo localizado em `src/main/java/br/com/fiap/medix_api/MedixApiApplication.java`.**

4. **Utilize o atalho `Shift + F10` para iniciar a aplicação.**

---
## Diagramas
### Diagrama de Classes
![Diagrama de Classes](https://imgur.com/bbp1pf5.png)

### Diagrama Relacional
![Diagrama Relacional](https://imgur.com/0dTBUN3.png)

---
## PITCH
- [Apresentação MEDIX - YouTube](https://youtu.be/xYQXVIVLfek)

---

## Cronograma de desenvolvimento Sprint 1: Java Advanced


1. **Modelagem de Dados e JPA**

- **Descrição:** Definir as entidades do sistema (UnidadeSaude, Usuario, Colaborador, Paciente, enums), seus atributos e os relacionamentos entre eles. Mapear as entidades usando JPA.
- **Responsável:** Mateus da Silveira Lima
- **Status:** Concluído
- **Data:** 30 de Setembro de 2025

2. **Configuração do Projeto e Dependências**

- **Descrição:** Configurar o projeto Spring Boot, adicionar as dependências necessárias no pom.xml e configurar o banco de dados H2 no application.yml.

- **Responsável:** Mateus da Silveira Lima

- **Status:** Concluído

- **Data:** 1 de Outubro de 2025

3. **Desenvolvimento da API (Endpoints CRUD)**

- **Descrição:** Implementar os controladores (UnidadeSaudeController, ColaboradorController, PacienteController) e a lógica de serviço (Service) para as operações de Criar, Listar, Buscar, Atualizar e Excluir logicamente.
- **Responsável:** Mateus da Silveira Lima
- **Status:** Concluído
- **Data:** 4 de Outubro de 2025

4. **Implementação de Segurança (JWT)**

- **Descrição:** Adicionar os filtros de autenticação, o serviço de token JWT e a configuração de segurança do Spring Security para proteger os endpoints da API.
- **Responsável:** Mateus da Silveira Lima
- **Status:** Concluído
- **Data:** 5 de Outubro de 2025


5. **Testes e Validação da API**

- **Descrição:** Criar a coleção do Postman para testar todos os endpoints, incluindo a autenticação, cadastro e manipulação de dados em todas as entidades. Validar se o fluxo está funcionando corretamente.
- Responsável: Mateus da Silveira Lima
- **Status:** Concluído
- **Data:** 6 de Outubro de 2025

---

## Endpoints da API
A API do Medix, desenvolvida com Spring Boot, possui endpoints para as operações CRUD (Criar, Listar, Buscar, Atualizar e Excluir logicamente) para as entidades `UnidadeSaude`, `Colaborador`, e `Paciente`. A autenticação é protegida por filtros de segurança e um serviço de token JWT.

**AuthController**
* `POST /auth/login` - Autenticação e geração de token JWT.

**PacienteController**
* `GET /pacientes` - Listar todos os pacientes.
* `GET /pacientes?status=deletado` - Listar todos os pacientes deletados.
* `GET /pacientes/{id}` - Buscar um paciente por ID.
* `PUT /pacientes/{id}` - Atualizar um paciente.
* `DELETE /pacientes/{id}` - Excluir logicamente um paciente.

**ColaboradorController**
* `GET /colaboradores` - Listar todos os colaboradores.
* `GET /colaboradores?status=deletado` - Listar todos os colaboradores deletados.
* `GET /colaboradores/{id}` - Buscar um colaborador por ID.
* `POST /colaboradores` - Criar um novo colaborador.
* `POST /colaboradores` - Criar um novo colaborador.
* `POST /colaboradores/{id}/pacientes` - Criar um novo paciente.
* `PUT /colaboradores/{id}` - Atualizar um colaborador.
* `DELETE /colaboradores/{id}` - Excluir logicamente um colaborador.

**UnidadeSaudeController**
* `GET /unidades` - Listar todas as unidades de saúde.
* `GET /unidades?status=deletado` - Listar todas as unidades de saúde deletadas.
* `GET /unidades/{id}` - Buscar uma unidade de saúde por ID.
* `POST /unidades` - Criar uma nova unidade de saúde.
* `PUT /unidades/{id}` - Atualizar uma unidade de saúde.
* `DELETE /unidades/{id}` - Excluir logicamente uma unidade de saúde.

---
## Link do repositório
- [https://github.com/challengeoracle/sprint-1-java](https://github.com/challengeoracle/sprint-1-java)

