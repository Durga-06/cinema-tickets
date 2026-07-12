package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
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
    void ticketPurchaseWithValidTicketTypeRequestAndValidAccountIdIsAllowed() {
        TicketTypeRequest singleAdult = new TicketTypeRequest(Type.ADULT, 1);

        ticketService.purchaseTickets(1L, singleAdult);

        verify(ticketPaymentService).makePayment(1L, 25);
        verify(seatReservationService).reserveSeat(1L, 1);
    }
}
