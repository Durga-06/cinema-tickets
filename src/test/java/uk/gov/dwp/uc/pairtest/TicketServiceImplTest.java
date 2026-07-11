package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketServiceImplTest {

    @Test
    void ticketPurchaseWithNullAccountIdIsRejected() {
        TicketServiceImpl ticketService = new TicketServiceImpl();
        TicketTypeRequest singleAdult = new TicketTypeRequest(Type.ADULT, 1);

        assertThrows(
            InvalidPurchaseException.class,
            () -> ticketService.purchaseTickets(null, singleAdult)
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -999L})
    void ticketPurchaseWithNonPositiveAccountIdIsRejected(long accountId) {
        TicketServiceImpl ticketService = new TicketServiceImpl();
        TicketTypeRequest singleAdult = new TicketTypeRequest(Type.ADULT, 1);

        assertThrows(
            InvalidPurchaseException.class,
            () -> ticketService.purchaseTickets(accountId, singleAdult)
        );
    }
}
