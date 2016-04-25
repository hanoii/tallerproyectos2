package sebastian.orderTracker.sincronizacion.product;

import java.util.ArrayList;

import sebastian.orderTracker.entities.Product;
import sebastian.orderTracker.TabProductList;

public class ProductResponse {
	private ArrayList<Product> listadoProductos;
    private TabProductList caller;

	public ProductResponse(String json, TabProductList caller) {
        this.caller = caller;
  //      this.parse(json);
	}
/*
	public void parse(String json) {
        Gson gson = new Gson();
        Product[] arr = gson.fromJson(json, Product[].class);
        listadoProductos = new ArrayList<Product>(Arrays.asList(arr));
        caller.updateProducts(listadoProductos);
	}
*/
	public ArrayList<Product> getListadoProductos() {
		return listadoProductos;
	}

	public void setListadoProductos(ArrayList<Product> inspecciones) {
		this.listadoProductos = inspecciones;
	}
}
