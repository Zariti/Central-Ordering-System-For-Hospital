package com.example.hospital.controllers;

import com.example.hospital.models.Order;
import com.example.hospital.models.Result;
import com.example.hospital.models.User;
import com.example.hospital.services.OrderService;
import com.example.hospital.services.ResultService;
import jakarta.servlet.http.HttpSession;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/results")
public class ResultController {


    private final ResultService resultService;
    private final OrderService orderService;

    @Autowired
    public ResultController(ResultService resultService,OrderService orderService){
        this.resultService = resultService;
        this.orderService=orderService;
    }

//    @GetMapping
//    public String getAllResults(Model model) {
//        List<Result> results = resultService.getAllResults();
//        model.addAttribute("results", results);
//        return "list";  // Assuming you have a Thymeleaf template named "result/list.html"
//    }

//    @GetMapping("/{id}")
//    public String getResultById(@PathVariable String id, Model model) {
//        Result result = resultService.getResultById(id);
//        model.addAttribute("result", result);
//        return "result/details";  // Assuming you have a Thymeleaf template named "result/details.html"
//    }
//
//    @GetMapping("/search")
//    public String searchResults(@RequestParam Map<String, String> params, Model model) {
//        List<Result> results = resultService.searchResults(params);
//        model.addAttribute("results", results);
//        return "result/list";  // Assuming you have a Thymeleaf template named "result/list.html"
//    }

    @GetMapping("/make")
    public String showResultForm(Model model) {
        model.addAttribute("result", new Result());
        return "/create_result";
    }




    @DeleteMapping("/{id}")
    public String deleteResult(@PathVariable String id) {
        resultService.deleteResult(id);
        return "redirect:/results";
    }

    @GetMapping("/create/{orderId}")  // dohvaca formu koja mijenja result sa strane specijalista
    public String showCreateResultForm(Model model,@PathVariable String orderId, HttpSession session) { // dohvaca orderid ordera kojeg mijenja
        model.addAttribute("result", new Result());
        Optional<Order> orderOptional=orderService.getOrderById(orderId); // priko id ordera dohvaca order objekt
        orderOptional.ifPresent(order -> session.setAttribute("order", order));
        orderOptional.ifPresent(value -> model.addAttribute("order", value));
        //model.addAttribute("order", order);

        return "create_result";
    }

    @PostMapping("/create")
    public String createResult(Result result, HttpSession httpSession) {
        Order order=(Order) httpSession.getAttribute("order"); // order dohvacamo iz sesije kako bi mu dodali result
        resultService.createResult(result);  // result se sprema u tablicu
        orderService.updateResultForOrder(order.getId(),result);  // u order se doda ID od novokreiranog resulta

        return "redirect:/orders/"+ order.getId();
    }
}
