# FlashMessage - Documentação

## O que é?

O `FlashMessage` é uma classe utilitária que gerencia mensagens temporárias exibidas aos usuários após uma ação (redirecionamentos, submissão de formulários, etc). As mensagens são armazenadas na sessão HTTP e exibidas uma única vez.

## Localização

- **Classe**: `src/main/java/com/tobias/application/FlashMessage.java`
- **Template**: `src/main/webapp/WEB-INF/templates/layout/partials/flash.jspf`

## Como Usar

### 1. Definindo uma mensagem flash no Controller

Use o método `set()` para definir uma mensagem que será exibida ao usuário:

```java
import com.tobias.application.FlashMessage;
import jakarta.servlet.http.HttpServletRequest;

@PostMapping("/salvar")
public String salvar(HttpServletRequest request, /* outros parâmetros */) {
    try {
        // ... lógica de salvamento ...
        FlashMessage.set(request, "success", "Disciplina salva com sucesso!");
        return "redirect:/disciplinas";
    } catch (Exception e) {
        FlashMessage.set(request, "danger", "Erro ao salvar: " + e.getMessage());
        return "redirect:/disciplinas";
    }
}
```

### 2. Tipos de mensagens disponíveis

Os tipos são baseados em classes Bootstrap Alert:

- **success**: Mensagem de sucesso (verde) - `alert-success`
- **danger**: Mensagem de erro (vermelho) - `alert-danger`
- **warning**: Mensagem de aviso (amarelo) - `alert-warning`
- **info**: Mensagem informativa (azul) - `alert-info`

### 3. Recuperando a mensagem no template

No template JSP, a mensagem é automaticamente recuperada se você incluir o partial:

```jsp
<%@ include file="/WEB-INF/templates/layout/partials/flash.jspf" %>
```

Exemplo em `base.jsp`:

```jsp
<div class="container mt-3">
    <%@ include file="partials/flash.jspf" %>
    <!-- Conteúdo da página -->
</div>
```

## Fluxo completo

```
1. Usuário submete formulário
   ↓
2. Controller valida e processa
   ↓
3. Se sucesso: FlashMessage.set(request, "success", "...")
   Se erro: FlashMessage.set(request, "danger", "...")
   ↓
4. Controller faz redirect para outra página
   ↓
5. Novo request chega
   ↓
6. Na JSP, o partial flash.jspf recupera e exibe a mensagem
   ↓
7. Mensagem é automaticamente limpa da sessão
```

## Exemplo prático completo

### Controller (AuthController.java)

```java
@PostMapping("/login")
public String login(String email, String senha, HttpServletRequest request) {
    try {
        User user = authDAO.autenticar(email, senha);
        request.getSession().setAttribute("usuario", user);
        FlashMessage.set(request, "success", "Bem-vindo, " + user.getNome() + "!");
        return "redirect:/dashboard";
    } catch (Exception e) {
        FlashMessage.set(request, "danger", "Email ou senha incorretos");
        return "redirect:/login";
    }
}
```

### Template (login.jsp)

```jsp
<%@ include file="/WEB-INF/templates/layout/base.jsp" %>

<div class="container mt-5">
    <%@ include file="/WEB-INF/templates/layout/partials/flash.jspf" %>
    
    <form method="post" action="/auth/login">
        <input type="email" name="email" required>
        <input type="password" name="senha" required>
        <button type="submit">Entrar</button>
    </form>
</div>
```

## Boas práticas

✅ **Faça:**
- Use mensagens claras e diretas
- Sempre use um tipo apropriado (success/danger/warning/info)
- Inclua a mensagem flash antes do conteúdo principal da página
- Combine com validação do lado do servidor

❌ **Não faça:**
- Não use para dados que precisam ser persistidos (use banco de dados)
- Não confie apenas em flash messages para feedback (considere validação frontend também)
- Não deixe a página sem incluir o partial flash.jspf se deseja exibir mensagens

## Referências

- [Bootstrap Alerts](https://getbootstrap.com/docs/5.3/components/alerts/)
- Classe: `FlashMessage.java`
- Template: `flash.jspf`
