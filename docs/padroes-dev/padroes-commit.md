# 📦 Guia de Padrão de Commits

Este guia define o **padrão de mensagens de commit** que deve ser seguido no projeto para garantir clareza, histórico organizado e integração com ferramentas como changelog automático, CI/CD e revisão de código.

---

## ✅ Por que usar um padrão?
- Facilita entender o que mudou sem abrir o diff  
- Ajuda na geração automática de changelogs  
- Deixa o histórico mais limpo e rastreável  
- Permite automações com GitHub Actions, GitLab CI, etc

---

## 🧱 Estrutura do Commit

```bash
<tipo>(escopo opcional): descrição
```

Exemplos:
```bash
feat: adicionar tela de login
fix(dashboard): corrigir bug no filtro de datas
docs(readme): atualizar instruções de execução
refactor(api): remover lógica duplicada
test: adicionar testes para componente Header
```

## 🔠 Tipos mais comuns

| Tipo | Descrição |
|------|-----------|
| feat | Adição de nova funcionalidade |
| fix | Correção de bugs |
| docs | Alterações na documentação |
| style | Mudanças visuais ou de formatação (semântica, identação) |
| refactor | Refatoração de código sem alterar comportamento |
| test | Adição ou modificação de testes |
| chore | Tarefas de build, configs, setup (ex: eslint, docker) |
| perf | Melhorias de desempenho |

## 🧪 Exemplos Reais

```bash
feat(auth): permitir login com Google
fix(form): validação de campo obrigatório
docs: criar seção de execução com docker
style: remover console.log e ajustar identação
refactor: extrair lógica de envio para service
test(api): cobrir rota de criação de usuário
chore: adicionar prettier ao projeto
```

## 🚫 Evite mensagens genéricas

❌ "update", "ajustes", "mudança", "teste 1"

✅ Seja claro e específico sobre o que foi feito e onde.

## 💡 Dica: use git commit -m "tipo: descrição"

## Guia Oficial"

[Conventional Commits](https://www.conventionalcommits.org/pt-br/v1.0.0/)