package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Objects;

public class TicketServiceImpl implements TicketService {

    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {

        this.ticketPaymentService = Objects.requireNonNull(ticketPaymentService, "ticketPaymentService must not be null");
        this.seatReservationService = Objects.requireNonNull(seatReservationService, "seatReservationService must not be null");
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        if (accountId == null) {
            throw new InvalidPurchaseException("Account Id should not be null");
        }

        if (accountId <= 0) {
            throw new InvalidPurchaseException("Account Id should be greater than 0");
        }

        int totalAmount = 0;
        int numberOfSeats = 0;

        for (TicketTypeRequest request : ticketTypeRequests) {

            int price = switch (request.type()) {
                case ADULT -> 25;
                case CHILD -> 15;
                case INFANT -> 0;
            };

            int seats = switch (request.type()) {
                case ADULT, CHILD -> 1;
                case INFANT -> 0;
            };

            totalAmount += price * request.noOfTickets();
            numberOfSeats += seats * request.noOfTickets();
        }

        ticketPaymentService.makePayment(accountId, totalAmount);
        seatReservationService.reserveSeat(accountId, numberOfSeats);
    }
}
