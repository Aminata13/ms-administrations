package com.safelogisitics.gestionentreprisesusers.security.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.Privilege;
import com.safelogisitics.gestionentreprisesusers.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String id;

	private String username;

	private InfosPerso infosPerso;

	private int status;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(String id, InfosPerso infosPerso, String username, String password, int status,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.infosPerso = infosPerso;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = getGrantedAuthorities(user.getInfosPerso().getTypeAndPrivileges()); /* user.getInfosPerso().getComptes().stream()
      .map(compte -> new SimpleGrantedAuthority(compte.getType().name()))
      .collect(Collectors.toList()); */

		return new UserDetailsImpl(
      user.getId(),
      user.getInfosPerso(),
      user.getUsername(), 
      user.getPassword(),
      user.getStatut(),
      authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getId() {
		return id;
	}

	public InfosPerso getInfosPerso() {
		return infosPerso;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		if (status == -1) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (status != -1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

  public static List<GrantedAuthority> getGrantedAuthorities(List<String> typeAndPrivileges) {

    List<GrantedAuthority> _authorities = new ArrayList<>();

    for (String _authority : typeAndPrivileges) {
      _authorities.add(new SimpleGrantedAuthority(_authority));
    }

    return _authorities;
  } 
}
