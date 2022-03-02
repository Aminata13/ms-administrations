package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.util.List;

import com.safelogisitics.gestionentreprisesusers.data.model.Compte;

import org.springframework.stereotype.Component;

@Component
public class CompteAggregationDto {
  
  private int count;

  private List<Compte> comptes;

  public CompteAggregationDto() {}

  public int getCount() {
    return this.count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public List<Compte> getComptes() {
    return this.comptes;
  }

  public void setComptes(List<Compte> comptes) {
    this.comptes = comptes;
  }

}
