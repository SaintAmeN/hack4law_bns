package pl.hack4law.lawrules.model;

import lombok.Getter;

@Getter
public enum StepName {
    DROPPED_NO_ACTION("No action taken", "Sprawa odrzucona z powodu braku postępów"),
    CREDITOR_FAILED_TO_PAY("Creditor failed to pay advance", "Wierzyciel nie wpłacił zaliczki"),
    BAILIFF_ADVANCE_COSTS_ESTIMATION
            ("Estimation the amount of the advance on bailiff costs", "Oszacowanie wysokości zaliczki na poczet kosztów komorniczych"),
    BAILIFF_ADVANCE_COSTS_SENT_TO_CREDITOR
            ("Email with advance costs sent to creditor", "Wysłanie maila zawierającego wysokość zaliczki do wierzyciela"),
    BAILIFF_ADVANCE_COSTS_RECEIVED_BY_CREDITOR
            ("Email with advance costs received by creditor", "Odebranie wiadomości zawierającej wysokość zaliczki przez wierzyciela"),
    CREDITOR_PAID_ADVANCE
            ("Creditor paid advance", "Wierzyciel opłacił zaliczkę");


    private final String contentEN;
    private final String contentPL;

    StepName(String contentEN, String contentPL) {
        this.contentEN = contentEN;
        this.contentPL = contentPL;
    }

}
