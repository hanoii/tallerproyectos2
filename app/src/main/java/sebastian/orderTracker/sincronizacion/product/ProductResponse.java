package sebastian.orderTracker.sincronizacion.product;

import android.util.Log;

import java.util.List;

import sebastian.orderTracker.Product;

public class ProductResponse {
	private List<Product> listadoProductos;

	public ProductResponse(String json) {
		this.parse(json);
	}

	public void parse(String json) {
		Log.w("PARSE: ", json);
	}

	public List<Product> getListadoProductos() {
		return listadoProductos;
	}

	public void setListadoProductos(List<Product> inspecciones) {
		this.listadoProductos = inspecciones;
	}
}
