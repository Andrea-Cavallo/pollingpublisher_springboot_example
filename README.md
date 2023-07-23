#  Progetto di Simulazione di Cattura da un DB Legacy

Questo progetto simula la necessità di catturare cambiamenti da un database legacy (in questo caso, SQLServer) utilizzando una REST API creata ad hoc. Il cuore di questa simulazione sfrutta il Change Data Capture (CDC), in particolare tramite l'uso di SpringBoot e Debezium Embedded.

## Panoramica
La nostra architettura è strutturata in modo tale che tutti i cambiamenti sulla tabella dbo.outbox vengano catturati e gestiti. In aggiunta, è stato applicato il pattern outbox per garantire l'atomicità delle operazioni e la consistenza dei dati.

## Kafka e Docker Compose
Per simulare l'ambiente distribuito, abbiamo incorporato Kafka e un'interfaccia Kafka UI, entrambi eseguiti attraverso Docker Compose. In questo modo, possiamo simulare facilmente l'invio di messaggi e l'interazione tra i microservizi.
Secondo Microservizio e Kafka Producer

## Primo Microservizio [crud-sqlserver-ms]
Il primo microservizio svolge un ruolo fondamentale all'interno del nostro progetto: simula un database legacy, nel nostro caso SQLServer. Questo si rende possibile attraverso l'implementazione di un modello di dati e delle funzionalità che riflettono quelle di un tipico database SQLServer.
In aggiunta alla simulazione del DB, il primo microservizio applica anche il pattern Outbox. Questo modello di design consente di assicurare l'atomicità delle operazioni e la consistenza dei dati nel contesto di un sistema distribuito.

## Secondo Microservizio [polling-ms]
Utilizzando il pattern Polling Publisher, siamo in grado di trasmettere gli eventi al nostro message-broker, svolgendo un ruolo fondamentale nell'interazione dei nostri servizi.
Conserviamo lo stato degli eventi nell'Outbox, dove monitoriamo l'ACK al momento della pubblicazione. Questo sistema di verifica ci consente di garantire la ricezione di ogni evento. Inoltre, esiste la possibilità per il consumatore di segnare un evento come "correttamente processato" utilizzando peresempio un correlation-id.
A differenza di quanto visto nell'altro esempio con CDC- Debezium questa volta abbiamo implementato PollingPublisher pattern
Da notare come è stato implementato lo scheduler
In particolare è stata fatta una assunzione, ovvero che al tempo t0 debba esser fatta una findAll mentre al tempo t1, si fara' una query in base a un certo istante di tempo, cosi da stare in ascolto e catturare "manualmente" tutte le eventuali modifiche.


## Autore
Andrea Cavallo
