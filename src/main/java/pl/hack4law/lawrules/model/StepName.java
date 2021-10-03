package pl.hack4law.lawrules.model;

import lombok.Getter;

@Getter
public enum StepName {
    DROPPED_NO_ACTION(true,"No action taken", "Sprawa odrzucona z powodu braku postępów"),
    CREDITOR_FAILED_TO_PAY(true,"Creditor failed to pay advance", "Wierzyciel nie wpłacił zaliczki"),
    BAILIFF_ADVANCE_COSTS_ESTIMATION
            (false,"Estimation the amount of the advance on bailiff costs", "Oszacowanie wysokości zaliczki na poczet kosztów komorniczych"),
    BAILIFF_ADVANCE_COSTS_SENT_TO_CREDITOR
            (false,"Email with advance costs sent to creditor", "Wysłanie maila zawierającego wysokość zaliczki do wierzyciela"),
    BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR
            (false,"Email with advance costs received by creditor", "Odebranie wiadomości zawierającej wysokość zaliczki przez wierzyciela"),
    CREDITOR_PAID_ADVANCE
            (false,"Creditor paid advance", "Wierzyciel opłacił zaliczkę"),
    CASE_DISCOUNTED_BY_CREDITOR
            (true,"Case discounted by creditor", "Sprawa umorzona przez wierzyciela"),
    CHECK_THE_GIVEN_ACCOUNT
            (false,"Check the given account", "Sprawdźić podane w sprawie konto"),
    CHECK_BANKS_FOR_ACCOUNT
            (false,"Check banks for creditor accounts", "Sprawdźić banki czy istnieją inne konta dłużnika"),
    SECURE_THE_DEBTS_ON_DEBTORS_ACCOUNT
            (false,"Secure the debts on the debtor's account", "zabezpiecz wierzytelności na koncie dłużnika"),
    SEND_NOTIFICATION_TO_CREDITOR
            (false,"Send notification to creditor","Wyślij powiadomienie do dłużnika"),
    GET_THE_CLAIM_FROM_DEBTORS_ACCOUNT
            (false,"Get the claim from the debtor's account","Pobierz wierzytelność z konta dłużnika"),
    SUMMARIZE_CAUSE_COST
            (false,"Summarize the costs and expenses for the cause","Podsumuj koszty i wydatki na rzecz sprawy (należność, rozliczenie z zaliczki)"),
    SEND_DEBT_TO_CREDITOR
            (false,"Send the debt to creditor","Wyslij należnosc wierzycielowi"),
    CASE_CLOSED
            (true,"Case Closed","Zamknięcie sprawy"),
    CHECK_BANK_FROM_APPLICATION
            (false,"Check bank from the application","Sprawdź bank z wniosku"),
    CHECK_CEPIK_AND_ZUS
            (false,"Check CEPIK and ZUS databases","Sprawdź bazy ZUS i CEPIKu"),
    REQUEST_LIST_OF_ASSETS_TO_DEBTOR
            (false,"Request list of assets to the debtor", "Żądanie złożenia wykazu majątku do dłużnika"),
    AUCTION
            (false,"Auction of debtor properties","Aukcja zajętych ruchomości i nieruchomości"),
    SEIZURE_OF_MOVABLE_PROPERTY
            (false,"Seizure of movable property","Zajęcie ruchomości przez komornika");


    private final boolean ending;
    private final String contentEN;
    private final String contentPL;

    StepName(boolean ending, String contentEN, String contentPL) {
        this.ending = ending;
        this.contentEN = contentEN;
        this.contentPL = contentPL;
    }
}
