# projeto-poo


## Tutorial para configurar SQLite no projeto Java

### 1. Usando **Eclipse**

**Passo 1: Importe seu projeto no Eclipse**

* Abra o Eclipse.
* Vá em **File > Import > Existing Projects into Workspace**.
* Selecione a pasta do seu projeto e finalize.

**Passo 2: Adicione o driver SQLite no build path**

* Clique com o botão direito no projeto > **Properties**.
* Vá em **Java Build Path > Libraries**.
* Clique em **Add External JARs...**.
* Navegue até a pasta `lib` dentro do seu projeto e selecione o arquivo `sqlite-jdbc.jar`.
* Clique em **Apply and Close**.

**Passo 3: Verifique o banco**

* Certifique-se que o arquivo `.db` (se houver) está na pasta correta e o caminho no código esteja certo.

**Passo 4: Execute o projeto**

* Clique com o botão direito no arquivo principal (ex: `Main.java`) > **Run As > Java Application**.

---

### 2. Usando **IntelliJ IDEA**

**Passo 1: Importe ou abra o projeto**

* Abra o IntelliJ IDEA.
* Escolha **Open** e selecione a pasta do seu projeto.

**Passo 2: Adicione o driver SQLite como biblioteca**

* No painel lateral, clique com o botão direito no projeto > **Open Module Settings** (ou use `F4`).
* Vá na aba **Libraries**.
* Clique no ícone `+` > **Java**.
* Navegue até a pasta `lib` e selecione o `sqlite-jdbc.jar`.
* Clique em **OK** e depois **Apply**.

**Passo 3: Verifique o banco**

* Confira se o arquivo do banco `.db` está no lugar esperado e o caminho usado no código está correto.

**Passo 4: Rode o projeto**

* Abra o arquivo principal (ex: `Main.java`).
* Clique com o botão direito > **Run 'Main'**.