package com.cydeo.controller;

import com.cydeo.dto.UserDto;
import com.cydeo.service.CompanyService;
import com.cydeo.service.RoleService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final RoleService roleService;
    private final UserService userService;
    private final CompanyService companyService;

    public UserController(RoleService roleService, UserService userService, CompanyService companyService) {
        this.roleService = roleService;
        this.userService = userService;
        this.companyService = companyService;
    }

    //    End-user should be able to List (display) all Users in the user_list page...
    @GetMapping("/list")
    public String getAllUsers(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("users", userService.getAllUsers());
        return "user/user-list";
    }

    //    End-user should be able to Edit each User, when click on Edit button, end-user should land on user_update
    //    page and the edit form should be populated with the information of that very same User.
    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
        model.addAttribute("companies", companyService.getCompanyDtoByLoggedInUser());
        model.addAttribute("users", userService.getAllUsers());
        return "user/user-update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
        boolean emailExist = userService.findByUsernameCheck(userDTO.getUsername());

        if (bindingResult.hasErrors()) {
            if (emailExist) {
                bindingResult.rejectValue("username", " ", "A user with this email already exists. Please try with different email.");
            }
            model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
            model.addAttribute("companies", companyService.getCompanyDtoByLoggedInUser());
            model.addAttribute("users", userService.getAllUsers());
            return "user/user-update";

        }
        userDTO.setUsername(userDTO.getUsername());
        userService.updateUser(userDTO);
        return "redirect:/users/list";
    }

    //    End-user should be able to Delete each User (soft delete), then end up to the user_list page with updated User list.
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/users/list";
    }

    //    When End-User clicks on "Create-User" button, user_create page should be displayed with an Empty user form,
    @GetMapping("/create")
    public String createUser(Model model) {
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
        model.addAttribute("companies", companyService.getCompanyDtoByLoggedInUser());
        return "user/user-create";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("newUser") UserDTO userDTO, BindingResult bindingResult, Model model) {
        boolean emailExist = userService.findByUsernameCheck(userDTO.getUsername());
        if (bindingResult.hasErrors()) {
            if (emailExist) {
                bindingResult.rejectValue("username", " ", "A user with this email already exists. Please try with different email.");
            }

            model.addAttribute("userRoles", roleService.getAllRolesForCurrentUser());
            model.addAttribute("companies", companyService.getCompanyDtoByLoggedInUser());
            return "/user/user-create";
        }

        userService.save(userDTO);
        return "redirect:/users/list";
    }

}