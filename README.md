# 🏨 Hadja Hotel — Sistema Interno de Gestão

Aplicativo Android desenvolvido em Kotlin com Jetpack Compose para uso interno de funcionários de hotel
O sistema permite gerenciar reservas, hóspedes, eventos, manutenção de ar-condicionado, abastecimento do veículo e relatórios operacionais

---

## Telas

| Login | Home | Reservas |
|---|---|---|
| Autenticação com bloqueio após 3 tentativas | Menu principal com acesso aos 6 módulos | Formulário em stepper com grade visual de quartos |

| Hóspedes | Detalhes do Hóspede | Eventos |
|---|---|---|
| Lista com busca por prefixo e cadastro via bottom sheet | Perfil com histórico de reservas | Pipeline completo com cálculo de garçons e buffet |

| Ar-Condicionado | Abastecimento | Relatórios |
|---|---|---|
| Comparativo automático de orçamentos | Análise de custo-benefício entre postos | Métricas consolidadas da sessão |

---

## Arquitetura

O projeto adota uma arquitetura modular em três camadas, com separação clara de responsabilidades

```
┌─────────────────────────────────────────┐
│              UI (Screens)               │
│   Jetpack Compose · sem lógica          │
├─────────────────────────────────────────┤
│            ViewModel                    │
│   Estado reativo · ponte UI ↔ Service  │
├─────────────────────────────────────────┤
│            Services                     │
│   Kotlin puro · sem Android · testável  │
├─────────────────────────────────────────┤
│             Model                       │
│   Data classes · Enums · Estado         │
└─────────────────────────────────────────┘
```

### Por que essa separação?

Os `Services` são Kotlin puro, sem nenhuma dependência do Android
Isso significa que toda a lógica de negócio pode ser desenvolvida, entendida e testada de forma completamente independente da interface
O `HotelViewModel` age como ponte, mantendo o estado vivo entre recomposições e expondo os dados via `mutableStateOf` para que o Compose reaja automaticamente a qualquer mudança

---

## Estrutura de Arquivos

```
app/src/main/java/com/hadjahotel/
│
├── model/
│   ├── HotelState.kt          
│   ├── Reservation.kt
│   ├── Guest.kt
│   ├── Event.kt
│   ├── AcQuote.kt
│   ├── FuelResult.kt
│   ├── BuffetResult.kt
│   ├── ReportData.kt
│   ├── RoomType.kt            
│   └── OperationResult.kt     
│
├── service/
│   ├── AuthService.kt
│   ├── ReservationService.kt
│   ├── GuestService.kt
│   ├── EventService.kt
│   ├── AirConditioningService.kt
│   ├── FuelService.kt
│   └── ReportService.kt
│
├── viewmodel/
│   └── HotelViewModel.kt
│
├── ui/
│   ├── screen/
│   │   ├── LoginScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── ReservationScreen.kt
│   │   ├── GuestScreen.kt
│   │   ├── GuestDetailScreen.kt
│   │   ├── EventScreen.kt
│   │   ├── AirConditioningScreen.kt
│   │   ├── FuelScreen.kt
│   │   └── ReportScreen.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── navigation/
│   └── NavGraph.kt
│
└── util/
    ├── Formatter.kt           
    └── Validator.kt           
```

---

## Módulos do Sistema

### 🛏 Reservas de Quartos
Gerencia as 20 acomodações do hotel. 
O atendente informa os dados da estadia e o sistema calcula o valor final com base no tipo de quarto (Standard, Executivo ou Luxo) e aplica a taxa de serviço de 10%. Um grid visual 4×5 exibe o status de cada quarto em tempo real.

```
subtotal = diária × noites × fator do tipo
taxa     = subtotal × 10%
total    = subtotal + taxa
```

### 👥 Cadastro de Hóspedes
Agenda interna com capacidade para 15 hóspedes ativos. Suporta busca por prefixo, cadastro, edição e remoção. Ao confirmar uma reserva, o hóspede é registrado automaticamente.
A tela de detalhes exibe o perfil completo com o histórico de reservas.

### 🎪 Eventos
Pipeline completo para reserva do auditório. O sistema escolhe automaticamente entre Auditório Laranja (até 220 pessoas) e Colorado (até 350), verifica disponibilidade por janela horária, e calcula garçons necessários e custo do buffet (café, água e salgados).

### ❄️ Ar-Condicionado
Comparativo de orçamentos para manutenção. Suporta múltiplas empresas com regras de desconto por volume e custo de deslocamento. 
O comparativo aparece automaticamente ao cadastrar 2 ou mais orçamentos, destacando o mais barato.

### ⛽ Abastecimento
Análise de custo-benefício entre os postos parceiros Wayne Oil e Stark Petrol.
O sistema aplica a regra dos 70% para determinar se o etanol é vantajoso e calcula o custo de abastecimento completo (42 litros) em cada cenário.

### 📊 Relatórios Operacionais
Consolida em tempo real os dados da sessão: reservas confirmadas, taxa de ocupação, hóspedes cadastrados, eventos realizados e receita acumulada separada por hospedagem e eventos.

---

## Stack

| Tecnologia | Uso |
|---|---|
| Kotlin | Linguagem principal |
| Jetpack Compose | Interface declarativa |
| Material Design 3 | Design system |
| ViewModel | Gerenciamento de estado |
| Navigation Compose | Navegação entre telas |

---

## Identidade Visual

A paleta foi pensada para transmitir sofisticação com personalidade:

- **Verde `#1D9E75`** — cor primária, botões, confirmações e elementos ativos
- **Dourado `#BA7517`** — cor de acento, detalhes decorativos e destaques
- **Off-white `#FAFAF9`** — fundo das telas, leveza e limpeza visual

---

## Sobre o Projeto

Desenvolvido como projeto acadêmico com foco em arquitetura de software modular. 
A lógica de negócio foi construída primeiro em Kotlin puro, completamente desacoplada do Android, e depois integrada à interface mobile. 
Essa abordagem garante que os `Services` sejam coesos, testáveis e independentes de qualquer framework.
