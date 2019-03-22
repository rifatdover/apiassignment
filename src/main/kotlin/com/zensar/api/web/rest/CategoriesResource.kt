package com.zensar.api.web.rest

import com.zensar.api.service.BackendCallService
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class CategoriesResource {

    @Autowired
    lateinit var backendCallService: BackendCallService

    @RequestMapping("/products/{path}")
    @ResponseBody
    fun getProducts(@PathVariable("path") path: String = "600001506",
                    @RequestParam("labelType") type: String?): List<ProductDTO>? {
        return runBlocking {
            backendCallService.getProducts(path, type?.trim())
        }
    }

    @RequestMapping("/products")
    @ResponseBody
    fun getProducts(@RequestParam("labelType") type: String?): List<ProductDTO>? {
        return runBlocking {
            backendCallService.getProducts("600001506", type?.trim())
        }
    }
}