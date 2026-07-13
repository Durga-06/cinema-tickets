package uk.gov.dwp.uc.pairtest.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TicketPurchaseSteps {

    private final TicketPaymentService paymentService = mock(TicketPaymentService.class);
    private final SeatReservationService seatReservationService = mock(SeatReservationService.class);
    private final TicketServiceImpl ticketService =
        new TicketServiceImpl(paymentService, seatReservationService);

    private static final long ACCOUNT_ID = 1L;
    private TicketTypeRequest[] requests;

    @Given("a valid account")
    public void aValidAccount() {
        // account id is fixed above(ACCOUNT_ID) and valid for these scenarios
    }

    @When("they purchase {int} adult, {int} child and {int} infant tickets")
    public void theyPurchase(int adults, int children, int infants) {
        ticketService.purchaseTickets(ACCOUNT_ID, buildRequests(adults, children, infants));
    }

    @When("they attempt to purchase {int} adult, {int} child and {int} infant tickets")
    public void theyAttemptToPurchase(int adults, int children, int infants) {
        this.requests = buildRequests(adults, children, infants);
    }

    @Then("payment of {int} is taken")
    public void paymentIsTaken(int amount) {
        verify(paymentService).makePayment(ACCOUNT_ID, amount);
    }

    @Then("{int} seats are reserved")
    public void seatsAreReserved(int seats) {
        verify(seatReservationService).reserveSeat(ACCOUNT_ID, seats);
    }

    @Then("the purchase is rejected")
    public void thePurchaseIsRejected() {
        assertThrows(InvalidPurchaseException.class,
            () -> ticketService.purchaseTickets(ACCOUNT_ID, requests));
    }

    private TicketTypeRequest[] buildRequests(int adults, int children, int infants) {
        List<TicketTypeRequest> list = new ArrayList<>();
        if (adults > 0) list.add(new TicketTypeRequest(Type.ADULT, adults));
        if (children > 0) list.add(new TicketTypeRequest(Type.CHILD, children));
        if (infants > 0) list.add(new TicketTypeRequest(Type.INFANT, infants));
        return list.toArray(new TicketTypeRequest[0]);
    }
}
