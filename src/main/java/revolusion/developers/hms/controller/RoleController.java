package revolusion.developers.hms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.exceptions.RoleException;
import revolusion.developers.hms.exceptions.UserException;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.RoleDto;
import revolusion.developers.hms.payload.UserDto;
import revolusion.developers.hms.service.RoleService;

import java.util.Optional;

/**
 * Controller for handling requests related to Role operations.
 * This controller provides RESTful endpoints to manage user records,
 * including creating, updating, retrieving, and deleting role information.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;



    /**
     * Retrieve a paginated list of roles.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of roles per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated RoleDto list
     */
    @Operation(summary = "Get all Roles with Pagination", description = "Retrieve a paginated list of all roles.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of roles.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<RoleDto>>> getAllRoles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<RoleDto> roleDtos = roleService.getAllRoles(page,size);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the list of roles.",
                true,
                roleDtos), HttpStatus.OK);
    }





    /**
     * Retrieve a user by their unique ID using the provided UserDto.
     *
     * @param id the ID of the user to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the UserDto and
     *         an HTTP status of OK
     */
    @Operation(summary = "Get Role by ID", description = "Retrieve a role by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the role.")
    @ApiResponse(responseCode = "404", description = "Role not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoleDto>> getRoleById(@PathVariable Long id) {
        RoleDto roleDto = roleService.getRoleById(id)
                .orElseThrow(() -> new RoleException("Role not found"));
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Successfully retrieved the role.",
                true,
                roleDto), HttpStatus.OK);
        }


    /**
     * Creates a new role.
     *
     * @param roleDto the DTO containing the role information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved role data
     */
    @Operation(summary = "Create a new Role", description = "Create a new role record.")
    @ApiResponse(responseCode = "201", description = "Role created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<RoleDto>> createRole(@Valid @RequestBody RoleDto roleDto){
        RoleDto savedRole = roleService.createRole(roleDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Role created successfully",
                true,
                savedRole), HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing role using the provided RoleDto.
     *
     * @param id the ID of the role to be updated
     * @param roleDto the DTO containing updated role details
     * @return a ResponseEntity containing a CustomApiResponse with the updated RoleDto
     */
    @Operation(summary = "Update role", description = "Update the details of an existing role.")
    @ApiResponse(responseCode = "200", description = "Role updated successfully")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoleDto>>  updateRole(
            @PathVariable Long id,
            @RequestBody RoleDto roleDto) {
        RoleDto updatedRole = roleService.updateRole(id, roleDto);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Role updated successfully",
                true,
                updatedRole), HttpStatus.OK);
        }



    /**
     * Delete a role by their ID.
     *
     * @param id the ID of the role to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation
     */
    @Operation(summary = "Delete Role", description = "Delete a role by its ID.")
    @ApiResponse(responseCode = "204", description = "Role deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Role not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return new ResponseEntity<>(new CustomApiResponse<>(
                "Role deleted successfully.",
                true,
                null), HttpStatus.NO_CONTENT);
        }
    }

