package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.Collection;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.dao.filter.PrivilegeDefaultFields;
import com.safelogisitics.gestionentreprisesusers.model.Role;
import com.safelogisitics.gestionentreprisesusers.payload.request.RoleRequest;
import com.safelogisitics.gestionentreprisesusers.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('COMPTE_ADMINISTRATEUR')")
public class RoleController {

  @Autowired
  private RoleService roleService;

  @GetMapping("/list")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'READ')")
	public ResponseEntity<?> allRoles() {
    Collection<Role> roles = roleService.getRoles();
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @PostMapping("/add")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'CREATE')")
	public ResponseEntity<?> addRole(@Valid @RequestBody RoleRequest roleRequest) {
    Role role = roleService.createRole(roleRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(role);
	}

  @PostMapping("/add-multiple")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'CREATE')")
	public ResponseEntity<?> addMultiRole(@Valid @RequestBody Collection<RoleRequest> roleRequests) {
    Collection<Role> roles = roleService.createMultiRole(roleRequests);
		return ResponseEntity.status(HttpStatus.CREATED).body(roles);
	}

  @PutMapping("/update/{id}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'WRITE')")
	public ResponseEntity<?> updateRole(@PathVariable(value = "id") String id, @Valid @RequestBody RoleRequest roleRequest) {
    Role role = roleService.updateRole(id, roleRequest);
		return ResponseEntity.status(HttpStatus.OK).body(role);
	}

  @PutMapping("/update-multiple")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'WRITE')")
	public ResponseEntity<?> updateMultiRole(@Valid @RequestBody Collection<RoleRequest> roleRequests) {
    Collection<Role> roles = roleService.updateMultiRole(roleRequests);
		return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

	@DeleteMapping("delete/{id}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'DELETE')")
	public ResponseEntity<?> deleteRole(@PathVariable(value = "id") String id) {
    roleService.deleteRole(id);
		return ResponseEntity.status(HttpStatus.OK).body(new String("Role is deleted"));
	}

  @DeleteMapping("delete/{ids}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'DELETE')")
	public ResponseEntity<?> deleteMultiRole(@PathVariable(value = "ids") Collection<String> ids) {
    roleService.deleteMultiRole(ids);
		return ResponseEntity.status(HttpStatus.OK).body(new String("Role is deleted"));
	}

  @GetMapping("/privileges/list")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'READ')")
	public ResponseEntity<?> allPrivileges() {
    Collection<PrivilegeDefaultFields> privileges = roleService.getPrivileges();
    return ResponseEntity.status(HttpStatus.OK).body(privileges);
	}
}
