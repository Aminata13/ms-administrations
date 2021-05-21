package com.safelogisitics.gestionentreprisesusers.dao.filter;

import com.safelogisitics.gestionentreprisesusers.model.enums.EPrivilege;

public interface PrivilegeDefaultFields {
  public String getId();

  public String getLibelle();

  public EPrivilege getValeur();

  public int getStatut();
}
