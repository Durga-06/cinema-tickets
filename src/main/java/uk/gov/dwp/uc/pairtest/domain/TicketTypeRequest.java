package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable value object
 */

public record TicketTypeRequest(Type type, int noOfTickets) {

    public enum Type {
        ADULT, CHILD, INFANT
    }
}