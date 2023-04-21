/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;

import java.sql.SQLException;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author eeuser
 */
@RestController
@RequestMapping("api/todolists")
public class ToDoListController {
    private ToDoListDao toDoListDao;
    
    public ToDoListController(ToDoListDao toDoListDao) {
        this.toDoListDao = toDoListDao;
    }

    //curl -i http://localhost:80/api/todolists/all   
    @GetMapping("all")
    public List<ToDoList> getAllToDoLists() {
        return toDoListDao.getAllToDoLists();
    }
    
    // curl -i http://localhost:80/api/todolists?status=Completed   
    @GetMapping("")
    public List<ToDoList> getAllToDoListsByStatus(@RequestParam String status) {
        return toDoListDao.getAllToDoListsByStatus(status);
    }

    // curl -i -X POST -H "Content-Type: application/json" -d "{\"id\":20, \"item\":\"Shopping\", \"status\":\"pending\"}" http://localhost:80/api/todolists
    @PostMapping("")
    public void addToDoList(@RequestBody ToDoList toDoList) {
        toDoListDao.addToDoList(toDoList);
    }

    // curl -i -X PUT -H "Content-Type: application/json" -d '{\"id\":1, \"item\":\"Hangout\", \"status\":\"pending\"}' http://localhost:80/api/todolists/10004
    @PutMapping("/{id}")
    public void updateToDoList(@PathVariable int id, @RequestBody ToDoList toDoList) {
        toDoList.setId(id);
        toDoListDao.updateToDoList(toDoList);
    }
    
    // curl -i -X DELETE http://localhost:80/api/todolists/1
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        toDoListDao.deleteToDoList(id);
    }
    
   
    
}
