package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
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
}
