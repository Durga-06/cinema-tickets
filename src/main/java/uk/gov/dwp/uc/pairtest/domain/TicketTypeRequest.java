package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable value object
 */

public record TicketTypeRequest(Type type, int noOfTickets) {

    public enum Type {
        ADULT(25, 1),
        CHILD(15, 1),
        INFANT(0, 0);

        private final int cost;
        private final int seatsToBeAllocated;

        Type(int cost, int seatsToBeAllocated) {
            this.cost = cost;
            this.seatsToBeAllocated = seatsToBeAllocated;
        }

        public int cost() {
            return cost;
        }

        public int seatsToBeAllocated() {
            return seatsToBeAllocated;
        }
    }
}
