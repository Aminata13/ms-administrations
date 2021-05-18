package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.Collection;

import com.safelogisitics.gestionentreprisesusers.model.Privilege;
import com.safelogisitics.gestionentreprisesusers.model.Role;
import com.safelogisitics.gestionentreprisesusers.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {
  
  @Autowired
  private RoleService roleService;

  @GetMapping("/privileges/list")
	public ResponseEntity<?> allPrivileges() {
    Collection<Privilege> privileges = roleService.getPrivileges();
    return ResponseEntity.status(HttpStatus.OK).body(privileges);
	}

  @GetMapping("/list")
	public ResponseEntity<?> allRoles() {
    Collection<Role> roles = roleService.getRoles();
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}
}
