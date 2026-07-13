package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.CalculatedTicketCounts;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;
import java.util.Objects;

public class TicketServiceImpl implements TicketService {

    private static final int MAX_TICKETS = 25;

    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {

        this.ticketPaymentService = Objects.requireNonNull(ticketPaymentService, "ticketPaymentService must not be null");
        this.seatReservationService = Objects.requireNonNull(seatReservationService, "seatReservationService must not be null");
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        evaluateForAccountId(accountId);
        evaluateRequests(ticketTypeRequests);

        CalculatedTicketCounts counts = CalculatedTicketCounts.from(ticketTypeRequests);
        evaluateBusinessRules(counts);

        ticketPaymentService.makePayment(accountId, counts.totalCost());
        seatReservationService.reserveSeat(accountId, counts.totalSeats());
    }

    private void evaluateForAccountId(Long accountId) {

        if (accountId == null) {
            throw new InvalidPurchaseException("Account Id cannot be null");
        }
        if (accountId <= 0) {
            throw new InvalidPurchaseException("Account Id should be greater than 0");
        }
    }

    private void evaluateRequests(TicketTypeRequest... ticketTypeRequests) {

        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException("At least one ticket must be requested");
        }
        if (Arrays.stream(ticketTypeRequests).anyMatch(Objects::isNull)) {
            throw new InvalidPurchaseException("Ticket requests cannot contain null");
        }
    }

    private void evaluateBusinessRules(CalculatedTicketCounts counts) {

        if (counts.totalNoOfTickets() == 0) {
            throw new InvalidPurchaseException("At least one ticket must be requested");
        }
        if (counts.totalNoOfTickets() > MAX_TICKETS) {
            throw new InvalidPurchaseException("Cannot purchase more than " + MAX_TICKETS + " tickets");
        }
        if (counts.adultTickets() == 0) {
            throw new InvalidPurchaseException("At least one adult ticket should be added");
        }
        if (counts.infantTickets() > counts.adultTickets()) {
            throw new InvalidPurchaseException("Infants must not exceed the number of adults");
        }
    }
}
