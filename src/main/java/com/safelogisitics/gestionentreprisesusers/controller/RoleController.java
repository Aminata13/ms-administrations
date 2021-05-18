package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.Collection;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.model.Privilege;
import com.safelogisitics.gestionentreprisesusers.model.Role;
import com.safelogisitics.gestionentreprisesusers.payload.request.RoleRequest;
import com.safelogisitics.gestionentreprisesusers.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class RoleController {
  
  @Autowired
  private RoleService roleService;

  @GetMapping("/list")
	public ResponseEntity<?> allRoles() {
    Collection<Role> roles = roleService.getRoles();
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @PostMapping("/add")
	public ResponseEntity<?> addRole(@Valid @RequestBody RoleRequest roleRequest) {
    Role role = roleService.createRole(roleRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(role);
	}

  @PostMapping("/add-multiple")
	public ResponseEntity<?> addMultiRole(@Valid @RequestBody Collection<RoleRequest> roleRequests) {
    Collection<Role> roles = roleService.createMultiRole(roleRequests);
		return ResponseEntity.status(HttpStatus.CREATED).body(roles);
	}

  @PutMapping("/update/{id}")
	public ResponseEntity<?> updateRole(@PathVariable(value = "id") String id, @Valid @RequestBody RoleRequest roleRequest) {
    Role role = roleService.updateRole(id, roleRequest);
		return ResponseEntity.status(HttpStatus.OK).body(role);
	}

  @PutMapping("/update-multiple")
	public ResponseEntity<?> updateMultiRole(@Valid @RequestBody Collection<RoleRequest> roleRequests) {
    Collection<Role> roles = roleService.updateMultiRole(roleRequests);
		return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteRole(@PathVariable(value = "id") String id) {
    roleService.deleteRole(id);
		return ResponseEntity.status(HttpStatus.OK).body(new String("Role is deleted"));
	}

  @DeleteMapping("delete/{ids}")
	public ResponseEntity<?> deleteMultiRole(@PathVariable(value = "ids") Collection<String> ids) {
    roleService.deleteMultiRole(ids);
		return ResponseEntity.status(HttpStatus.OK).body(new String("Role is deleted"));
	}

  @GetMapping("/privileges/list")
	public ResponseEntity<?> allPrivileges() {
    Collection<Privilege> privileges = roleService.getPrivileges();
    return ResponseEntity.status(HttpStatus.OK).body(privileges);
	}
}
