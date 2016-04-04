package sebastian.orderTracker.sincronizacion.product;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import sebastian.orderTracker.Product;
import sebastian.orderTracker.TabProductList;

public class ProductResponse {
	private ArrayList<Product> listadoProductos;
    private TabProductList caller;

	public ProductResponse(String json, TabProductList caller) {
        this.caller = caller;
        this.parse(json);
	}

	public void parse(String json) {
        Gson gson = new Gson();
        Product[] arr = gson.fromJson(json, Product[].class);
        listadoProductos = new ArrayList<Product>(Arrays.asList(arr));
        caller.updateProducts(listadoProductos);
	}

	public ArrayList<Product> getListadoProductos() {
		return listadoProductos;
	}

	public void setListadoProductos(ArrayList<Product> inspecciones) {
		this.listadoProductos = inspecciones;
	}
}
