package org.example.Controllers;

import org.example.DTO.ApiAboutDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("api/about")
    public ResponseEntity<ApiAboutDTO> getHelloMessage(){
        ApiAboutDTO aboutDTO=new ApiAboutDTO("Gurshan Aulakh and Prottoy Zaman Awesome App","Gurshan Aulakh and Prottoy Zaman");
        return new ResponseEntity<>(aboutDTO, HttpStatus.OK);

    }
}
