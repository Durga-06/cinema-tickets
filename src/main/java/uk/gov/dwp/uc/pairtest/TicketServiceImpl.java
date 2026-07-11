package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {

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
