package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import lombok.Getter;

@Getter
public enum OrderState {
  CREATED("CREATED", "Utworzony"),
  IN_QUEUE("IN_QUEUE", "W kolejce"),
  WAITING_FOR_CHEMIST_APPROVAL("WAITING_FOR_CHEMIST_APPROVAL", "Oczekuje na zatwierdzenie przez farmaceutę"),
  APPROVED("APPROVED", "Zatwierdzony"),
  REJECTED_BY_CHEMIST("REJECTED_BY_CHEMIST", "Odrzucony przez farmaceutę"),
  REJECTED_BY_PATIENT("REJECTED_BY_PATIENT", "Odrzucony przez pacjenta");

  private String orderStateName;
  private String orderStateDescription;

  OrderState(String orderStateName, String orderStateDescription) {
    this.orderStateName = orderStateName;
    this.orderStateDescription = orderStateDescription;
  }
}
