package sebastian.orderTracker.sincronizacion.product;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import sebastian.orderTracker.sincronizacion.HttpManager;

public class ProductSyncTask extends AsyncTask<String, Void, ProductResponse> {
	public static final String STANDARD_TRANSPORT_CONNECTOR_SUFFIX = ";deviceside=true;";
	public static final String URL = "http://dev-taller2.pantheonsite.io/api/productos.json";

	private Activity activity;

	public ProductSyncTask(Activity activity) {
		this.activity = activity;
	}

    @Override
	protected ProductResponse doInBackground(String... urls) {
		ProductResponse result = null;
		try {
			String response = HttpManager.getInstance(activity).get(URL);

			if (HttpManager.ERROR_CODE.equals(response)) {
                Log.w("ERROR RESPONSE: ", response);
				throw new RuntimeException("Ocurrio un error durante la sincronización. No se pudieron obtener los productos");
			}

			result = new ProductResponse(response);

			// Quizas sirva si se debe volver a crear la pantalla
			/*activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ProductSyncTask.this.activity.recreate();
				}
			});*/

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
