## Added
- Added Second Microservice [polling-ms].
Utilizes the Polling Publisher pattern to send events to the message broker, playing a fundamental role in our services' interactions.
Tracks event states in the Outbox, monitoring ACKs at the time of publication to ensure each event is received.
Provides the option for consumers to mark events as "correctly processed" using a correlation-id.
Implements a scheduler to perform a findAll at time t0 and query based on a certain time instant at time t1, allowing manual capture of all possible modifications.
- Added Third Microservice [polling-consumer-ms].
Responsible for consuming all the potential topics produced by polling-ms and storing them in MongoDB.

## Changed
Updated the README with instructions on how to use the application.

