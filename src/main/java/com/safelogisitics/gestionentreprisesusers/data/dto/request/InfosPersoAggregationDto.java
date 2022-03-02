package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.util.List;

import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;

import org.springframework.stereotype.Component;

@Component
public class InfosPersoAggregationDto {
  
  private int count;

  private List<InfosPersoModel> infosPersos;

  public InfosPersoAggregationDto() {}

  public int getCount() {
    return this.count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public List<InfosPersoModel> getInfosPersos() {
    return this.infosPersos;
  }

  public void setInfosPersos(List<InfosPersoModel> infosPersos) {
    this.infosPersos = infosPersos;
  }

}
