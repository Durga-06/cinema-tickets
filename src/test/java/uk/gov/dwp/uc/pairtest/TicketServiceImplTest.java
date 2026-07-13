package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketPaymentService ticketPaymentService;

    @Mock
    private SeatReservationService seatReservationService;

    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(ticketPaymentService, seatReservationService);
    }

    @Test
    void ticketPurchaseWithNullAccountIdIsRejected() {
        TicketTypeRequest singleAdult = new TicketTypeRequest(Type.ADULT, 1);

        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(null, singleAdult));
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -999L})
    void ticketPurchaseWithNonPositiveAccountIdIsRejected(long accountId) {
        TicketTypeRequest singleAdult = new TicketTypeRequest(Type.ADULT, 1);

        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(accountId, singleAdult));
    }

    @Test
    void ticketPurchaseWithValidSingleTicketTypeRequestAndValidAccountIdIsAllowed() {
        TicketTypeRequest singleAdult = new TicketTypeRequest(Type.ADULT, 1);

        ticketService.purchaseTickets(1L, singleAdult);

        verify(ticketPaymentService).makePayment(1L, 25);
        verify(seatReservationService).reserveSeat(1L, 1);
    }

    @Test
    void ticketPurchaseWithValidMultipleTicketTypeRequestAndValidAccountIdIsAllowed() {
        TicketTypeRequest twoAdults = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest singleChild = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest twoInfants = new TicketTypeRequest(Type.INFANT, 2);

        ticketService.purchaseTickets(1L, twoAdults, singleChild, twoInfants);

        verify(ticketPaymentService).makePayment(1L, 65);
        verify(seatReservationService).reserveSeat(1L, 3);
    }

    @Test
    void ticketPurchaseWithNoTicketsIsRejected() {
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(1L));
    }

    @Test
    void ticketPurchaseWithNullRequestElementIsRejected() {
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(1L, (TicketTypeRequest) null));
    }

    @ParameterizedTest
    @EnumSource(value = Type.class, names = {"CHILD", "INFANT"})
    void ticketPurchaseWithoutAdultIsRejected(Type type) {
        TicketTypeRequest noAdultRequest = new TicketTypeRequest(type, 1);

        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(1L, noAdultRequest));
    }

    @Test
    void ticketPurchaseMoreThanMaxTicketsIsRejected() {
        TicketTypeRequest twentySixAdults = new TicketTypeRequest(Type.ADULT, 26);

        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(1L, twentySixAdults));
    }

    @Test
    void ticketPurchaseWithLessAdultsThanInfantsIsRejected() {
        TicketTypeRequest oneAdult = new TicketTypeRequest(Type.ADULT, 1);
        TicketTypeRequest twoInfants = new TicketTypeRequest(Type.INFANT, 2);

        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(1L, oneAdult, twoInfants));
    }

    @Test
    void ticketPurchaseWithExactTicketLimitsIsAllowed() {
        TicketTypeRequest twentyFiveAdults = new TicketTypeRequest(Type.ADULT, 25);

        ticketService.purchaseTickets(1L, twentyFiveAdults);

        verify(ticketPaymentService).makePayment(1L, 625);
        verify(seatReservationService).reserveSeat(1L, 25);
    }

    @Test
    void ticketPurchaseWithEqualInfantsAndAdultsIsAllowed() {
        TicketTypeRequest oneAdult = new TicketTypeRequest(Type.ADULT, 1);
        TicketTypeRequest oneInfant = new TicketTypeRequest(Type.INFANT, 1);

        ticketService.purchaseTickets(1L, oneAdult, oneInfant);

        verify(ticketPaymentService).makePayment(1L, 25);
        verify(seatReservationService).reserveSeat(1L, 1);
    }
}
