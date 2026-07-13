package uk.gov.dwp.uc.pairtest.domain;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketTypeRequestTest {

    @Test
    void negativeNumberOfTicketsIsRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new TicketTypeRequest(Type.ADULT, -1));
    }

    @Test
    void nullTypeIsRejected() {
        assertThrows(NullPointerException.class,
            () -> new TicketTypeRequest(null, 1));
    }
}
