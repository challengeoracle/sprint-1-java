# MEDIX
## Desafio Sprint 1 – MEDIX
Medix é uma plataforma que visa criar um ecossistema integrado para conectar de forma inteligente hospitais a pacientes. Ela busca resolver a fragmentação e ineficiência do ecossistema de saúde, onde os pacientes não têm acesso a informações centralizadas sobre a disponibilidade de hospitais, especialidades ou tempo de espera.

---
## Integrantes do grupo
* **Arthur Thomas Mariano de Souza (RM 561061)** - Responsável pelas matérias de Iot e .NET
* **Davi Cavalcanti Jorge (RM 559873)** - Responsável pelas matérias de Compliance and Q.A, DevOps e Mobile
* **Mateus da Silveira Lima (RM 559728)** - Responsável pelas matérias de Banco de Dados, Java e Mobile

---
## ⚠ ANTES DE COMPILAR O PROJETO
Verifique se o arquivo `application.yml` tem as seguintes credenciais datasource:
```yml
    datasource:
        # Dados de conexão
        url: jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
        username: rm559728
        password: 250306
        driver-class-name: oracle.jdbc.driver.OracleDriver
```
Utilize as minhas credenciais pois as tabelas já estão criadas e populadas! 

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
![Diagrama Relacional](https://imgur.com/hbAQjaA.png)

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

## Cronograma de desenvolvimento Sprint 2: Agendamento Inteligente e Segurança

1. **Expansão do Domínio e Novos Relacionamentos**

- **Descrição:** Implementação das novas entidades essenciais para o módulo de agendamento: `Agendamento`, `Sala` e `Especialidade`. Atualização da entidade `Colaborador` para incluir vínculo com `Especialidade`. Criação dos enums `StatusAgendamento` e `TipoAgendamento` para controle de fluxo e regras de negócio.  
- **Responsável:** Mateus da Silveira Lima  
- **Status:** Concluído  
- **Data:** 01 de Novembro de 2025  

2. **Lógica de Negócio de Agendamento e Disponibilidade**

- **Descrição:** Desenvolvimento do `AgendamentoService` com validação robusta de conflitos de horário (impedindo sobreposição para médicos, pacientes e salas). Implementação do `DisponibilidadeService` com lógica complexa para calcular dias e horários livres com base na agenda dos colaboradores.  
- **Responsável:** Mateus da Silveira Lima  
- **Status:** Concluído  
- **Data:** 03 de Novembro de 2025  

3. **API Avançada: Fluxo de Agendamento e Gestão**

- **Descrição:** Criação do `AgendamentoController` centralizando o fluxo sequencial de 5 passos para consulta de disponibilidade (Especialidades → Unidades → Dias → Horários → Profissionais) e operações de agendamento (criar, confirmar, cancelar, consultar próxima). Implementação do `SalaController` para gestão de infraestrutura.  
- **Responsável:** Mateus da Silveira Lima  
- **Status:** Concluído  
- **Data:** 05 de Novembro de 2025  

4. **Refatoração: Segurança e Organização (ModelMapper)**

- **Descrição:** Reforço crítico na segurança (`SecurityConfig`) restringindo operações administrativas apenas para o perfil `COLABORADOR` e liberando consultas necessárias para `PACIENTE`. Criação do componente `ModelMapper` para centralizar e limpar a conversão de Entidades para DTOs em todos os controladores, melhorando a manutenção do código.  
- **Responsável:** Mateus da Silveira Lima  
- **Status:** Concluído  
- **Data:** 06 de Novembro de 2025  

5. **Testes Integrados e Documentação (Swagger/Postman)**

- **Descrição:** Atualização completa da documentação via Swagger (OpenAPI) em todos os controllers para refletir respostas claras (201, 404, 409). Criação da coleção Postman **"Sprint 2"** cobrindo todos os fluxos, incluindo cenários de erro (conflito de agenda) e perfis de acesso diferentes.  
- **Responsável:** Mateus da Silveira Lima  
- **Status:** Concluído  
- **Data:** 07 de Novembro de 2025  

---

## Planejamento Futuro: Sprint 3

1. **Integração de Avaliações e Feedbacks**

- **Descrição:** Conectar a entidade `Avaliacao` ao fluxo de agendamentos, permitindo que pacientes avaliem salas, atendimentos e profissionais após o término dos serviços. Implementar o `AvaliacaoController` e a lógica de cálculo de médias de avaliação por colaborador e unidade.  
- **Responsável:** Mateus da Silveira Lima  
- **Status:** Planejado  
- **Previsão:** Fevereiro de 2026 

2. **Relatórios e Painel Administrativo**

- **Descrição:** Criar um módulo de relatórios com endpoints para estatísticas de uso, taxas de cancelamento, horários mais procurados e desempenho de colaboradores. Implementar o `DashboardController` e serviços analíticos com consultas agregadas.  
- **Responsável:** Mateus da Silveira Lima  
- **Status:** Planejado  
- **Previsão:** Fevereiro de 2026  

3. **Notificações Inteligentes (E-mail e Push)**

- **Descrição:** Adicionar o serviço de notificações automáticas para lembrar pacientes e colaboradores sobre agendamentos futuros e avisar sobre alterações ou cancelamentos. Integração com serviço externo de e-mail e push notifications.  
- **Responsável:** Mateus da Silveira Lima  
- **Status:** Planejado  
- **Previsão:** Fevereiro de 2026

4. **Melhoria de Segurança e Perfis de Acesso**

- **Descrição:** Refinar o controle de permissões no `SecurityConfig`, introduzindo perfis adicionais (ADMINISTRADOR, SUPORTE). Implementar auditoria de ações críticas (criação, alteração e exclusão de agendamentos) com logs persistentes.  
- **Responsável:** Mateus da Silveira Lima  
- **Status:** Planejado  
- **Previsão:** Fevereiro de 2026  

---

## Endpoints da API

A API do **Medix**, desenvolvida com **Spring Boot**, expandiu suas funcionalidades na Sprint 2 para incluir o módulo de **Agendamentos**, além de melhorias de segurança e organização com `ModelMapper` e controle de acesso via perfis (`PACIENTE`, `COLABORADOR`).

---

### **AuthController**
* `POST /auth/login` — Autenticação e geração de token JWT.

---

### **PacienteController**
* `GET /pacientes` — Listar todos os pacientes.
* `GET /pacientes?status=deletado` — Listar todos os pacientes deletados.
* `GET /pacientes/{id}` — Buscar um paciente por ID.
* `POST /pacientes` — Criar um novo paciente.
* `PUT /pacientes/{id}` — Atualizar um paciente.
* `DELETE /pacientes/{id}` — Excluir logicamente um paciente.

---

### **ColaboradorController**
* `GET /colaboradores` — Listar todos os colaboradores.
* `GET /colaboradores?status=deletado` — Listar todos os colaboradores deletados.
* `GET /colaboradores/{id}` — Buscar um colaborador por ID.
* `POST /colaboradores` — Criar um novo colaborador.
* `PUT /colaboradores/{id}` — Atualizar um colaborador.
* `DELETE /colaboradores/{id}` — Excluir logicamente um colaborador.

---

### **UnidadeSaudeController**
* `GET /unidades` — Listar todas as unidades de saúde.
* `GET /unidades?status=deletado` — Listar todas as unidades deletadas.
* `GET /unidades/{id}` — Buscar uma unidade de saúde por ID.
* `POST /unidades` — Criar uma nova unidade de saúde.
* `PUT /unidades/{id}` — Atualizar uma unidade de saúde.
* `DELETE /unidades/{id}` — Excluir logicamente uma unidade de saúde.

---

### **EspecialidadeController**
* `GET /especialidades` — Listar todas as especialidades disponíveis.
* `GET /especialidades/{id}` — Buscar uma especialidade por ID.
* `POST /especialidades` — Criar uma nova especialidade.
* `PUT /especialidades/{id}` — Atualizar uma especialidade.
* `DELETE /especialidades/{id}` — Excluir logicamente uma especialidade.

---

### **SalaController**
* `GET /salas` — Listar todas as salas.
* `GET /salas/{id}` — Buscar uma sala por ID.
* `POST /salas` — Cadastrar uma nova sala.
* `PUT /salas/{id}` — Atualizar informações da sala.
* `DELETE /salas/{id}` — Excluir logicamente uma sala.

---

### **AgendamentoController**
* `GET /agendamentos` — Listar todos os agendamentos.
* `GET /agendamentos/{id}` — Buscar um agendamento por ID.
* `POST /agendamentos` — Criar um novo agendamento.
* `PUT /agendamentos/{id}` — Atualizar um agendamento.
* `DELETE /agendamentos/{id}` — Cancelar um agendamento.
* `GET /agendamentos/disponibilidade/especialidades` — Consultar especialidades disponíveis.
* `GET /agendamentos/disponibilidade/unidades` — Consultar unidades com disponibilidade.
* `GET /agendamentos/disponibilidade/dias` — Consultar dias disponíveis.
* `GET /agendamentos/disponibilidade/horarios` — Consultar horários livres.
* `GET /agendamentos/disponibilidade/profissionais` — Consultar profissionais disponíveis.
* `GET /agendamentos/proximo/{idPaciente}` — Consultar o próximo agendamento do paciente.

---

### **DisponibilidadeController**
* `GET /disponibilidades` — Listar todas as disponibilidades cadastradas.
* `GET /disponibilidades/{idColaborador}` — Consultar a disponibilidade de um colaborador específico.
* `POST /disponibilidades` — Registrar a disponibilidade de um colaborador.

---

Esses endpoints refletem o estado atual da **Sprint 2**, com foco em **Agendamento Inteligente**, **Segurança Refinada** e **Organização de Domínio**.

---

## Link do repositório
- [https://github.com/challengeoracle/sprint-1-java](https://github.com/challengeoracle/sprint-1-java)

