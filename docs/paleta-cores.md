# Paleta de Cores - TobIAs

## Visão Geral

A paleta de cores do TobIAs foi desenvolvida para criar uma identidade visual profissional e acessível, inspirada na coruja presente no logo da aplicação. As cores foram selecionadas para transmitir confiança, inteligência e sabedoria - características associadas à coruja.

## Cores Principais

### Azul Escuro (#1e3a8a)
- **Uso**: Sidebar (lado esquerdo), elementos de foco, kickers de seção
- **Propósito**: Transmite profissionalismo e confiança
- **Inspiração**: Corresponde às cores mais profundas da coruja

### Azul Médio (#2563eb)
- **Uso**: Botões primários, gradientes, ícones principais
- **Propósito**: Cor primária da interface, acesso e ação
- **Inspiração**: Harmoniza com o tom médio da coruja

### Azul Escuro para Info (#1d4ed8)
- **Uso**: Informações, ícones informativos, painéis
- **Propósito**: Diferenciar informações de ações
- **Inspiração**: Variação intermediária entre azul escuro e médio

---

## Paleta Completa de Cores

| Cor | Hex | Nome da Variável | Utilização |
|-----|-----|------------------|-----------|
| Azul Escuro | #1e3a8a | `--primary-color` | Sidebar, elementos principais |
| Azul Médio | #2563eb | `--primary-color` | Botões, links, ações |
| Azul Info | #1d4ed8 | `--info-color` | Informações, alertas info |
| Teal/Verde | #0f766e | `--secondary-color` | Elementos secundários |
| Verde Sucesso | #10b981 | `--success-color` | Alertas positivos, confirmações |
| Amarelo Aviso | #f59e0b | `--warning-color` | Alertas de atenção |
| Vermelho Perigo | #ef4444 | `--danger-color` | Erros, alertas críticos |

---

## Gradientes Utilizados

### Sidebar Gradient
```css
background: linear-gradient(135deg, #1e3a8a 0%, #2563eb 100%);
```
- Transição suave de azul escuro para azul médio
- Cria profundidade visual na barra lateral

### Hero Section Gradient
```css
background: linear-gradient(135deg, #1e3a8a 0%, #2563eb 100%);
```
- Mesmo gradiente da sidebar para consistência
- Destaca a seção principal do dashboard

### Progress Bar Gradient
```css
background: linear-gradient(90deg, #f8fafc 0%, #bfdbfe 100%);
```
- Transição de branco para azul claro
- Indica progresso visualmente

---

## Aplicação das Cores

### Componentes Principais

#### Sidebar
- **Cor de fundo**: Gradiente azul (#1e3a8a → #2563eb)
- **Texto**: Branco (#ffffff)
- **Links ativos**: Fundo com opacidade branca (10%)
- **Hover**: Fundo com opacidade branca (10%)

#### Topbar
- **Cor de fundo**: Branco (#ffffff)
- **Ícones**: Cinza (#6b7280)
- **Texto**: Cinza escuro (#0f172a)

#### Hero Section
- **Cor de fundo**: Gradiente azul (#1e3a8a → #2563eb)
- **Texto**: Branco (#ffffff)
- **Botões**: Branco com fundo transparente

#### Cards (Estatísticas)
- **Ícones**: Azul médio (#2563eb) com fundo azul claro
- **Números**: Cinza escuro (#0f172a)
- **Rótulos**: Cinza médio (#64748b)

#### Painéis de Atividade
- **Ícones info**: Azul info (#3b82f6) com fundo azul claro
- **Ícones sucesso**: Verde (#10b981) com fundo verde claro
- **Ícones aviso**: Amarelo (#f59e0b) com fundo amarelo claro

---

## Variações de Opacidade

### Backgrounds Sutis
Para criar elementos secundários mantendo a consistência visual, utilizamos variações de opacidade:

- **Subtle (10%)**: `rgba(37, 99, 235, 0.1)` - Fundo muito leve
- **Subtle (18%)**: `rgba(15, 23, 42, 0.18)` - Panels com vidro fosco
- **Subtle (14%)**: `rgba(255, 255, 255, 0.14)` - Elementos brancos com transparência

---

## Acessibilidade

### Contraste
Todas as combinações de cores seguem as diretrizes WCAG AA:
- Texto branco sobre azul escuro: ✓ Excelente contraste
- Texto cinza sobre branco: ✓ Bom contraste
- Ícones coloridos sobre branco: ✓ Bom contraste

### Significado Não Apenas Cor
Elementos importantes como alertas também utilizam ícones e texto além da cor para transmitir significado.

---

## Consistência Visual

### Tokens de Cor CSS
Todas as cores são definidas como variáveis CSS no arquivo `theme.css`:

```css
:root {
  --primary-color: #2563eb;
  --secondary-color: #0f766e;
  --success-color: #10b981;
  --warning-color: #f59e0b;
  --danger-color: #ef4444;
  --info-color: #1d4ed8;
}
```

Isso garante que mudanças futuras na paleta possam ser feitas em um único local.

---

## Arquivos de Estilo

### theme.css
Define cores principais, variáveis CSS, e estilos globais da aplicação.

### dashboard.css
Define cores específicas para componentes do dashboard, mantendo a consistência com `theme.css`.

---

## Inspiração da Coruja

A coruja é um símbolo de:
- **Sabedoria**: Representada pelos tons azuis profundos
- **Inteligência**: Refletida na paleta equilibrada e harmônica
- **Confiança**: Transmitida pelo uso consistente de azuis

A paleta de azuis foi escolhida para evocar esses atributos, criando uma interface que inspira confiança nos usuários do sistema educacional TobIAs.

---

## Como Modificar a Paleta

Para alterar as cores da aplicação:

1. Abra `src/main/webapp/assets/css/theme.css`
2. Modifique as variáveis CSS em `:root`
3. Recompile o projeto com: `mvn clean install` (ou use Docker)
4. As mudanças serão aplicadas automaticamente a todos os componentes

---

**Data de Criação**: 20 de abril de 2026  
**Versão**: 1.0  
**Sistema**: TobIAs - Sistema de Educação
