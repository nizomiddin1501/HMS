package revolusion.developers.hms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import revolusion.developers.hms.payload.CustomApiResponse;
import revolusion.developers.hms.payload.RoleDto;
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
     * Retrieve a paginated list of all roles.
     *
     * This method fetches a paginated list of role records and returns them as a list of RoleDto.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of roles per page (default is 10)
     * @return a ResponseEntity containing a CustomApiResponse with the paginated list of RoleDto representing all roles
     */
    @Operation(summary = "Get all Roles with Pagination", description = "Retrieve a paginated list of all roles.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of roles.")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Page<RoleDto>>> getAllRoles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<RoleDto> roleDtos = roleService.getAllRoles(page,size);
        CustomApiResponse<Page<RoleDto>> response = new CustomApiResponse<>(
                "Successfully retrieved the list of roles.",
                true,
                roleDtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }





    /**
     * Retrieve a role by their unique ID using the provided RoleDto.
     *
     * This method retrieves a role's details based on their ID and returns
     * a CustomApiResponse containing the corresponding RoleDto if found.
     * If the role does not exist, it returns a CustomApiResponse with a
     * message indicating that the role was not found and a 404 Not Found status.
     *
     * @param id the ID of the role to retrieve
     * @return a ResponseEntity containing a CustomApiResponse with the RoleDto and
     *         an HTTP status of OK, or a NOT FOUND status if the role does not exist.
     */
    @Operation(summary = "Get Role by ID", description = "Retrieve a role by their unique identifier.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the role.")
    @ApiResponse(responseCode = "404", description = "Role not found.")
    @GetMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoleDto>> getRoleById(@PathVariable Long id) {
        Optional<RoleDto> roleDto = roleService.getRoleById(id);
        if (roleDto.isPresent()){
            CustomApiResponse<RoleDto> response = new CustomApiResponse<>(
                    "Successfully retrieved the role.",
                    true,
                    roleDto.get()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<RoleDto> response = new CustomApiResponse<>(
                    "Role not found.",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Creates a new role.
     *
     * This method validates the incoming role data (received via DTO) and saves it to the database
     * if valid.
     *
     * @param roleDto the DTO containing the role information to be saved
     * @return a ResponseEntity containing a CustomApiResponse with the saved role data
     */
    @Operation(summary = "Create a new Role", description = "Create a new role record.")
    @ApiResponse(responseCode = "201", description = "Role created successfully.")
    @PostMapping
    public ResponseEntity<CustomApiResponse<RoleDto>> createRole(@Valid @RequestBody RoleDto roleDto){
        RoleDto savedRole = roleService.createRole(roleDto);
        CustomApiResponse<RoleDto> response = new CustomApiResponse<>(
                "Role created successfully",
                true,
                savedRole
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    /**
     * Update the details of an existing role using the provided RoleDto.
     *
     * This method accepts the role's ID and a DTO containing updated role details.
     * It updates the role record if it exists and returns the updated RoleDto object.
     *
     * @param id the ID of the role to be updated
     * @param roleDto the DTO containing updated role details
     * @return a ResponseEntity containing a CustomApiResponse with the updated RoleDto,
     *         or a NOT FOUND response if the role does not exist
     */
    @Operation(summary = "Update role", description = "Update the details of an existing role.")
    @ApiResponse(responseCode = "200", description = "Role updated successfully")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse<RoleDto>>  updateRole(
            @PathVariable Long id,
            @RequestBody RoleDto roleDto) {
        Optional<RoleDto> roleDtoOptional = roleService.getRoleById(id);
        if (roleDtoOptional.isPresent()) {
            RoleDto updatedRole = roleService.updateRole(id, roleDto);
            CustomApiResponse<RoleDto> response = new CustomApiResponse<>(
                    "Role updated successfully",
                    true,
                    updatedRole
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            CustomApiResponse<RoleDto> response = new CustomApiResponse<>(
                    "Role not found",
                    false,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }



    /**
     * Delete a role by their ID.
     *
     * This method deletes the role record based on the given ID if it exists.
     *
     * @param id the ID of the role to delete
     * @return a ResponseEntity containing a CustomApiResponse with the status of the operation,
     *         or NOT FOUND if the role does not exist
     */
    @Operation(summary = "Delete Role", description = "Delete a role by its ID.")
    @ApiResponse(responseCode = "204", description = "Role deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Role not found.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomApiResponse<Void>> deleteRole(@PathVariable Long id) {
        Optional<RoleDto> roleDto = roleService.getRoleById(id);
        if (roleDto.isPresent()) {
            roleService.deleteRole(id);
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Role deleted successfully.",
                    true,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NO_CONTENT);
        } else {
            CustomApiResponse<Void> customApiResponse = new CustomApiResponse<>(
                    "Role not found with ID: " + id,
                    false,
                    null);
            return new ResponseEntity<>(customApiResponse, HttpStatus.NOT_FOUND);
        }
    }



}
