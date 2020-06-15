package com.vn.controller;

import com.vn.model.Category;
import com.vn.model.Customer;
import com.vn.repository.ProductRepository1;
import com.vn.service.CategoryService;
import com.vn.service.CustomerService;
import com.vn.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductRepository1 productRepository1;

    @RequestMapping("/home")
    public String home(Model model, HttpSession session,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String categoryName) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            model.addAttribute("customer", new Customer());
        } else {
            model.addAttribute("customer", customer);
        }
        if (categoryName.length() == 0) {
            model.addAttribute("productList", productRepository1.findAll(PageRequest.of(page, 6)));
        } else {
            model.addAttribute("productList", productRepository1.findByCategory(categoryName, PageRequest.of(page, 6)));
        }
        model.addAttribute("categoryList", categoryService.findAll());
        model.addAttribute("categoryName", categoryName);
        return "index";
    }

    @RequestMapping("/loginCustomerView")
    public String loginCustomerView(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                                    @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                                    Model model,
                                    HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            model.addAttribute("customer", new Customer());
        } else {
            model.addAttribute("customer", customer);
        }
        model.addAttribute("emailCustomer", emailCustomer);
        model.addAttribute("passwordCustomer", passwordCustomer);
        return "loginUser";
    }

    @PostMapping("/loginCustomer")
    public String loginCustomer(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer1,
                                Customer customer, HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) {
        List<Customer> customerList = customerService.findAll();
        for (Customer customer1 : customerList) {
            if (customer1.getEmailCustomer().equalsIgnoreCase(customer.getEmailCustomer().trim())) {
                if (customer1.getPasswordCustomer().equals(customer.getPasswordCustomer())) {
                    Cookie emailCustomer = new Cookie("emailCustomer", customer1.getEmailCustomer());
                    Cookie passwordCustomer = new Cookie("passwordCustomer", customer1.getPasswordCustomer());
                    if (request.getParameter("rememberCustomer") != null) {
                        emailCustomer.setMaxAge(3600);
                        passwordCustomer.setMaxAge(3600);
                    } else {
                        emailCustomer.setMaxAge(0);
                        passwordCustomer.setMaxAge(0);
                    }
                    response.addCookie(emailCustomer);
                    response.addCookie(passwordCustomer);
                    session.setAttribute("customer", customer1);
                    return home(model, session, 0, "");
                } else {
                    model.addAttribute("message", "Wrong password !");
                    model.addAttribute("alert", "alert alert-danger");
                    model.addAttribute("emailCustomer", emailCustomer1);
                    model.addAttribute("customer", new Customer());
                    return "/loginUser";
                }
            } else {
                continue;
            }
        }
        model.addAttribute("message", "Can not find this email address !");
        model.addAttribute("alert", "alert alert-danger");
        model.addAttribute("emailCustomer", emailCustomer1);
        model.addAttribute("customer", new Customer());
        return "/loginUser";
    }

    @RequestMapping("/logoutCustomer")
    public String logout(HttpServletRequest request, HttpSession session, Model model) {
        Customer customer = checkCookie(request);
        model.addAttribute("emailCustomer", customer.getEmailCustomer());
        model.addAttribute("passwordCustomer", customer.getPasswordCustomer());
        session.removeAttribute("customer");
        model.addAttribute("customer", new Customer());
        return "loginUser";
    }

    public Customer checkCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Customer customer = new Customer();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("emailCustomer")) {
                customer.setEmailCustomer(cookie.getValue());
            }
            if (cookie.getName().equalsIgnoreCase("passwordCustomer")) {
                customer.setPasswordCustomer(cookie.getValue());
            }
        }
        if (customer.getEmailCustomer() == null) {
            customer.setEmailCustomer("");
        }
        return customer;
    }

    @RequestMapping("/signupCustomer")
    public String signupCustomer(@CookieValue(name = "emailCustomer", defaultValue = "") String emailCustomer,
                                 @CookieValue(name = "passwordCustomer", defaultValue = "") String passwordCustomer,
                                 @RequestParam(name = "genderCustomer", required = false) String gender,
                                 Customer customer, Model model, HttpServletRequest request) {

        model.addAttribute("customer", new Customer());
        model.addAttribute("emailCustomer", emailCustomer);
        model.addAttribute("passwordCustomer", passwordCustomer);

        String validate = validateSignup(model, customer, customer.getEmailCustomer(), customer.getCustomerName(),
                customer.getPhone(), gender, customer.getPasswordCustomer(), request.getParameter("confirm"), "loginUser");
        if (validate.length() > 0) {
            return validate;
        }

        customer.setStatus("");

        customerService.save(customer);

        model.addAttribute("message1", "Register Successfully !");
        model.addAttribute("alert1", "alert alert-success");
        return "loginUser";
    }

    public String validateSignup(Model model, Customer customer, String emailCustomer, String customerName,
                                 String phone, String gender, String passwordCustomer, String confirmPassword, String view) {
        List<Customer> customerList = customerService.findAll();
        for (Customer customer1 : customerList) {
            if (emailCustomer.equalsIgnoreCase(customer1.getEmailCustomer())) {
                model.addAttribute("message1", "This Email Address Is Taken !");
                model.addAttribute("alert1", "alert alert-danger");
                return view;
            }
        }
        return validateUpdate(model, customer, emailCustomer, customerName, phone, gender, passwordCustomer, confirmPassword, view);
    }

    public String validateUpdate(Model model, Customer customer, String emailCustomer, String customerName,
                                 String phone, String gender, String passwordCustomer, String confirmPassword, String view) {
        if (!emailCustomer.matches("\\w+@\\w+(\\.\\w+){1,2}")) {
            model.addAttribute("message1", "Invalid Email Address !");
            model.addAttribute("alert1", "alert alert-danger");
            return view;
        }
        if (!customerName.matches("^[a-zA-Z\\s\\p{L}]+")) {
            model.addAttribute("message1", "Only Alphabet and White Space Characters !");
            model.addAttribute("alert1", "alert alert-danger");
            return view;
        }
        if (!phone.matches("0\\d{9}")) {
            model.addAttribute("message1", "Invalid Phone Number !");
            model.addAttribute("alert1", "alert alert-danger");
            return view;
        }
        if (gender == null) {
            model.addAttribute("message1", "Choose your gender !");
            model.addAttribute("alert1", "alert alert-danger");
            return view;
        } else if (gender.equalsIgnoreCase("male")) {
            customer.setGender(true);
        } else {
            customer.setGender(false);
        }
        if (passwordCustomer.trim().length() < 3) {
            model.addAttribute("message1", "Password at least 3 letters !");
            model.addAttribute("alert1", "alert alert-danger");
            return view;
        } else if (!passwordCustomer.equals(confirmPassword)) {
            model.addAttribute("message1", "Password and confirm password are not match !");
            model.addAttribute("alert1", "alert alert-danger");
            return view;
        }
        return "";
    }

    @RequestMapping("/updateCustomerView")
    public String updateCustomerView(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", customer);

        return "updateUser";
    }

    @PostMapping("/updateCustomer")
    public String updateCustomer(HttpSession session, Model model, Customer customer, HttpServletRequest request,
                                 @RequestParam(name = "genderCustomer", required = false) String gender) {
        Customer customer1 = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", customer1);

        String validate = validateUpdate(model, customer, customer.getEmailCustomer(), customer.getCustomerName(),
                customer.getPhone(), gender, customer.getPasswordCustomer(), request.getParameter("confirm"), "updateUser");
        if (validate.length() > 0) {
            return validate;
        }

        customer.setIdCustomer(customer1.getIdCustomer());
        session.setAttribute("customer", customer);
        model.addAttribute("customer", customer);
        customerService.save(customer);

        model.addAttribute("message1", "Update Successfully !");
        model.addAttribute("alert1", "alert alert-success");
        return "updateUser";
    }
}
