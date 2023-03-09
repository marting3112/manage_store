package co.edu.umanizales.manage_store.controller;

import co.edu.umanizales.manage_store.controller.dto.ResponseDTO;
import co.edu.umanizales.manage_store.controller.dto.SaleDTO;
import co.edu.umanizales.manage_store.model.Sale;
import co.edu.umanizales.manage_store.model.Seller;
import co.edu.umanizales.manage_store.model.Store;
import co.edu.umanizales.manage_store.service.SaleService;
import co.edu.umanizales.manage_store.service.SellerService;
import co.edu.umanizales.manage_store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "sale")
public class SaleController {
    @Autowired
    private SaleService saleService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private StoreService storeService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getSales(){
        return new ResponseEntity<>(
                new ResponseDTO(200,
                        saleService.getSales(),
                        null),
                HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ResponseDTO> createSale(@RequestBody
                                                  SaleDTO saleDTO){
        Seller findSeller = sellerService.getSellerById(saleDTO.getSellerId());
        if( findSeller == null){
            return new ResponseEntity<>(new ResponseDTO(400,
                    "El vendedor ingresado no existe",null),
                    HttpStatus.BAD_REQUEST);
        }
        Store findStore = storeService.getStoreById(saleDTO.getStoreId());
        if( findStore == null){
            return new ResponseEntity<>(new ResponseDTO(400,
                    "La tienda ingresada no existe",null),
                    HttpStatus.BAD_REQUEST);
        }
        saleService.addSale(new Sale(findStore,findSeller,
                saleDTO.getQuantity()));
        return new ResponseEntity<>(new ResponseDTO(200,
                "Venta adicionada",null),
                HttpStatus.OK);

    }


    @GetMapping(path="/totalsales")
    public ResponseEntity<ResponseDTO> getTotalSales(){
        return new ResponseEntity<>(new ResponseDTO(200,
                saleService.getTotalSales(),null),
                HttpStatus.OK);

    }
    @GetMapping(path="/totalsalesbyseller/{code}")
    public ResponseEntity<ResponseDTO> getTotalSalesBySeller(@PathVariable String code){
        return new ResponseEntity<>(new ResponseDTO(200,
                saleService.getTotalSalesBySeller(code),null),
                HttpStatus.OK);


    }

    @GetMapping (path =  "/bestseller")
    public ResponseEntity<ResponseDTO> getBestSeller()
    {
        Seller findSeller = saleService.getBestSeller();

        if( findSeller == null){
            return new ResponseEntity<>(new ResponseDTO(400,
                    "no hay ventas",null),
                    HttpStatus.BAD_REQUEST);
        }
        int  num = saleService.getTotalSalesBySeller(findSeller.getCode());
        return new ResponseEntity<>(new ResponseDTO(200,
                "el mejor vendedor fue " + findSeller  +" con una cantidad de "+ num ,null),
                HttpStatus.OK);

    }


    @GetMapping (path =  "/totalsalesbystore/{code}")
    public ResponseEntity<ResponseDTO> getTotalsSalesByStore(@PathVariable String code  /*esto es para que lo que me me envie sea escrito en el postman */
    ){return  new ResponseEntity<>(new ResponseDTO(200,
            saleService.getTotalSalesByStore(code),null),
            HttpStatus.OK);

    }

    @GetMapping (path =  "/beststore")
    public ResponseEntity<ResponseDTO> getBestStore()
    {
        Store findStore = saleService.getBestStore();

        if( findStore == null){
            return new ResponseEntity<>(new ResponseDTO(400,
                    "no hay ventas",null),
                    HttpStatus.BAD_REQUEST);
        }
        int  num = saleService.getTotalSalesByStore(findStore.getCode());
        return new ResponseEntity<>(new ResponseDTO(200,
                "La mejor tienda fue " + findStore +" con una cantidad de "+ num ,null),
                HttpStatus.OK);

    }


    @GetMapping(path = "/averagesalesbystore")
    public ResponseEntity<ResponseDTO> getAverageSalesByStore(){



       if (storeService.getStores().isEmpty())
        {
            return new ResponseEntity<>(new ResponseDTO(400,
                    "lista de tienda vacia",null),
                    HttpStatus.BAD_REQUEST);
        }
        else if (saleService.getSales().isEmpty() ){
        return new ResponseEntity<>(new ResponseDTO(400,
                " lista de ventas vacia ",null),
                HttpStatus.BAD_REQUEST);
        }
        else{

        return new ResponseEntity<>(new ResponseDTO(200,
                saleService.getTotalSales()/(float)storeService.getStores().size(),
                null),HttpStatus.OK);
        }
    }

    @GetMapping(path = "/averagesalesbyseller")
    public ResponseEntity<ResponseDTO> getAverageSalesBySeller(){

        if (sellerService.getSellers().isEmpty())
        {
            return new ResponseEntity<>(new ResponseDTO(400,
                    "lista de tienda vacia",null),
                    HttpStatus.BAD_REQUEST);
        }
        else if (saleService.getSales().isEmpty() ){
            return new ResponseEntity<>(new ResponseDTO(400,"lista de ventas vacia ",null),
                    HttpStatus.BAD_REQUEST);
        }

        
        else{

            return new ResponseEntity<>(new ResponseDTO(200,
                    saleService.getTotalSales()/(float)storeService.getStores().size(),
                    null),HttpStatus.OK);
        }
    }

}
