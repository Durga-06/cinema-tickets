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
    }

}
