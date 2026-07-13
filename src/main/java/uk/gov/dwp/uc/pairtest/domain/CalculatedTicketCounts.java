package uk.gov.dwp.uc.pairtest.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public record CalculatedTicketCounts(int totalNoOfTickets, int adultTickets, int infantTickets,
                                     int totalCost, int totalSeats) {

    public static CalculatedTicketCounts from(TicketTypeRequest[] ticketTypeRequests) {

        int totalTicketsCount = countByType(ticketTypeRequests, type -> true);
        int adultTicketsCount = countByType(ticketTypeRequests, type -> type == TicketTypeRequest.Type.ADULT);
        int infantTicketsCount = countByType(ticketTypeRequests, type -> type == TicketTypeRequest.Type.INFANT);

        int combinedTotalCost = Arrays.stream(ticketTypeRequests)
            .mapToInt(request -> request.type().cost() * request.noOfTickets())
            .sum();

        int totalSeatsCount = Arrays.stream(ticketTypeRequests)
            .mapToInt(request -> request.type().seatsToBeAllocated() * request.noOfTickets())
            .sum();

        return new CalculatedTicketCounts(totalTicketsCount, adultTicketsCount, infantTicketsCount, combinedTotalCost, totalSeatsCount);
    }

    private static int countByType(TicketTypeRequest[] ticketTypeRequests, Predicate<TicketTypeRequest.Type> typeFilter) {
        return Arrays.stream(ticketTypeRequests)
            .filter(request -> typeFilter.test(request.type()))
            .mapToInt(TicketTypeRequest::noOfTickets)
            .sum();
    }
}
