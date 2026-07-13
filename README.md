# Cinema Tickets

A small service that takes a ticket purchase request, checks it against the
business rules, and if it is valid it calculates total cost (payment) and the number of
seats, then calls the payment and seat reservation services.

## Requirements

- Java 21
- Maven

## Building and running the tests

From the project root, run `mvn test`.

This compiles the code and runs both the unit tests (JUnit 5 and Mockito) and
the acceptance tests (Cucumber).

## How it works

The entry point is `TicketServiceImpl.purchaseTickets`. It works in four steps:

1. Validate the account id.
2. Validate the ticket requests are present and are in valid form.
3. Work out the totals once (ticket counts, amount to pay, seats to reserve)
   using `CalculatedTicketCounts`.
4. Check the business rules against those totals, and if everything passes,
   call the payment and seat reservation services.

Ticket prices and seat allocation live on the `Type` enum, so each type knows its own cost
and whether it takes a seat.

## Assumptions and decisions

A few of the rules were open to interpretation, so here is how I read them:

- Infants do not pay and do not get a seat, as they sit on an adult's lap.
- I assumed the number of infants cannot be greater than the number of adults,
  since each infant needs a lap to sit on. This one is derived from the seating
  rule rather than stated directly.
- The maximum of 25 tickets includes infants, as an infant is still a ticket type.
- A request with no tickets, or one that adds up to zero tickets, is rejected.
- A negative number of tickets is rejected when the request object is created,
  so an invalid request cannot reach the service.
- Child and infant tickets cannot be purchased without at least one adult ticket.
- Account ids must not be null and must be greater than zero.

## Design choices

- I have put everything in a single service rather than adding Spring or another
  framework. For one service a framework would add weight with no real benefit.
- I have put validation and calculation together in the service as private methods
  instead of splitting them into separate classes, which keeps the related logic
  in one place and easy to follow.
- I wrote the rules as plain guard clauses in a validation method rather than a
  heavier structure. They are a small fixed set that always run in order, so
  guard clauses read more clearly here.

## Testing approach

I built this test first and committed in small steps, so the history shows the
red, green, refactor cycle.

The unit tests cover each rule and boundary in detail (for example exactly 25
tickets is allowed but 26 is not). On top of those, a small set of Cucumber
scenarios describe the main business rules in plain language so they can be read
by someone who is not looking at the Java. The two layers do different jobs: the
unit tests are thorough, and the acceptance tests are a readable summary of the
business rules.
