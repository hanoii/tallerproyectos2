package sebastian.orderTracker;

/**
 * Created by Senastian on 01/04/2016.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;


        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

import org.w3c.dom.Text;

public class ProductItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;
    public ImageView image;
    public TextView codigo;
    public TextView precio;
    public TextView descripcion;

    public ProductItemHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        name = (TextView) itemView.findViewById(R.id.product_name);
        image = (ImageView) itemView.findViewById(R.id.product_image);
        codigo = (TextView) itemView.findViewById(R.id.product_code);
        precio = (TextView) itemView.findViewById(R.id.product_price);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
        // Aca mandaria los datos del producto (mejor aun mandaria el producto entero como serializable)
        intent.putExtra("titulo", name.getText());
        view.getContext().startActivity(intent);
//        Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}
